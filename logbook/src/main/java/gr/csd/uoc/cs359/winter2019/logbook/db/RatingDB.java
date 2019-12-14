/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csd.uoc.cs359.winter2019.logbook.db;

import gr.csd.uoc.cs359.winter2019.logbook.model.Rating;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author papadako
 */
public class RatingDB {

    /**
     * Get all ratings
     *
     * @param
     * @return
     * @throws ClassNotFoundException
     */
    public static List<Rating> getRatings() throws ClassNotFoundException {
        List<Rating> ratings = new ArrayList<>();

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM ratings;");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            while (res.next() == true) {
                Rating rating = new Rating();
                rating.setID(res.getInt("rating_id"));
                rating.setUserName(res.getString("user_name"));
                rating.setPostID(res.getInt("post_id"));
                rating.setRate(res.getInt("rating"));
                ratings.add(rating);
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(RatingDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return ratings;
    }

    /**
     * Get ratings for specific initiative
     *
     * @param initiativeID
     * @return
     * @throws ClassNotFoundException
     */
    public static List<Rating> getRatings(int postID) throws ClassNotFoundException {
        List<Rating> ratings = new ArrayList<>();

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM ratings WHERE ")
                    .append(" post_id = ").append("'").append(postID).append("';");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            while (res.next() == true) {
                Rating rating = new Rating();
                rating.setID(res.getInt("rating_id"));
                rating.setUserName(res.getString("user_name"));
                rating.setPostID(res.getInt("post_id"));
                rating.setRate(res.getInt("rating"));
                ratings.add(rating);
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(RatingDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return ratings;
    }

    /**
     * Get Rating
     *
     * @param id
     * @return
     * @throws ClassNotFoundException
     */
    public static Rating getRating(int id) throws ClassNotFoundException {
        Rating rating = new Rating();
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM ratings ")
                    .append(" WHERE ")
                    .append(" rating_id = ").append("'").append(id).append("';");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            if (res.next() == true) {
                rating.setID(res.getInt("rating_id"));
                rating.setUserName(res.getString("user_name"));
                rating.setPostID(res.getInt("post_id"));
                rating.setRate(res.getInt("rating"));
            }
        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(RatingDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return rating;
    }

    /**
     * Establish a database connection and add the rating into the database.
     *
     * @param rating
     * @throws ClassNotFoundException
     */
    public static void addRating(Rating rating) throws ClassNotFoundException {
        // Check that we have all we need
        try {
            rating.checkFields();

        } catch (Exception ex) {
            // Log exception
            Logger.getLogger(RatingDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("INSERT INTO ")
                    .append(" ratings (post_id, user_name, created_by, rating) ")
                    .append(" VALUES (")
                    //.append("'").append(rating.getID()).append("',")
                    .append("'").append(rating.getPostID()).append("',")
                    .append("'").append(rating.getUserName()).append("',")
                    .append("'").append(CS359DB.getUserName()).append("',")
                    .append("'").append(rating.getRate()).append("');");

            String generatedColumns[] = {"rating_id"};
            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString(), generatedColumns);
            stmtIns.executeUpdate();

            // Get information magically completed from database
            ResultSet rs = stmtIns.getGeneratedKeys();
            if (rs.next()) {
                // Update value of setID based on database
                int id = rs.getInt(1);
                rating.setID(id);
            }
            System.out.println("#DB: The rate was successfully added in the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(RatingDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Updates information for specific rating
     *
     * @param rating
     * @throws ClassNotFoundException
     */
    public static void updateRating(Rating rating) throws ClassNotFoundException {
        // Check that we have all we need
        try {
            rating.checkFields();

        } catch (Exception ex) {
            // Log exception
            Logger.getLogger(RatingDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("UPDATE ratings ")
                    .append(" SET ")
                    .append(" rating = ").append("'").append(rating.getRate()).append("'")
                    .append(" WHERE rating_id = ").append("'").append(rating.getID()).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: The rating was successfully updated in the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(RatingDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Delete specific rating
     *
     * @param rating
     * @throws ClassNotFoundException
     */
    public static void deleteRating(Rating rating) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("DELETE FROM ratings ")
                    .append(" WHERE ")
                    .append(" rating_id = ").append("'").append(rating.getID()).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: The rating was successfully deleted from the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(RatingDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Delete specific rating
     *
     * @param id
     * @throws ClassNotFoundException
     */
    public static void deleteRating(int id) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("DELETE FROM ratings ")
                    .append(" WHERE ")
                    .append(" rating_id = ").append("'").append(id).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: The rating was successfully deleted from the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(RatingDB.class.getName()).log(Level.SEVERE, null, ex);
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
