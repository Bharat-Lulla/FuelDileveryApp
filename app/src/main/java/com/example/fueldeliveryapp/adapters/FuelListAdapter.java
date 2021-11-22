package com.example.fueldeliveryapp.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fueldeliveryapp.R;
import com.example.fueldeliveryapp.model.FuelModal;
import com.bumptech.glide.Glide;

import java.util.List;

public class FuelListAdapter extends RecyclerView.Adapter<FuelListAdapter.MyViewHolder> {

    private List<FuelModal> fuelModalList;
    private FuelListClickListener clickListener;

    public FuelListAdapter(List<FuelModal> fuelModalList, FuelListClickListener clickListener) {
        this.fuelModalList = fuelModalList;
        this.clickListener = clickListener;
    }

    public void updateData(List<FuelModal> fuelModalList) {
        this.fuelModalList = fuelModalList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row, parent, false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.fuelName.setText(fuelModalList.get(position).getName());
        holder.fuelAddress.setText("Address: "+ fuelModalList.get(position).getAddress());
        holder.fuelHours.setText("Today's hours: " + fuelModalList.get(position).getHours().getTodaysHours());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(fuelModalList.get(position));
//                System.out.println(fuelModalList.ge);
            }
        });
        Glide.with(holder.thumbImage)
                .load(fuelModalList.get(position).getImage())
                .into(holder.thumbImage);

    }

    @Override
    public int getItemCount() {
        return fuelModalList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  fuelName;
        TextView  fuelAddress;
        TextView  fuelHours;
        ImageView thumbImage;

        public MyViewHolder(View view) {
            super(view);
            fuelName = view.findViewById(R.id.fuelName);
            fuelAddress = view.findViewById(R.id.fuelAddress);
            fuelHours = view.findViewById(R.id.fuelHours);
            thumbImage = view.findViewById(R.id.thumbImage);

        }
    }

    public interface FuelListClickListener {
        public void onItemClick(FuelModal fuelModal);
    }
}
