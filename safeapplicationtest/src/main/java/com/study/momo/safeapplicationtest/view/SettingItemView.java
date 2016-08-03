package com.study.momo.safeapplicationtest.view;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.study.momo.safeapplicationtest.R;

/**
 * Created by momo on 2016/7/15.
 */
public class SettingItemView extends RelativeLayout{
    private CheckBox cb_box;
    private TextView tv_des;
    private String mDestitle;
    private String mDesoff;
    private String mDeson;
    private static final String tag = null;
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.study.momo.safeapplicationtest";

    public SettingItemView(Context context) {
        this(context,null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //xml 转化成view   将设置界面的一个条目转换成view对象
        View.inflate(context, R.layout.setting_item_view, this);

        //自定义组合空间中的标题描述
        TextView tv_title = (TextView)findViewById(R.id.tv_title);
        tv_des = (TextView)findViewById(R.id.tv_des);
        cb_box= (CheckBox)findViewById(R.id.cb_box);

        //获取自定义以及原生属性的操作，AttributeSet attrs对象获取
        initAttrs(attrs);
        tv_title.setText(mDestitle);

    }

    /**
     * 返回属性集合中自定义属性的属性值
     * @param attrs 构造方法中维护好的属性集合
     */
    private void initAttrs(AttributeSet attrs){
        //获取属性的总个数
        /*Log.i(tag, "attrs.getAttributeCount() = " + attrs.getAttributeCount());
        //获取属性名称以及属性值
        for(int i =0; i < attrs.getAttributeCount(); i++){
            Log.i(tag, "name = " + attrs.getAttributeName(i));
            Log.i(tag, "value = "+ attrs.getAttributeValue(i));
        }*/

        //通过命名空间+属性值获取属性值
        mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(NAMESPACE, "deson");

        Log.i(tag, mDestitle);
        Log.i(tag, mDesoff);
        Log.i(tag, mDeson);


    }

    /**
     * 判断是否开启的方法
     * 返回当前SettingItemView是否选中状态  true开启  （checkBox放回true）
     * @return
     */
    public  boolean isCheck(){
        return cb_box.isChecked();
    }

    /**
     *
     * @param isCheck  作为是否开启的变量，由点击过程中去传递
     */

    public  void setCheck(boolean isCheck){
        //当前条目在选中的过程中，cb_box选中状态也随着isCheck变化
        cb_box.setChecked(isCheck);
        if (isCheck){
            //开启
            tv_des.setText(mDeson);
        }else {
            //关闭
            tv_des.setText(mDesoff);
        }

    }


}
