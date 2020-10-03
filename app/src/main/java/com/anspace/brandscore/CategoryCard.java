package com.anspace.brandscore;


public class CategoryCard {
    private String title;
    private String catScore;


    public CategoryCard(String title,String catScore) {
        this.title = title;
        this.catScore = catScore;
    }

    public CategoryCard() {
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getCatScore() {
        return catScore;
    }

    public void setCatScore(String catScore) {
        this.catScore = catScore;
    }
}
