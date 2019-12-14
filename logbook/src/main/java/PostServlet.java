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

import gr.csd.uoc.cs359.winter2019.logbook.db.PostDB;
import gr.csd.uoc.cs359.winter2019.logbook.db.UserDB;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONErrorResponse;
import gr.csd.uoc.cs359.winter2019.logbook.model.JSONResponse;
import gr.csd.uoc.cs359.winter2019.logbook.model.Post;
import gr.csd.uoc.cs359.winter2019.logbook.model.User;

/**
 * Servlet implementation class PostServlet
 */
@WebServlet("/post")
@MultipartConfig
public class PostServlet extends HttpServlet {

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

        String mode = req.getParameter("mode");
        String username = req.getParameter("username");
        String postId = req.getParameter("id");

        PrintWriter out = resp.getWriter();

        String username1 = (String) session.getAttribute("username");


        if (username1 == null) {
            JSONErrorResponse jsonErrorResponse = new JSONErrorResponse("You are not logged in", 400);
            resp.setStatus(400);
            out.print(gson.toJson(jsonErrorResponse));
            Logger.getLogger(SignInServlet.class.getName()).log(Level.INFO, "No cookie with username");
        } else {
            Logger.getLogger(SignInServlet.class.getName()).log(Level.INFO, "cookie with username");
            User currentUser = null;
            try {
                currentUser = UserDB.getUser(username1);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (currentUser != null) {
                try {
                    JSONResponse response = null;
                    if (mode != null && mode.equals("top_ten")) {
                        if (username != null) {
                            // TODO: check if username exists
//                            PostDB.getTop10RecentPostsOfUser(username);
                            response = new JSONResponse("Posts Found", 200, PostDB.getTop10RecentPostsOfUser(username));
                        } else {
//                            PostDB.getTop10RecentPosts();
                            response = new JSONResponse("Posts Found", 200, PostDB.getTop10RecentPosts());
                        }
                    } else {
                        if (postId != null) {
                            response = new JSONResponse("Post Found", 200, PostDB.getPost(Integer.parseInt(postId)));
                        }
                    }
                    out.print(gson.toJson(response));

                } catch (ClassNotFoundException e) {

                    JSONErrorResponse errorResponse = new JSONErrorResponse("Data may no longer be available", 400);
                    out.print(gson.toJson(errorResponse));
                    e.printStackTrace();
                }
            } else {
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

        String user_name = getPartValue(req.getPart("username"));
        String description = getPartValue(req.getPart("description"));
        String resource_URL = getPartValue(req.getPart("resource_URL"));
        String image_URL = getPartValue(req.getPart("image_URL"));
        String image_base64 = getPartValue(req.getPart("image_base64"));
        String latitude = getPartValue(req.getPart("latitude"));
        String longitude = getPartValue(req.getPart("longitude"));
        String created_at = getPartValue(req.getPart("created_at"));


        Post tmpPost = new Post();
        tmpPost.setUserName(user_name);
        tmpPost.setDescription(description);
        tmpPost.setResourceURL(resource_URL);
        tmpPost.setImageURL(image_URL);
        tmpPost.setImageBase64(image_base64);
        tmpPost.setLatitude(latitude);
        tmpPost.setLongitude(longitude);
        tmpPost.setCreatedAt(created_at);

        String username_ = (String) session.getAttribute("username");


        if (username_ != null) {
            User currentUser = null;
            try {
                currentUser = UserDB.getUser(user_name);
                if (currentUser != null) {
                    try {
                        PostDB.addPost(tmpPost);
                        JSONResponse resp1 = new JSONResponse("Successfully post in data base", 200, tmpPost);
                        resp.setStatus(200);
                        out.print(gson.toJson(resp1));
                    } catch (ClassNotFoundException e) {
                        JSONErrorResponse resp1 = new JSONErrorResponse("Post didn't add to the database", 500);
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

        String delete_id = req.getParameter("id");

        String username_ = (String) session.getAttribute("username");

        User currentUser = null;
        try {
            Post tmpPost = PostDB.getPost(Integer.parseInt(delete_id));
            currentUser = UserDB.getUser(username_);

            if(currentUser == null){
                JSONErrorResponse resp1 = new JSONErrorResponse("You're not logged in", 400);
                resp.setStatus(400);
                out.print(gson.toJson(resp1));
            }
            if(tmpPost == null){
                JSONErrorResponse resp1 = new JSONErrorResponse("post not found", 404);
                resp.setStatus(404);
                out.print(gson.toJson(resp1));
            }
            else if (currentUser.getUserName().equals(tmpPost.getUserName())) {
                PostDB.deletePost(Integer.parseInt(delete_id));
                JSONErrorResponse resp1 = new JSONErrorResponse("Successfully delete post", 200);
                resp.setStatus(200);
                out.print(gson.toJson(resp1));
            }else{
                JSONErrorResponse resp1 = new JSONErrorResponse("post is not yours to delete", 404);
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

