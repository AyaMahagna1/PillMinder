package com.application.pillminderplus.home;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.pillminderplus.NetworkConnection;
import com.application.pillminderplus.R;
import com.application.pillminderplus.Helper;
import com.application.pillminderplus.database.room.medicinedose.LocalSourceMedicineDose;
import com.application.pillminderplus.medecinetasks.addingmedicine.AddMedicineActivity;
import com.application.pillminderplus.model.DoseStatus;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.network.FirebaseClient;
import com.application.pillminderplus.notifications.NotificationDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class HomeFragment extends Fragment implements OnMedicineClickListener {
    View view;
    Calendar selectedDate;
    Date dateSelected;
    HorizontalCalendar horizontalCalendar;
    HorizontalCalendarView horizontalCalendarView;
    HomeFragmentPresenter presenter;
    ImageView imgViewNoPills;
    TextView home, homeDescription;
    RecyclerView recyclerView;
    HomeFragmentAdapter homeFragmentAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        initComponents();

        presenter.getAllDosesWithMedicineName(changeDateFormat(Calendar.getInstance(Locale.US).getTime()), view.getContext());

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                dateSelected = changeDateFormat(date.getTime());
                presenter.getAllDosesWithMedicineName(dateSelected, view.getContext());
            }
        });

        view.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
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

    private void initComponents() {

        presenter = new HomeFragmentPresenter(this, HomeFragmentRepository.getInstance(LocalSourceMedicineDose.getInstance(view.getContext()), FirebaseClient.getInstance()));

        imgViewNoPills = view.findViewById(R.id.imgViewNoPills);
        home = view.findViewById(R.id.txtHome);
        homeDescription = view.findViewById(R.id.txtHomeDescription);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
//        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        homeFragmentAdapter = new HomeFragmentAdapter(view.getContext(), new HashMap<>(), this);
        recyclerView.setAdapter(homeFragmentAdapter);

        selectedDate = Calendar.getInstance();

        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        horizontalCalendarView = view.findViewById(R.id.calendarView);
        horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();
        horizontalCalendar.selectDate(selectedDate,true);

        if (Objects.requireNonNull(getActivity()).getIntent().getStringExtra("uid") != null) {
            String uid = getActivity().getIntent().getStringExtra("uid");
            presenter.getAllDosesWithMedicineNameForUser(changeDateFormat(Calendar.getInstance(Locale.US).getTime()), uid);
        }
    }

    private Date changeDateFormat(Date currentDate) {
        Date date = null;
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(currentDate);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            date = format.parse(dateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public void setDosesToAdapter(Map<Medicine, MedicineDose> allDosesWithMedicineName) {

        if (allDosesWithMedicineName.entrySet().size() <= 0) {
            recyclerView.setVisibility(View.GONE);
            imgViewNoPills.setVisibility(View.VISIBLE);
            home.setVisibility(View.VISIBLE);
            homeDescription.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            imgViewNoPills.setVisibility(View.GONE);
            home.setVisibility(View.GONE);
            homeDescription.setVisibility(View.GONE);
        }

        homeFragmentAdapter.setDataToAdapter(allDosesWithMedicineName);
    }

    public void onError(String error) {
        Helper.showAlert(view.getContext(), R.string.error, error, R.drawable.error_icon);
    }

    @Override
    public void onMedicineClick(Medicine medicine, MedicineDose medicineDose) {
        if(medicineDose.getStatus().equals(DoseStatus.FUTURE.getStatus()) || medicineDose.getStatus().equals(DoseStatus.SKIPPED.getStatus())) {
            NotificationDialog dialog = new NotificationDialog(getContext(), medicine, medicineDose, getViewLifecycleOwner());
            dialog.show();
        }
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