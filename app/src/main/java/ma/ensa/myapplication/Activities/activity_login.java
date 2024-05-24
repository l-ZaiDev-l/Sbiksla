package ma.ensa.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ma.ensa.myapplication.Database.DatabaseHelper;
import ma.ensa.myapplication.R;

public class activity_login extends AppCompatActivity {

    private EditText user_nameEditText,passwordEditText;
    private Button login_button;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView signupRedirectText = findViewById(R.id.signupRedirectText);
        databaseHelper = new DatabaseHelper(this, null, null, 1);

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_login.this,activity_sign_up.class);
                startActivity(intent);
            }

        });

        user_nameEditText = findViewById(R.id.user_nameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = user_nameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                boolean check = databaseHelper.checkUserExistence(username,password);
                if (check) {

                    String[] userInfo = databaseHelper.getinfo(username);
                    String first_name = userInfo[0];
                    String family_name = userInfo[1];
                    String email = userInfo[2];

                    // Stocker les informations de l'utilisateur dans SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("FIRST_NAME", first_name);
                    editor.putString("LAST_NAME", family_name);
                    editor.putString("EMAIL", email);
                    editor.apply();

                    Toast.makeText(activity_login.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    /*
                    Toast.makeText(activity_login.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_login.this, MainActivity.class);
                    intent.putExtra("FIRST_NAME", first_name);
                    intent.putExtra("LAST_NAME", family_name);
                    startActivity(intent);
                    finish();*/
                } else {
                    Toast.makeText(activity_login.this, "username or password incorrect !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(activity_login.this, activity_login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}