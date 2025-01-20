package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Sanjuna
 */
@WebServlet(name = "SignIn", urlPatterns = {"/SignIn"})
public class SignIn extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        User_DTO user_DTO = gson.fromJson(request.getReader(), User_DTO.class);

        Session session = HibernateUtil.getSessionFactory().openSession();

        if (user_DTO.getEmail().isEmpty()) {
            response_DTO.setContent("Please enter your Email");

        } else if (user_DTO.getPassword().isEmpty()) {
            response_DTO.setContent("Please enter your Password");

        } else {

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
            criteria1.add(Restrictions.eq("password", user_DTO.getPassword()));

            if (!criteria1.list().isEmpty()) {

                User user = (User) criteria1.list().get(0);

                if (!user.getVerification().equals("Verified")) {
                    //Unverified
                    request.getSession().setAttribute("email", user_DTO.getEmail());
                    response_DTO.setSuccess(false);
                    response_DTO.setContent("Unverified");

                } else {
                    //Verified
                    user_DTO.setFirst_name(user.getFirst_name());
                    user_DTO.setLast_name(user.getLast_name());
                    //removing password 
                    user_DTO.setPassword(null);
                    request.getSession().setAttribute("user", user_DTO);

                    // Session cart info transferred to Database Cart
                    if (request.getSession().getAttribute("sessionCart") != null) {
                        // Session cart is found
                        ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) request.getSession().getAttribute("sessionCart");

                        Criteria criteria2 = session.createCriteria(Cart.class);
                        criteria1.add(Restrictions.eq("user", user));

                        List<Cart> dbCart = criteria2.list();

                        //DB Cart is empty
                        if (dbCart.isEmpty()) {
                            //Add Session Cart Items into DBCart

                            for (Cart_DTO cart_DTO : sessionCart) {
                                Cart cart = new Cart();
                                cart.setProduct(cart_DTO.getProduct()); //No User
                                cart.setQty(cart_DTO.getQty());
                                cart.setUser(user);
                                session.save(cart);
                            }

                        } else {
                            // DB Cart has items

                            for (Cart_DTO cart_DTO : sessionCart) {

                                boolean itemsInDBCart = false;

                                for (Cart cart : dbCart) {
                                    // Checking if each item is in both carts

                                    if (cart_DTO.getProduct().getId() == cart.getProduct().getId()) {
                                        //Same
                                        itemsInDBCart = true;

                                        if ((cart_DTO.getQty() + cart.getQty()) <= cart.getProduct().getQty()) {
                                            //Quantity Available
                                            cart.setQty(cart_DTO.getQty() + cart.getQty());
                                            session.update(cart);

                                        } else {
                                            //No quantity avaiable
                                            //Set max available Quantity
                                            cart.setQty(cart.getProduct().getQty());
                                            session.update(cart);
                                        }

                                    }

                                }

                                if (!itemsInDBCart) {
                                    // Not found in DB cart
                                    Cart cart = new Cart();
                                    cart.setProduct(cart_DTO.getProduct()); //No User
                                    cart.setQty(cart_DTO.getQty());
                                    cart.setUser(user);
                                    session.save(user);
                                }

                            }

                        }

                        request.getSession().removeAttribute("sessionCart");
                        session.beginTransaction().commit();

                    }

                    response_DTO.setSuccess(true);
                    response_DTO.setContent("Sign In Success");

                }

            } else {
                response_DTO.setContent("Invalid Details! Please try again");

            }

        }
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));

        session.close();

    }
}
