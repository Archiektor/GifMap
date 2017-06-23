package com.example.archiektor.testtasksoftteco;

/**
 * Created by Archiektor on 23.06.2017.
 */

public final class DBScheme {

    public static final class DbTable {
        public static final String TABLE_NAME = "contacts";
    }

    /* Inner class that defines the table contents */

    public static final class Cols {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String WEBSITE = "website";
        public static final String PHONE = "phone";
        public static final String CITY = "city";
        //public static final String COLUMN_NAME_NULLABLE = ;
    }
}

