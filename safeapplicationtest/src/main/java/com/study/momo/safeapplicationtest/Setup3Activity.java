package com.study.momo.safeapplicationtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.study.momo.safeapplicationtest.utils.ConstantValue;
import com.study.momo.safeapplicationtest.utils.SpUtils;
import com.study.momo.safeapplicationtest.utils.ToastUtil;

/**
 * Created by momo on 2016/7/28.
 */
public class Setup3Activity extends BaseSetupActivity {
    private EditText et_phone_number;
    private Button bt_select_number;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setup3);

        initUI();

    }
    private void initUI(){
        //显示电话号码的输入框
        et_phone_number = (EditText)findViewById(R.id.et_phone_number);
        //获取联系人电话号码的回显显示
        String phone = SpUtils.getString(this,ConstantValue.CONTACT_PHONE,"");
        et_phone_number.setText(phone);

        //点击选择联系人的对话框
        bt_select_number = (Button)findViewById(R.id.bt_select_number);

        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ContactListActivity.class);
                startActivityForResult(intent,0);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( data!= null) {
            //返回到当前界面时候，接受结果的方法
            String phone = data.getStringExtra("phone");
            //将特殊字符过滤(中划线转换成空字符串)
            phone = phone.replace("-", "").replace(" ", "").trim();
            et_phone_number.setText(phone);

            SpUtils.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    public void showNextPage(){
        //点击按钮之后，需要获取输入框中的联系人，再做下一页操作
        String phone = et_phone_number.getText().toString();

        //在sp中存储了相关联系人之后可以跳转到下一个界面
       // String contact_phone = SpUtils.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
        if (!TextUtils.isEmpty(phone)) {
            Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
            startActivity(intent);
            finish();

            //如果是输入的电话号码，则需要保存数据
            SpUtils.putString(getApplicationContext(),ConstantValue.CONTACT_PHONE,phone);

            //开启平移动画
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);

        }else {
            ToastUtil.show(this, "请输入电话号码");
        }
    }

    public void showPrePage(){
        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);
        finish();
        //开启平移动画
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }
}
