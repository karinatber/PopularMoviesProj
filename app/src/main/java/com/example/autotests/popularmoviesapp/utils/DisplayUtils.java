package com.example.autotests.popularmoviesapp.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class DisplayUtils {
    final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private Display mDisplay;

    public DisplayUtils(WindowManager windowManager, Context context) {
        mDisplay = windowManager.getDefaultDisplay();
        mContext = context;
    }

    public int getOrientation(){
        if (mDisplay.getRotation() == Surface.ROTATION_0 || mDisplay.getRotation() == Surface.ROTATION_180)
            return 0; //portrait
        else
            return 1; //landscape
    }

    public int[] getSpanByDisplay(){
        Point displaySize = getDisplaySize();
        Log.i(TAG, "getSpanByDisplay: displaySize->  x: "+displaySize.x+" y: "+displaySize.y);

        DisplayMetrics metrics = new DisplayMetrics();
        mDisplay.getMetrics(metrics);
        float density = mContext.getResources().getDisplayMetrics().density;
        int convertFactor = (int)(160*density);
        int[] spanxy = new int[]{
                displaySize.x/convertFactor,
                displaySize.y/convertFactor
        };
        Log.i(TAG, "getSpanByDisplay: span -> x: "+spanxy[0]+" y:"+spanxy[1]);
        return spanxy;
    }

    public Point getDisplaySize(){
        Point size = new Point();
        mDisplay.getSize(size);
        return size;
    }
}
