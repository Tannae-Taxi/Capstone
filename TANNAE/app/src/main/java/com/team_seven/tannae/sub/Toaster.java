package com.team_seven.tannae.sub;

import android.content.Context;
import android.widget.Toast;

public class Toaster {
    public static Toast toast;
    public static void show(Context context, String message) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
