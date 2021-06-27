package com.example.smartrecipe.util;

public class Constants {

    public static final String[] DEFAULT_SEARCH_CATEGORIES = {"Breakfast", "Lunch", "Dinner", "Beverages", "Appetizers", "Soups", "Salads", "Desserts", "Sauces"};

    public static final String[] DEFAULT_SEARCH_CATEGORY_IMAGES = {
            "breakfast",
            "lunch",
            "dinner",
            "beverages",
            "appetizers",
            "soups",
            "salads",
            "desserts",
            "sauces"
    };

    // To get App ID and App Key register for free on edamam.com
    public static final String BASE_URL = "https://api.edamam.com";
    public static final String APP_ID = "7c96b3e1";
    public static final String APP_KEY = "7c43f354cdcd0d9cd2d501910c82c7a2";

    public static final int CONNECTION_TIMEOUT = 10;
    public static final int READ_TIMEOUT = 3;
    public static final int WRITE_TIMEOUT = 3;
}