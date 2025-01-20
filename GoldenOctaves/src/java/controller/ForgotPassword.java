package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import entity.User;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import model.Mail;
import model.Validations;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "ForgotPassword", urlPatterns = {"/ForgotPassword"})
public class ForgotPassword extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new Gson();
        JsonObject dto = gson.fromJson(request.getReader(), JsonObject.class);

        String email = dto.has("email") ? dto.get("email").getAsString() : null;

        if (email == null || !Validations.isEmailValid(email)) {
            response_DTO.setContent("Invalid email address.");
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(response_DTO));
            return;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        Criteria criteria = session.createCriteria(User.class);
        criteria.add(Restrictions.eq("email", email));
        User user = (User) criteria.uniqueResult();

        if (user != null && (user.getAccount_Status().getId() == 1)) {
            // Generate Verification Code
            String code = String.valueOf((int) (Math.random() * 1000000)); // Generate a 6-digit code
            user.setVerification(code);

            // Start the thread for sending the email and handling verification
            Thread sendMailThread = new Thread(() -> {
                try {
                    Mail.sendMail(email, "Golden Octaves Change Password",
                            "<div style=\"position: relative; width: 100%; height: 100%; padding: 20px; font-family: Arial, sans-serif;\">"
                            + "<div style=\"position: absolute; top: 0; left: 0; width: 100%; height: 100%; "
                            + "background: url('https://i.imgur.com/hd05N1R.jpeg') no-repeat center center; background-size: cover; "
                            + "filter: blur(8px) brightness(40%); z-index: 1;\"></div>"
                            + "<div style=\"position: relative; z-index: 2; max-width: 600px; margin: auto; background: rgba(255, 255, 255, 0.9); "
                            + "border-radius: 8px; padding: 20px; box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.2);\">"
                            + "<h1 style=\"text-align: center; color: #ffc107; font-size: 36px; margin-bottom: 20px;\">Golden Octaves</h1>"
                            + "<p style=\"font-size: 20px; color: coralblue; text-align: center; font-weight: bold; margin-bottom: 30px;\">"
                            + "Your Verification Code: " + code + "</p>"
                            + "</div>"
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            sendMailThread.start();

            session.update(user);
            session.beginTransaction().commit();

            request.getSession().setAttribute("email", email);
            response_DTO.setSuccess(true);
            response_DTO.setContent("Password change code sent successfully. Please check your inbox.");

        } else {
            response_DTO.setContent("Email not found or account is inactive.");
        }

        session.close();

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }
}


