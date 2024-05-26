package ma.ensa.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;

import ma.ensa.myapplication.R;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import ma.ensa.myapplication.Adapters.UserAdapter;
import ma.ensa.myapplication.Database.DatabaseHelper;
import ma.ensa.myapplication.Domains.UserDomain;
import ma.ensa.myapplication.R;
import ma.ensa.myapplication.components.bottom_bar;

public class activity_display_users extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_users);

        // Retrieve the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewUsers);

        // Create a LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Retrieve users from the database
        ArrayList<UserDomain> userList = getUsersFromDatabase();

        // Create and set the adapter for the RecyclerView
        UserAdapter userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);
        bottombar();
    }

    private ArrayList<UserDomain> getUsersFromDatabase() {
        // Initialize database helper
        DatabaseHelper databaseHelper = new DatabaseHelper(this, null, null, 1);

        // Retrieve users from the database
        ArrayList<UserDomain> userList = new ArrayList<>();
        // Example: Fetching all users
        Cursor cursor = databaseHelper.getAllUsers();
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int firstNameIndex = cursor.getColumnIndex("first_name");
            int familyNameIndex = cursor.getColumnIndex("family_name");
            int emailIndex = cursor.getColumnIndex("email");

            do {
                // Check if column indices are valid
                if (idIndex != -1 && firstNameIndex != -1 && familyNameIndex != -1 && emailIndex != -1) {
                    // Retrieve data from cursor
                    int id = cursor.getInt(idIndex);
                    String firstName = cursor.getString(firstNameIndex);
                    String familyName = cursor.getString(familyNameIndex);
                    String email = cursor.getString(emailIndex);

                    // Create UserDomain object
                    UserDomain user = new UserDomain(id, firstName, familyName, "", email, "", "", 0);

                    // Add UserDomain object to the list
                    userList.add(user);
                } else {
                    // Handle case where column index is invalid
                    // Log an error message or take appropriate action
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return userList;
    }
    private void bottombar(){
        bottom_bar bottomBarClickListener = new bottom_bar(this);

        LinearLayout profileBtn = findViewById(R.id.profileBtn);
        LinearLayout homeBtn = findViewById(R.id.homeBtn);

        profileBtn.setOnClickListener(bottomBarClickListener);
        homeBtn.setOnClickListener(bottomBarClickListener);
    }

}

