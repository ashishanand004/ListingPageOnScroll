package com.example.diagnaltest;

import com.example.diagnaltest.Util.Utils;
import com.example.diagnaltest.model.Item;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void suggestionList_test() {
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("The Birds", 1));
        itemList.add(new Item("Rear Window", 2));
        itemList.add(new Item("The Birds", 1));
        itemList.add(new Item("Rear Window", 2));
        itemList.add(new Item("The Birds", 1));
        List<String> suggestions = new Utils().loadSuggestions("ind", itemList);
        assertEquals("Error in getting suggestion items count", 2, suggestions.size());
    }
}