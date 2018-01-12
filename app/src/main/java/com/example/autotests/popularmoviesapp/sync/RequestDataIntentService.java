package com.example.autotests.popularmoviesapp.sync;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by karina.bernice on 12/01/2018.
 */

public class RequestDataIntentService extends IntentService {

    public RequestDataIntentService(){
        super ("RequestDataIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        int id = intent.getIntExtra(RequestDataTasks.MOVIE_ID, 0);
        RequestDataTasks.executeTask(this, action, id);
        sendBroadcast(new Intent(RequestDataTasks.ACTION_REQUESTS_FINISHED));
    }
}
