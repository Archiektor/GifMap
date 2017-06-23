package com.example.archiektor.testtasksoftteco;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Archiektor on 16.06.2017.
 */

public class Items {

   // @SerializedName("id")
    private String id;
    //@SerializedName("title")
    private String title;
   // @SerializedName("userId")
    private String userId;

    public Items() {

    }


    public Items(String id, String title, String userId) {
        this.id = id;
        this.title = title;
        this.userId = userId;
    }

    private static int lastId = 0;

    public static List<Items> createList(int numId) {
        List<Items> items = new ArrayList<>();

        for (int i = 1; i <= numId; i++) {
            items.add(new Items("" + ++lastId, "" + lastId, "" + lastId));
        }

        return items;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "id: " + getId() + "title: " + getTitle() + "userId: " + getUserId();
    }
}
