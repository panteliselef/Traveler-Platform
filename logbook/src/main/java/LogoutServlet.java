import com.google.gson.Gson;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONErrorResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        PrintWriter out = resp.getWriter();
        if (session == null) {
            JSONErrorResponse jsonErrorResponse = new JSONErrorResponse("Unable to Log out",400);
            resp.setStatus(400);
            out.print(gson.toJson(jsonErrorResponse));
            return;
        }

        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");


        session.invalidate();


        JSONErrorResponse jsonErrorResponse = new JSONErrorResponse("Logged out",200);
        resp.setStatus(200);
        out.print(gson.toJson(jsonErrorResponse));
    }
}
