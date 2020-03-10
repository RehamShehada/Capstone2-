package com.example.giftsstore.Widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.giftsstore.R;
import com.example.giftsstore.database.Gift;

import java.util.ArrayList;
import java.util.List;

public class LaterBuyWidget extends AppWidgetProvider {
    private static List<Gift> gifts = new ArrayList<>();
    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int[] appWidgetIds, List<Gift > laterbuy) {

        Log.d("MyLog","widgetUpdate");
        gifts = laterbuy;
        for (int appWidgetId : appWidgetIds) {
            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.will_buy_activity);
            Intent intent = new Intent(context, widgetListAdapter.class);
            views.setRemoteAdapter(R.id.widget_willbuy_list, intent);
            ComponentName component = new ComponentName(context, LaterBuyWidget.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_willbuy_list);
            appWidgetManager.updateAppWidget(component, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static List<Gift> getGifts(){
        return gifts;
    }
}
