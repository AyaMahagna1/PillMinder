package com.application.pillminderplus.medications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.pillminderplus.R;
import com.application.pillminderplus.medications.repository.MedicationsSectionPojo;
import com.application.pillminderplus.medications.repository.MedicationsPojo;

import java.util.List;

public class MedicationsMainAdapter extends RecyclerView.Adapter<MedicationsMainAdapter.ViewHolder> {

    List<MedicationsSectionPojo> data;

    public MedicationsMainAdapter(List<MedicationsSectionPojo> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MedicationsMainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.medications_row_outer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationsMainAdapter.ViewHolder holder, int position) {
        holder.sectionName.setText(data.get(position).getSectionName());

        List<MedicationsPojo> medList = data.get(position).getMedPojo();
        MedicationsSubAdapter medAdapter = new MedicationsSubAdapter(medList);
        holder.childRecycler.setAdapter(medAdapter);
    }

    public void setList(List<MedicationsSectionPojo> updatedData) {
        this.data = updatedData;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sectionName;
        RecyclerView childRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.sectionTextId);
            childRecycler = itemView.findViewById(R.id.childId);
        }
    }
}
