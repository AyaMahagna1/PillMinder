package com.application.pillminderplus.medications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.pillminderplus.NetworkConnection;
import com.application.pillminderplus.R;
import com.application.pillminderplus.medecinetasks.addingmedicine.AddMedicineActivity;
import com.application.pillminderplus.medications.repository.MedicationsLocalSource;
import com.application.pillminderplus.medications.repository.MedicationsRepository;
import com.application.pillminderplus.medications.repository.MedicationsSectionPojo;
import com.application.pillminderplus.medications.repository.MedicationsPojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class MedicationsFragment extends Fragment  {

    MedicationsPresenter presenter;
    MedicationsMainAdapter mainAdapter;
    List<MedicationsPojo> activeData;
    List<MedicationsPojo> inactiveData;
    List<MedicationsSectionPojo> data;
    RecyclerView recyclerView;

    Button addMedBtn;
    View view;

    public MedicationsFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        recyclerView = view.findViewById(R.id.recyclerViewId);
        addMedBtn = view.findViewById(R.id.addMedId);

        presenter = new MedicationsPresenter(this, MedicationsRepository.getInstance(getContext(), MedicationsLocalSource.getInstance(getContext())));

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        data = Arrays.asList(new MedicationsSectionPojo("", new ArrayList<>()), new MedicationsSectionPojo("", new ArrayList<>()));
        mainAdapter = new MedicationsMainAdapter(data);
        recyclerView.setAdapter(mainAdapter);

        presenter.getActiveMeds().observe(getViewLifecycleOwner(), new Observer<List<MedicationsPojo>>() {
            @Override
            public void onChanged(List<MedicationsPojo> medicationsPojos) {
                activeData = medicationsPojos;
                data =  Arrays.asList(new MedicationsSectionPojo("", activeData), new MedicationsSectionPojo("", inactiveData));
                mainAdapter.setList(data);
                mainAdapter.notifyDataSetChanged();
            }
        });

        presenter.getInactiveMeds().observe(getViewLifecycleOwner(), new Observer<List<MedicationsPojo>>() {
            @Override
            public void onChanged(List<MedicationsPojo> medicationsPojos) {
                inactiveData = medicationsPojos;
                data =  Arrays.asList(new MedicationsSectionPojo("", activeData), new MedicationsSectionPojo("", inactiveData));
                mainAdapter.setList(data);
                mainAdapter.notifyDataSetChanged();
            }
        });

        addMedBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("BatteryLife")
            @Override
            public void onClick(View view) {

                PowerManager pm = (PowerManager) view.getContext().getSystemService(Context.POWER_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !Settings.canDrawOverlays(view.getContext())) {
                    runtimePermissionForUser();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !pm.isIgnoringBatteryOptimizations(view.getContext().getPackageName())) {
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + view.getContext().getPackageName()));
                    startActivity(intent);
                } else {
                    if(NetworkConnection.isNetworkAvailable(getContext())) {
                        startActivity(new Intent(getContext(), AddMedicineActivity.class));
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                                .setMessage("No Connection")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });

                        builder.create().show();
                    }
                }
            }
        });

    }
    public void runtimePermissionForUser() {
        if (!Settings.canDrawOverlays(view.getContext())) {
            if ("xiaomi".equals(Build.MANUFACTURER.toLowerCase(Locale.ROOT))) {
                final Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter",
                        "com.miui.permcenter.permissions.PermissionsEditorActivity");
                intent.putExtra("extra_pkgname", view.getContext().getPackageName());

                new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.additional_permissions)
                        .setMessage(R.string.additional_permissions_description)
                        .setPositiveButton(R.string.go_to_settings, (dialog, which) -> startActivity(intent))
                        .setIcon(R.drawable.ic_warning)
                        .show();
            } else {
                new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.warning)
                        .setMessage(R.string.error_msg_permission_required)
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                            Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + view.getContext().getPackageName()));

                            runtimePermissionResultLauncher.launch(permissionIntent);
                        })
                        .setIcon(R.drawable.ic_warning)
                        .show();
            }
        }
    }

    private final ActivityResultLauncher<Intent> runtimePermissionResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

            }
    );
}