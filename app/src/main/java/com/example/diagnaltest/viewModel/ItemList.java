package com.example.diagnaltest.viewModel;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.diagnaltest.model.Content;
import com.example.diagnaltest.model.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ItemList {
    ArrayList<Item> mItemList;
    private String TAG = ItemList.class.getSimpleName();
    public String mTitle;
    public ItemList(){
        mItemList = new ArrayList<Item>();
    }

    public ArrayList<Item> getmItemList() {
        return mItemList;
    }

    private void setmItemList(ArrayList<Item> mItemList) {
        this.mItemList = mItemList;
    }

    public ArrayList<Item> loadItemList(Resources resources, ContentItems contentItems) {
        ArrayList<Content> contentList = contentItems.getContent();
        for (int i = 0; i < contentList.size(); i++) {
            Content content = contentList.get(i);
            String imageName = content.getContentPosterImage().substring(0, content.getContentPosterImage().indexOf('.'));
            int resID = resources.getIdentifier(imageName, "drawable", "com.example.diagnaltest");
            if (resID != 0) {
                mItemList.add(new Item(content.getContentName(), resID));
            }
        }
        return mItemList;
    }

    public JSONObject loadFromFile(Context context, @NonNull String fileName) {
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
            StringBuilder buffer = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
            return new JSONObject(buffer.toString());
        } catch (IOException | JSONException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public void mergeItemListWithAPI(ContentItems contentItems, @Nullable JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        try {
            JSONObject pageObject = jsonObject.getJSONObject("page");
            mTitle = pageObject.getString("title");
            JSONArray contentPageAPIResponse = pageObject.getJSONObject("content-items").getJSONArray("content");
            ArrayList<Content> contentList = new ArrayList<>();
            for (int i = 0; i < contentPageAPIResponse.length(); i++) {
                String name = contentPageAPIResponse.getJSONObject(i).getString("name");
                String poster_image = contentPageAPIResponse.getJSONObject(i).getString("poster-image");
                Content content = new Content();
                content.setContentName(name);
                content.setContentPosterImage(poster_image);
                contentList.add(content);
            }
            contentItems.setContent(contentList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
