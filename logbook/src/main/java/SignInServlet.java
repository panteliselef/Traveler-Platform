import com.google.gson.Gson;
import gr.csd.uoc.cs359.winter2019.logbook.db.UserDB;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONErrorResponse;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONResponse;
import gr.csd.uoc.cs359.winter2019.logbook.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/signin")
@MultipartConfig
public class SignInServlet extends HttpServlet {

    private Gson gson = new Gson();

    private String getPartValue(Part part) throws IOException {
        if (part == null) return null;
        return new String(part.getInputStream().readAllBytes());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);

        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");

        PrintWriter out = resp.getWriter();

        String username = (String) session.getAttribute("username");
        if (username == null) {
            JSONErrorResponse jsonErrorResponse = new JSONErrorResponse("You are not logged in", 400);
            resp.setStatus(400);
            out.print(gson.toJson(jsonErrorResponse));
            Logger.getLogger(SignInServlet.class.getName()).log(Level.INFO, "No cookie with username");
        } else {
            Logger.getLogger(SignInServlet.class.getName()).log(Level.INFO, "cookie with username");
            User currentUser = null;
            try {
                currentUser = UserDB.getUser(username);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (currentUser != null) {
                JSONResponse response = new JSONResponse("User already Signed in", 200, currentUser);
                resp.setStatus(200);
                out.print(gson.toJson(response));
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession(true);

        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");


        String username = getPartValue(req.getPart("username"));
        String password = getPartValue(req.getPart("password"));

        PrintWriter out = resp.getWriter();

        String username_ = (String) session.getAttribute("username");
        if (username_ != null) {
            User currentUser = null;
            try {
                currentUser = UserDB.getUser(username);
                if (currentUser != null) {
                    JSONResponse response = new JSONResponse("User already Signed in", 200, currentUser);
                    resp.setStatus(200);
                    out.print(gson.toJson(response));
                    return;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (password == null || password.trim().isEmpty()) {
            JSONErrorResponse errorResponse = new JSONErrorResponse("Missing Password", 400);
            resp.setStatus(400);
            out.print(gson.toJson(errorResponse));
        } else {
            if (username == null) {
                JSONErrorResponse errorResponse = new JSONErrorResponse("Missing Username or Email", 400);
                resp.setStatus(400);
                out.print(gson.toJson(errorResponse));
            } else {
                if (!username.isEmpty()) {
                    try {
                        User currentUser = UserDB.getUser(username);
                        if (currentUser != null && currentUser.getPassword().equals(password)) {

                            session.setAttribute("username", username);
                            session.setMaxInactiveInterval(60 * 10); // 10 minutes
                            JSONResponse response = new JSONResponse("Signed in successfully", 200, currentUser);
                            resp.setStatus(200);
                            out.print(gson.toJson(response));
                        } else {
                            JSONErrorResponse errorResponse = new JSONErrorResponse("Invalid Password or Username", 400);
                            resp.setStatus(400);
                            out.print(gson.toJson(errorResponse));
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        out.flush();

    }
}
