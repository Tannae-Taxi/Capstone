package com.example.tannae.sub;

import android.content.Context;
import android.widget.Toast;

public class Toaster {
    private static Toast toast;
    public static void show(Context context, String message) {
        if (toast == null)
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        else {
            toast.cancel();
            toast.setText(message);
        }
        toast.show();
    }
}
