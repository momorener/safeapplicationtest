package com.study.momo.safeapplicationtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.study.momo.safeapplicationtest.utils.ConstantValue;
import com.study.momo.safeapplicationtest.utils.SpUtils;
import com.study.momo.safeapplicationtest.utils.ToastUtil;

/**
 * Created by momo on 2016/7/28.
 */
public class Setup4Activity extends BaseSetupActivity {
    private CheckBox cb_box;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup4);

        initUI();
    }
    private void initUI(){
        cb_box = (CheckBox) findViewById(R.id.cb_box);
        //是否选中状态的回显
        boolean open_security = SpUtils.getBoolean(this,ConstantValue.OPEN_SECURITY,false);
        //根据状态，修改checkbox后续的文字显示
        cb_box.setChecked(open_security);
        if (open_security){
            cb_box.setText("安全设置已开启");
        }else {
            cb_box.setText("安全设置已关闭");
        }
        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //isChecked点击后的状态，存储点击后状态
                SpUtils.putBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, isChecked);
                //根据开启关闭状态去修改显示的文字
                if (isChecked){
                    cb_box.setText("安全设置已开启");
                }else {
                    cb_box.setText("安全设置已关闭");
                }

            }
        });

    }

    public void showNextPage(){
        boolean open_security = SpUtils.getBoolean(this, ConstantValue.OPEN_SECURITY,false);
        if (open_security) {
            Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
            startActivity(intent);
            finish();
            SpUtils.putBoolean(this, ConstantValue.SETUP_OVER, true);
            //开启平移动画
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else {
            ToastUtil.show(getApplicationContext(),"请开启防盗保护设置");
        }
    }

    public void showPrePage(){
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);
        finish();
        //开启平移动画
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }
}
