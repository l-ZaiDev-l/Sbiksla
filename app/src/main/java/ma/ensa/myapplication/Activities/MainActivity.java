package ma.ensa.myapplication.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ma.ensa.myapplication.Database.DatabaseHelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import java.sql.Array;
import java.util.ArrayList;

import ma.ensa.myapplication.Adapters.CategoryAdapter;
import ma.ensa.myapplication.Adapters.PupolarAdapter;
import ma.ensa.myapplication.Domains.CategoryDomain;
import ma.ensa.myapplication.Domains.PopularDomain;
import ma.ensa.myapplication.R;
import ma.ensa.myapplication.components.bottom_bar;

import android.database.Cursor;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView fullNameTextView;
    DatabaseHelper dbHelper;
    private RecyclerView.Adapter adapterPopular,adapterCat;
    private RecyclerView recyclerViewPopular,recyclerViewCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Récupérer les informations de l'utilisateur à partir de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String firstName = sharedPreferences.getString("FIRST_NAME", "Unknown");
        String lastName = sharedPreferences.getString("LAST_NAME", "User");

        /*
        //FullName Setup
        String firstName = getIntent().getStringExtra("FIRST_NAME");
        String lastName = getIntent().getStringExtra("LAST_NAME");
        String email = getIntent().getStringExtra("EMAIL");
        TextView fullNameTextView = findViewById(R.id.full_name);
        fullNameTextView.setText(firstName + " " + lastName);

         */

        fullNameTextView = findViewById(R.id.full_name);
        fullNameTextView.setText(firstName + " " + lastName);


        dbHelper = new DatabaseHelper(this, "MainDatabase", null, 1);
        bottombar();

        initRecyclerView();
    }
    private void initRecyclerView(){
        ArrayList<PopularDomain> items=new ArrayList<>();
        Cursor popularCursor = dbHelper.getAll();


        if (popularCursor != null && popularCursor.moveToFirst()) {
            int titleIndex = popularCursor.getColumnIndex("title");
            int locationIndex = popularCursor.getColumnIndex("location");
            int descriptionIndex = popularCursor.getColumnIndex("description");
            int bedIndex = popularCursor.getColumnIndex("bed");
            int guideIndex = popularCursor.getColumnIndex("guide");
            int scoreIndex = popularCursor.getColumnIndex("score");
            int picIndex = popularCursor.getColumnIndex("pic");
            int wifiIndex = popularCursor.getColumnIndex("wifi");
            int priceIndex = popularCursor.getColumnIndex("price");
            do {
                if (titleIndex != -1 && locationIndex != -1 ) {
                    String title = popularCursor.getString(titleIndex);
                    String location = popularCursor.getString(locationIndex);
                    String description = popularCursor.getString(descriptionIndex);
                    int bed = popularCursor.getInt(bedIndex);
                    boolean guide = (popularCursor.getInt(guideIndex) == 1);
                    double score = popularCursor.getDouble(scoreIndex);
                    String pic = popularCursor.getString(picIndex);
                    boolean wifi = (popularCursor.getInt(wifiIndex) == 1);
                    int price = popularCursor.getInt(priceIndex);
                    // Create a PopularDomain object with the extracted data
                    PopularDomain popular = new PopularDomain(title, location, description, bed, guide, score, pic, wifi, price);
                    // Add the PopularDomain object to the list
                    items.add(popular);
                }



            } while (popularCursor.moveToNext());
        }

        // Close the cursor after use
        if (popularCursor != null) {
            popularCursor.close();
        }
        recyclerViewPopular=findViewById(R.id.view_pop);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapterPopular=new PupolarAdapter(items);
        recyclerViewPopular.setAdapter(adapterPopular);


        ArrayList<CategoryDomain> catsList=new ArrayList<>();
        catsList.add(new CategoryDomain("Beaches","cat1"));
        catsList.add(new CategoryDomain("Camps","cat2"));
        catsList.add(new CategoryDomain("Forest","cat3"));
        catsList.add(new CategoryDomain("Desert","cat4"));
        catsList.add(new CategoryDomain("Montain","cat5"));

        recyclerViewCategory=findViewById(R.id.view_cat);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapterCat=new CategoryAdapter(catsList);
        recyclerViewCategory.setAdapter(adapterCat);


    }
    private void bottombar(){
        bottom_bar bottomBarClickListener = new bottom_bar(this);

        LinearLayout profileBtn = findViewById(R.id.profileBtn);
        LinearLayout homeBtn = findViewById(R.id.homeBtn);

        profileBtn.setOnClickListener(bottomBarClickListener);
        homeBtn.setOnClickListener(bottomBarClickListener);
    }

}