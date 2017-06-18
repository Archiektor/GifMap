package com.example.archiektor.testtasksoftteco;

import java.util.ArrayList;

/**
 * Created by Archiektor on 16.06.2017.
 */

public class Items {

    private String id;
    private String title;

    public Items(String id, String title) {
        this.id = id;
        this.title = title;
    }

    private static int lastId = 0;

    public static ArrayList<Items> createList(int numId) {
        ArrayList<Items> items = new ArrayList<>();

        for (int i = 1; i <= numId; i++) {
            items.add(new Items("Id " + ++lastId, "Title is: " + lastId));
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
}
