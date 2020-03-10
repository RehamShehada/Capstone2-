package com.example.giftsstore;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class buyNowActivity extends AppCompatActivity {
    public void readIt(View view) {
        String giftUrl= "";
        Intent n = new Intent(Intent.ACTION_VIEW, Uri.parse(giftUrl));
        startActivity(n);

    }
}