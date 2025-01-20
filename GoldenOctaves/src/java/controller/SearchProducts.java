package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Category;
import entity.Colour;
import entity.Model;
import entity.Product;
import entity.Product_Condition;
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

@WebServlet(name = "SearchProducts", urlPatterns = {"/SearchProducts"})
public class SearchProducts extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            Criteria criteria1 = session.createCriteria(Product.class);

            // Add Category Filter
            if (requestJsonObject.has("category_name")) {
                String categoryName = requestJsonObject.get("category_name").getAsString();
                Criteria categoryCriteria = session.createCriteria(Category.class);
                categoryCriteria.add(Restrictions.eq("name", categoryName));
                Category category = (Category) categoryCriteria.uniqueResult();
                if (category != null) {
                    criteria1.add(Restrictions.eq("category", category));
                }
            }

             // Add Brand Filter
            if (requestJsonObject.has("brand_name")) {
                String brandName = requestJsonObject.get("brand_name").getAsString();
                Criteria modelCriteria = session.createCriteria(Model.class);
                modelCriteria.createAlias("brand", "b");  // Alias for Brand
                modelCriteria.add(Restrictions.eq("b.name", brandName));
                List<Model> modelList = modelCriteria.list();
                if (!modelList.isEmpty()) {
                    // Filter Products by Model
                    criteria1.createAlias("model", "m");  // Correct Alias for Model
                    List<Integer> modelIds = new ArrayList<Integer>();
                    for (Model model : modelList) {
                        modelIds.add(model.getId());  // Assuming getId() returns the model ID
                    }
                    criteria1.add(Restrictions.in("model.id", modelIds));  // Adjusted to use model IDs
                }
            }


            // Add Condition Filter
            if (requestJsonObject.has("condition_name")) {
                String conditionName = requestJsonObject.get("condition_name").getAsString();
                Criteria conditionCriteria = session.createCriteria(Product_Condition.class);
                conditionCriteria.add(Restrictions.eq("name", conditionName));
                Product_Condition condition = (Product_Condition) conditionCriteria.uniqueResult();
                if (condition != null) {
                    criteria1.add(Restrictions.eq("product_Condition", condition));
                }
            }

            // Add Colour Filter
            if (requestJsonObject.has("colour_name")) {
                String colourName = requestJsonObject.get("colour_name").getAsString();
                Criteria colourCriteria = session.createCriteria(Colour.class);
                colourCriteria.add(Restrictions.eq("name", colourName));
                Colour colour = (Colour) colourCriteria.uniqueResult();
                if (colour != null) {
                    criteria1.add(Restrictions.eq("colour", colour));
                }
            }

            // Filter Products by Price Range
            double priceRangeStart = requestJsonObject.get("price_range_start").getAsDouble();
            double priceRangeEnd = requestJsonObject.get("price_range_end").getAsDouble();
            criteria1.add(Restrictions.ge("price", priceRangeStart));
            criteria1.add(Restrictions.le("price", priceRangeEnd));

            // Get total Product count
            responseJsonObject.addProperty("allProductCount", criteria1.list().size());

            // Set product Range
            int firstResult = requestJsonObject.get("firstResult").getAsInt();
            criteria1.setFirstResult(firstResult);
            criteria1.setMaxResults(6);

            // Get Product List
            List<Product> productList = criteria1.list();
            for (Product product : productList) {
                product.setUser(null);  // Avoid lazy loading issues
            }

            responseJsonObject.add("productList", gson.toJsonTree(productList));
            responseJsonObject.addProperty("success", true);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        // Send Response
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));
    }
}
