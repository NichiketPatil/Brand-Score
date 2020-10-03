package com.anspace.brandscore;

public class BrandCard {
    private String t;
    private String n;
    private String c;

    public BrandCard(String t, String n, String c) {
        this.t = t;//title
        this.n = n;//users no.
        this.c = c;//chinese or not
    }

    public BrandCard() {}

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }
}
