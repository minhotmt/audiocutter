package com.example.minko.mp3cutter.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by HP 6300 Pro on 5/8/2018.
 */

public class SqliteUtils {

    public static final String DATABASE ="dulieuu.db";
    public static final String TABLE_VIDEO ="video";
    public static final String TABLE_MUSIC ="music";
    public static final String F_NAME ="name";
    public static final String F_URI ="uri";
    public static final String F_ID ="id";
    public static final String F_PATH ="path";
    public static final String F_TIME ="time";
    public static final String F_ARTIST ="artist";

    public static SQLiteDatabase Docfiledb(String name, Context context) {
        String fileName = name;
        File file = context.getDatabasePath(fileName );
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }

            InputStream inputStream = null;
            try {
                inputStream = context.getAssets().open(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[1024 * 8];
            int numOfBytesToRead;
            try {
                while((numOfBytesToRead = inputStream.read(buffer)) > 0)
                    outputStream.write(buffer, 0, numOfBytesToRead);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file, null);
        return db;

    }

}
