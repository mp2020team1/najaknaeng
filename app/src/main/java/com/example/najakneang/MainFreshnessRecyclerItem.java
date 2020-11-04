package com.example.najakneang;

public class MainFreshnessRecyclerItem {
    private String name;
    private int image;
    private int remain;

    public MainFreshnessRecyclerItem(String name, int image, int remain) {
        this.name = name;
        this.image = image;
        this.remain = remain;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }
}
