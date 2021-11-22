package com.example.fueldeliveryapp.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fueldeliveryapp.R;
import com.example.fueldeliveryapp.TargetClass;

import java.util.ArrayList;
import java.util.Locale;
public class TargetDataAdapter extends RecyclerView.Adapter<TargetDataAdapter.TargetViewHolder>{
    ArrayList<TargetClass> targetsArrayList;

    public TargetDataAdapter(ArrayList<TargetClass> mTargetData) {
        targetsArrayList = mTargetData;
    }

    @NonNull
    @Override
    public TargetViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.target_trow,viewGroup,false);
        return new TargetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TargetViewHolder viewHolder, int i) {
        viewHolder.androidTargetName.setText(targetsArrayList.get(i).AutoGas );
        viewHolder.androidTargetNumber.setText(String.format(Locale.getDefault(), "API Level: %d", targetsArrayList.get(i).Disel));
        viewHolder.androidTargetShortName.setText(targetsArrayList.get(i).Petrol);
    }

    @Override
    public int getItemCount() {
        if(targetsArrayList == null)
            return 0;
        return targetsArrayList.size();
    }

    public static class TargetViewHolder extends RecyclerView.ViewHolder {
        protected TextView androidTargetName;
        protected TextView androidTargetNumber;
        protected TextView androidTargetShortName;
        public TargetViewHolder(@NonNull View itemView) {
            super(itemView);
            androidTargetShortName= (TextView) itemView.findViewById(R.id.textView2);
            androidTargetName= (TextView) itemView.findViewById(R.id.textView3);
            androidTargetNumber= (TextView) itemView.findViewById(R.id.textView4);
        }
    }
}
