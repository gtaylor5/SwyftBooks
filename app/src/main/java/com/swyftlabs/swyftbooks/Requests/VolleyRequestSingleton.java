package com.swyftlabs.swyftbooks.Requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Gerard on 2/17/2017.
 */

public class VolleyRequestSingleton {

    private static VolleyRequestSingleton instance;
    private RequestQueue requestQueue;
    private static Context context;

    private VolleyRequestSingleton(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleyRequestSingleton getInstance(Context context){
        if(instance == null){
            instance = new VolleyRequestSingleton(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        requestQueue.start();
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }

}
