package com.application.pillminderplus.medications;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.application.pillminderplus.R;
import com.application.pillminderplus.medications.repository.MedicationsPojo;

import java.util.List;

public class MedicationsSubAdapter extends RecyclerView.Adapter<MedicationsSubAdapter.ViewHolder>{
    List<MedicationsPojo> data;

    public MedicationsSubAdapter(List<MedicationsPojo> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameText;
        TextView strengthText;
        TextView pillText;
        View layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.friendReqImgId);
            nameText = itemView.findViewById(R.id.friendReqNameId);
            strengthText = itemView.findViewById(R.id.text_view_med_name_display_med);
            pillText = itemView.findViewById(R.id.textView3);
            layout = itemView.findViewById(R.id.innerRowId);
        }
    }

    @NonNull
    @Override
    public MedicationsSubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup recyclerView, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(recyclerView.getContext());
        View v = inflater.inflate(R.layout.medications_row_inner, recyclerView, false);

        return new ViewHolder(v);
    }

    public void setList(List<MedicationsPojo> updatedData){
        this.data = updatedData;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if(data.get(position).getForm().equals("topical"))
            holder.imageView.setImageResource(R.drawable.ic_topical);
        else if(data.get(position).getForm().equals("solution"))
            holder.imageView.setImageResource(R.drawable.ic_solution);
        else if(data.get(position).getForm().equals("injection"))
            holder.imageView.setImageResource(R.drawable.ic_injection);
        else if(data.get(position).getForm().equals("powder"))
            holder.imageView.setImageResource(R.drawable.ic_powder);
        else if(data.get(position).getForm().equals("drops"))
            holder.imageView.setImageResource(R.drawable.ic_drops);
        else if(data.get(position).getForm().equals("inhaler"))
            holder.imageView.setImageResource(R.drawable.ic_inhaler);
        else
            holder.imageView.setImageResource(R.drawable.ic_pillsgif);

        holder.nameText.setText(data.get(position).getName());
        holder.strengthText.setText("");
        holder.pillText.setText(data.get(position).getRemainingMedAmount().toString() + " Pill(s) left");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString("medicineID", data.get(position).getId());
                Navigation.findNavController(view).navigate(R.id.action_navigation_dashboard_to_displayMedFragment, args);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(data!=null)
        return data.size();
        else return 0;
    }
}
