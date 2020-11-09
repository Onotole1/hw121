package com.artyushin.hw121;

import android.app.Activity;

public class Sample extends Activity {
    private static String sample;

    public static void setSample(String sample) {
        Sample.sample = sample;
    }

    public static String getSample() {
        return sample;
    }

    public String toString(){
        return String.valueOf(sample);
    }
}
