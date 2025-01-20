package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import dto.User_DTO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "CheckSignedInUserDetails", urlPatterns = {"/CheckSignedInUserDetails"})
public class CheckSignedInUserDetails extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new Gson();

        try {
            if (request.getSession().getAttribute("user") != null) {
                // Already Signed In
                User_DTO user_DTO = (User_DTO) request.getSession().getAttribute("user");
                response_DTO.setSuccess(true);
                response_DTO.setContent(user_DTO);
            } else {
                // Not Signed In
                response_DTO.setSuccess(false);
                response_DTO.setContent("Not Signed In");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response_DTO.setSuccess(false);
            response_DTO.setContent("An error occurred.");
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
    }
}
