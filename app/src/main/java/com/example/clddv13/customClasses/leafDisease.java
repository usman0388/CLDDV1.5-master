package com.example.clddv13.customClasses;

public class leafDisease {
    public leafDisease(String title, String description, int thumbnail) {
        this.title = title;
        Description = description;
        Thumbnail = thumbnail;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        Thumbnail = thumbnail;
    }

    private String title;
    private String Description;
    private int Thumbnail;
}
