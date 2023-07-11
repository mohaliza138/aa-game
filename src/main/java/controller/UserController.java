package controller;

import model.Messages;
import model.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import com.thoughtworks.xstream.XStream;
import model.Validations;
import view.Main;

public class UserController {
    private ArrayList<User> users;
    private User currentUser;
    
    public User getCurrentUser () {
        return currentUser;
    }
    
    public void setCurrentUser (User currentUser) {
        this.currentUser = currentUser;
    }
    
    public UserController() {
        loadUsers();
    }
    
    public void removeCurrentUser () {
        if (currentUser != null) users.remove(currentUser);
        setCurrentUser(null);
    }
    
    public User getUserByUsername (String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) return user;
        }
        return null;
    }
    
    public ArrayList<User> getUsers () {
        return users;
    }
    
    public Messages SignUp (String username, String password) {
        if (!Validations.PASSWORD.check(password)) return Messages.INVALID_PASSWORD;
        if (getUserByUsername(username) != null) return Messages.USED_USERNAME;
        setCurrentUser(new User(username, password));
        users.add(getCurrentUser());
        MainController.userController.saveUsers();
        return Messages.SUCCESS;
    }
    
    public Messages SignIn (String username, String password) {
        User user;
        if ((user = getUserByUsername(username)) == null) return Messages.NO_USER_WITH_THIS_ID;
        if (!user.checkPassword(password)) return Messages.WRONG_PASSWORD;
        setCurrentUser(user);
        return Messages.SUCCESS;
    }
    
    public void saveUsers () {
        try (FileWriter writer = new FileWriter("users_database.xml")) {
            XStream xstream = new XStream();
            xstream.alias("user", User.class);
            String xml = xstream.toXML(users);
            writer.write(xml);
            saveCurrentUser();
        } catch (IOException ignored) {
        }
    }
    
    public void saveCurrentUser () {
        try (FileWriter writer = new FileWriter("current_user.xml")) {
            XStream xstream = new XStream();
            xstream.alias("user", User.class);
            String xml = xstream.toXML(currentUser);
            writer.write(xml);
        } catch (IOException ignored) {
        }
    }
    
    public void loadUsers () {
        XStream xstream = new XStream();
        xstream.allowTypesByWildcard(new String[] {"model.User"});
        xstream.alias("user", User.class);
        try (FileReader reader = new FileReader("users_database.xml")) {
            ArrayList<User> temp = (ArrayList<User>) xstream.fromXML(reader);
            if (temp != null) {
                users = temp;
                loadCurrentUser();
            }
            else users = new ArrayList<>();
        } catch (Exception e) {
            System.out.println(e);
            users = new ArrayList<>();
        }
    }
    
    public void loadCurrentUser () {
        XStream xstream = new XStream();
        xstream.allowTypesByWildcard(new String[] {"model.User"});
        xstream.alias("user", User.class);
        try (FileReader reader = new FileReader("current_user.xml")) {
            User temp = (User) xstream.fromXML(reader);
            if (temp != null) {
                for (User user : users) {
                    if (user.equals(temp)) {
                        setCurrentUser(user);
                        return;
                    }
                }
            }
            else currentUser = null;
        } catch (Exception ignored) {
            currentUser = null;
        }
    }
}
