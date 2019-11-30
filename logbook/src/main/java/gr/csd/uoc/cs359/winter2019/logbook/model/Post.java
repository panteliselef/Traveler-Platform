/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csd.uoc.cs359.winter2019.logbook.model;

import java.io.Serializable;

/**
 *
 * @author papadako
 */
public class Post implements Serializable {

    private Integer postID = 0;
    private String userName = "";
    private String description = "";
    private String resourceURL = "";
    private String imageURL = "";
    private String imageBase64 = "";
    private String latitude = "";
    private String longitude = "";
    private String createdAt = "";

    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public void setResourceURL(String resource_URL) {
        this.resourceURL = resource_URL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String image_URL) {
        this.imageURL = image_URL;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String image_base64) {
        this.imageBase64 = image_base64;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Method that checks that all mandatory fields are set
     *
     * @throws Exception
     */
    public void checkFields() throws Exception {
        // Check that everything is ok
        if ((description == null || description.trim().isEmpty())
                && userName == null || userName.trim().isEmpty()) {
            throw new Exception("Missing fields!");  // Something went wrong with the fields
        }
    }

    /**
     * Returns a string representation of this object
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("POST: ").append(postID).append("\n")
                .append("userName: ").append(userName).append("\n")
                .append("description: ").append(description).append("\n")
                .append("resourceURL: ").append(resourceURL).append("\n")
                .append("imageURL: ").append(imageURL).append("\n")
                .append("imageBase64: ").append(imageBase64).append("\n")
                .append("latitude").append(latitude).append("\n")
                .append("longitude").append(longitude).append("\n")
                .append("createdAt").append(createdAt).append("\n");

        return sb.toString();

    }

}
