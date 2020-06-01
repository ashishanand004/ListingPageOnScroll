package com.example.diagnaltest.model;

import java.util.Objects;

public class Item {
    public String mMovieName;
    public int mMovieImage;

    public  Item() {

    }
    public Item(String mMovieName, int mMovieImage) {
        this.mMovieName = mMovieName;
        this.mMovieImage = mMovieImage;
    }

    public String getmMovieName() {
        return mMovieName;
    }

    public int getmMovieImage() {
        return mMovieImage;
    }

    public void setmMovieName(String mMovieName) {
        this.mMovieName = mMovieName;
    }

    public void setmMovieImage(int mMovieImage) {
        this.mMovieImage = mMovieImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return mMovieName.equals(item.mMovieName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mMovieName);
    }
}
