package com.example.hello.hellomovie.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by Hello on 2016/12/20.
 */

public class MovieToastUtil {


    private MovieToastUtil() {

    }

    private static Toast mToast = null;

    public static void showMsg(Context context, final String text) {

        if (context == null || TextUtils.isEmpty(text)) {
            return;
        }

        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public static void showMsg(Context context, int resId) {

        if (context == null) {
            return;
        }

        if (mToast == null) {
            mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }

        mToast.show();
    }
}