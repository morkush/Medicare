package com.example.medicare.data;

public class uploading {
    public String imageName;
    public String imageURL;
    public uploading(){}

    public uploading(String name, String url) {
        this.imageName = name;
        this.imageURL = url;
    }

    public String getImageName() {
        return imageName;
    }
    public String getImageURL() {
        return imageURL;
    }
}
