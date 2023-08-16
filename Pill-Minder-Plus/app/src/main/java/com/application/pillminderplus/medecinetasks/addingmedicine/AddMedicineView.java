package com.application.pillminderplus.medecinetasks.addingmedicine;

import android.content.Context;

public interface AddMedicineView {
    void closeActivity();
    void showToast(String text);
    void showProgressDialog();
    void hideProgressDialog();
    Context getContext();
}
