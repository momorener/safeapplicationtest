package com.study.momo.safeapplicationtest;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.study.momo.safeapplicationtest.utils.ConstantValue;
import com.study.momo.safeapplicationtest.utils.SpUtils;
import com.study.momo.safeapplicationtest.view.SettingItemView;

/**
 * Created by momo on 2016/7/15.
 */
public class SettingActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        initUpdate();
    }

    /**
     * 版本更新开关
     */
    private void initUpdate(){
        final SettingItemView siv_update = (SettingItemView)findViewById(R.id.siv_update);

        //获取已有的开关状态，用作显示
        boolean open_update = SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        //是否选中，根据上一次存储的结果决定
        siv_update.setCheck(open_update);

        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取之前的选中状态
                boolean isCheck = siv_update.isCheck();
                //将原有状态取反
                siv_update.setCheck(!isCheck);
                //将取反后的状态存储到相应的sp中
                SpUtils.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !isCheck);
            }
        });
    }
}
