package com.alphawallet.app.dropdown;

public class DropdownItem {

    private final boolean isSearched;
    private final int imageResId;
    private final String text;
    private final String imgUrl;
    private final int decimal;

    private final String tokenContractAddress;

    public DropdownItem(boolean isSearched, int imageResId, String text, String imgUrl, String address, int decimal ) {
        this.isSearched = isSearched;
        this.imageResId = imageResId;
        this.text = text;
        this.imgUrl = imgUrl;
        this.tokenContractAddress = address;
        this.decimal = decimal;
    }

    public boolean getIsSearched() {
        return isSearched;
    }
    public int getImageResId() {
        return imageResId;
    }

    public String getText() {
        return text;
    }
    public String getImageUrl() {
        return imgUrl;
    }
    public String getAddress() {return tokenContractAddress;}
    public String getTokenContractAddress() { return tokenContractAddress; }
    public int getDecimal() { return decimal; }
}

