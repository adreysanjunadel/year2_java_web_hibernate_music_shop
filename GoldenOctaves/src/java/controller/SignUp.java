package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Account_Status;
import entity.User;
import java.io.IOException;
import java.util.Date;
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

@WebServlet(name = "SignUp", urlPatterns = {"/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        User_DTO user_DTO = gson.fromJson(request.getReader(), User_DTO.class);

        // Validations
        if (user_DTO.getFirst_name().isEmpty()) {
            response_DTO.setContent("Please enter your First Name");
        } else if (user_DTO.getLast_name().isEmpty()) {
            response_DTO.setContent("Please enter your Last Name");
        } else if (user_DTO.getEmail().isEmpty()) {
            response_DTO.setContent("Please enter your Email");
        } else if (!Validations.isEmailValid(user_DTO.getEmail())) {
            response_DTO.setContent("Please enter a valid Email");
        } else if (user_DTO.getPassword().isEmpty()) {
            response_DTO.setContent("Please enter your Password");
        } else if (!Validations.isPasswordValid(user_DTO.getPassword())) {
            response_DTO.setContent("Password must include at least one uppercase letter, number, special character "
                    + "and be at least 8 characters long");
        } else {

            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));

            if (!criteria1.list().isEmpty()) {
                response_DTO.setContent("User with this Email already exists");
            } else {

                // Generate Verification Code
                int code = (int) (Math.random() * 1000000);

                // Save User
                final User user = new User();
                user.setFirst_name(user_DTO.getFirst_name());
                user.setLast_name(user_DTO.getLast_name());
                user.setEmail(user_DTO.getEmail());
                user.setPassword(user_DTO.getPassword());
                user.setDate_time(new Date());
                user.setVerification(String.valueOf(code));

                // Get Active Status
                Account_Status account_Status = (Account_Status) session.get(Account_Status.class, 1);
                user.setAccount_Status(account_Status);

                // Start the thread for sending the email and handling verification
                Thread sendMailThread = new Thread(() -> {
                    try {
                        // Send verification email with the link
                        Mail.sendMail(user_DTO.getEmail(), "Golden Octaves Verification",
                                "<div style=\"position: relative; width: 100%; height: 100%; padding: 20px; font-family: Arial, sans-serif;\">"
                                + "<div style=\"position: absolute; top: 0; left: 0; width: 100%; height: 100%; "
                                + "background: url('https://i.imgur.com/hd05N1R.jpeg') no-repeat center center; background-size: cover; "
                                + "filter: blur(8px) brightness(40%); z-index: 1;\"></div>"
                                + "<div style=\"position: relative; z-index: 2; max-width: 600px; margin: auto; background: rgba(255, 255, 255, 0.9); "
                                + "border-radius: 8px; padding: 20px; box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.2);\">"
                                + "<h1 style=\"text-align: center; color: #ffc107; font-size: 36px; margin-bottom: 20px;\">Golden Octaves</h1>"
                                + "<p style=\"font-size: 20px; color: #28a745; text-align: center; font-weight: bold; margin-bottom: 30px;\">"
                                + "Your Verification Code: " + user.getVerification() + "</p>"
                                + "<div style=\"text-align: center; margin: 20px 0;\">"
                                + "<hr style=\"border: none; height: 1px; background: #ccc; margin-bottom: 10px;\">"
                                + "<span style=\"font-size: 18px; color: #6c757d;\">---- OR ----</span>"
                                + "<hr style=\"border: none; height: 1px; background: #ccc; margin-top: 10px;\"></div>"
                                + "<p style=\"font-size: 16px; text-align: center; margin-bottom: 20px;\">"
                                + "<a href=\"https://309c-112-134-175-122.ngrok-free.app/GoldenOctaves/SignUp?action=verify&code=" + code + "\" "
                                + "style=\"display: inline-block; padding: 10px 25px; background-color: #007bff; color: #fff; text-decoration: none; "
                                + "border-radius: 5px; font-size: 16px;\">Click here to verify your account</a></p>"
                                + "</div>"
                                + "</div>"
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                sendMailThread.start();

                // Save user to the database
                session.save(user);
                session.beginTransaction().commit();
                request.getSession().setAttribute("email", user_DTO.getEmail());
                response_DTO.setSuccess(true);
                response_DTO.setContent("Registration Complete. Please check your inbox for the verification email.");

            }

            session.close();
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String code = request.getParameter("code");

        if (action != null && action.equals("verify") && code != null) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(User.class);
            criteria.add(Restrictions.eq("verification", code));

            User user = (User) criteria.uniqueResult();
            if (user != null) {
                session.beginTransaction();
                user.setVerification("Verified");
                session.update(user);
                session.getTransaction().commit();

                // Redirect to success page
                response.sendRedirect("verify-success.html");
            } else {
                // Redirect to failure page if code is invalid
                response.sendRedirect("verify-failure.html");
            }
            session.close();
        }
    }
}
