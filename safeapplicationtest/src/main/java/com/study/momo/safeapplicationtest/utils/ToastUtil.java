package com.study.momo.safeapplicationtest.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by momo on 2016/7/8.
 */
public class ToastUtil {
    /**
     *
     * @param context 上下文
     * @param text  打印内容
     */
    public static void show(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
}
