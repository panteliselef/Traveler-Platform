import com.google.gson.Gson;

import gr.csd.uoc.cs359.winter2019.logbook.db.CommentDB;
import gr.csd.uoc.cs359.winter2019.logbook.db.PostDB;
import gr.csd.uoc.cs359.winter2019.logbook.db.RatingDB;
import gr.csd.uoc.cs359.winter2019.logbook.db.UserDB;
import gr.csd.uoc.cs359.winter2019.logbook.model.Comment;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONErrorResponse;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONResponse;
import gr.csd.uoc.cs359.winter2019.logbook.model.Post;
import gr.csd.uoc.cs359.winter2019.logbook.model.Rating;
import gr.csd.uoc.cs359.winter2019.logbook.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/user")
@MultipartConfig
public class UserServlet extends HttpServlet {

    Gson gson = new Gson();

    private String getPartValue(Part part) throws IOException{
        if(part == null) return null;
        return new String(part.getInputStream().readAllBytes());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials","true");
        PrintWriter out = resp.getWriter();

        String username = req.getParameter("username");
        username = username.trim();
        try {
            User user = UserDB.getUser(username);
            if (user == null){
                JSONErrorResponse jsonErrorResponse = new JSONErrorResponse("user not found",404);
                resp.setStatus(404);
                out.print(gson.toJson(jsonErrorResponse));
            }else{
                JSONResponse jsonResponse = new JSONResponse("User found",200,user);
                resp.setStatus(200);
                out.print(gson.toJson(jsonResponse));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials","true");
        PrintWriter out = resp.getWriter();
        List<Post> allPosts = new ArrayList<>();
        List<Comment> allComments = new ArrayList<>();
        List<Rating> allRatings = new ArrayList<>();


        String username = getPartValue(req.getPart("username"));
        String password = getPartValue(req.getPart("password"));

        if(username == null){
            JSONErrorResponse jsonErrorResponse = new JSONErrorResponse("Missing username",400);
            resp.setStatus(400);
            out.print(gson.toJson(jsonErrorResponse));
            return;
        }
        try {
            User user = UserDB.getUser(username);
            if(password.equals(user.getPassword())){
            	
            	//delete all posts of a user
            	allPosts = PostDB.getPosts();
            	allPosts.forEach(post ->{
            		if(post.getUserName().equals(username)) {
            			try {
							PostDB.deletePost(post);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            		}
            	});
            	
            	//delete all comments of a user
            	allComments = CommentDB.getComments();
            	allComments.forEach(comment ->{
            		if(comment.getUserName().equals(username)) {
            			try {
							CommentDB.deleteComment(comment);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            		}
            	});
            	
            	//delete all ratings of a user
            	allRatings = RatingDB.getRatings();
            	allRatings.forEach(rating -> {
            		if(rating.getUserName().equals(username));
            		try {
						RatingDB.deleteRating(rating);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	});
            	
            	
            	            	
                UserDB.deleteUser(user);
                HttpSession session = req.getSession();
                session.invalidate();
                JSONResponse response = new JSONResponse("User has been deleted",200,username);
                resp.setStatus(200);
                out.print(gson.toJson(response));
            }else{
                JSONErrorResponse response = new JSONErrorResponse("username and password do not match",400);
                resp.setStatus(400);
                out.print(gson.toJson(response));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        out.flush();
    }
}