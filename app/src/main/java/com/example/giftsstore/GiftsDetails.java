package com.example.giftsstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.giftsstore.adapter.GiftsAdapter;
import com.example.giftsstore.database.AppDatabase;
import com.example.giftsstore.database.AppExecutors;
import com.example.giftsstore.database.Gift;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GiftsDetails extends AppCompatActivity {
    @BindView(R.id.buynow)
    Button buy_url;
    @BindView(R.id.add_later)
    Button add_later;
    @BindView(R.id.add_fav)
    Button add_fav;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.giftpic)
    ImageView giftPic;
    @BindView(R.id.giftdesc)
    TextView giftdesc;
    private Gift b;
    private AppDatabase db;
    private LiveData<Gift> f;
    private boolean fav;

    private FirebaseAuth firebaseAuth;
    private GiftsAdapter mGiftsAdapter;
    private Context context;
    private Bundle bundle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifts_details);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.TOP);
            slide.addTarget(R.id.add_fav);
            getWindow().setEnterTransition(slide);
            getWindow().setExitTransition(new Explode());
        }

        db = AppDatabase.getInstance(getApplicationContext());

        String Extra = getIntent().getStringExtra(Gift.class.getName());
        Gson gson = new Gson();
        if (savedInstanceState == null) {
            b = gson.fromJson(Extra, Gift.class);
        }
        giftdesc.setText(b.getDesc());
        String imageUri = b.getPic_url();
        Picasso.get().load(imageUri).into(giftPic);

        //setSupportActionBar(mToolBar);
        title.setText(b.getName());
        //setTitle("");
        add_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (fav) {
                            //remove from database
                            db.laterDAO().deleteGift(b);
                        } else {
                            //add to database
                            db.laterDAO().insertGift(b);
                        }
                    }
                });
                if (fav) {
                    add_later.setText("@string/add_later");
                    Toast.makeText(GiftsDetails.this,
                            "Removed from Later read", Toast.LENGTH_SHORT).show();
                } else {
                    add_later.setText("@string/remove_later");
                    Toast.makeText(GiftsDetails.this,
                            "Added To Later", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buy_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String giftUrl = b.getBuy_url();
                Intent n = new Intent(Intent.ACTION_VIEW, Uri.parse(giftUrl));
                startActivity(n);
            }
        });
    }
    @Override
    protected void onResume() {
        f = db.laterDAO().loadgiftById(b.getId());
        f.observeForever(new Observer< Gift >() {
            @Override
            public void onChanged(Gift exercise) {
                if (f.getValue() != null) {
                    fav = true;
                    add_fav.setText("Remove from Later buy");
                } else {
                    fav = false;
                }
            }
        });
        if (fav) {
            add_later.setText("Remove from Later");
        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("gift", b);
        super.onSaveInstanceState(outState);

    }
}
