import com.google.gson.Gson;
import gr.csd.uoc.cs359.winter2019.logbook.db.UserDB;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONErrorResponse;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/checkExistence")
@MultipartConfig
public class CheckExistenceServlet extends HttpServlet {

    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");

        PrintWriter out = resp.getWriter();
        if (req.getParameter("username") == null && req.getParameter("email") == null) {
            JSONErrorResponse jsonErrorResponse = new JSONErrorResponse("This is not a username or email", 400);
            resp.setStatus(400);
            out.print(gson.toJson(jsonErrorResponse));
        } else {
            try {
                if (req.getParameter("username") != null) {
                    String username = req.getParameter("username").trim();
                    if (UserDB.checkValidUserName(username)) {
                        JSONResponse jsonResponse = new JSONResponse("Valid Username", 200, username);
                        resp.setStatus(200);
                        out.print(gson.toJson(jsonResponse));
                    } else {
                        JSONErrorResponse jsonErrorResponse = new JSONErrorResponse("Username already in use", 400);
                        resp.setStatus(400);
                        out.print(gson.toJson(jsonErrorResponse));
                    }
                } else if (req.getParameter("email") != null) {
                    String email = req.getParameter("email").trim();

                    if (UserDB.checkValidEmail(email)) {
                        JSONResponse jsonResponse = new JSONResponse("Valid Email", 200, email);
                        resp.setStatus(200);
                        out.print(gson.toJson(jsonResponse));
                    } else {
                        JSONErrorResponse jsonErrorResponse = new JSONErrorResponse("Email already in use", 400);
                        resp.setStatus(400);
                        out.print(gson.toJson(jsonErrorResponse));
                    }
                }
            } catch (ClassNotFoundException e) {
                JSONErrorResponse jsonErrorResponse = new JSONErrorResponse("Server Error", 500);
                resp.setStatus(500);
                out.print(gson.toJson(jsonErrorResponse));
            }


        }
    }
}
