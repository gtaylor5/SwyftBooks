package com.swyftlabs.swyftbooks1.Requests;

import com.swyftlabs.swyftbooks1.Classes.Offer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Gerard on 2/16/2017.
 */

public interface ServerCallback {

    <T> void onSuccess(ArrayList<T> items);
    void onSuccess(HashMap<String, ArrayList<Offer>> items);


}
