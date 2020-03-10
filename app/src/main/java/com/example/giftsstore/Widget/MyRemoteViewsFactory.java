package com.example.giftsstore.Widget;

import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.giftsstore.R;
import com.example.giftsstore.database.Gift;

import java.util.List;

public class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private List<Gift> gifts;
    public MyRemoteViewsFactory(Context context){
        this.context = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        gifts = LaterBuyWidget.getGifts();
    }
    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return gifts.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_card);
        Gift b = gifts.get(i);
        views.setTextViewText(R.id.gift_info, b.getName()+" : "+b.getBuy_url());
        Log.d("MyLog",b.getName());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}