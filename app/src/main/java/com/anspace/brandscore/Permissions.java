package com.anspace.brandscore;

public class Permissions {
    private  String category;
    private  String brand;
    private  String desc;
    private  String link;

    public Permissions(String category, String brand,String link, String desc) {
        this.category = category;
        this.brand = brand;
        this.link = link;
        this.desc = desc;
    }

    public Permissions() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
