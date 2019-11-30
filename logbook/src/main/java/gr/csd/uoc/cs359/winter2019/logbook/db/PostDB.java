/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csd.uoc.cs359.winter2019.logbook.db;

import gr.csd.uoc.cs359.winter2019.logbook.model.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author papadako
 */
public class PostDB {

    /**
     * Get post
     *
     * @return
     * @throws ClassNotFoundException
     */
    public static List<Post> getPosts() throws ClassNotFoundException {
        List<Post> posts = new ArrayList<>();

        Statement stmt = null;
        Connection con = null;

        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM posts;");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            while (res.next() == true) {
                Post post = new Post();
                post.setPostID(res.getInt("post_id"));
                post.setUserName(res.getString("user_name"));
                post.setDescription(res.getString("description"));
                post.setResourceURL(res.getString("resource_URL"));
                post.setImageURL(res.getString("image_URL"));
                post.setImageBase64(res.getString("image_base64"));
                post.setLatitude(res.getString("latitude"));
                post.setLongitude(res.getString("longitude"));
                post.setCreatedAt(res.getString("created_at"));
                posts.add(post);
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return posts;
    }

    /**
     * Get post with id
     *
     * @param postid
     * @return
     * @throws ClassNotFoundException
     */
    public static Post getPost(Integer postID) throws ClassNotFoundException {
        Post post = null;
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();

            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM posts ")
                    .append(" WHERE ")
                    .append(" post_id = ").append("'").append(postID).append("';");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            if (res.next() == true) {
                post = new Post();
                post.setPostID(res.getInt("post_id"));
                post.setUserName(res.getString("user_name"));
                post.setDescription(res.getString("description"));
                post.setResourceURL(res.getString("resource_URL"));
                post.setImageURL(res.getString("image_URL"));
                post.setImageBase64(res.getString("image_base64"));
                post.setLatitude(res.getString("latitude"));
                post.setLongitude(res.getString("longitude"));
                post.setCreatedAt(res.getString("created_at"));
            } else {
                System.out.println("Post with post id" + postID + "was not found");
            }
        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PostDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return post;
    }

    /**
     * Establish a database connection and add the post in the database.
     *
     * @param post
     * @throws ClassNotFoundException
     */
    public static void addPost(Post post) throws ClassNotFoundException {
        // Check that we have all we need
        try {
            post.checkFields();

        } catch (Exception ex) {
            // Log exception
            Logger.getLogger(PostDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("INSERT INTO ")
                    .append(" posts (user_name, description, resource_URL, "
                            + "image_URL, image_base64, latitude, longitude, created_at, "
                            + "created_by) ")
                    .append(" VALUES (")
                    .append("'").append(post.getUserName()).append("',")
                    .append("'").append(post.getDescription()).append("',")
                    .append("'").append(post.getResourceURL()).append("',")
                    .append("'").append(post.getImageURL()).append("',")
                    .append("'").append(post.getImageBase64()).append("',")
                    .append("'").append(post.getLatitude()).append("',")
                    .append("'").append(post.getLongitude()).append("',")
                    .append("'").append(timestamp).append("',")
                    .append("'").append(CS359DB.getUserName()).append("');");

            String generatedColumns[] = {"post_id"};
            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString(), generatedColumns);
            stmtIns.executeUpdate();

            // Get information magically completed from database
            ResultSet rs = stmtIns.getGeneratedKeys();
            if (rs.next()) {
                // Update value of setID based on database
                int id = rs.getInt(1);
                post.setPostID(id);
            }

            System.out.println("#DB: The post was successfully added in the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Updates information for specific post
     *
     * @param post
     * @throws ClassNotFoundException
     */
    public static void updatePost(Post post) throws ClassNotFoundException {
        // Check that we have all we need
        try {
            post.checkFields();

        } catch (Exception ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        }

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE posts ")
                    .append(" SET ")
                    .append(" description = ").append("'").append(post.getDescription()).append("',")
                    .append(" resource_URL = ").append("'").append(post.getResourceURL()).append("',")
                    .append(" image_URL = ").append("'").append(post.getImageURL()).append("',")
                    .append(" image_base64 = ").append("'").append(post.getImageBase64()).append("',")
                    .append(" latitude = ").append("'").append(post.getLatitude()).append("',")
                    .append(" longitude = ").append("'").append(post.getLongitude()).append("'")
                    .append(" WHERE post_id= ").append("'").append(post.getPostID()).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: The post was successfully updated in the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PostDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Returns 10 most recent posts
     *
     * @param post
     * @throws ClassNotFoundException
     */
    public static List<Post> getTop10RecentPosts() throws ClassNotFoundException {
        List<Post> posts = new ArrayList<>();

        Statement stmt = null;
        Connection con = null;

        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM posts ORDER BY created_at DESC LIMIT 10;");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            while (res.next() == true) {
                Post post = new Post();
                post.setPostID(res.getInt("post_id"));
                post.setUserName(res.getString("user_name"));
                post.setDescription(res.getString("description"));
                post.setResourceURL(res.getString("resource_URL"));
                post.setImageURL(res.getString("image_URL"));
                post.setImageBase64(res.getString("image_base64"));
                post.setLatitude(res.getString("latitude"));
                post.setLongitude(res.getString("longitude"));
                post.setCreatedAt(res.getString("created_at"));
                posts.add(post);
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PostDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return posts;
    }

    /**
     * Returns 10 most recent posts of user
     *
     * @param userName
     * @throws ClassNotFoundException
     */
    public static List<Post> getTop10RecentPostsOfUser(String userName) throws ClassNotFoundException {
        List<Post> posts = new ArrayList<>();

        Statement stmt = null;
        Connection con = null;

        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM posts")
                    .append(" WHERE  user_name = ").append("'").append(userName).append("'")
                    .append(" ORDER BY created_at DESC  LIMIT 10;");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            while (res.next() == true) {
                Post post = new Post();
                post.setPostID(res.getInt("post_id"));
                post.setUserName(res.getString("user_name"));
                post.setDescription(res.getString("description"));
                post.setResourceURL(res.getString("resource_URL"));
                post.setImageURL(res.getString("image_URL"));
                post.setImageBase64(res.getString("image_base64"));
                post.setLatitude(res.getString("latitude"));
                post.setLongitude(res.getString("longitude"));
                post.setCreatedAt(res.getString("created_at"));
                posts.add(post);
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PostDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return posts;
    }

    /**
     * Delete specific post
     *
     * @param post
     * @throws ClassNotFoundException
     */
    public static void deletePost(Post post) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("DELETE FROM posts ")
                    .append(" WHERE ")
                    .append(" post_id = ").append("'").append(post.getPostID()).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: The post was successfully deleted from the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PostDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Delete specific post
     *
     * @param post
     * @throws ClassNotFoundException
     */
    public static void deletePost(Integer postID) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("DELETE FROM posts ")
                    .append(" WHERE ")
                    .append(" post_id = ").append("'").append(postID).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: The post was successfully deleted from the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(PostDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Close db connection
     *
     * @param stmt
     * @param con
     */
    private static void closeDBConnection(Statement stmt, Connection con) {
        // Close connection
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                Logger.getLogger(PostDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(PostDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
