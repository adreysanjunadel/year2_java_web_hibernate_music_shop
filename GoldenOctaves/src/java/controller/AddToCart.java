package controller;

import com.google.gson.Gson;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.Product;
import entity.User;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "AddToCart", urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            String id = request.getParameter("id");
            String qty = request.getParameter("qty");

            // Validation: ID and Quantity
            if (!Validations.isInteger(id)) {
                response_DTO.setContent("Product not found");
            } else if (!Validations.isInteger(qty)) {
                response_DTO.setContent("Invalid Quantity");
            } else {
                int productId = Integer.parseInt(id);
                int productQty = Integer.parseInt(qty);

                // Ensure Quantity is greater than 0
                if (productQty <= 0) {
                    response_DTO.setContent("Quantity must be greater than 0");
                } else {
                    Product product = (Product) session.get(Product.class, productId);

                    if (product != null) {
                        // Check if the user is logged in (DB Cart)
                        if (request.getSession().getAttribute("user") != null) {

                            User_DTO user_DTO = (User_DTO) request.getSession().getAttribute("user");
                            Criteria criteria1 = session.createCriteria(User.class);
                            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
                            User user = (User) criteria1.uniqueResult();

                            // Check if the product is already in the user's cart
                            Criteria criteria2 = session.createCriteria(Cart.class);
                            criteria2.add(Restrictions.eq("user", user));
                            criteria2.add(Restrictions.eq("product", product));

                            if (criteria2.list().isEmpty()) {
                                // Product not in cart, add new item
                                if (productQty <= product.getQty()) {
                                    Cart cart = new Cart();
                                    cart.setUser(user);
                                    cart.setProduct(product);
                                    cart.setQty(productQty);
                                    session.save(cart);
                                    
                                    transaction.commit();  // Commit the transaction after save
                                    System.out.println("Transaction committed: Item Added");

                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("Item Added to Your Cart");
                                } else {
                                    response_DTO.setContent("Quantity not available");
                                }

                            } else {
                                // Product already in cart, update quantity
                                Cart cartItem = (Cart) criteria2.uniqueResult();
                                if ((cartItem.getQty() + productQty) <= product.getQty()) {
                                    cartItem.setQty(cartItem.getQty() + productQty);
                                    session.update(cartItem);
                                    
                                    transaction.commit();  // Commit the transaction after update
                                    System.out.println("Transaction committed: Cart Item Updated");

                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("Cart Item Updated");
                                } else {
                                    response_DTO.setContent("Cannot update Your Cart. Quantity not available");
                                }
                            }

                        } else {
                            // Session Cart (No logged-in user)
                            HttpSession httpSession = request.getSession();

                            ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) httpSession.getAttribute("sessionCart");
                            if (sessionCart == null) {
                                sessionCart = new ArrayList<>();
                            }

                            Cart_DTO foundCart_DTO = null;

                            for (Cart_DTO cart_DTO : sessionCart) {
                                if (cart_DTO.getProduct().getId() == product.getId()) {
                                    foundCart_DTO = cart_DTO;
                                    break;
                                }
                            }

                            if (foundCart_DTO != null) {
                                // Product found in session cart, update quantity
                                if ((foundCart_DTO.getQty() + productQty) <= product.getQty()) {
                                    foundCart_DTO.setQty(foundCart_DTO.getQty() + productQty);
                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("Cart Item Updated");
                                } else {
                                    response_DTO.setContent("Quantity not available");
                                }
                            } else {
                                // Product not found in session cart, add new item
                                if (productQty <= product.getQty()) {
                                    Cart_DTO cart_DTO = new Cart_DTO();
                                    cart_DTO.setProduct(product);
                                    cart_DTO.setQty(productQty);
                                    sessionCart.add(cart_DTO);

                                    httpSession.setAttribute("sessionCart", sessionCart);

                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("Item Added to Your Cart");
                                } else {
                                    response_DTO.setContent("Quantity not available");
                                }
                            }
                        }

                    } else {
                        // Product not found
                        response_DTO.setContent("Product not found");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();  // Rollback the transaction in case of an exception
                System.out.println("Transaction rolled back due to error.");
            }
            response_DTO.setContent("Unable to process your request!");
        } finally {
            if (transaction != null && !transaction.wasCommitted()) {
                transaction.commit();  // Ensure transaction is committed before closing
                System.out.println("Transaction committed in finally block.");
            }
            session.close();  // Close the session in the finally block to ensure it is always closed
            System.out.println("Session closed.");
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));
    }

}
