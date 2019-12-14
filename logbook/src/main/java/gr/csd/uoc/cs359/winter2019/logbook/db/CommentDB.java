/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csd.uoc.cs359.winter2019.logbook.db;

import gr.csd.uoc.cs359.winter2019.logbook.model.Comment;
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
public class CommentDB {

    /**
     * Get all photos
     *
     * @return
     * @throws ClassNotFoundException
     */
    public static List<Comment> getComments() throws ClassNotFoundException {
        List<Comment> comments = new ArrayList<>();

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM comments;");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            while (res.next() == true) {
                Comment comment = new Comment();
                comment.setID(res.getInt("comment_id"));
                comment.setUserName(res.getString("user_name"));
                comment.setPostID(res.getInt("post_id"));
                comment.setCreated(res.getString("created_at"));
                comment.setModified(res.getString("modified_at"));
                comment.setComment(res.getString("comment"));
                comments.add(comment);
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(CommentDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return comments;
    }

    /**
     * Get comment for specific photo
     *
     * @param postID
     * @return
     * @throws ClassNotFoundException
     */
    public static List<Comment> getComments(int postID) throws ClassNotFoundException {
        List<Comment> comments = new ArrayList<>();
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM comments WHERE ")
                    .append(" post_id = ").append("'").append(postID).append("';");;

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            while (res.next() == true) {
                Comment comment = new Comment();
                comment.setID(res.getInt("comment_id"));
                comment.setUserName(res.getString("user_name"));
                comment.setPostID(res.getInt("post_id"));
                comment.setCreated(res.getString("created_at"));
                comment.setModified(res.getString("modified_at"));
                comment.setComment(res.getString("comment"));
                comments.add(comment);
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(CommentDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return comments;
    }

    /**
     * Get comment
     *
     * @param commentID
     * @return
     * @throws ClassNotFoundException
     */
    public static Comment getComment(int commentID) throws ClassNotFoundException {
        Comment comment = new Comment();
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM comments ")
                    .append(" WHERE ")
                    .append(" comment_id = ").append("'").append(commentID).append("';");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            if (res.next() == true) {
                comment.setID(res.getInt("comment_id"));
                comment.setUserName(res.getString("user_name"));
                comment.setPostID(res.getInt("post_id"));
                comment.setCreated(res.getString("created_at"));
                comment.setModified(res.getString("modified_at"));
                comment.setComment(res.getString("comment"));
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(CommentDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return comment;
    }

    /**
     * Establish a database connection and add the comment into the database.
     *
     * @param comment
     * @throws ClassNotFoundException
     */
    public static void addComment(Comment comment) throws ClassNotFoundException {
        // Check that we have all we need
        try {
            comment.checkFields();

        } catch (Exception ex) {
            // Log exception
            Logger.getLogger(CommentDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());

            insQuery.append("INSERT INTO ")
                    .append(" comments (user_name, post_id, created_at, modified_at, created_by, comment) ")
                    .append(" VALUES (")
                    //.append("'").append(comment.getID()).append("',")
                    .append("'").append(comment.getUserName()).append("',")
                    .append("'").append(comment.getPostID()).append("',")
                    .append("'").append(timestamp).append("',")
                    .append("'").append(timestamp).append("',")
                    .append("'").append(CS359DB.getUserName()).append("',")
                    .append("'").append(comment.getComment()).append("');");

            String generatedColumns[] = {"comment_id"};
            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString(), generatedColumns);
            stmtIns.executeUpdate();

            // Get information magically completed from database
            ResultSet rs = stmtIns.getGeneratedKeys();
            if (rs.next()) {
                // Update value of setID based on database
                int id = rs.getInt(1);
                comment.setID(id);
            }

            System.out.println("#DB: The comment was successfully added in the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(CommentDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Updates information for specific comment
     *
     * @param comment
     * @throws ClassNotFoundException
     */
    public static void updateComment(Comment comment) throws ClassNotFoundException {
        // Check that we have all we need
        try {
            comment.checkFields();

        } catch (Exception ex) {
            // Log exception
            Logger.getLogger(CommentDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE comments ")
                    .append(" SET ")
                    .append(" COMMENT = ").append("'").append(comment.getComment()).append("'")
                    .append(" WHERE comment_id = ").append("'").append(comment.getID()).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: The comment was successfully updated in the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(CommentDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Delete specific comment
     *
     * @param comment
     * @throws ClassNotFoundException
     */
    public static void deleteComment(Comment comment) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("DELETE FROM comments ")
                    .append(" WHERE ")
                    .append(" comment_id = ").append("'").append(comment.getID()).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: The comment was successfully deleted from the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(CommentDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Delete specific comment
     *
     * @param id
     * @throws ClassNotFoundException
     */
    public static void deleteComment(int id) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("DELETE FROM comments ")
                    .append(" WHERE ")
                    .append(" comment_id = ").append("'").append(id).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: The comment was successfully deleted from the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(CommentDB.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
