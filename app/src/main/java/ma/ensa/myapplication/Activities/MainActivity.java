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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView fullNameTextView;
    DatabaseHelper dbHelper;
    private RecyclerView.Adapter adapterPopular,adapterCat;
    private EditText searchBar;
    private ArrayList<PopularDomain> originalItems;
    private RecyclerView recyclerViewPopular,recyclerViewCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this, "MainDatabase", null, 1);

        // Récupérer les informations de l'utilisateur à partir de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String firstName = sharedPreferences.getString("FIRST_NAME", "Unknown");
        String lastName = sharedPreferences.getString("LAST_NAME", "User");

        searchBar = findViewById(R.id.searchBar);

        originalItems = dbHelper.getAllPopularItems();



        fullNameTextView = findViewById(R.id.full_name);
        fullNameTextView.setText(firstName + " " + lastName);


        dbHelper = new DatabaseHelper(this, "MainDatabase", null, 1);
        bottombar();
        setupSeeAllClickListener();
        setupSearchFunctionality();

        initRecyclerView();
    }
    private void initRecyclerView(){


        // Pour les Popular
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
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapterPopular=new PupolarAdapter(items);
        recyclerViewPopular.setAdapter(adapterPopular);

        // Pour les Category

        ArrayList<CategoryDomain> catsList=new ArrayList<>();
        Cursor categoryCursor = dbHelper.getAllCategories();


        if (categoryCursor != null && categoryCursor.moveToFirst()) {
            /**/int idIndex = categoryCursor.getColumnIndex("id");
            int titleIndex = categoryCursor.getColumnIndex("title");
            int picPathIndex = categoryCursor.getColumnIndex("picPath"); // Index for the picPath column

            do {
                if (titleIndex != -1 && picPathIndex != -1) {
                    /**/int categoryId = categoryCursor.getInt(idIndex);
                    String title = categoryCursor.getString(titleIndex);
                    String picPath = categoryCursor.getString(picPathIndex); // Retrieve the image path
                    //catsList.add(new CategoryDomain(title, picPath)); // Store both title and picPath in CategoryDomain
                    CategoryDomain category = new CategoryDomain();
                    category.setId(categoryId);
                    category.setTitle(title);
                    category.setPicPath(picPath);
                    catsList.add(category);
                }
            } while (categoryCursor.moveToNext());
        }

// Close the cursor after use
        if (categoryCursor != null) {
            categoryCursor.close();
        }


        recyclerViewCategory=findViewById(R.id.view_cat);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        adapterCat = new CategoryAdapter(catsList);
        recyclerViewCategory.setAdapter(adapterCat);


    }
    private void bottombar(){
        bottom_bar bottomBarClickListener = new bottom_bar(this);

        LinearLayout profileBtn = findViewById(R.id.profileBtn);
        LinearLayout homeBtn = findViewById(R.id.homeBtn);

        profileBtn.setOnClickListener(bottomBarClickListener);
        homeBtn.setOnClickListener(bottomBarClickListener);
    }
    public void updatePopularItems(ArrayList<PopularDomain> filteredList) {

        adapterPopular = new PupolarAdapter(filteredList);
        recyclerViewPopular.setAdapter(adapterPopular);
    }

    /************************Filter Category**********************************/
    public void filterPopularItemsByCategory(int categoryId) {
        ArrayList<PopularDomain> filteredItems = dbHelper.getPopularItemsByCategory(categoryId);
        updatePopularItems(filteredItems);
    }
    private void setupSeeAllClickListener() {
        TextView seeAllTextView = findViewById(R.id.seeAll); // Get your "See all" TextView by its ID
        seeAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When the user clicks on "See all", retrieve all items and update the RecyclerView
                ArrayList<PopularDomain> allItems = dbHelper.getAllPopularItems(); // Assuming you have a method to get all items
                adapterPopular = new PupolarAdapter(allItems);
                recyclerViewPopular.setAdapter(adapterPopular);
            }
        });
    }

    private void setupSearchFunctionality() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter items based on entered text
                String searchText = charSequence.toString().toLowerCase();
                ArrayList<PopularDomain> filteredList = new ArrayList<>();
                for (PopularDomain item : originalItems) {
                    if (item.getTitle().toLowerCase().contains(searchText) ||
                            item.getLocation().toLowerCase().contains(searchText)) {
                        filteredList.add(item);
                    }
                }
                updatePopularItems(filteredList); // Update RecyclerView with filtered data
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not used
            }
        });
    }







}