package com.alphawallet.app;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class SwapData {
    private JsonArray jsonArray1;
    private JsonArray jsonArray2;

    public SwapData() {
        this.jsonArray1 = new JsonArray();
        this.jsonArray2 = new JsonArray();
    }

    // Add a JsonObject to the specified array
    public void addObjectToArray1(JsonObject jsonObject) {
        jsonArray1.add(jsonObject);
    }

    public void addObjectToArray2(JsonObject jsonObject) {
        jsonArray2.add(jsonObject);
    }

    // Remove a JsonObject from the specified array by index
    public void removeObjectFromArray1(int index) {
        if (index >= 0 && index < jsonArray1.size()) {
            jsonArray1.remove(index);
        } else {
            System.out.println("Index out of bounds for Array 1");
        }
    }

    public void removeObjectFromArray2(int index) {
        if (index >= 0 && index < jsonArray2.size()) {
            jsonArray2.remove(index);
        } else {
            System.out.println("Index out of bounds for Array 2");
        }
    }

    // Update a JsonObject in the specified array by index
    public void updateObjectInArray1(int index, JsonObject jsonObject) {
        if (index >= 0 && index < jsonArray1.size()) {
            jsonArray1.set(index, jsonObject);
        } else {
            System.out.println("Index out of bounds for Array 1");
        }
    }

    public void updateObjectInArray2(int index, JsonObject jsonObject) {
        if (index >= 0 && index < jsonArray2.size()) {
            jsonArray2.set(index, jsonObject);
        } else {
            System.out.println("Index out of bounds for Array 2");
        }
    }

    // Get a JsonObject from the specified array by index
    public JsonObject getObjectFromArray1(int index) {
        if (index >= 0 && index < jsonArray1.size()) {
            return jsonArray1.get(index).getAsJsonObject();
        } else {
            System.out.println("Index out of bounds for Array 1");
            return null;
        }
    }

    public JsonObject getObjectFromArray2(int index) {
        if (index >= 0 && index < jsonArray2.size()) {
            return jsonArray2.get(index).getAsJsonObject();
        } else {
            System.out.println("Index out of bounds for Array 2");
            return null;
        }
    }

    // Merge jsonArray2 into jsonArray1
    public void mergeArrays() {
        for (JsonElement element : jsonArray2) {
            jsonArray1.add(element);
        }
    }

    // Convert the JSON arrays to pretty-printed strings
    public String toStringArray1() {
        return jsonArray1.toString();
    }

    public String toStringArray2() {
        return jsonArray2.toString();
    }

    // Load JSON data from a string and initialize arrays
    public void loadFromString(String jsonString1, String jsonString2) {
        try {
            this.jsonArray1 = JsonParser.parseString(jsonString1).getAsJsonArray();
            this.jsonArray2 = JsonParser.parseString(jsonString2).getAsJsonArray();
        } catch (JsonSyntaxException e) {
            System.out.println("Invalid JSON syntax: " + e.getMessage());
        }
    }

    // Convert JSON arrays to strings
    public String convertArraysToString() {
        return "Array 1: " + jsonArray1.toString() + "\nArray 2: " + jsonArray2.toString();
    }
}
