import com.google.gson.Gson;
import gr.csd.uoc.cs359.winter2019.logbook.db.UserDB;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONErrorResponse;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONResponse;
import gr.csd.uoc.cs359.winter2019.logbook.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/getUsers")
@MultipartConfig
public class GetUsers extends HttpServlet {
    private Gson gson = new Gson();

    private String getPartValue(Part part) throws IOException {
        if (part == null) return null;
        return new String(part.getInputStream().readAllBytes());
    }

    private String getValidHTML(String input) {
        StringBuffer filtered =
                new StringBuffer(input.length());
        char c;
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            switch (c) {
                case '<':
                    filtered.append("&lt;");
                    break;
                case '>':
                    filtered.append("&gt;");
                    break;
                case '"':
                    filtered.append("&quot;");
                    break;
                case '&':
                    filtered.append("&amp;");
                    break;
                default:
                    filtered.append(c);
            }
        }
        return filtered.toString();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");


        String username = getPartValue(req.getPart("username"));
        String password = getPartValue(req.getPart("password"));

        PrintWriter out = resp.getWriter();
        if (password == null) {
            JSONErrorResponse errorResponse = new JSONErrorResponse("Missing Password", 400);
            resp.setStatus(400);
            out.print(gson.toJson(errorResponse));
        } else {
            if (username == null && username.trim().isEmpty()) {
                JSONErrorResponse errorResponse = new JSONErrorResponse("Missing Username", 400);
                resp.setStatus(400);
                out.print(gson.toJson(errorResponse));
            } else {
                try {
                    User currentUser = UserDB.getUser(username);
                    if (currentUser != null && currentUser.getPassword().equals(password)) {
                        ArrayList<User> users = (ArrayList<User>) UserDB.getUsers();
                        users.forEach(user -> {
                            user.setInfo(getValidHTML(user.getInfo()));
                        });
                        JSONResponse response = new JSONResponse("Fetched users successfully", 200, users);
                        resp.setStatus(200);
                        out.print(gson.toJson(response));
                    } else {
//                        JSONErrorResponse errorResponse = new JSONErrorResponse("Invalid Password or Username", 400);
                        JSONResponse errorResponse = new JSONResponse("Invalid Password", 400, currentUser);
                        resp.setStatus(400);
                        out.print(gson.toJson(errorResponse));
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }


        out.flush();

    }
}
