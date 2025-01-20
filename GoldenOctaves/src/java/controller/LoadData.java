package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Brand;
import entity.Category;
import entity.Colour;
import entity.Origin_Country;
import entity.Product;
import entity.Product_Condition;
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
import org.hibernate.criterion.Projections;

/**
 *
 * @author Sanjuna
 */
@WebServlet(name = "LoadData", urlPatterns = {"/LoadData"})
public class LoadData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("success", false);

        Session session = HibernateUtil.getSessionFactory().openSession();

        //main code
        try {

            //Get Category list from DB
            Criteria criteria1 = session.createCriteria(Category.class);
            List<Category> categoryList = criteria1.list();
            jsonObject.add("categoryList", gson.toJsonTree(categoryList));

            //Get Condition list from DB
            Criteria criteria2 = session.createCriteria(Brand.class);
            List<Brand> brandList = criteria2.list();
            jsonObject.add("brandList", gson.toJsonTree(brandList));

            //Get Condition list from DB
            Criteria criteria3 = session.createCriteria(Product_Condition.class);
            List<Product_Condition> conditionList = criteria3.list();
            jsonObject.add("conditionList", gson.toJsonTree(conditionList));

            //Get Color list from DB
            Criteria criteria4 = session.createCriteria(Colour.class);
            List<Colour> colourList = criteria4.list();
            jsonObject.add("colourList", gson.toJsonTree(colourList));

            //Get Product list from DB
            Criteria criteria5 = session.createCriteria(Product.class);

            // Get latest Product
            criteria5.addOrder(Order.desc("id"));
            jsonObject.addProperty("allProductCount", criteria5.list().size());  // Update to criteria6

            // Set Product Range
            criteria5.setFirstResult(0);
            criteria5.setMaxResults(6);

            List<Product> productList = criteria5.list();  // Use criteria6 to get the product list

            // Remove User from each product
            for (Product product : productList) {
                product.setUser(null);
            }

            jsonObject.add("productList", gson.toJsonTree(productList));

            jsonObject.addProperty("success", true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //main code

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));

        session.close();

    }

}
