package com.example.toDoAppBackend.util;


import org.springframework.data.util.Pair;

import java.util.Set;

public class StemmerHelper {
    public static Pair<String ,String> getPlaceHolders(String text, Set<String> protectedWords){
        String variable = "";
        for(String term: protectedWords){
            variable = term;
            if(text.contains(term)){
                text = text.replace(term, "_PLACEHOLDER_");
            }
        }
        return Pair.of(text,variable);
    }
}
