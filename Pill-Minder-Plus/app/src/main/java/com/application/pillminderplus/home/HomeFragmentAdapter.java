package com.application.pillminderplus.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.application.pillminderplus.R;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
//For showing the medicine doses in calender
public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder> {
    private final Context context;
    Map<Medicine, MedicineDose> allDosesWithMedicineName;
    List<Medicine> keysList = new ArrayList<>();
    List<MedicineDose> dosesList = new ArrayList<>();
    private OnMedicineClickListener onMedClickListener;
    String sectionTime = "";

    public HomeFragmentAdapter(Context context, Map<Medicine, MedicineDose> allDosesWithMedicineName, OnMedicineClickListener onMedClickListener) {
        this.context = context;
        this.allDosesWithMedicineName = allDosesWithMedicineName;
        createKeysAndDosesLists(allDosesWithMedicineName);
        this.onMedClickListener = onMedClickListener;
    }

    private void createKeysAndDosesLists(Map<Medicine, MedicineDose> allDosesWithMedicineName) {
        allDosesWithMedicineName = sortByValue(allDosesWithMedicineName);
        keysList.clear();
        dosesList.clear();
        for (Map.Entry<Medicine, MedicineDose> entries : allDosesWithMedicineName.entrySet()) {
            Medicine key = entries.getKey();
            keysList.add(key);
            MedicineDose value = entries.getValue();
            dosesList.add(value);
        }
    }

    private static Map<Medicine, MedicineDose> sortByValue(Map<Medicine, MedicineDose> unsortMap) {
        List<Map.Entry<Medicine, MedicineDose>> list = new LinkedList<>(unsortMap.entrySet());
        //  Sort list with Collections.sort(), provide a custom Comparator
        //    Try switch the o1 o2 position for a different order
        Collections.sort(list, new Comparator<Map.Entry<Medicine, MedicineDose>>() {
            public int compare(Map.Entry<Medicine, MedicineDose> o1,
                               Map.Entry<Medicine, MedicineDose> o2) {
                return (o1.getValue().getTime()).compareTo(o2.getValue().getTime());
            }
        });
        // Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<Medicine, MedicineDose> sortedMap = new LinkedHashMap<Medicine, MedicineDose>();
        for (Map.Entry<Medicine, MedicineDose> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.upcoming_medicines, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getViewSectionTime().setText(dosesList.get(position).getTime().split("T")[1]);
        if (sectionTime.equals(dosesList.get(position).getTime().split("T")[1])) {
        } else {
            holder.getViewSectionTime().setVisibility(View.VISIBLE);
        }
        holder.getViewMedicineName().setText(keysList.get(position).getName());
        holder.getViewStrengthUnit().setText(String.valueOf(keysList.get(position).getStrength()).concat(" ").concat(keysList.get(position).getUnit()));
        holder.getViewDayFrequency().setText(keysList.get(position).getDayFrequency());
        holder.getViewInstructions().setText(keysList.get(position).getInstructions());
        holder.getViewStatus().setText(dosesList.get(position).getStatus());
        holder.getViewTime().setText(dosesList.get(position).getTime().split("T")[1]);
        holder.getViewAmountForm().setText(String.valueOf(dosesList.get(position).getAmount()).concat(" ").concat(keysList.get(position).getForm()));
        switch (keysList.get(position).getForm()) {
            case "pills":
                Glide.with(context).load(R.drawable.ic_pillsgif).into(holder.getImgViewPill());
                break;
            case "solution":
                Glide.with(context).load(R.drawable.ic_solution).into(holder.getImgViewPill());
                break;
            case "injection":
                Glide.with(context).load(R.drawable.ic_injection).into(holder.getImgViewPill());
                break;
            case "powder":
                Glide.with(context).load(R.drawable.ic_powder).into(holder.getImgViewPill());
                break;
            case "drops":
                Glide.with(context).load(R.drawable.ic_drops).into(holder.getImgViewPill());
                break;
            case "inhaler":
                Glide.with(context).load(R.drawable.ic_inhaler).into(holder.getImgViewPill());
                break;
            case "topical":
                Glide.with(context).load(R.drawable.ic_topical).into(holder.getImgViewPill());
                break;
        }
        sectionTime = dosesList.get(position).getTime().split("T")[1];
        holder.getCardView().setOnClickListener(view -> onMedClickListener.onMedicineClick(keysList.get(position), dosesList.get(position)));
    }

    @Override
    public int getItemCount() {
        return allDosesWithMedicineName.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataToAdapter(Map<Medicine, MedicineDose> allDosesWithMedicineName) {
        this.allDosesWithMedicineName = allDosesWithMedicineName;
        createKeysAndDosesLists(allDosesWithMedicineName);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private CircleImageView imgViewPill;
        private TextView viewMedicineName, viewStrengthUnit, viewDayFrequency, viewInstructions, viewStatus, viewTime, viewAmountForm, viewSectionTime;
        private ConstraintLayout constraint_layout;
        private View card_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public CardView getCardView() {
            if (cardView == null) {
                cardView = itemView.findViewById(R.id.cardView);
            }
            return cardView;
        }

        public CircleImageView getImgViewPill() {
            if (imgViewPill == null) {
                imgViewPill = itemView.findViewById(R.id.imgViewPill);
            }
            return imgViewPill;
        }

        public TextView getViewMedicineName() {
            if (viewMedicineName == null) {
                viewMedicineName = itemView.findViewById(R.id.txtViewMedicineName);
            }
            return viewMedicineName;
        }
        public TextView getViewStrengthUnit() {
            if (viewStrengthUnit == null) {
                viewStrengthUnit = itemView.findViewById(R.id.txtViewStrengthUnit);
            }
            return viewStrengthUnit;
        }
        public TextView getViewDayFrequency() {
            if (viewDayFrequency == null) {
                viewDayFrequency = itemView.findViewById(R.id.txtViewDayFrequency);
            }
            return viewDayFrequency;
        }
        public TextView getViewInstructions() {
            if (viewInstructions == null) {
                viewInstructions = itemView.findViewById(R.id.txtViewInstructions);
            }
            return viewInstructions;
        }
        public TextView getViewStatus() {
            if (viewStatus == null) {
                viewStatus = itemView.findViewById(R.id.txtViewStatus);
            }
            return viewStatus;
        }
        public TextView getViewTime() {
            if (viewTime == null) {
                viewTime = itemView.findViewById(R.id.txtViewTime);
            }
            return viewTime;
        }
        public TextView getViewAmountForm() {
            if (viewAmountForm == null) {
                viewAmountForm = itemView.findViewById(R.id.txtViewAmountForm);
            }
            return viewAmountForm;
        }
        public TextView getViewSectionTime() {
            if (viewSectionTime == null) {
                viewSectionTime = itemView.findViewById(R.id.txtViewSectionTime);
            }
            return viewSectionTime;
        }
    }
}
