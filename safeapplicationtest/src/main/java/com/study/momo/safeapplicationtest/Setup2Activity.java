package com.study.momo.safeapplicationtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.study.momo.safeapplicationtest.utils.ConstantValue;
import com.study.momo.safeapplicationtest.utils.SpUtils;
import com.study.momo.safeapplicationtest.utils.ToastUtil;
import com.study.momo.safeapplicationtest.view.SettingItemView;

/**
 * Created by momo on 2016/7/28.
 */
public class Setup2Activity extends BaseSetupActivity {
    private SettingItemView siv_sim_bound;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup2);

        initUI();
    }
    private void initUI(){
        siv_sim_bound = (SettingItemView)findViewById(R.id.siv_sim_bound);
        //回显过程（读取已有绑定状态，用作显示，sp中是否存储了sim卡的序列号）
        String sim_number = SpUtils.getString(this, ConstantValue.SIM_NUMBER,"");
        //判断是否序列卡号未空
        if (TextUtils.isEmpty(sim_number)){
            siv_sim_bound.setCheck(false);
        }else{
            siv_sim_bound.setCheck(true);
        }

        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取原有状态
                boolean isCheck = siv_sim_bound.isCheck();
                //将原有状态取反，设置当前条目，存储（序列卡号）
                siv_sim_bound.setCheck(!isCheck);
                if (!isCheck){
                    //存储序列卡号
                        //获取sim卡序列号TelephoneManager
                    TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String simSerialNumber = manager.getSimSerialNumber();
                    SpUtils.putString(getApplicationContext(),ConstantValue.SIM_NUMBER,simSerialNumber);
                }else {
                    //将存储序列卡号的节点从sp中删除
                    SpUtils.remove(getApplicationContext(),ConstantValue.SIM_NUMBER);
                }
            }
        });

    }

    public void showNextPage(){
        String serialNumber = SpUtils.getString(this,ConstantValue.SIM_NUMBER,"");
        if (!TextUtils.isEmpty(serialNumber)){
            Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
            startActivity(intent);
            finish();
            //开启平移动画
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);

        }else{
            ToastUtil.show(this, "请绑定sim卡");
        }


    }

    public void showPrePage(){
        Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
        startActivity(intent);
        finish();
        //开启平移动画
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }
}
