package com.example.qichaoqun.myapplication;

/**
 * @author qichaoqun
 * @date 2018/12/19
 */
public class Music {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String singer;
    private String image;
}
