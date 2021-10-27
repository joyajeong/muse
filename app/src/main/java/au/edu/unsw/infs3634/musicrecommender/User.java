package au.edu.unsw.infs3634.musicrecommender;

public class User {

    public String display_name;
    public String id;

    public User(String id, String display_name) {
        this.display_name = display_name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return display_name;
    }

    public void setName(String name) {
        this.display_name = name;
    }
}

