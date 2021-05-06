package com.bandreit.expensetracker.model.Categories;

public class Category {
    String id;
    String name;
    int backgroundId;
    int imageId;

    public Category(String id, String name, int backgroundId, int imageId) {
        this.id = id;
        this.name = name;
        this.backgroundId = backgroundId;
        this.imageId = imageId;
    }

    public Category() {

    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBackgroundId() {
        return backgroundId;
    }

    public void setBackgroundId(int backgroundId) {
        this.backgroundId = backgroundId;
    }
}
