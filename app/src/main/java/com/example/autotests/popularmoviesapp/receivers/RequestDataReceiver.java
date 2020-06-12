package com.example.autotests.popularmoviesapp.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.autotests.popularmoviesapp.interfaces.RequestDataHandlerInterface;
import com.example.autotests.popularmoviesapp.sync.RequestDataTasks;

public class RequestDataReceiver extends BroadcastReceiver {

    private RequestDataHandlerInterface mRequestDataHandlerInterface;

    public RequestDataReceiver(Context context) {
        mRequestDataHandlerInterface = (RequestDataHandlerInterface) context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String actionId = intent.getStringExtra(RequestDataTasks.REQUEST_ID);

        mRequestDataHandlerInterface.onDataReady(actionId, intent.getParcelableArrayListExtra(RequestDataTasks.REQUEST_DATA_EXTRA));
    }
}
