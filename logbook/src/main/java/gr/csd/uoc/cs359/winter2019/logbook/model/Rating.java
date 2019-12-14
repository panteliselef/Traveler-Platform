/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csd.uoc.cs359.winter2019.logbook.model;

/**
 *
 * @author papadako
 */
public class Rating {

    private String userName = "";    // (unique)
    private int ID = -1;
    private int postID = -1;
    private int rate = -1;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ratingID) {
        this.ID = ratingID;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    /**
     * Method that checks that all mandatory fields are set
     *
     * @throws Exception
     */
    public void checkFields() throws Exception {
        // Check that everything is ok
        if ((userName == null || userName.trim().isEmpty())
                || (rate > 5) || (rate < 0)
                || (postID < 0)) {
            throw new Exception("Missing fields or wrong rate!");  // Something went wrong with the fields
        }
    }

    /**
     * Returns a string representation of this object (use it only
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(this.ID).append("\n")
                .append("User: ").append(this.userName).append("\n")
                .append("PostID: ").append(this.postID).append("\n")
                .append("Rate: ").append(this.rate).append("\n");

        return sb.toString();

    }

}
