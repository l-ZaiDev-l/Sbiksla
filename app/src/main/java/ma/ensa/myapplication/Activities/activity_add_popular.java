package ma.ensa.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ma.ensa.myapplication.Database.DatabaseHelper;
import ma.ensa.myapplication.R;
import ma.ensa.myapplication.components.bottom_bar;

public class activity_add_popular extends AppCompatActivity {

    private EditText titleEditText, locationEditText, descriptionEditText, scoreEditText, picEditText, priceEditText;
    private SwitchCompat guideSwitch, bedSwitch, wifiSwitch; // Change Switch to SwitchCompat
    private Button addButton,deleteButton;
    private Spinner categorySpinner;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_popular);
        bottombar();
        databaseHelper = new DatabaseHelper(this, null, null, 1);

        // Initialize Views
        titleEditText = findViewById(R.id.title);
        locationEditText = findViewById(R.id.location);
        descriptionEditText = findViewById(R.id.description);
        scoreEditText = findViewById(R.id.score);
        picEditText = findViewById(R.id.pic);
        priceEditText = findViewById(R.id.price);

        guideSwitch = findViewById(R.id.GuideSwitch);
        bedSwitch = findViewById(R.id.BedSwitch);
        wifiSwitch = findViewById(R.id.wifiSwitch);

        addButton = findViewById(R.id.SubmitButton);
        deleteButton = findViewById(R.id.deleteButton);
        categorySpinner = findViewById(R.id.categorySpinner);
        populateCategorySpinner();

        // Set OnClickListener for Add Button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve data from EditText fields and Switches
                String title = titleEditText.getText().toString().trim();
                String location = locationEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                float score = Float.parseFloat(scoreEditText.getText().toString().trim());
                String pic = picEditText.getText().toString().trim();
                double price = Double.parseDouble(priceEditText.getText().toString().trim());

                int guide = guideSwitch.isChecked() ? 1 : 0;
                int bed = bedSwitch.isChecked() ? 1 : 0;
                int wifi = wifiSwitch.isChecked() ? 1 : 0;

                // Category

                String selectedCategory = (String) categorySpinner.getSelectedItem();
                int categoryId = databaseHelper.getCategoryIdByName(selectedCategory);

                // Insert data into the database
                long result = databaseHelper.insertPopular(title, location, description, bed, guide, score, pic, wifi, price,categoryId);

                // Check if insertion was successful
                if (result != -1) {
                    Toast.makeText(activity_add_popular.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_add_popular.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(activity_add_popular.this, "Failed to insert data", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_add_popular.this, activity_admin.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleEditText.getText().toString().trim();
                int result = databaseHelper.deletePopular(title);

                if (result > 0) {
                    // Deletion successful
                    Toast.makeText(getApplicationContext(), "Element deleted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_add_popular.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    // No row found with the given title
                    Toast.makeText(getApplicationContext(), "No element found with the given title", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_add_popular.this, activity_admin.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }

    private void populateCategorySpinner() {
        Cursor cursor = databaseHelper.getAllCategories();
        ArrayList<String> categories = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(cursor.getColumnIndex("title")));
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void bottombar(){
        bottom_bar bottomBarClickListener = new bottom_bar(this);

        LinearLayout profileBtn = findViewById(R.id.profileBtn);
        LinearLayout homeBtn = findViewById(R.id.homeBtn);

        profileBtn.setOnClickListener(bottomBarClickListener);
        homeBtn.setOnClickListener(bottomBarClickListener);
    }
}
