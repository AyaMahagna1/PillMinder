package com.application.pillminderplus;

import com.application.pillminderplus.model.Medicine;
import com.application.pillminderplus.model.MedicineDose;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
//Convert Json objects to database (model) objects and vice versa
public class JSONSerializer {
    public static String serializeMedicine(Medicine medicine) {
        Gson gson = new Gson();
        return gson.toJson(medicine);
    }

    public static Medicine deserializeMedicine(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Medicine.class);
    }

    public static String serializeMedicineDose(MedicineDose dose) {
        Gson gson = new Gson();
        return gson.toJson(dose);
    }

    public static MedicineDose deserializeMedicineDose(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, MedicineDose.class);
    }

    public static String serializeMedicineDoses(ArrayList<MedicineDose> doses) {
        Gson gson = new Gson();
        return gson.toJson(doses);
    }

    public static ArrayList<MedicineDose> deserializeMedicineDoses(String jsonString) {
        Gson gson = new Gson();
        Type listOfMedicineDose = new TypeToken<ArrayList<MedicineDose>>() {}.getType();
        return gson.fromJson(jsonString, listOfMedicineDose);
    }
}
