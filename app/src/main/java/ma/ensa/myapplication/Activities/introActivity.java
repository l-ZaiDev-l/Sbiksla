package ma.ensa.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import ma.ensa.myapplication.R;

public class introActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ConstraintLayout introBtn =findViewById(R.id.introBtn);
        introBtn.setOnClickListener(v -> startActivity(new Intent(introActivity.this, activity_login.class)));
    }
}
