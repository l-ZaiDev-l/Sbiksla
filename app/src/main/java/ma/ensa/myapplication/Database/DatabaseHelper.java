package ma.ensa.myapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MainDatabase";
    private static final int DATABASE_VERSION = 1;

    //les tables

    private static final String SQL_CREATE_CATEGORY_TABLE =
            "CREATE TABLE category (" +
                    "id INTEGER PRIMARY KEY," +
                    "title TEXT," +
                    "picPath TEXT" +
                    ")";

    private static final String SQL_CREATE_POPULAR_TABLE =
            "CREATE TABLE popular (" +
                    "id INTEGER PRIMARY KEY," +
                    "title TEXT," +
                    "location TEXT," +
                    "description TEXT," +
                    "bed INTEGER," +
                    "guide INTEGER," +
                    "score REAL," +
                    "pic TEXT," +
                    "wifi INTEGER," +
                    "price INTEGER," +
                    "category_id INTEGER," +
                    "FOREIGN KEY (category_id) REFERENCES category(id)" +
                    ")";

    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE Users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "first_name TEXT NOT NULL," +
                    "family_name TEXT NOT NULL," +
                    "user_name TEXT NOT NULL," +
                    "email TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL," +
                    "registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS category");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS popular");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(sqLiteDatabase);

    }
    //select *
    public Cursor getAll(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor resultat = sqLiteDatabase.rawQuery("SELECT * FROM popular", null);
        return resultat;
    }
    public long insertPopular(String title, String location, String description, int bed, int guide, float score, String pic, int wifi, double price,int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("location", location);
        values.put("description", description);
        values.put("bed", bed);
        values.put("guide", guide);
        values.put("score", score);
        values.put("pic", pic);
        values.put("wifi", wifi);
        values.put("price", price);
        values.put("category_id", categoryId);
        long result = db.insert("popular", null, values);
        db.close();
        return result;
    }
    public long insertUser(String first_name,String family_name,String user_name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", first_name);
        values.put("family_name", family_name);
        values.put("user_name", user_name);
        values.put("email", email);
        values.put("password", password);
        long result = db.insert("Users", null, values);
        db.close();
        return result;
    }
    public int deletePopular(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("popular", "title=?", new String[]{title});
        db.close();
        return result;
    }
    public boolean checkUserExistence(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id"};
        String selection = "user_name=? AND password=?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query("Users", columns, selection, selectionArgs, null, null, null);
        boolean userExists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return userExists;
    }

    public String[] getinfo(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] result = new String[3]; // Array to store first name, last name, and email

        Cursor cursor = db.query("Users", new String[]{"first_name", "family_name", "email"}, "user_name=?", new String[]{username}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int firstNameIndex = cursor.getColumnIndex("first_name");
            int lastNameIndex = cursor.getColumnIndex("family_name");
            int emailIndex = cursor.getColumnIndex("email");

            // Check if column indices are valid
            if (firstNameIndex >= 0 && lastNameIndex >= 0 && emailIndex >= 0) {
                result[0] = cursor.getString(firstNameIndex);
                result[1] = cursor.getString(lastNameIndex);
                result[2] = cursor.getString(emailIndex);
            } else {
                // Handle case where column index is invalid

            }

            cursor.close();
        }

        db.close();
        return result;
    }
    // Get all categories
    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM category", null);
    }

    // Get category ID by name
    public int getCategoryIdByName(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM category WHERE title=?", new String[]{title});
        if (cursor.moveToFirst()) {
            int categoryId = cursor.getInt(cursor.getColumnIndex("id"));
            cursor.close();
            return categoryId;
        }
        cursor.close();
        return -1; // If not found
    }




}
