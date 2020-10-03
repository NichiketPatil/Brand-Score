package com.anspace.brandscore;

import java.util.ArrayList;

public class SavedData {

    private String category = "";
    private ArrayList<String> brandIdListI = new ArrayList<>();
    private ArrayList<String> brandIdListF = new ArrayList<>();

    public SavedData(String category, ArrayList<String> brandIdListI,ArrayList<String> brandIdListF) {
        this.category = category;
        this.brandIdListI = brandIdListI;
        this.brandIdListF = brandIdListF;
    }

    public SavedData() {}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getBrandIdListI() {
        return brandIdListI;
    }

    public void setBrandIdListI(ArrayList<String> brandIdListI) {
        this.brandIdListI = brandIdListI;
    }

    public ArrayList<String> getBrandIdListF() {
        return brandIdListF;
    }

    public void setBrandIdListF(ArrayList<String> brandIdListF) {
        this.brandIdListF = brandIdListF;
    }
}
