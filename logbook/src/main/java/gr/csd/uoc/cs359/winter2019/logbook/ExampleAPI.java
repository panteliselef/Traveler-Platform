/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.csd.uoc.cs359.winter2019.logbook;

import gr.csd.uoc.cs359.winter2019.logbook.db.PostDB;
import gr.csd.uoc.cs359.winter2019.logbook.db.UserDB;
import gr.csd.uoc.cs359.winter2019.logbook.model.Post;
import gr.csd.uoc.cs359.winter2019.logbook.model.User;
import java.util.List;

/**
 *
 * @author papadako
 */
public class ExampleAPI {

    /**
     * An example of adding a new member in the database. Turing is a user of
     * our system
     *
     * @param args the command line arguments
     * @throws ClassNotFoundException
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws ClassNotFoundException, InterruptedException {

        // O Turing έσπασε τον κώδικα enigma που χρησιμοποιούσαν οι Γερμανοί
        // στον Παγκόσμιο Πόλεμο ΙΙ για να κρυπτογραφήσουν την επικοινωνία τους.
        // Άρα είναι πιθανό να χρησιμοποιούσε σαν passwd τη λέξη enigma, κάπως
        // τροποποιημένη :)
        // http://en.wikipedia.org/wiki/Enigma_machine
        // md5 της λέξης 3n!gm@ είναι e37f7cfcb0cd53734184de812b5c6175
        User user = new User();
        user.setUserName("turing");
        user.setEmail("turing@csd.uoc.gr");
        user.setPassword("e37f7cfcb0cd53734184de812b5c6175");
        user.setFirstName("Alan");
        user.setLastName("Turing");
        user.setBirthDate("07/07/1912");
        user.setCountry("Science");
        user.setTown("Computer Science");
        user.setAddress("Computability");
        user.setOccupation("Xompistas");
        user.setGender("Male");
        user.setInterests("Enigma, decyphering");
        user.setInfo("You will have a job due to my work! :)");

        if (UserDB.checkValidUserName("turing")) {
            // Add turing to database
            System.out.println("==>Adding users");
            UserDB.addUser(user);
            System.out.println(user.toString());
            System.out.println("==>Added user");
        } else {
            System.out.println("User already exists.... No more Turings please!");
        }

        List<User> users = UserDB.getUsers();

        int i = 0;
        System.out.println("==>Retrieving");
        for (User userIt : users) {
            System.out.println("userIt:" + i++);
            System.out.println(userIt);
        }

        // Add a wish as info
        System.out.println("==>Updating");
        user = UserDB.getUser("turing");
        if (user != null) {
            System.out.println("Updating" + user.getUserName());
            user.setInfo("I hope you follow my path...");
            UserDB.updateUser(user);
        }

        user = UserDB.getUser("turing");
        if (user != null) {
            System.out.println("==>Updated");
            System.out.println(UserDB.getUser("turing"));
        }

        Post post = new Post();
        post.setUserName("kernelpanic");
        post.setDescription("This is my first post");
        PostDB.addPost(post);

        System.out.println("==>Deleting");
        UserDB.deleteUser("turing");
        System.out.println("==>Deleted");
        if (UserDB.checkValidUserName("turing")) {
            // You can be a new Turing!
            System.out.println("Well, Turing is gone for a long time now!");
            System.out.println("Hope we find a new one in this 2019 class!");
        }
    }
}
