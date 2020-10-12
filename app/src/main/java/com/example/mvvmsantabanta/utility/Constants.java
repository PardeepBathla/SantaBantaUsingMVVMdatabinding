package com.example.mvvmsantabanta.utility;

public class Constants {
    public static  String REDIRECTING_FROM = "";
    public static String LANGUAGE_SELECTED = "language_selected";
    public static String NAVIGATION_DRAWER_SCREEN = "NAVIGATION_DRAWER";
    public static String SMS_CATEGORIES_SCREEN = "SMS_CATEGORIES_SCREEN";
    public static String JOKES_CATEGORIES_SCREEN = "JOKES_CATEGORIES_SCREEN";

    public interface COMMON {

        int AdPlacementPosition = 4;
        String THEME_MODE_LIGHT  = "theme_mode_light";
        String LOCALE  = "en";
        String IMAGE_BASE_URL = "http://media.santabanta.com/images/picsms/";
        String CATEGORY_IMAGE_BASE_URL = "https://santabantaapi.techpss.com/images/";
        String BASE_URL = "https://santabantaapi.techpss.com/api/v1/";
        String CATEGORIES_SUBCATEGORIES = "CATEGORIES_SUBCATEGORIES";
        String SELECTED_CATEGORY_POSITION = "SELECTED_CATEGORY";

        String WHATSAPP = "WHATSAPP";
        String FACEBOOK = "FACEBOOK";
        String TWITTER = "TWITTER";
        String INSTAGRAM = "INSTAGRAM";
        String PINTREST = "PINTREST";
        String SNAPCHAT = "SNAPCHAT";



        String SMS = "SMS";
        String JOKES = "JOKES";
        String SELECTED_SUBCATEGORY_SLUG = "SELECTED_SUBCATEGORY_NAME";
        String SELECTED_SUBCATEGORY_SLUG_ID = "SELECTED_SUBCATEGORY_SLUG_ID";
        String ENGLISH = "english";
        String HINDI = "hindi";
    }

    public interface DATABASE {
        String TABLE_NAME_NOTE = "favourite";
        String DB_NAME = "favourites";
    }
}