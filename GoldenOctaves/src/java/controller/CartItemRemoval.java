package controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.Cart_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "CartItemRemoval", urlPatterns = {"/CartItemRemoval"})
public class CartItemRemoval extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Parse JSON from request
        JsonObject jsonRequest = JsonParser.parseReader(request.getReader()).getAsJsonObject();
        String action = jsonRequest.get("action").getAsString();

        HttpSession httpSession = request.getSession();
        User_DTO user_DTO = (User_DTO) httpSession.getAttribute("user");

        // Open Hibernate session
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        try {
            // If the user is logged in, handle the database cart
            if (user_DTO != null) {
                // Fetch the user from the database
                Criteria criteriaUser = session.createCriteria(User.class);
                criteriaUser.add(Restrictions.eq("email", user_DTO.getEmail()));
                User user = (User) criteriaUser.uniqueResult();

                // Handle remove single item
                if ("removeItem".equals(action)) {
                    String productId = jsonRequest.get("productId").getAsString();

                    // Fetch the cart item for the specific product
                    Criteria criteriaCart = session.createCriteria(Cart.class);
                    criteriaCart.add(Restrictions.eq("user", user));
                    criteriaCart.add(Restrictions.eq("product.id", Integer.parseInt(productId)));

                    Cart cartItem = (Cart) criteriaCart.uniqueResult();
                    if (cartItem != null) {
                        session.delete(cartItem);  // Remove the item from the cart in the database
                        transaction.commit();
                    }
                }

                // Handle clear cart
                else if ("clearCart".equals(action)) {
                    // Fetch all cart items for the user
                    Criteria criteriaCart = session.createCriteria(Cart.class);
                    criteriaCart.add(Restrictions.eq("user", user));

                    List<Cart> cartList = criteriaCart.list();
                    for (Cart cartItem : cartList) {
                        session.delete(cartItem);  // Remove each cart item from the database
                    }
                    transaction.commit();
                }
            }
            // If the user is not logged in, handle session-based cart (guest cart)
            else {
                // Handle remove single item from the session-based cart
                List<Cart_DTO> sessionCart = (List<Cart_DTO>) httpSession.getAttribute("sessionCart");

                if (sessionCart != null) {
                    if ("removeItem".equals(action)) {
                        String productId = jsonRequest.get("productId").getAsString();

                        // Remove the item from the session cart
                        sessionCart.removeIf(item -> item.getProduct().getId() == Integer.parseInt(productId));
                        httpSession.setAttribute("sessionCart", sessionCart);
                    }

                    // Handle clear cart for session-based cart
                    else if ("clearCart".equals(action)) {
                        // Clear the entire session cart
                        httpSession.setAttribute("sessionCart", null);
                    }
                }
            }

            // Send success response
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            session.close();
        }
    }
}
