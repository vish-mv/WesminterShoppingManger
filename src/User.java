import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String password;
    private int purchased_count;
    private String bought_items;
    private String name;


    public User( String name,String username, String password, int purchased_count,String bought_items ) {
        this.username = username;
        this.password = password;
        this.purchased_count=purchased_count;
        this.bought_items=bought_items;
        this.name=name;
    }

    // Getters and setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getName(){
        return name;
    }
    public int getPurchased_count(){
        return purchased_count;
    }
}
