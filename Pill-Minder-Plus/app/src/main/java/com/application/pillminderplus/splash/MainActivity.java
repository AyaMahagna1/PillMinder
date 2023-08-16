package com.application.pillminderplus.splash;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.application.pillminderplus.ConnectionReceiver;
import com.application.pillminderplus.R;
//The main activity - runs first time the user runs the app
public class MainActivity extends AppCompatActivity implements ConnectionReceiver.NetworkStateReceiverListener {

    // Receiver that detects network state changes
    private ConnectionReceiver networkStateReceiver;

    View connectionLostMain;
    FragmentContainerView fragmentContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectionLostMain = findViewById(R.id.connectionLostMain);
        fragmentContainerView = findViewById(R.id.fragmentContainerView);

        startNetworkBroadcastReceiver(this);
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
     Unregister the NetworkStateReceiver with your activity
     */
    public void unregisterNetworkBroadcastReceiver(Context currentContext) {
        currentContext.unregisterReceiver(networkStateReceiver);
    }

    @Override
    public void networkAvailable() {
        //Proceed with online actions in activity (e.g. hide offline UI from user, start services, etc...)
        fragmentContainerView.setVisibility(View.VISIBLE);
        connectionLostMain.setVisibility(View.GONE);
    }

    @Override
    public void networkUnavailable() {
        //Proceed with offline actions in activity (e.g. sInform user they are offline, stop services, etc...)
        fragmentContainerView.setVisibility(View.GONE);
        connectionLostMain.setVisibility(View.VISIBLE);
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