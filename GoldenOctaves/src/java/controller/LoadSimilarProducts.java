package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.Product;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

@WebServlet(name = "LoadSimilarProducts", urlPatterns = {"/LoadSimilarProducts"})
public class LoadSimilarProducts extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JsonObject jsonObject = new JsonObject();
        
        Gson gson1 = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {

            //Get Last 8 Products
            Criteria criteria1 = session.createCriteria(Product.class);
            criteria1.addOrder(Order.desc("id"));
            criteria1.setMaxResults(8);
            List<Product> productList = criteria1.list();
            for (Product product : productList) {
                product.setUser(null);
            }
                        
            jsonObject.add("products", gson1.toJsonTree(productList));

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        response.getWriter().write(gson1.toJson(jsonObject));

    }

}
