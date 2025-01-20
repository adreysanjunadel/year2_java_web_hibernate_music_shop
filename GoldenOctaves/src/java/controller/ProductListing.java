package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Brand;
import entity.Category;
import entity.Colour;
import entity.Description;
import entity.Model;
import entity.Product;
import entity.Product_Condition;
import entity.Product_Status;
import entity.Origin_Country;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "ProductListing", urlPatterns = {"/ProductListing"})
public class ProductListing extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();

        Gson gson = new Gson();

        // Get User in Session
        User_DTO user_DTO = (User_DTO) request.getSession().getAttribute("user");

        String categoryId = request.getParameter("categoryId");
        String brandId = request.getParameter("brandId");
        String modelId = request.getParameter("modelId");
        String title = request.getParameter("title");
        String short_description = request.getParameter("description");
        String warranty = request.getParameter("warranty");
        String originId = request.getParameter("originId");
        String colourId = request.getParameter("colourId");
        String conditionId = request.getParameter("conditionId");
        String price = request.getParameter("price");
        String quantity = request.getParameter("quantity");

        Part image1 = request.getPart("image1");
        Part image2 = request.getPart("image2");
        Part image3 = request.getPart("image3");

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            if (!Validations.isInteger(categoryId)) {
                response_DTO.setContent("Invalid Category");

            } else if (!Validations.isInteger(brandId)) {
                response_DTO.setContent("Invalid Brand");

            } else if (!Validations.isInteger(modelId)) {
                response_DTO.setContent("Invalid Model");

            } else if (!Validations.isInteger(originId)) {
                response_DTO.setContent("Invalid Storage");

            } else if (!Validations.isInteger(colourId)) {
                response_DTO.setContent("Invalid Colour");

            } else if (!Validations.isInteger(conditionId)) {
                response_DTO.setContent("Invalid Condition");

            } else if (title.isEmpty()) {
                response_DTO.setContent("Please fill Title");

            } else if (short_description.isEmpty()) {
                response_DTO.setContent("Please fill Description");

            } else if (warranty.isEmpty()) {
                response_DTO.setContent("Please fill Warranty");

            } else if (price.isEmpty()) {
                response_DTO.setContent("Please fill Price");

            } else if (!Validations.isDouble(price)) {
                response_DTO.setContent("Invalid Price");

            } else if (Double.parseDouble(price) <= 0) {
                response_DTO.setContent("Price must be greater than 0");

            } else if (quantity.isEmpty()) {
                response_DTO.setContent("Please fill Quantity");

            } else if (!Validations.isInteger(quantity)) {
                response_DTO.setContent("Invalid Quantity");

            } else if (Integer.parseInt(quantity) <= 0) {
                response_DTO.setContent("Quantity must be greater than 0");

            } else if (image1.getSubmittedFileName() == null) {
                response_DTO.setContent("Please upload Image 1");

            } else if (image2.getSubmittedFileName() == null) {
                response_DTO.setContent("Please upload Image 2");

            } else if (image3.getSubmittedFileName() == null) {
                response_DTO.setContent("Please upload Image 3");

            } else {

                Category category = (Category) session.get(Category.class, Integer.parseInt(categoryId));

                if (category == null) {
                    response_DTO.setContent("Please select a valid Category");

                } else {

                    Brand brand = (Brand) session.get(Brand.class, Integer.parseInt(brandId));

                    if (brand == null) {
                        response_DTO.setContent("Please select a valid Brand");

                    } else {

                        Model model = (Model) session.get(Model.class, Integer.parseInt(modelId));

                        if (model == null) {
                            response_DTO.setContent("Please select a valid Model");

                        } else {

                            if (model.getBrand().getId() != brand.getId()) {
                                response_DTO.setContent("Please select a valid Model");

                            } else {

                                Origin_Country origin = (Origin_Country) session.get(Origin_Country.class, Integer.parseInt(originId));

                                if (origin == null) {
                                    response_DTO.setContent("Please select a valid Country of Origin");

                                } else {

                                    Colour colour = (Colour) session.get(Colour.class, Integer.parseInt(colourId));

                                    if (colour == null) {
                                        response_DTO.setContent("Please select a valid Color");

                                    } else {

                                        Product_Condition productCondition = (Product_Condition) session.get(Product_Condition.class, Integer.parseInt(conditionId));

                                        if (productCondition == null) {
                                            response_DTO.setContent("Please select a valid Product Condition");

                                        } else {

                                            // Create and persist Description entity
                                            Description description = new Description();
                                            description.setShort_description(short_description);
                                            description.setOrigin_Country(origin);
                                            description.setWarranty(warranty);

                                            session.save(description);

                                            //Product Save
                                            Product product = new Product();
                                            product.setCategory(category);
                                            product.setColour(colour);
                                            product.setDatetime_updated(new Date());

                                            product.setDescription(description);

                                            product.setModel(model);
                                            product.setPrice(Double.parseDouble(price));

                                            //get Active Status
                                            Product_Status product_Status = (Product_Status) session.get(Product_Status.class, 1);
                                            product.setProduct_Status(product_Status);

                                            //set Product Condition
                                            product.setProduct_Condition(productCondition);
                                            product.setQty(Integer.parseInt(quantity));
                                            product.setTitle(title);

                                            // Check if user_DTO is null
                                            if (user_DTO == null) {
                                                response_DTO.setContent("User is not logged in.");
                                                response.setContentType("application/json");
                                                response.getWriter().write(gson.toJson(response_DTO));
                                                return; // Exit early to avoid further processing
                                            }

                                            // Check if email is null
                                            if (user_DTO.getEmail() == null) {
                                                response_DTO.setContent("User email is not available.");
                                                response.setContentType("application/json");
                                                response.getWriter().write(gson.toJson(response_DTO));
                                                return; // Exit early to avoid further processing
                                            }

                                            // Proceed with criteria query
                                            Criteria criteria1 = session.createCriteria(User.class);
                                            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
                                            User user = (User) criteria1.uniqueResult();

                                            if (user == null) {
                                                response_DTO.setContent("User not found.");
                                                response.setContentType("application/json");
                                                response.getWriter().write(gson.toJson(response_DTO));
                                                return; // Exit early to avoid further processing
                                            }

                                            product.setUser(user);

                                            int pid = (int) session.save(product);

                                            //Save Images
                                            String applicationPath = request.getServletContext().getRealPath("");
                                            String newApplicationPath = applicationPath.replace("build" + File.separator + "web", "web");
                                            File folder = new File(newApplicationPath + "//product-images//" + pid);
                                            if (!folder.mkdirs()) {
                                                throw new RuntimeException("Failed to create directory: " + folder.getAbsolutePath());
                                            }
                                            System.out.println("Application Path: " + newApplicationPath);
                                            System.out.println("Folder Path: " + folder.getAbsolutePath());

                                            saveImage(image1, folder, "image1");
                                            saveImage(image2, folder, "image2");
                                            saveImage(image3, folder, "image3");

                                            response_DTO.setSuccess(true);
                                            response_DTO.setContent("New Product Added");

                                            session.getTransaction().commit();

                                        }

                                    }

                                }

                            }

                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response_DTO.setContent("An error occurred while adding the product.");
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        } finally {
            session.close();
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));
    }

    private void saveImage(Part image, File folder, String imageName) {
        try {
            if (!folder.exists() || !folder.isDirectory()) {
                throw new RuntimeException("Folder does not exist or is not a directory: " + folder.getAbsolutePath());
            }
            String fileExtension = image.getContentType().split("/")[1];
            File file = new File(folder, imageName + "." + fileExtension);
            System.out.println("Saving file: " + file.getAbsolutePath());
            try (InputStream inputStream = image.getInputStream()) {
                Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving " + imageName);
        }
    }

}
