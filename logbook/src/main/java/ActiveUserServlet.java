import com.google.gson.Gson;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/active")
@MultipartConfig
public class ActiveUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        resp.getWriter().print(new Gson().toJson(new JSONResponse("get active group",200,getServletContext().getAttribute("activeUsers"))));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");

        ArrayList<String> activeUsers = (ArrayList<String>) getServletContext().getAttribute("activeUsers");

        if(activeUsers == null){
            activeUsers=  new ArrayList<>();
        }
        activeUsers.add(username);

        getServletContext().setAttribute("activeUsers",activeUsers);

        resp.getWriter().print(new Gson().toJson(new JSONResponse("Set user in active group",200,username)));
    }
}
