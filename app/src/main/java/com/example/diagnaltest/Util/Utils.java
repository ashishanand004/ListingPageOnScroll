package com.example.diagnaltest.Util;

import android.util.Log;

import com.example.diagnaltest.model.Item;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public List<String> loadSuggestions(String keyword, List<Item> itemList) {
        List<String> suggestionList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).mMovieName.toLowerCase().contains(keyword.toLowerCase())) {
                suggestionList.add(itemList.get(i).mMovieName);
            }
        }
        return suggestionList;
    }
}
