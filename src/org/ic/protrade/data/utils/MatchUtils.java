package org.ic.protrade.data.utils;

public final class MatchUtils {
    
    private MatchUtils(){}
    
    public static boolean isMatch(String name){
        return name.contains(" v ");
    }
}