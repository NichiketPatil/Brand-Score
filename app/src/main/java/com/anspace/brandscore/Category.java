package com.anspace.brandscore;

public class Category {
      private String n;
      private float r;

    public Category(String n, float r) {

        this.n = n;
        this.r = r;
    }

    public Category(){}

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }
}
