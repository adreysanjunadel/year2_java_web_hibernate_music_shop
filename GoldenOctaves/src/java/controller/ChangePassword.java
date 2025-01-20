package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
import entity.User;
import java.io.IOException;
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

@WebServlet(name = "ChangePassword", urlPatterns = {"/ChangePassword"})
public class ChangePassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new Gson();
        JsonObject dto = gson.fromJson(request.getReader(), JsonObject.class);

        String verification = dto.has("verification") ? dto.get("verification").getAsString() : null;
        String password = dto.has("password") ? dto.get("password").getAsString() : null;
        String confirm_password = dto.has("confirm_password") ? dto.get("confirm_password").getAsString() : null;

        if (request.getSession().getAttribute("email") != null) {

            String email = request.getSession().getAttribute("email").toString();

            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", email));
            criteria1.add(Restrictions.eq("verification", verification));

            if (!criteria1.list().isEmpty()) {

                if (password != null && !password.isEmpty()) {

                    if (Validations.isPasswordValid(password)) {

                        if (confirm_password != null && !confirm_password.isEmpty()) {

                            if (password.equals(confirm_password)) {
                                User user = (User) criteria1.list().get(0);
                                user.setVerification("Verified");
                                user.setPassword(password); // Update the user's password here

                                session.update(user);
                                session.beginTransaction().commit();

                                User_DTO user_DTO = new User_DTO();
                                user_DTO.setPassword(password);
                                request.getSession().removeAttribute("email");
                                request.getSession().setAttribute("user", user_DTO);

                                response_DTO.setSuccess(true);
                                response_DTO.setContent("Password Changed Successfully");

                            } else {
                                // Unmatching Passwords
                                response_DTO.setContent("Please make sure both passwords are same!");

                            }
                        } else {
                            // Missing Confirm Password
                            response_DTO.setContent("Please confirm your new password!");

                        }

                    } else {
                        // Invalid Password
                        response_DTO.setContent("Password must include at least one uppercase letter, number, special character "
                                + "and be at least 8 characters long");

                    }

                } else {
                    // Missing Password
                    response_DTO.setContent("Please enter your new password!");

                }

            } else {
                // Invalid Verification Code
                response_DTO.setContent("Please enter a valid Verification Code!");

            }

            session.close();

        } else {
            response_DTO.setContent("Unable to Change Password Please try again later");

        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));
    }
}

