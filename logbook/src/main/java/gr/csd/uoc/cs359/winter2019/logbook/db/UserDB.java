/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csd.uoc.cs359.winter2019.logbook.db;

import gr.csd.uoc.cs359.winter2019.logbook.model.User;
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
public class UserDB {

    /**
     * Get user
     *
     * @return
     * @throws ClassNotFoundException
     */
    public static List<User> getUsers() throws ClassNotFoundException {
        List<User> users = new ArrayList<>();

        Statement stmt = null;
        Connection con = null;

        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM users;");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            while (res.next() == true) {
                User user = new User();
                user.setUserName(res.getString("user_name"));
                user.setUserID(res.getInt("user_id"));
                user.setEmail(res.getString("email"));
                user.setPassword(res.getString("password"));
                user.setFirstName(res.getString("first_name"));
                user.setLastName(res.getString("last_name"));
                user.setGender(res.getString("gender"));
                user.setBirthDate(res.getString("birth_date"));
                user.setCountry(res.getString("country"));
                user.setOccupation(res.getString("occupation"));
                user.setInterests(res.getString("interests"));
                user.setAddress(res.getString("address"));
                user.setTown(res.getString("town"));
                user.setInfo(res.getString("info"));
                user.setRegisteredSince(res.getString("registered_since"));
                users.add(user);
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return users;
    }

    /**
     * Get all userNames
     *
     * @return
     * @throws ClassNotFoundException
     */
    public static List<String> getAllUsersNames() throws ClassNotFoundException {
        List<String> userNames = new ArrayList<>();

        Statement stmt = null;
        Connection con = null;

        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT user_name FROM users;");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            while (res.next() == true) {
                userNames.add(res.getString("user_name"));
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return userNames;
    }

    /**
     * Get user
     *
     * @param userName
     * @return
     * @throws ClassNotFoundException
     */
    public static User getUser(String userName) throws ClassNotFoundException {
        User user = null;
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();

            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM users ")
                    .append(" WHERE ")
                    .append(" user_name = ").append("'").append(userName).append("';");

            stmt.execute(insQuery.toString());

            ResultSet res = stmt.getResultSet();

            if (res.next() == true) {
                user = new User();
                user.setUserName(res.getString("user_name"));
                user.setUserID(res.getInt("user_id"));
                user.setEmail(res.getString("email"));
                user.setPassword(res.getString("password"));
                user.setFirstName(res.getString("first_name"));
                user.setLastName(res.getString("last_name"));
                user.setGender(res.getString("gender"));
                user.setBirthDate(res.getString("birth_date"));
                user.setCountry(res.getString("country"));
                user.setTown(res.getString("town"));
                user.setAddress(res.getString("address"));
                user.setOccupation(res.getString("occupation"));
                user.setInterests(res.getString("interests"));
                user.setInfo(res.getString("info"));
                user.setRegisteredSince(res.getString("registered_since"));
            } else {
                System.out.println("User with user name " + userName + "was not found");
            }
        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return user;
    }

    /**
     * Establish a database connection and add the member in the database.
     *
     * @param user
     * @throws ClassNotFoundException
     */
    public static void addUser(User user) throws ClassNotFoundException {
        // Check that we have all we need
        try {
            user.checkFields();

        } catch (Exception ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
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
                    .append(" users (user_name, email, password, first_name, "
                            + "last_name, birth_date, gender, country, town, "
                            + "address, occupation, interests, info, "
                            + "registered_since, created_by) ")
                    .append(" VALUES (")
                    .append("'").append(user.getUserName()).append("',")
                    .append("'").append(user.getEmail()).append("',")
                    .append("'").append(user.getPassword()).append("',")
                    .append("'").append(user.getFirstName()).append("',")
                    .append("'").append(user.getLastName()).append("',")
                    .append("'").append(user.getBirthDate()).append("',")
                    .append("'").append(user.getGender()).append("',")
                    .append("'").append(user.getCountry()).append("',")
                    .append("'").append(user.getTown()).append("',")
                    .append("'").append(user.getAddress()).append("',")
                    .append("'").append(user.getOccupation()).append("',")
                    .append("'").append(user.getInterests()).append("',")
                    .append("'").append(user.getInfo()).append("',")
                    .append("'").append(timestamp).append("',")
                    .append("'").append(CS359DB.getUserName()).append("');");

            String generatedColumns[] = {"user_id"};
            PreparedStatement stmtIns = con.prepareStatement(insQuery.toString(), generatedColumns);
            stmtIns.executeUpdate();

            // Get information magically completed from database
            ResultSet rs = stmtIns.getGeneratedKeys();
            if (rs.next()) {
                // Update value of setID based on database
                int id = rs.getInt(1);
                user.setUserID(id);
            }
            System.out.println("#DB: The member was successfully added in the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Updates information for specific user
     *
     * @param user
     * @throws ClassNotFoundException
     */
    public static void updateUser(User user) throws ClassNotFoundException {
        // Check that we have all we need
        try {
            user.checkFields();

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

            insQuery.append("UPDATE users ")
                    .append(" SET ")
                    .append(" email = ").append("'").append(user.getEmail()).append("',")
                    .append(" password = ").append("'").append(user.getPassword()).append("',")
                    .append(" first_name = ").append("'").append(user.getFirstName()).append("',")
                    .append(" last_name = ").append("'").append(user.getLastName()).append("',")
                    .append(" birth_date = ").append("'").append(user.getBirthDate()).append("',")
                    .append(" gender = ").append("'").append(user.getGender()).append("',")
                    .append(" country = ").append("'").append(user.getCountry()).append("',")
                    .append(" town = ").append("'").append(user.getTown()).append("',")
                    .append(" address = ").append("'").append(user.getAddress()).append("',")
                    .append(" interests = ").append("'").append(user.getInterests()).append("',")
                    .append(" occupation = ").append("'").append(user.getOccupation()).append("',")
                    .append(" info = ").append("'").append(user.getInfo()).append("'")
                    .append(" WHERE user_name= ").append("'").append(user.getUserName()).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: The member was successfully updated in the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Delete information for specific user
     *
     * @param user
     * @throws ClassNotFoundException
     */
    public static void deleteUser(User user) throws ClassNotFoundException {
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("DELETE FROM users ")
                    .append(" WHERE ")
                    .append(" user_name = ").append("'").append(user.getUserName()).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: The member was successfully deleted from the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    /**
     * Delete information for specific user
     *
     * @param userName
     * @throws ClassNotFoundException
     */
    public static void deleteUser(String userName) throws ClassNotFoundException {

        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("DELETE FROM users ")
                    .append(" WHERE ")
                    .append(" user_name = ").append("'").append(userName).append("';");

            stmt.executeUpdate(insQuery.toString());
            System.out.println("#DB: The member was successfully deleted from the database.");

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }
    }

    public static boolean checkValidUserName(String userName) throws ClassNotFoundException {
        boolean valid = true;
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM users ")
                    .append(" WHERE ")
                    .append(" user_name = ").append("'").append(userName).append("';");

            stmt.execute(insQuery.toString());

            if (stmt.getResultSet().next() == true) {
                System.out.println("#DB: The member already exists");
                valid = false;
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return valid;
    }

    public static boolean checkValidEmail(String email) throws ClassNotFoundException {
        boolean valid = true;
        Statement stmt = null;
        Connection con = null;
        try {

            con = CS359DB.getConnection();
            stmt = con.createStatement();

            StringBuilder insQuery = new StringBuilder();

            insQuery.append("SELECT * FROM users ")
                    .append(" WHERE ")
                    .append(" EMAIL = ").append("'").append(email).append("';");

            stmt.execute(insQuery.toString());
            if (stmt.getResultSet().next() == true) {
                System.out.println("#DB: The member alreadyExists");
                valid = false;
            }

        } catch (SQLException ex) {
            // Log exception
            Logger.getLogger(UserDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // close connection
            closeDBConnection(stmt, con);
        }

        return valid;
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
