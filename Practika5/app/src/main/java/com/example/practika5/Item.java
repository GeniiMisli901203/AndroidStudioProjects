package com.example.practika5;

public class Item {
    private int imageResource;
    private String text;

    public Item(int imageResource, String text) {
        this.imageResource = imageResource;
        this.text = text;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getText() {
        return text;
    }
}

