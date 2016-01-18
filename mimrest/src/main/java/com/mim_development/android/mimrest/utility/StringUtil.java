package com.mim_development.android.mimrest.utility;

/**
 * Contains String management tools
 */
public class StringUtil {

    public static String EMPTY = "";

    public static boolean isEmpty(String target){
        return target == null || EMPTY.equals(target);
    }

    public static boolean isNotEmpty(String target){
        return target != null && !EMPTY.equals(target);
    }

}
