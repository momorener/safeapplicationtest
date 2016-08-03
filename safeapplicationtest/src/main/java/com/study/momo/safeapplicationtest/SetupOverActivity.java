package com.study.momo.safeapplicationtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.study.momo.safeapplicationtest.utils.ConstantValue;
import com.study.momo.safeapplicationtest.utils.SpUtils;

/**
 * Created by momo on 2016/7/28.
 */
public class SetupOverActivity extends Activity {
    private TextView  tv_safe_number;
    private TextView  tv_reset_setup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean setup_over = SpUtils.getBoolean(this, ConstantValue.SETUP_OVER, false);

        if (setup_over){
            //密码输入成功，并且四个导航界面设置完成 -->停留在设置完成功能列表界面
            setContentView(R.layout.activity_setup_over);
            //Log.i("tag","密码输入成功，并且四个导航界面设置完成 -->停留在设置完成功能列表界面");
            initUI();

        }else {
            //密码输入成功，四个导航界面未设置完成 -->跳转到导航界面第1个
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);
            //Log.i("tag","密码输入成功，四个导航界面未设置完成 -->跳转到导航界面第1个");
            finish();
        }


    }

    private void initUI(){
        tv_safe_number = (TextView)findViewById(R.id.tv_safe_number);
        String phone = SpUtils.getString(this,ConstantValue.CONTACT_PHONE,"");
        tv_safe_number.setText(phone);

        tv_reset_setup = (TextView)findViewById(R.id.tv_reset_setup);
        tv_reset_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
                startActivity(intent);
                //Log.i("tag","密码输入成功，四个导航界面未设置完成 -->跳转到导航界面第1个");
                finish();
            }
        });
    }


}
