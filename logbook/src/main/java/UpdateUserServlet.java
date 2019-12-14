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


@WebServlet("/updateUser")
@MultipartConfig
public class UpdateUserServlet extends HttpServlet {

    private String getPartValue(Part part) throws IOException {
        if (part == null) return null;
        return new String(part.getInputStream().readAllBytes());
    }

    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");


        PrintWriter out = resp.getWriter();

        String username = getPartValue(req.getPart("username"));
        String password = getPartValue(req.getPart("password"));
        String email = getPartValue(req.getPart("email"));
        String firstName = getPartValue(req.getPart("firstname"));
        String lastName = getPartValue(req.getPart("lastname"));
        String birthdate = getPartValue(req.getPart("birthdate"));
        String registeredSince = getPartValue(req.getPart("registeredSince"));
        String gender = getPartValue(req.getPart("gender"));
        String country = getPartValue(req.getPart("country"));
        String city = getPartValue(req.getPart("city"));
        String address = getPartValue(req.getPart("address"));
        String job = getPartValue(req.getPart("job"));
        String info = getPartValue(req.getPart("info"));
        String interests = getPartValue(req.getPart("interests"));

        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBirthDate(birthdate);
        user.setRegisteredSince(registeredSince);
        user.setGender(gender.toLowerCase());
        user.setCountry(country);
        user.setTown(city);
        user.setOccupation(job);
        user.setInfo(info);
        user.setInterests(interests);
        user.setAddress(address);


        try {
            user.checkFields();
            UserDB.updateUser(user);
            JSONResponse response = new JSONResponse("test", 200, user);
            out.print(gson.toJson(response));
        } catch (ClassNotFoundException e) {
            JSONErrorResponse errorResponse = new JSONErrorResponse(e.getMessage(), 500);
            resp.setStatus(500);
            out.print(gson.toJson(errorResponse));
        } catch (Exception e) {
            JSONResponse errorRes = new JSONResponse(e.getMessage(),400,user);
            resp.setStatus(400);
            out.print(gson.toJson(errorRes));
        }
    }
}
