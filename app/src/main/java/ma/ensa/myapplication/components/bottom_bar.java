package ma.ensa.myapplication.components;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import ma.ensa.myapplication.Activities.MainActivity;
import ma.ensa.myapplication.Activities.activity_admin;
import ma.ensa.myapplication.R;

public class bottom_bar implements View.OnClickListener {

    private Context mContext;

    public bottom_bar(Context context) {
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profileBtn:
                Intent profileIntent = new Intent(mContext, activity_admin.class);
                mContext.startActivity(profileIntent);
                break;
            case R.id.homeBtn:
                Intent homeIntent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(homeIntent);
                break;

            // Add more cases for other buttons if needed
        }
    }
    }
