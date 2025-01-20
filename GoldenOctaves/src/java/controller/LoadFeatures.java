package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Brand;
import entity.Category;
import entity.Colour;
import entity.Model;
import entity.Origin_Country;
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

@WebServlet(name = "LoadFeatures", urlPatterns = {"/LoadFeatures"})
public class LoadFeatures extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {        

        Gson gson = new Gson();

        Session session = HibernateUtil.getSessionFactory().openSession();

        Criteria criteria1 = session.createCriteria(Category.class);
        criteria1.addOrder(Order.asc("name"));
        List<Category> categoryList = criteria1.list();
        
        Criteria criteria2 = session.createCriteria(Brand.class);
        criteria2.addOrder(Order.asc("name"));
        List<Brand> brandList = criteria2.list();

        Criteria criteria3 = session.createCriteria(Model.class);
        criteria3.addOrder(Order.asc("name"));
        List<Model> modelList = criteria3.list();

        Criteria criteria4 = session.createCriteria(Origin_Country.class);
        criteria4.addOrder(Order.asc("id"));
        List<Origin_Country> originList = criteria4.list();

        Criteria criteria5 = session.createCriteria(Colour.class);
        criteria4.addOrder(Order.asc("id"));
        List<Colour> colourList = criteria5.list();

        Criteria criteria6 = session.createCriteria(Product_Condition.class);
        criteria6.addOrder(Order.asc("name"));
        List<Product_Condition> conditionList = criteria6.list();

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("categoryList", gson.toJsonTree(categoryList));
        jsonObject.add("brandList", gson.toJsonTree(brandList));
        jsonObject.add("modelList", gson.toJsonTree(modelList));
        jsonObject.add("originList", gson.toJsonTree(originList));
        jsonObject.add("colourList", gson.toJsonTree(colourList));
        jsonObject.add("conditionList", gson.toJsonTree(conditionList));

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(jsonObject));
        
        session.close();

    }

}

