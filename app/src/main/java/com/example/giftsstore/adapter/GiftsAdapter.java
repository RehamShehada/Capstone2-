package com.example.giftsstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftsstore.R;
import com.example.giftsstore.database.Gift;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GiftsAdapter extends RecyclerView.Adapter<GiftsAdapter.ViewHolder>{

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.gift_name)
        TextView name;

        private ViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(onGiftsClickListener);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<Gift> gifts;
    private View.OnClickListener onGiftsClickListener;
    private Context context;

    // Pass in the tasks array into the constructor
    public GiftsAdapter(List<Gift> gifts , Context context) {
        this.gifts = gifts;
        this.context = context;
    }

    public void setGifts(List<Gift> gifts){
        this.gifts = gifts;
    }

    public List<Gift> getGifts(){
        return gifts;
    }

    public void setItemClickListener(View.OnClickListener clickListener) {
        onGiftsClickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View TaskView = inflater.inflate(R.layout.gift_card, parent, false);

        // Return a new holder instance
        return new ViewHolder(TaskView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Gift e = gifts.get(position);
        holder.name.setText(e.getName());
    }

    @Override
    public int getItemCount() {
        return gifts.size();
    }
}