

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.google.gson.Gson;

import gr.csd.uoc.cs359.winter2019.logbook.db.CommentDB;
import gr.csd.uoc.cs359.winter2019.logbook.db.PostDB;
import gr.csd.uoc.cs359.winter2019.logbook.db.UserDB;
import gr.csd.uoc.cs359.winter2019.logbook.model.Comment;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONErrorResponse;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONResponse;
import gr.csd.uoc.cs359.winter2019.logbook.model.Post;
import gr.csd.uoc.cs359.winter2019.logbook.model.User;

/**
 * Servlet implementation class CommetServlet
 */
@WebServlet("/comments")
@MultipartConfig
public class CommetServlet extends HttpServlet {
	
    private Gson gson = new Gson();

    private String getPartValue(Part part) throws IOException {
        if (part == null) return null;
        return new String(part.getInputStream().readAllBytes());
    
    }
   
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
        HttpSession session = req.getSession(true);

        resp.addHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        resp.addHeader("Access-Control-Max-Age", "1728000");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        
        String ID = req.getParameter("postID");
        
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
                    JSONResponse response = new JSONResponse("Comments Found", 200, CommentDB.getComments(Integer.parseInt(ID)));
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
        String postID = getPartValue(req.getPart("postID"));
        String comment = getPartValue(req.getPart("comment"));
        String createdAt = getPartValue(req.getPart("createdAt"));
        String modifiedAt = createdAt;

        Comment tmpComment = new Comment();
        
        tmpComment.setUserName(username);
        tmpComment.setPostID(Integer.parseInt(postID));
        tmpComment.setComment(comment);
        tmpComment.setCreated(createdAt);
        tmpComment.setModified(modifiedAt);
        
        String username_Session = (String) session.getAttribute("username");
        
        if (username_Session != null) {
            User currentUser = null;
            try{
                currentUser = UserDB.getUser(username);
                if (currentUser != null) {
                    try {
                        CommentDB.addComment(tmpComment);
                        JSONResponse resp1 = new JSONResponse("Successfully add comment in dataBase", 200, tmpComment);
                        resp.setStatus(200);
                        out.print(gson.toJson(resp1));
                    } catch (ClassNotFoundException e) {
                        JSONErrorResponse resp1 = new JSONErrorResponse("Comment didn't add to the database", 500);
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
        
        String ID = getPartValue(req.getPart("commentID"));
        
        String username_Session = (String) session.getAttribute("username");
        
        User currentUser = null;
        
        try {
            Comment tmpComment = CommentDB.getComment(Integer.parseInt(ID));
            currentUser = UserDB.getUser(username_Session);

            if(currentUser == null){
                JSONErrorResponse resp1 = new JSONErrorResponse("You're not logged in", 400);
                resp.setStatus(400);
                out.print(gson.toJson(resp1));
            }
            if(tmpComment == null){
                JSONErrorResponse resp1 = new JSONErrorResponse("Comment not found", 404);
                resp.setStatus(404);
                out.print(gson.toJson(resp1));
            }else if (currentUser.getUserName().equals(tmpComment.getUserName())) {
                CommentDB.deleteComment(Integer.parseInt(ID));
                JSONErrorResponse resp1 = new JSONErrorResponse("Successfully delete comment", 200);
                resp.setStatus(200);
                out.print(gson.toJson(resp1));
            }else{
                JSONErrorResponse resp1 = new JSONErrorResponse("comment is not yours to delete", 404);
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
        String postID = getPartValue(req.getPart("postID"));
        String comment = getPartValue(req.getPart("comment"));
        String createdAt = getPartValue(req.getPart("createdAt"));
        String modifiedAt = getPartValue(req.getPart("modifiedAt"));
        String ID = getPartValue(req.getPart("ID"));
        
        Comment UpdateComment = new Comment();
        
        UpdateComment.setUserName(username);
        UpdateComment.setPostID(Integer.parseInt(postID));
        UpdateComment.setComment(comment);
        UpdateComment.setCreated(createdAt);
        UpdateComment.setModified(modifiedAt);
        UpdateComment.setID(Integer.parseInt(ID));
        
        String username_Session = (String) session.getAttribute("username");
        User currentUser = null;
        
        
        try {
            Comment tmpComment = CommentDB.getComment(Integer.parseInt(ID));
            currentUser = UserDB.getUser(username_Session);

            if(currentUser == null){
                JSONErrorResponse resp1 = new JSONErrorResponse("You're not logged in", 400);
                resp.setStatus(400);
                out.print(gson.toJson(resp1));
            }
            if(tmpComment == null){
                JSONErrorResponse resp1 = new JSONErrorResponse("Comment not found", 404);
                resp.setStatus(404);
                out.print(gson.toJson(resp1));
            }else if (currentUser.getUserName().equals(tmpComment.getUserName())) {
                CommentDB.updateComment(UpdateComment);
                JSONErrorResponse resp1 = new JSONErrorResponse("Successfully update comment", 200);
                resp.setStatus(200);
                out.print(gson.toJson(resp1));
            }else{
                JSONErrorResponse resp1 = new JSONErrorResponse("comment is not yours to update", 404);
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
	

}
