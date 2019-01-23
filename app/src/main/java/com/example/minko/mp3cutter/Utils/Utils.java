package com.example.minko.mp3cutter.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by HP 6300 Pro on 5/10/2018.
 */

public class Utils {

    public static final String MARKET_DETAILS_ID = "market://details?id=";
    public static final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=";
    public static final String PLAY_DEV_LINK = "https://play.google.com/store/apps/dev?id=";
    public static final String MARKET_DEV_ID = "market://dev?id=";
    public static final String HDP_SOLUTION_ID = "8500255335661561211";
    public static void goToStore(Context context, String appId) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_DETAILS_ID + appId)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_LINK + appId)));
        }
    }

    public static void goToMoreApp(Context context, String devid) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_DEV_ID + devid)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_DEV_LINK + devid)));
        }
    }

    public static void ShareApp(Context context, String appid){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,PLAY_STORE_LINK+appid);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }
}
