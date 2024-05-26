package ma.ensa.myapplication.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;

import java.util.ArrayList;

import ma.ensa.myapplication.Activities.DetailActivity;
import ma.ensa.myapplication.Domains.UserDomain;
import ma.ensa.myapplication.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    ArrayList<UserDomain> items;

    public UserAdapter(ArrayList<UserDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_users, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        UserDomain user = items.get(position);

        holder.textViewName.setText(user.getFirstName() + " " + user.getFamilyName());
        holder.textViewEmail.setText(user.getEmail());

        int drawableResId = user.getPic(); // Retrieve the image resource ID from UserDomain
        if (drawableResId == 0) {
            drawableResId = R.drawable.baseline_person_24; // Default profile picture resource ID
        }
        Glide.with(holder.itemView.getContext())
                .load(drawableResId)
                .transform(new CenterCrop(), new GranularRoundedCorners(40, 40, 40, 40))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName;
        TextView textViewEmail;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
        }
    }
}

