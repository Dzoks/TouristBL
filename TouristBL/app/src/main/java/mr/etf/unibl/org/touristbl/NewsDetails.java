package mr.etf.unibl.org.touristbl;

import com.google.gson.annotations.SerializedName;

class NewsDetails {

    private String imageUrl;
    @SerializedName("Tjelo")
    private String fullArticle;
    @SerializedName("Naslov")
    private String title;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullArticle() {
        return fullArticle;
    }

    public void setFullArticle(String fullArticle) {
        this.fullArticle = fullArticle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
