package ma.ensa.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ma.ensa.myapplication.Database.DatabaseHelper;
import ma.ensa.myapplication.R;

public class activity_sign_up extends AppCompatActivity {

    private EditText first_nameEditText,family_nameEditText,emailEditText,passwordEditText,user_nameEditText;
    private Button signup_button;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView loginRedirectText = findViewById(R.id.loginRedirectText);
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_sign_up.this,activity_login.class);
                startActivity(intent);
            }
        });

        databaseHelper = new DatabaseHelper(this,null,null,1);
        first_nameEditText = findViewById(R.id.first_nameEditText);
        family_nameEditText = findViewById(R.id.family_nameEditText);
        user_nameEditText = findViewById(R.id.user_nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signup_button = findViewById(R.id.signup_button);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first_name = first_nameEditText.getText().toString().trim();
                String family_name = family_nameEditText.getText().toString().trim();
                String user_name = user_nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                long result = databaseHelper.insertUser(first_name, family_name,user_name,  email,  password);
                if (result != -1) {
                    Toast.makeText(activity_sign_up.this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_sign_up.this, activity_login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(activity_sign_up.this, "Failed to insert data", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_sign_up.this, activity_sign_up.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}