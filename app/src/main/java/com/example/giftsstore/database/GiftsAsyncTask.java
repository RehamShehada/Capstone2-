package com.example.giftsstore.database;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GiftsAsyncTask extends AsyncTask<Void, Void, List<Gift>> {
    private FirebaseFirestore db;
    private List<Gift> gifts;

    public GiftsAsyncTask(){
        gifts = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected List<Gift> doInBackground(Void... voids) {
        db.collection("gift")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Log.d("Fetch Gift", document.getId() + " => " + document.getData());
                            Gift e=document.toObject(Gift.class);
                            e.setId(Integer.parseInt(document.getId()));
                            gifts.add(e);
                        }
                        // update based on adapter
                        onPostExecute(gifts);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Fetch Gift", "Error getting documents.", e);
                    }
                });
        return gifts;
    }

    protected void onPostExecute(List<Gift> result) {
    }

}