package classes;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.InterruptibleChannel;
import java.util.ArrayList;


public class User {
    private String username, password, email;
    private ArrayList<Intervention> userInterventions = new ArrayList<>();


    public User(JSONObject object){
        try {
            this.username = object.getString("username");
            this.password = object.getString("password");
            this.email = object.getString("email");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Intervention> getUserInterventions() {
        return userInterventions;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserInterventions(ArrayList<User> usersList) {
        this.userInterventions = userInterventions;
    }

    @Override
    public  boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.username.equals(user.username) &&
                this.email.equals(user.email);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public  void addIntervention(Intervention intervention){
      //  if (user.userExists(user.getUsername(),user.getEmail()))
       System.out.println(intervention.toString());
        userInterventions.add(intervention);
    }

}
