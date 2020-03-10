package com.example.giftsstore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.ActivityOptions;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftsstore.Widget.LaterBuyWidget;
import com.example.giftsstore.adapter.GiftsAdapter;
import com.example.giftsstore.database.LaterViewModel;
import com.example.giftsstore.database.Gift;
import com.example.giftsstore.database.GiftsAsyncTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
public class StoreActivity extends AppCompatActivity {
    @BindView(R.id.fav) Button fav;
    @BindView(R.id.buyLater) Button buyLater;
    @BindView(R.id.bought)Button bought;
    @BindView(R.id.all)Button all;

    @BindView(R.id.toolbar)
    Toolbar myToolBar;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private FirebaseAuth firebaseAuth;
    private GiftsAdapter mGiftsAdapter;
    private ArrayList<Gift> gifts = new ArrayList<>();
    private Context context;
    private Bundle bundle;
    private List<Gift> later = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.addTarget(R.id.recycler_view);
            getWindow().setEnterTransition(slide);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        setSupportActionBar(myToolBar);
        title.setText("Gifts ");
        setTitle("");
        context = getApplicationContext();
        initRecyclerView();
        setUpViewModel();
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callTheAsyncTask();
            }
        });
        buyLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGiftsAdapter.setGifts(later);
                mGiftsAdapter.notifyDataSetChanged();            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all:
                callTheAsyncTask();
                break;
            case R.id.buyLater:
                mGiftsAdapter.setGifts(later);
                mGiftsAdapter.notifyDataSetChanged();
                break;
            case R.id.log_out:
                LogOut();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    private void LogOut() {
        firebaseAuth.signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mGiftsAdapter = new GiftsAdapter(gifts, context);
        mRecyclerView.setAdapter(mGiftsAdapter);
        mGiftsAdapter.setItemClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
                int position = viewHolder.getAdapterPosition();
                Intent n = new Intent(context, GiftsDetails.class);
                Gson gson = new Gson();
                n.putExtra(Gift.class.getName(), gson.toJson(mGiftsAdapter.getGifts().get(position)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    startActivity(n, bundle);
                }
            }
        });
    }

    private void callTheAsyncTask(){
        new GiftsAsyncTask(){
            @Override
            protected void onPostExecute(List<Gift> result) {
                super.onPostExecute(result);
                mGiftsAdapter.setGifts(result);
                mGiftsAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void setUpViewModel(){
        Log.d("MyLog","setUpViewModel");
        LaterViewModel viewModel = ViewModelProviders.of(this).get(LaterViewModel.class);
        viewModel.getLater().observe(this, new Observer<List<Gift>>() {
            @Override
            public void onChanged(List<Gift> gifts) {
                later.clear();
                later.addAll(gifts);
                mGiftsAdapter.notifyDataSetChanged();
                for (int i=0; i< gifts.size();i++)
                    Log.d("MyLogWidget", gifts.get(i).getId()+"");
                addLaterToWidget(gifts);
            }
        });}

    private void addLaterToWidget(List<Gift> b){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(context, LaterBuyWidget.class));
        LaterBuyWidget.updateAppWidget(context, appWidgetManager, appWidgetIds, b);

    }
}
