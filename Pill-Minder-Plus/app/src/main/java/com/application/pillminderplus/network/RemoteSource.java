package com.application.pillminderplus.network;

import android.app.Activity;
import android.net.Uri;

import com.application.pillminderplus.medecinetasks.displaymedicine.DeleteMedicineDelegate;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.application.pillminderplus.medecinetasks.addingmedicine.AddingMedicineDelegate;
import com.application.pillminderplus.medecinetasks.displaymedicine.DisplayMedicineDelegate;
import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//Interface for firebase Client
public interface RemoteSource {
    void enqueueCall(NetworkImageProfileDelegate networkImageProfileDelegate, Uri uriProfileImage);
    void enqueueCall(NetworkRegisterDelegate networkRegisterDelegate, String name, String email, String password, String profileImageURI);
    void enqueueCall(NetworkLoginDelegate networkDelegate, String email, String password);
    void enqueueCall(NetworkDelegate networkDelegate, String email);

    GoogleSignInClient getGoogleSignInClient(Activity activity);
    void signInWithGoogle(NetworkLoginDelegate networkDelegate, String idToken);
    void enqueueCall(AddingMedicineDelegate networkDelegate, Medicine medicine, ArrayList<MedicineDose> doses);
    void enqueueCall(DisplayMedicineDelegate networkDelegate, String medID);
    void enqueueCall(DeleteMedicineDelegate networkDelegate, Medicine medicine, ArrayList<MedicineDose> doses);

    void signOut();

    void syncMedicineListToFirebase(NetworkSyncDelegate networkDelegate, List<Medicine> unSyncedMedicines);
    void syncMedicineDosesListToFirebase(NetworkSyncDelegate networkDelegate, List<MedicineDose> unSyncedMedicinesDoses);

    void getAllDosesWithMedicineNameForUser(Date currentDate, String uid, NetworkHomeDelegate networkHomeDelegate);
}
