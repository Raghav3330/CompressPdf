
package com.app.compress.pdf.stash.post.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post implements Parcelable {

    @SerializedName("multimedia")
    @Expose
    private Multimedia multimedia;
    @SerializedName("dateTime")
    @Expose
    private long dateTime;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("canLike")
    @Expose
    private String canLike;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("shares")
    @Expose
    private int shares;
    @SerializedName("likes")
    @Expose
    private int likes;
    @SerializedName("dislikes")
    @Expose
    private int dislikes;
    @SerializedName("calias")
    @Expose
    private int categoryAlias;

    protected Post(Parcel in) {
        multimedia = in.readParcelable(Multimedia.class.getClassLoader());
        dateTime = in.readLong();
        tag = in.readString();
        canLike = in.readString();
        body = in.readString();
        url = in.readString();
        title = in.readString();
        id = in.readInt();
        shares = in.readInt();
        likes = in.readInt();
        dislikes = in.readInt();
        categoryAlias = in.readInt();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public Multimedia getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(Multimedia multimedia) {
        this.multimedia = multimedia;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCanLike() {
        return canLike;
    }

    public void setCanLike(String canLike) {
        this.canLike = canLike;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getCategoryAlias() {
        return categoryAlias;
    }
    public void setCategoryAlias(int categoryAlias) {
        this.categoryAlias = categoryAlias;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(multimedia, flags);
        dest.writeLong(dateTime);
        dest.writeString(tag);
        dest.writeString(canLike);
        dest.writeString(body);
        dest.writeString(url);
        dest.writeString(title);
        dest.writeInt(id);
        dest.writeInt(shares);
        dest.writeInt(likes);
        dest.writeInt(dislikes);
        dest.writeInt(categoryAlias);
    }


    @Override
    public String toString() {
        return "Post{" +
                "multimedia=" + multimedia +
                ", dateTime=" + dateTime +
                ", tag='" + tag + '\'' +
                ", canLike='" + canLike + '\'' +
                ", body='" + body + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", id=" + id +
                ", shares=" + shares +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", categoryAlias=" + categoryAlias +
                '}';
    }
}
