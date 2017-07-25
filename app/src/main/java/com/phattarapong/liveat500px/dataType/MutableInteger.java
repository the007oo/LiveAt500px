package com.phattarapong.liveat500px.dataType;

import android.os.Bundle;

/**
 * Created by Phattarapong on 21-Jul-17.
 */

public class MutableInteger {
    public MutableInteger(int value) {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Bundle onSavedInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putInt("value", value);
        return bundle;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        value = savedInstanceState.getInt("value");

    }
}
