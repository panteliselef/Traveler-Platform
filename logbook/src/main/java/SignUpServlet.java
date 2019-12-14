import com.google.gson.Gson;
import gr.csd.uoc.cs359.winter2019.logbook.db.UserDB;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONResponse;
import gr.csd.uoc.cs359.winter2019.logbook.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/SignUpServlet")
@MultipartConfig
public class SignUpServlet extends HttpServlet {

    private String getPartValue(Part part) throws IOException{
        if(part == null) return null;
        return new String(part.getInputStream().readAllBytes());
    }

    private Boolean cookieExists(Cookie[] cookies, String name){
        if(cookies == null || cookies.length ==0) return false;
        for (Cookie cookie:cookies
             ) {
            if (cookie.getName().equals(name)) return true;
        }
        return false;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials","true");
        PrintWriter out = resp.getWriter();



//        if(req.getPart("session") != null){
//            String session = getPartValue(req.getPart("session"));
//            if(session.equals("end")){
//                Cookie[] cookies = req.getCookies();
//                for (Cookie cookie:cookies
//                     ) {
//                    out.println(cookie.getName());
//                    if(cookie.getName().equals("username")) {
//                        cookie.setMaxAge(0);
//                        resp.addCookie(cookie);
//                    }
//                }
//
//            }
//            return;
//        }

        String username = getPartValue(req.getPart("username"));
        String password = getPartValue(req.getPart("password"));
        String email = getPartValue(req.getPart("email"));
        String firstName = getPartValue(req.getPart("firstname"));
        String lastName = getPartValue(req.getPart("lastname"));
        String birthday = getPartValue(req.getPart("birthday"));
        String registeredSince = getPartValue(req.getPart("registeredSince"));
        String gender = getPartValue(req.getPart("gender"));
        String country = getPartValue(req.getPart("country"));
        String city = getPartValue(req.getPart("city"));
        String address = getPartValue(req.getPart("address"));
        String job = getPartValue(req.getPart("job"));
        String interests = getPartValue(req.getPart("interests"));
        String info = getPartValue(req.getPart("info"));

        User tmpUser = new User(username,email,password,firstName,lastName,birthday,job,country,city);

        tmpUser.setRegisteredSince(registeredSince);
        tmpUser.setGender(gender);
        tmpUser.setAddress(address);

        tmpUser.setInterests(interests);
        tmpUser.setInfo(info);


        Gson gson = new Gson();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");


        try {
            tmpUser.checkFields();
            if (!UserDB.checkValidUserName(username)) {
                throw new Exception("username already in use");
            }else if(!UserDB.checkValidEmail(email)){
                throw new Exception("email already in use");
            }
            UserDB.addUser(tmpUser); // adding to DB

            HttpSession session = req.getSession(true);
            session.setAttribute("username", username);
            session.setMaxInactiveInterval(60 * 60 * 100); // 100 hours

            JSONResponse response = new JSONResponse("Successful sign up",200,tmpUser);
            resp.setStatus(200);
            out.print(gson.toJson(response));
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        catch (Exception e) {
            JSONResponse errorRes = new JSONResponse(e.getMessage(),400,tmpUser);
            resp.setStatus(400);
            out.print(gson.toJson(errorRes));
        }
        out.flush();
    }
}
