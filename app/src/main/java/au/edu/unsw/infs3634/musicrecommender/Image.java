package au.edu.unsw.infs3634.musicrecommender;

public class Image {

    private int height, width;
    private String url;

    public Image(String url, int height, int width) {
        this.url = url;
        this.height = height;
        this.width = width;
    }
    public String getURL() {
        return url;
    }

}
