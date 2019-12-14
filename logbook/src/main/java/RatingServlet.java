import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.google.gson.Gson;

import gr.csd.uoc.cs359.winter2019.logbook.db.CommentDB;
import gr.csd.uoc.cs359.winter2019.logbook.db.PostDB;
import gr.csd.uoc.cs359.winter2019.logbook.db.RatingDB;
import gr.csd.uoc.cs359.winter2019.logbook.db.UserDB;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONErrorResponse;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONResponse;
import gr.csd.uoc.cs359.winter2019.logbook.model.Post;
import gr.csd.uoc.cs359.winter2019.logbook.model.Rating;
import gr.csd.uoc.cs359.winter2019.logbook.model.User;

@WebServlet("/RatingServlet")
public class RatingServlet extends HttpServlet {
	
    private Gson gson = new Gson();

    private String getPartValue(Part part) throws IOException {
        if (part == null) return null;
        return new String(part.getInputStream().readAllBytes());
    }

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
        HttpSession session = req.getSession(true);

        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");

        String id = req.getParameter("ID");

        PrintWriter out = resp.getWriter();

        String username_Session = (String) session.getAttribute("username");
        
        if (username_Session == null) {
            JSONErrorResponse jsonErrorResponse = new JSONErrorResponse("You are not logged in", 400);
            resp.setStatus(400);
            out.print(gson.toJson(jsonErrorResponse));
            Logger.getLogger(SignInServlet.class.getName()).log(Level.INFO, "No cookie with username");
        }else {
        	Logger.getLogger(SignInServlet.class.getName()).log(Level.INFO, "cookie with username");
            User currentUser = null;
            try {
                currentUser = UserDB.getUser(username_Session);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (currentUser != null) {
                try {
                    JSONResponse response = new JSONResponse("Comments Found", 200, RatingDB.getRating(Integer.parseInt(id)));
                    out.print(gson.toJson(response));

                } catch (ClassNotFoundException e) {

                    JSONErrorResponse errorResponse = new JSONErrorResponse("Data may no longer be available", 400);
                    out.print(gson.toJson(errorResponse));
                    e.printStackTrace();
                }
            } else {
            	JSONResponse response = new JSONResponse("User not exist", 400, null);
                out.print(gson.toJson(response));
            }
        }

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
        HttpSession session = req.getSession(true);

        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        PrintWriter out = resp.getWriter();
        
        String username = getPartValue(req.getPart("userName"));
        String postID = getPartValue(req.getPart("PostID"));
        String rate = getPartValue(req.getPart("rate"));
        
        Rating tmpRating = new Rating();
        tmpRating.setUserName(username);
        tmpRating.setPostID(Integer.parseInt(postID));
        tmpRating.setRate(Integer.parseInt(rate));
        
        String username_Session = (String) session.getAttribute("username");

        if (username_Session != null) {
            User currentUser = null;
            Rating currentPost = null;
            try {
                currentUser = UserDB.getUser(username);
                currentPost = RatingDB.getRating(Integer.parseInt(postID));
                if (currentUser != null && currentPost != null) {
                    try {
                        RatingDB.addRating(tmpRating);
                        JSONResponse resp1 = new JSONResponse("Successfully add rating in data base", 200, tmpRating);
                        resp.setStatus(200);
                        out.print(gson.toJson(resp1));
                    } catch (ClassNotFoundException e) {
                        JSONErrorResponse resp1 = new JSONErrorResponse("Rating didn't add to the database", 500);
                        resp.setStatus(500);
                        out.print(gson.toJson(resp1));
                        e.printStackTrace();
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } 
	}
	
	@Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession session = req.getSession(true);

        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        PrintWriter out = resp.getWriter();
        
        String id = getPartValue(req.getPart("ID"));
        String postID = getPartValue(req.getPart("postID"));

        String username_Session = (String) session.getAttribute("username");

        User currentUser = null;
        Rating tmpRating = null;
        Post tmpPost = null;
        try {
            tmpRating = RatingDB.getRating(Integer.parseInt(id));
            currentUser = UserDB.getUser(username_Session);
            tmpPost = PostDB.getPost(Integer.parseInt(postID));

            if(currentUser == null){
                JSONErrorResponse resp1 = new JSONErrorResponse("You're not logged in", 400);
                resp.setStatus(400);
                out.print(gson.toJson(resp1));
            }
            if(tmpPost == null){
                JSONErrorResponse resp1 = new JSONErrorResponse("Post not found", 404);
                resp.setStatus(404);
                out.print(gson.toJson(resp1));
            }
            if(tmpRating == null){
                JSONErrorResponse resp1 = new JSONErrorResponse("Rating not found", 404);
                resp.setStatus(404);
                out.print(gson.toJson(resp1));
            }
            else if (currentUser.getUserName().equals(tmpPost.getUserName())) {
                RatingDB.deleteRating(Integer.parseInt(id));
                JSONErrorResponse resp1 = new JSONErrorResponse("Successfully delete rating", 200);
                resp.setStatus(200);
                out.print(gson.toJson(resp1));
            }else{
                JSONErrorResponse resp1 = new JSONErrorResponse("rating is not yours to delete", 404);
                resp.setStatus(400);
                out.print(gson.toJson(resp1));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JSONErrorResponse resp1 = new JSONErrorResponse("Server Error", 500);
            resp.setStatus(500);
            out.print(gson.toJson(resp1));
        }

    }
	
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
        HttpSession session = req.getSession(true);

        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        PrintWriter out = resp.getWriter();
        
        String username = getPartValue(req.getPart("userName"));
        String postID = getPartValue(req.getPart("PostID"));
        String rate = getPartValue(req.getPart("rate"));
        
        Rating newRating = new Rating();
        newRating.setUserName(username);
        newRating.setPostID(Integer.parseInt(postID));
        newRating.setRate(Integer.parseInt(rate));
        
        String username_Session = (String) session.getAttribute("username");

        if (username_Session != null) {
            User currentUser = null;
            Rating tmpRating = null;
            Post tmpPost = null;
            
            try {
                currentUser = UserDB.getUser(username);
                tmpPost = PostDB.getPost(Integer.parseInt(postID));
                tmpRating = RatingDB.getRating(Integer.parseInt(postID));
                if (currentUser != null && tmpPost != null && tmpRating != null) {
                    try {
                        RatingDB.updateRating(newRating);
                        JSONResponse resp1 = new JSONResponse("Successfully update rating in data base", 200, newRating);
                        resp.setStatus(200);
                        out.print(gson.toJson(resp1));
                    } catch (ClassNotFoundException e) {
                        JSONErrorResponse resp1 = new JSONErrorResponse("Rating didn't update to the database", 500);
                        resp.setStatus(500);
                        out.print(gson.toJson(resp1));
                        e.printStackTrace();
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } 
	}

}
