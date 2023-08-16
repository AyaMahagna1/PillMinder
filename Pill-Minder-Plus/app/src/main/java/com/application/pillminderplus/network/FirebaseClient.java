package com.application.pillminderplus.network;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.application.pillminderplus.medecinetasks.addingmedicine.AddingMedicineDelegate;
import com.application.pillminderplus.medecinetasks.displaymedicine.DeleteMedicineDelegate;
import com.application.pillminderplus.medecinetasks.displaymedicine.DisplayMedicineDelegate;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.application.pillminderplus.model.MedicineDoseStatus;
import com.application.pillminderplus.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
//Firebase client is where we connect the user with the firebase and implement the functions we need from firebase
public class FirebaseClient implements RemoteSource {
    private static FirebaseClient firebaseClient = null;
    private final FirebaseAuth mAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceMed;
    private DatabaseReference databaseReferenceMedDos;

    private FirebaseClient() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseClient getInstance() {
        if (firebaseClient == null) {
            firebaseClient = new FirebaseClient();
        }

        return firebaseClient;
    }

    // save profile image on firebase storage
    @Override
    public void enqueueCall(NetworkImageProfileDelegate networkImageProfileDelegate, Uri uriProfileImage) {
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Image").child(System.currentTimeMillis() + ".jpg");
        storageReference.putFile(uriProfileImage)
                .addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> downloadUrl = storageReference.getDownloadUrl();
                    downloadUrl
                            .addOnSuccessListener(uri -> networkImageProfileDelegate.onResponseSuccess(uri.toString()))
                            .addOnFailureListener(e -> networkImageProfileDelegate.onFailureResult(e.getMessage()));
                })
                .addOnFailureListener(e -> networkImageProfileDelegate.onFailureResult(e.getMessage()));
    }

    // register user with email and password and create user profile on firebase
    @Override
    public void enqueueCall(NetworkRegisterDelegate networkRegisterDelegate, String name, String email, String password, String profileImageURI) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(Uri.parse(profileImageURI))
                                    .build();

                            currentUser.updateProfile(profileChangeRequest)
                                    .addOnCompleteListener(task1 -> {
                                        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                                        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                                        User user = new User(uid, name, email, password, profileImageURI, null);
                                        databaseReference.child(uid).setValue(user)
                                                .addOnCompleteListener(task2 -> networkRegisterDelegate.onResponse(uid))
                                                .addOnFailureListener(e -> networkRegisterDelegate.onFailure(e.getMessage()));
                                    })
                                    .addOnFailureListener(e -> networkRegisterDelegate.onFailure(e.getMessage()));
                        }
                    }
                })
                .addOnFailureListener(e -> networkRegisterDelegate.onFailure(e.getMessage()));
    }

    // login user with email and password
    @Override
    public void enqueueCall(NetworkLoginDelegate networkDelegate, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        getUser(networkDelegate, Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                    }
                })
                .addOnFailureListener(e -> networkDelegate.onFailure(e.getMessage()));
    }

    private void getUser(NetworkLoginDelegate networkDelegate, String uid) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User value = dataSnapshot.getValue(User.class);
                    if (Objects.requireNonNull(value).getUserId().equals(uid)) {
                        networkDelegate.onResponse(value);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                networkDelegate.onFailure(error.getMessage());
            }
        });
    }

    // reset password
    @Override
    public void enqueueCall(NetworkDelegate networkDelegate, String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        networkDelegate.onResponse();
                    }
                })
                .addOnFailureListener(e -> networkDelegate.onFailure(e.getMessage()));
    }

    @Override
    public GoogleSignInClient getGoogleSignInClient(Activity activity) {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("108321287917-1fa0gn35ah7pi07i02au5d0kitt5t90q.apps.googleusercontent.com")
                .requestEmail()
                .build();

        return GoogleSignIn.getClient(activity, gso);
    }

    @Override
    public void signInWithGoogle(NetworkLoginDelegate networkDelegate, String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    String uid = Objects.requireNonNull(authResult.getUser()).getUid();
                    String displayName = authResult.getUser().getDisplayName();
                    String email = authResult.getUser().getEmail();
                    String photo_uri = Objects.requireNonNull(authResult.getUser().getPhotoUrl()).toString();

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    User user = new User(uid, displayName, email, null, photo_uri, null);
                    databaseReference.child(uid).setValue(user)
                            .addOnCompleteListener(task2 -> networkDelegate.onResponse(user))
                            .addOnFailureListener(e -> networkDelegate.onFailure(e.getMessage()));
                })
                .addOnFailureListener(e -> networkDelegate.onFailure(e.getMessage()));
    }

    //Add Medicine and its doses
    @Override
    public void enqueueCall(AddingMedicineDelegate networkDelegate, Medicine medicine, ArrayList<MedicineDose> doses) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String medID = medicine.getId();
        if (medID == null || medID.equals("")) {
            medID = databaseReference.child("medicine").push().getKey();
        }
        medicine.setUserID(uid);
        medicine.setId(medID);
        String finalMedID = medID;
        databaseReference.child("medicine").child(medID).setValue(medicine).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                for (int i = 0; i < doses.size(); i++) {
                    doses.get(i).setMedID(finalMedID);
                    String doseID = doses.get(i).getId();
                    if (doseID == null || doseID.equals("")) {
                        doseID = databaseReference.child("dose").push().getKey();
                    }
                    doses.get(i).setId(doseID);
                    databaseReference.child("dose").child(doseID).setValue(doses.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            networkDelegate.onFailure();
                        }
                    });
                }
                networkDelegate.onSuccess(medicine, doses);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                networkDelegate.onFailure();
            }
        });
    }

    //Get doses of a certain medicine
    @Override
    public void enqueueCall(DisplayMedicineDelegate networkDelegate, String medID) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("dose").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    GenericTypeIndicator<Map<String, MedicineDoseStatus>> t = new GenericTypeIndicator<Map<String, MedicineDoseStatus>>() {
                    };
                    Map<String, MedicineDoseStatus> dosesMap = task.getResult().getValue(t);

                    ArrayList<MedicineDose> doses = new ArrayList<>();


                    for (int i = 0; i < dosesMap.size(); i++) {
                        if (((MedicineDoseStatus) dosesMap.values().toArray()[i]).getMedID().equals(medID)) {
                            MedicineDose dose = new MedicineDose();
                            dose.setId((String) dosesMap.keySet().toArray()[i]);
                            dose.setTime(((MedicineDoseStatus) dosesMap.values().toArray()[i]).getTime());
                            dose.setAmount(((MedicineDoseStatus) dosesMap.values().toArray()[i]).getAmount());
                            dose.setStatus(((MedicineDoseStatus) dosesMap.values().toArray()[i]).getStatus());
                            dose.setGiverID(((MedicineDoseStatus) dosesMap.values().toArray()[i]).getGiverID());
                            dose.setSync(((MedicineDoseStatus) dosesMap.values().toArray()[i]).getSync());
                            dose.setMedID(((MedicineDoseStatus) dosesMap.values().toArray()[i]).getMedID());
                            doses.add(dose);
                        }
                    }
                    networkDelegate.onSuccess(doses);

                } else {
                    networkDelegate.onFailure();
                }
            }
        });
    }

    public void signOut() {
        mAuth.signOut();
    }

    @Override
    public void enqueueCall(DeleteMedicineDelegate networkDelegate, Medicine medicine, ArrayList<MedicineDose> doses) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String medID = medicine.getId();

        medicine.setUserID(uid);
        medicine.setId(medID);
        String finalMedID = medID;
        databaseReference.child("medicine").child(medID).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                for(int i = 0; i < doses.size(); i++) {
                    String doseID = doses.get(i).getId();

                    databaseReference.child("dose").child(doseID).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            networkDelegate.onFailure();
                        }
                    });
                }
                networkDelegate.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                networkDelegate.onFailure();
            }
        });
    }

    public void syncMedicineListToFirebase(NetworkSyncDelegate networkDelegate, List<Medicine> unSyncedMedicines) {
        databaseReference = FirebaseDatabase.getInstance().getReference("medicine");

        for (Medicine medicine : unSyncedMedicines) {
            medicine.setSync(true);
            databaseReference.child(medicine.getId()).setValue(medicine)
                    .addOnSuccessListener(unused -> {
                        networkDelegate.onResponse(medicine, true);
                    })
                    .addOnFailureListener(e -> networkDelegate.onFailure(e.getMessage()));
        }
    }

    @Override
    public void syncMedicineDosesListToFirebase(NetworkSyncDelegate networkDelegate, List<MedicineDose> unSyncedMedicinesDoses) {
        databaseReference = FirebaseDatabase.getInstance().getReference("dose");

        for (MedicineDose medicineDose : unSyncedMedicinesDoses) {
            medicineDose.setSync(true);
            databaseReference.child(medicineDose.getId()).setValue(medicineDose)
                    .addOnSuccessListener(unused -> {
                        networkDelegate.onResponse(medicineDose);
                    })
                    .addOnFailureListener(e -> networkDelegate.onFailure(e.getMessage()));
        }
    }

    @Override
    public void getAllDosesWithMedicineNameForUser(Date currentDate, String uid, NetworkHomeDelegate networkHomeDelegate) {
        Map<Medicine, List<MedicineDose>> listMap = new HashMap<>();
        List<MedicineDose> medicineDoseList = new ArrayList<>();

        databaseReferenceMed = FirebaseDatabase.getInstance().getReference("medicine");
        databaseReferenceMed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot medicineSnapshot : snapshot.getChildren()) {
                    Medicine medicine = medicineSnapshot.getValue(Medicine.class);
                    if (Objects.requireNonNull(medicine).getUserID().equals(uid)) {
                        databaseReferenceMedDos = FirebaseDatabase.getInstance().getReference("dose");
                        databaseReferenceMedDos.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot medicineDoseSnapshot : snapshot.getChildren()) {
                                    MedicineDose medicineDose = medicineDoseSnapshot.getValue(MedicineDose.class);
                                    if (Objects.requireNonNull(medicineDose).getMedID().equals(medicine.getId())) {
                                        medicineDoseList.add(medicineDose);
                                    }
                                }
                                listMap.put(medicine, medicineDoseList);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                networkHomeDelegate.onFailure(error.getMessage());
                            }
                        });
                    }
                }

                Map<Medicine, MedicineDose> returnedMedDosMap = new HashMap<>();

                for (Map.Entry<Medicine, List<MedicineDose>> entry : listMap.entrySet()) {
                    Medicine key = entry.getKey();
                    List<MedicineDose> value = entry.getValue();
                    for (int i=0; i<value.size(); i++) {
                        String[] dateTime = value.get(i).getTime().split("T"); // 2022-04-25 T 03:41
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date parsedDate = formatter.parse(dateTime[0]);

                            if (Objects.requireNonNull(parsedDate).equals(currentDate)) {
                                returnedMedDosMap.put(key, value.get(i));
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                networkHomeDelegate.onResponse(returnedMedDosMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                networkHomeDelegate.onFailure(error.getMessage());
            }
        });
    }
}