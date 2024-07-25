package com.alphawallet.app.dropdown;

public class ChainDropdownItem {

    private final int imageResId;
    private final String text;

    public ChainDropdownItem(int imageResId, String text) {
        this.imageResId = imageResId;
        this.text = text;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getText() {
        return text;
    }
}

