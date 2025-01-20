import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Model;
import entity.Product;
import entity.Description; // Import Description entity
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "LoadSingleProduct", urlPatterns = {"/LoadSingleProduct"})
public class LoadSingleProduct extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            String productId = request.getParameter("id");

            if (productId == null || !Validations.isInteger(productId)) {
                response.sendRedirect("index.html");
                return;
            }

            if (Validations.isInteger(productId)) {

                // Fetch the product by ID using Criteria
                Criteria productCriteria = session.createCriteria(Product.class);
                productCriteria.add(Restrictions.idEq(Integer.parseInt(productId)));
                Product product = (Product) productCriteria.uniqueResult();

                if (product != null) {
                    // Remove sensitive user information
                    if (product.getUser() != null) {
                        product.getUser().setPassword(null);
                        product.getUser().setVerification(null);
                        product.getUser().setEmail(null);
                    }

                    // Fetch the Description entity if applicable
                    Description description = product.getDescription(); // Assuming getDescription() method exists

                    // Fetch similar products
                    Criteria modelCriteria = session.createCriteria(Model.class);
                    modelCriteria.add(Restrictions.eq("brand", product.getModel().getBrand()));
                    List<Model> modelList = modelCriteria.list();

                    Criteria similarProductCriteria = session.createCriteria(Product.class);
                    similarProductCriteria.add(Restrictions.in("model", modelList));
                    similarProductCriteria.add(Restrictions.ne("id", product.getId()));
                    similarProductCriteria.setMaxResults(6);

                    List<Product> productList = similarProductCriteria.list();

                    for (Product similarProduct : productList) {
                        if (similarProduct.getUser() != null) {
                            similarProduct.getUser().setPassword(null);
                            similarProduct.getUser().setVerification(null);
                            similarProduct.getUser().setEmail(null);
                        }
                    }

                    // Prepare the JSON response
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add("product", gson.toJsonTree(product));
                    jsonObject.add("description", gson.toJsonTree(description)); // Add description to response
                    jsonObject.add("productList", gson.toJsonTree(productList));

                    response.setContentType("application/json");
                    response.getWriter().write(gson.toJson(jsonObject));

                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                }

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        } finally {
            session.close();
        }
    }
}
