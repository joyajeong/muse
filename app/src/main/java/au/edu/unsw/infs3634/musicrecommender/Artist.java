package au.edu.unsw.infs3634.musicrecommender;

public class Artist {
    private String id;
    private String name;
    private String[] genres;

    public Artist(String id, String name, String[] genres) {
        this.name = name;
        this.id = id;
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getGenres() {
        return genres;
    }
}
