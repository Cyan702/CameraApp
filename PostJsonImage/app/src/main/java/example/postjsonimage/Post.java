package example.postjsonimage;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Post implements Serializable {
    @SerializedName("post_id")
    public int post_id;

    @SerializedName("post_title")
    public String post_title;

    @SerializedName("post_text")
    public String post_text;

    @SerializedName("image_url")
    public String image_url;

    @SerializedName("video_url")
    public String video_url;
}
