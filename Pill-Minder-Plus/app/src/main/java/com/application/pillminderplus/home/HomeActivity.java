package com.application.pillminderplus.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.application.pillminderplus.ConnectionReceiver;
import com.application.pillminderplus.R;
import com.application.pillminderplus.Helper;
import com.application.pillminderplus.caregivers.CaregiversActivity;
import com.application.pillminderplus.database.room.medicine.LocalSourceMedicine;
import com.application.pillminderplus.database.room.medicinedose.LocalSourceMedicineDose;
import com.application.pillminderplus.database.room.user.LocalSourceUser;
import com.application.pillminderplus.friendrequest.FriendRequestActivity;
import com.application.pillminderplus.friends.FriendsActivity;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.model.User;
import com.application.pillminderplus.network.FirebaseClient;
import com.application.pillminderplus.splash.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
// Calender where you can see all the reminders in the calender and add additional reminders
public class HomeActivity extends AppCompatActivity implements ConnectionReceiver.NetworkStateReceiverListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    String userId;
    String userName;
    String userImg;
    HomePresenter presenter;
    CircleImageView profile_image;
    TextView txtViewName;
    // Receiver that detects network state changes
    private ConnectionReceiver networkStateReceiver;
    List<Medicine> unSyncedMedicines;
    List<MedicineDose> unSyncedMedicineDoses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initComponents();
        presenter.getUserFromRoom(this);
        presenter.getAllUnSyncMedicines();
        presenter.getAllUnSyncMedicineDoses();
        startNetworkBroadcastReceiver(this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void initComponents() {
        presenter = new HomePresenter(this,
                HomeRepository.getInstance(LocalSourceUser.getInstance(this),
                        LocalSourceMedicine.getInstance(this),
                        LocalSourceMedicineDose.getInstance(this),
                        FirebaseClient.getInstance()));

        unSyncedMedicines = new ArrayList<>();
        unSyncedMedicineDoses = new ArrayList<>();

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        setupToolbar();
        setListeners();
    }

    private void setupToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
    }

    @SuppressLint("NonConstantResourceId")
    private void setListeners() {
        navigationView.setNavigationItemSelectedListener(item -> {
            item.setChecked(true);

            switch (item.getItemId()) {
                case R.id.itemMedFriends:
                    Intent friendIntent = new Intent(this, FriendsActivity.class);
                    friendIntent.putExtra("userId", userId);
                    startActivityForResult(friendIntent, 100);
                    break;
                case R.id.itemMedFriendsReqs:
                    Intent friendReqsIntent = new Intent(this, FriendRequestActivity.class);
                    friendReqsIntent.putExtra("userId", userId);
                    startActivity(friendReqsIntent);
                    break;
                case R.id.itemInviteMedFriend:
                    Intent healthTakerIntent = new Intent(this, CaregiversActivity.class);
                    healthTakerIntent.putExtra("userId", userId);
                    healthTakerIntent.putExtra("userName", userName);
                    healthTakerIntent.putExtra("userImg", userImg);
                    startActivity(healthTakerIntent);
                    break;

                case R.id.itemLogout:
                    presenter.signOut(this);
                    break;
            }

            item.setChecked(!item.isChecked());

            drawerLayout.closeDrawers();
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            String uid = Objects.requireNonNull(data).getStringExtra("uid");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayUserInformation(User user) {
        userId = user.getUserId();
        userName = user.getUsername();
        userImg = user.getProfile_image_uri();

        View headerView = navigationView.getHeaderView(0);

        profile_image = headerView.findViewById(R.id.profile_image);
        txtViewName = headerView.findViewById(R.id.txtViewName);
        Picasso.with(this).load(user.getProfile_image_uri()).into(profile_image);
        txtViewName.setText(user.getUsername());
    }

    public void navigateToLoginScreen() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void syncMedicines(LiveData<List<Medicine>> unSyncMedicines) {
        unSyncMedicines.observe(this, new Observer<List<Medicine>>() {
            @Override
            public void onChanged(List<Medicine> medicines) {
                if (medicines.size() > 0) {
                    unSyncedMedicines.clear();
                    unSyncedMedicines = medicines;
                }
            }
        });
    }

    public void syncMedicineDoses(LiveData<List<MedicineDose>> unSyncMedicineDoses) {
        unSyncMedicineDoses.observe(this, new Observer<List<MedicineDose>>() {
            @Override
            public void onChanged(List<MedicineDose> medicineDoses) {
                if (medicineDoses.size() > 0) {
                    unSyncedMedicineDoses.clear();
                    unSyncedMedicineDoses = medicineDoses;
                }
            }
        });
    }

    public void showSyncError(String error) {
        Helper.showAlert(this, R.string.error, error, R.drawable.error_icon);
    }

    public void startNetworkBroadcastReceiver(Context currentContext) {
        networkStateReceiver = new ConnectionReceiver();
        networkStateReceiver.addListener((ConnectionReceiver.NetworkStateReceiverListener) currentContext);
        registerNetworkBroadcastReceiver(currentContext);
    }

    /**
     * Register the NetworkStateReceiver with your activity
     */
    public void registerNetworkBroadcastReceiver(Context currentContext) {
        currentContext.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
    }

    /**
     * Unregister the NetworkStateReceiver with your activity
     */
    public void unregisterNetworkBroadcastReceiver(Context currentContext) {
        currentContext.unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void networkAvailable() {
        if (unSyncedMedicines.size() > 0) {
            presenter.syncMedicineListToFirebase(unSyncedMedicines);
        }
        if (unSyncedMedicineDoses.size() > 0) {
            presenter.syncMedicineDosesListToFirebase(unSyncedMedicineDoses);
        }
    }

    @Override
    public void networkUnavailable() {
        Toast.makeText(this, R.string.connection_lost, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        unregisterNetworkBroadcastReceiver(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        registerNetworkBroadcastReceiver(this);
        super.onResume();
    }
}