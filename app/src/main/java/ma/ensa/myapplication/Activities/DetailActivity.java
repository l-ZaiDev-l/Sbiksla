package ma.ensa.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ma.ensa.myapplication.Domains.PopularDomain;
import ma.ensa.myapplication.R;
import ma.ensa.myapplication.components.bottom_bar;

public class DetailActivity extends AppCompatActivity {
    private TextView titleTxt,locationTxt,bedTxt,guideTxt,wifiTxt,descriptionTxt,scoreTxt;
    private PopularDomain item;
    ImageView picImg,backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initView();
        setVariable();
        bottombar();
    }
    private void setVariable() {
        item=(PopularDomain) getIntent().getSerializableExtra("object");
        titleTxt.setText(item.getTitle());
        scoreTxt.setText(""+(int) item.getScore());
        locationTxt.setText(item.getLocation());
        bedTxt.setText(item.getBed()+" Bed");
        descriptionTxt.setText(item.getDescription());

        if(item.isGuide()){
            guideTxt.setText("Guide");
        }else{
            guideTxt.setText("No-Guide");
        }
        if(item.isWifi()){
            wifiTxt.setText("Wifi");
        }else{
            wifiTxt.setText("No-Wifi");
        }
        int drawableResId = getResources().getIdentifier(item.getPic(),"drawable",getPackageName());
        Glide.with(this).load(drawableResId).into(picImg);

        backBtn.setOnClickListener(v -> finish());


    }

    private Void initView(){
        titleTxt=findViewById(R.id.titleTxt);
        locationTxt=findViewById(R.id.locationTxt);
        bedTxt=findViewById(R.id.bedTxt);
        guideTxt=findViewById(R.id.guideTxt);
        wifiTxt=findViewById(R.id.wifiTxt);
        descriptionTxt=findViewById(R.id.decriptionTxt);
        scoreTxt=findViewById(R.id.scoreTxt);
        picImg=findViewById(R.id.picImg);
        scoreTxt=findViewById(R.id.scoreTxt);
        backBtn=findViewById(R.id.backBtn);

        return null;
    }
    private void bottombar(){
        bottom_bar bottomBarClickListener = new bottom_bar(this);

        LinearLayout profileBtn = findViewById(R.id.profileBtn);
        LinearLayout homeBtn = findViewById(R.id.homeBtn);

        profileBtn.setOnClickListener(bottomBarClickListener);
        homeBtn.setOnClickListener(bottomBarClickListener);
    }
}