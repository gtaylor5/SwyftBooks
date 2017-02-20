package com.swyftlabs.swyftbooks;

import java.util.ArrayList;

/**
 * Created by Gerard on 2/16/2017.
 */

public interface ServerCallback {

    <T> void onSuccess(T items);
    <T> void onSuccess(ArrayList<T> items);

}
