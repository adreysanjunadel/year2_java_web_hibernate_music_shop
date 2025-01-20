package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.User_DTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.Delivery_Status;
import entity.Invoice;
import entity.Invoice_Item;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.PayHere;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "Checkout", urlPatterns = {"/Checkout"})
public class Checkout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();

        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        HttpSession httpSession = request.getSession();

        try {

            boolean isCurrentAddress = requestJsonObject.get("isCurrentAddress").getAsBoolean();
            String first_name = requestJsonObject.get("first_name").getAsString();
            String last_name = requestJsonObject.get("last_name").getAsString();
            String city_id = requestJsonObject.get("city_id").getAsString();
            String address1 = requestJsonObject.get("address1").getAsString();
            String address2 = requestJsonObject.get("address2").getAsString();
            String postal_code = requestJsonObject.get("postal_code").getAsString();
            String mobile = requestJsonObject.get("mobile").getAsString();

            if (httpSession.getAttribute("user") != null) {
                //User signed in

                //Get user from DB
                User_DTO user_DTO = (User_DTO) httpSession.getAttribute("user");
                Criteria criteria1 = session.createCriteria(User.class);
                criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
                User user = (User) criteria1.uniqueResult();

                if (isCurrentAddress) {
                    //Get Current address
                    Criteria criteria2 = session.createCriteria(Address.class);
                    criteria2.add(Restrictions.eq("user", user));
                    criteria2.addOrder(Order.desc("id"));
                    criteria2.setMaxResults(1);

                    if (criteria2.list().isEmpty()) {
                        //Current address not found. Please create a new address.
                        responseJsonObject.addProperty("message", "Current address not found. Please create a new address");
                    } else {
                        //Current address found(Proceed)
                        Address address = (Address) criteria2.list().get(0);

                        //***Complete the checkout process
                        saveOrders(session, transaction, user, address, responseJsonObject);
                    }

                } else {
                    //Create new address

                    if (first_name.isEmpty()) {
                        responseJsonObject.addProperty("message", "Please fill First name");

                    } else if (last_name.isEmpty()) {
                        responseJsonObject.addProperty("message", "Please fill Last name");

                    } else if (!Validations.isInteger(city_id)) {
                        responseJsonObject.addProperty("message", "Invalid City Selected");

                    } else {
                        //Check city from DB
                        Criteria criteria3 = session.createCriteria(City.class);
                        criteria3.add(Restrictions.eq("id", Integer.parseInt(city_id)));

                        if (criteria3.list().isEmpty()) {
                            //Invalid City Selected
                            responseJsonObject.addProperty("message", "Invalid City Selected");

                        } else {
                            //City found(Proceed)
                            City city = (City) criteria3.list().get(0);

                            if (address1.isEmpty()) {
                                responseJsonObject.addProperty("message", "Please fill Address Line 1");

                            } else if (address2.isEmpty()) {
                                responseJsonObject.addProperty("message", "Please fill Address Line 2");

                            } else if (postal_code.isEmpty()) {
                                responseJsonObject.addProperty("message", "Please fill Postal Code");

                            } else if (postal_code.length() != 5) {
                                responseJsonObject.addProperty("message", "Invalid Postal Code");

                            } else if (!Validations.isInteger(postal_code)) {
                                responseJsonObject.addProperty("message", "Invalid Postal Code");

                            } else if (mobile.isEmpty()) {
                                responseJsonObject.addProperty("message", "Please fill Mobile");

                            } else if (!Validations.isMobileNumberValid(mobile)) {
                                responseJsonObject.addProperty("message", "Invalid Mobile Number");

                            } else if (!Validations.isInteger(mobile)) {
                                responseJsonObject.addProperty("message", "Invalid Mobile Number");

                            } else {
                                //Create new address
                                Address address = new Address();
                                address.setCity(city);
                                address.setFirst_name(first_name);
                                address.setLast_name(last_name);
                                address.setLine1(address1);
                                address.setLine2(address2);
                                address.setMobile(mobile);
                                address.setPostal_code(postal_code);
                                address.setUser(user);

                                session.save(address);

                                //***Complete the checkout process 
                                saveOrders(session, transaction, user, address, responseJsonObject);
                            }

                        }

                    }
                }

            } else {
                //User not signed in
                responseJsonObject.addProperty("message", "User not signed in");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));

        session.close();

    }

    private void saveOrders(Session session, Transaction transaction, User user, Address address, JsonObject responseJsonObject) {

        try {
            //Create Order in DB
            Invoice invoice = new Invoice();
            invoice.setAddress(address);
            invoice.setDate_time(new Date());
            invoice.setUser(user);

            int order_id = (int) session.save(invoice);

            //Get Cart Items
            Criteria criteria4 = session.createCriteria(Cart.class);
            criteria4.add(Restrictions.eq("user", user));
            List<Cart> cartList = criteria4.list();

            //Get Order (5.Payment Pending) from DB
            Delivery_Status delivery_Status = (Delivery_Status) session.get(Delivery_Status.class, 5);

            //Create order Item in DB
            double amount = 0;
            String items = "";
            for (Cart cartItem : cartList) {
                
                //Calculate amount 
                amount+= cartItem.getQty() * cartItem.getProduct().getPrice();
                if(address.getCity().getId()==1){
                    amount+=800;
                } else {
                    amount+=1200;
                }
                //Calculate amount
                
                //Get Item Details
                items+=cartItem.getProduct().getTitle()+" x"+cartItem.getQty()+" ";
                //Get Items Details
                
                //Get Product
                Product product = cartItem.getProduct();

                Invoice_Item invoice_Item = new Invoice_Item();
                invoice_Item.setInvoice(invoice);
                invoice_Item.setDelivery_Status(delivery_Status);
                invoice_Item.setProduct(product);
                invoice_Item.setQty(cartItem.getQty());

                session.save(invoice_Item);

                //Update Product Quantity in DB
                product.setQty(product.getQty() - cartItem.getQty());
                session.update(product);

                //Delete Cart item from DB
                session.delete(cartItem);

            }
            
            //Get mobile for PayHere
            
            transaction.commit();

            //Start: Set Payment Data
            String merchant_id = "1228159";
            String formattedAmount = new DecimalFormat("0.00").format(amount);
            String currency = "LKR";
            String merchantSecret = "MjA4NTc0OTg5MjIyNTA3NDI1NDQxMTk5MDg3MTExMjI0NzkwOTg5MA==";
            String merchantSecretMd5Hash = PayHere.generateMD5(merchantSecret);
            
            JsonObject payhere = new JsonObject();
            payhere.addProperty("merchant_id", merchant_id);
            
            payhere.addProperty("return_url", "");
            payhere.addProperty("cancel_url", "");
            payhere.addProperty("notify_url", "https://67ee-112-134-172-81.ngrok-free.app/GoldenOctaves/VerifyPayments"); //***
            
            payhere.addProperty("first_name", user.getFirst_name());
            payhere.addProperty("last_name", user.getLast_name());
            payhere.addProperty("email", user.getEmail());
            
            payhere.addProperty("phone", address.getMobile());
            payhere.addProperty("address", address.getLine1() + address.getLine2());
            payhere.addProperty("city", address.getCity().getName());
            payhere.addProperty("country", "Sri Lanka");
            
            payhere.addProperty("order_id", String.valueOf(order_id));
            payhere.addProperty("items", items);
            payhere.addProperty("currency", currency);
            payhere.addProperty("amount", formattedAmount);
            payhere.addProperty("sandbox", true);
            
            //Generate MD5 Hash
            //merchantID + orderID + amountFormatted + currency + merchantSecretId
            String md5Hash = PayHere.generateMD5(merchant_id + order_id + formattedAmount + currency + merchantSecretMd5Hash);
            payhere.addProperty("hash", md5Hash);
           
            //End: Set Payment Data
            responseJsonObject.addProperty("success", true);
            responseJsonObject.addProperty("success", "Checkout Completed");

            Gson gson = new Gson();
            responseJsonObject.add("payhereJson", gson.toJsonTree(payhere));
            
        } catch (Exception e) {
            transaction.rollback();
        }

    }
}
