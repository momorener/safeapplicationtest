package com.study.momo.safeapplicationtest;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;


import com.study.momo.safeapplicationtest.utils.ConstantValue;
import com.study.momo.safeapplicationtest.utils.SpUtils;
import com.study.momo.safeapplicationtest.utils.ToastUtil;

public class HomeActivity extends Activity {

    private GridView gv_home;
    private String[] mTitleStrs;
    private int[] mDrawableIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();

        initDate();

    }
    private void initDate(){
        //准备数据（文字+图片）
        mTitleStrs = new String[]{
            "手机防盗","通信卫士","软件管理", "进程管理",
                "流量统计","手机杀毒","缓存清理","高级工具","设置中心"
        } ;
        mDrawableIds = new int[]{
                R.drawable.home_safe,R.drawable.home_callmsgsafe,R.drawable.home_apps,
                R.drawable.home_taskmanager,R.drawable.home_netmanager,R.drawable.home_trojan,
                R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings
        };
        //为九宫格控件设置数据适配器
        gv_home.setAdapter(new MyAdapter());

        //注册九宫格单个条目点击事件
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //点中列表条目索引position
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        showDialog();
                        //Log.i("tag","运行手机防盗");
                        break;
                    case 7:
                        Intent intent7 = new Intent(getApplicationContext(),AToolActivity.class);
                        startActivity(intent7);
                        break;
                    case 8:
                        Intent intent8 = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent8);
                        break;
                }
            }
        });

    }
    protected void showDialog(){
        //判断本地是否有存储密码
        String psd =  SpUtils.getString(this, ConstantValue.MOBILE_SAFE_PSD, "");

        if (TextUtils.isEmpty(psd)){
            //1、初始设置密码对话框
            showSetPsdDialog();
            //Log.i("tag","初始设置密码对话框");
        }else {
            //2、确认密码对话框
            showConfirmDialog();

            //Log.i("tag","设置确认密码对话框");
        }


    }

    /**
     * 设置密码对话框
     */
    private void showSetPsdDialog(){
        Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_set_psd , null);
        //让对话框显示一个自己定义的对话框界面效果
        dialog.setView(view);
        dialog.show();

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_set_psd = (EditText) view.findViewById(R.id.et_set_psd);
                EditText et_confirm_psd = (EditText) view.findViewById(R.id.et_confirm_psd);

                String psd = et_set_psd.getText().toString();
                String confirmPsd = et_confirm_psd.getText().toString();

                if (!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(confirmPsd)){
                    if (psd.equals(confirmPsd)) {
                        //进入应用手机防盗模块  开启一个新的activity
                        Intent intent = new Intent(getApplicationContext(),SetupOverActivity.class);
                        startActivity(intent);
                        //跳转到新的界面后需要隐藏对话框
                        dialog.dismiss();

                        SpUtils.putString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, psd);

                    }
                    else {
                        ToastUtil.show(getApplicationContext(), "确认密码错误");
                    }
                }else {
                    //提示用户密码输入有为空的情况
                    ToastUtil.show(getApplicationContext(),"请输入密码");
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    /**
     * 确认密码对话框
     */
    private void showConfirmDialog(){
        Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_confirm_psd , null);
        //让对话框显示一个自己定义的对话框界面效果
        dialog.setView(view);
        dialog.show();

        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_confirm_psd = (EditText) view.findViewById(R.id.et_confirm_psd);

                String confirmPsd = et_confirm_psd.getText().toString();

                if ( !TextUtils.isEmpty(confirmPsd)){
                    String psd = SpUtils.getString(getApplicationContext(),ConstantValue.MOBILE_SAFE_PSD,"");
                    if (psd.equals(confirmPsd)) {
                        //进入应用手机防盗模块  开启一个新的activity
                        Intent intent = new Intent(getApplicationContext(),SetupOverActivity.class);
                       //Log.i("tag", "确认密码对话框跳转");
                        startActivity(intent);
                        //跳转到新的界面后需要隐藏对话框
                        dialog.dismiss();
                    }
                    else {
                        ToastUtil.show(getApplicationContext(), "确认密码错误");
                    }
                }else {
                    //提示用户密码输入有为空的情况
                    ToastUtil.show(getApplicationContext(),"请输入密码");
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            //条目总数  文字组数 == 图片张数
            return mTitleStrs.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleStrs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(),R.layout.gridview_item,null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            ImageView iv_icon = (ImageView)view.findViewById(R.id.iv_icon);
            tv_title.setText(mTitleStrs[position]);
            iv_icon.setBackgroundResource(mDrawableIds[position]);
            return view;
        }
    }

    private void initUI(){
        gv_home = (GridView) findViewById(R.id.gv_home);
    }
}
