package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.User_DTO;
import entity.Address;
import entity.Cart;
import entity.City;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadCheckout", urlPatterns = {"/LoadCheckout"})
public class LoadCheckout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson checkoutGson = new Gson();
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        HttpSession httpSession = request.getSession();
        
        try {
            
            if (httpSession.getAttribute("user") != null){
                
                User_DTO user_DTO = (User_DTO) httpSession.getAttribute("user");
                
                // Get User From Database
                Criteria criteria1 = session.createCriteria(User.class);
                criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
                User user = (User) criteria1.uniqueResult();
                
                // Get User's list Address in Database
                Criteria criteria2 = session.createCriteria(Address.class);
                criteria2.add(Restrictions.eq("user", user));
                criteria2.addOrder(Order.desc("id"));
                criteria2.setMaxResults(1);
                Address address = (Address) criteria2.list().get(0);
                
                //Get Cities from Database
                Criteria criteria3 = session.createCriteria(City.class);
                criteria3.addOrder(Order.asc("name"));
                List<City> cityList = criteria3.list();
                
                //Get Cart Items from Database 
                Criteria criteria4 = session.createCriteria(Cart.class);
                criteria4.add(Restrictions.eq("user", user));
                List<Cart> cartList = criteria4.list();
                
                //Pack Address in JsonObject
                address.setUser(null);
                jsonObject.add("address", checkoutGson.toJsonTree(address));
                
                //Pack Cities in JsonObject
                jsonObject.add("cityList", checkoutGson.toJsonTree(cityList));
                
                // Pack Cart in JsonObject
                for (Cart cart : cartList) {
                    cart.setUser(null);
                    cart.getProduct().setUser(null);
                }
                jsonObject.add("cartList", checkoutGson.toJsonTree(cartList));
                
                jsonObject.addProperty("success", true);
                
                
            } else {
                //Not Signed In
                jsonObject.addProperty("message", "Not Signed In");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        response.setContentType("application/json");
        response.getWriter().write(checkoutGson.toJson(jsonObject));
        
        session.close();

    }

}
