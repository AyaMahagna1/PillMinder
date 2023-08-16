package com.application.pillminderplus.caregivers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.application.pillminderplus.NetworkConnection;
import com.application.pillminderplus.R;
import com.application.pillminderplus.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;
//Care giver / invite friend screen
public class CaregiversActivity extends AppCompatActivity {
    DatabaseReference checkEmailRef,reqRef;
    TextView emailText;
    Button sendBtn;
    //Sender data
    String senderId;
    String senderUsername;
    String userImg;
    User user;
    Boolean flag;

    //receiver/caregiver data
    String email;
    String receiverId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregivers);
        emailText = findViewById(R.id.emailTextId);
        sendBtn = findViewById(R.id.sendBtnId);
        Intent inIntent = getIntent();
        senderId = inIntent.getStringExtra("userId");
        senderUsername = inIntent.getStringExtra("userName");
        userImg = inIntent.getStringExtra("userImg");
        checkEmailRef = FirebaseDatabase.getInstance().getReference().child("Users");
        reqRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        sendBtn.setOnClickListener(v -> {
            email = emailText.getText().toString();
            if(NetworkConnection.isNetworkAvailable(this)){
                Toast.makeText(this, "You are connected", Toast.LENGTH_SHORT).show();
                checkEmailExisting(email);
            }
            else
                Toast.makeText(this, "You are not connected", Toast.LENGTH_SHORT).show();

        });
    }

    private void checkEmailExisting(String email){
        flag = false;
        checkEmailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    user = dataSnapshot.getValue(User.class);
                    if(Objects.requireNonNull(user).getEmail().equals(email)){
                        receiverId = user.getUserId();
                        sendRequest(receiverId);
                        flag = true;
                        break;
                    }

                }
                if(flag){
                    Toast.makeText(getApplicationContext(), "True Email", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "False Email", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendRequest(String receiverId) {
        String id = reqRef.push().getKey();
        reqRef.child(id).setValue(new RequestPojo(receiverId ,senderId,senderUsername,userImg, "pending")).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                    emailText.setText("");
                }

            }
        });
    }
}