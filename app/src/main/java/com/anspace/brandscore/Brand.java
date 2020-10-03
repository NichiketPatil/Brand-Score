package com.anspace.brandscore;

public class Brand {

    private  String t;//name
    private  String c;//china or not
    private  String n;//no. of users
    private  String d;//description

    public Brand(String t, String c, String n, String d) {
        this.t = t;
        this.c = c;
        this.n = n;
        this.d = d;
    }

    public Brand() {}

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }
}

