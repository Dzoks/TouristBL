package org.unibl.etf.mr.touristbl.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "news")

public class NewsDetails implements Serializable {
    @SerializedName("vijestID")
    private int id;
    private String imageUrl;
    @SerializedName("Tjelo")
    private String fullArticle;
    @SerializedName("Naslov")
    private String title;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsDetails that = (NewsDetails) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
