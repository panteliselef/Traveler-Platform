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
public class Comment {

    private String userName = "";    // (unique)
    private int postID = -1;
    private String comment = "";
    private String createdAt = "";
    private String modifiedAt = "";
    private int ID = -1;

    public int getID() {
        return ID;
    }

    public void setID(int commentID) {
        this.ID = commentID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreated(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModified(String modifiedAy) {
        this.modifiedAt = modifiedAt;
    }

    /**
     * Method that checks that all mandatory fields are set
     *
     * @throws Exception
     */
    public void checkFields() throws Exception {
        // Check that everything is ok
        if ((userName == null || userName.trim().isEmpty())
                || (comment == null || comment.trim().isEmpty())
                || (postID < 0)) {
            throw new Exception("Missing fields!");  // Something went wrong with the fields
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
                .append("Comment: ").append(this.comment).append("\n")
                .append("Created: ").append(this.getCreatedAt()).append("\n")
                .append("Last Modified: ").append(this.getModifiedAt()).append("\n");

        return sb.toString();

    }

}
