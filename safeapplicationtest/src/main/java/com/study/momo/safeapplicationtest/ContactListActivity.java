package com.study.momo.safeapplicationtest;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.study.momo.safeapplicationtest.utils.ConstantValue;
import com.study.momo.safeapplicationtest.utils.SpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by momo on 2016/7/28.
 */
public class ContactListActivity extends Activity {
    private ListView lv_contact;
    private MyAdapter mAdapter;
    private List<HashMap<String,String>> contactList = new ArrayList<HashMap<String,String>>();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //8、填充数据适配器
            mAdapter = new MyAdapter();
            lv_contact.setAdapter(mAdapter);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_list);

        initUI();
        initDate();
    }

    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public HashMap<String, String> getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(),R.layout.listview_contact_item, null);

            TextView tv_name = (TextView)view.findViewById(R.id.tv_name);
            TextView tv_phone = (TextView)view.findViewById(R.id.tv_phone);

            tv_name.setText(getItem(position).get("name"));
            tv_phone.setText(getItem(position).get("phone"));
            return view;
        }
    }


    /**
     * 获取系统联系人数据方法
     */
    private void initDate(){
        //因为读取联系人可能是一个耗时操作，放置在子线程中处理
        new Thread(){
            @Override
            public void run() {
                //1、获取内容解析器对象
                ContentResolver contentResolver = getContentResolver();
                //2、做查询系统俩喜人数据库表过程（读取联系人权限）
                Cursor cursor = contentResolver.query(
                        Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"},
                        null, null, null);
                contactList.clear();//避免出现数据重复
                //3、循环游标，直到没有数据为止
                while (cursor.moveToNext()){
                    String id = cursor.getString(0);
                    Log.i("tag","id = "+ id);

                    //4、根据用户唯一性id值，查询data表和mimetype表生成的视图没获取data以及mimetype字段
                    Cursor indexCursor = contentResolver.query(
                            Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1", "mimetype"},
                            "raw_contact_id = ?", new String[]{id}, null);

                    //5、循环获取每一个联系人的电话号码以及姓名和数据类型
                    HashMap<String, String> hashMap  =new HashMap<String, String>();

                    while (indexCursor.moveToNext()){
                        String data = indexCursor.getString(0);
                        String mimetype = indexCursor.getString(1);
                        Log.i("tag", "data = " + data);
                        Log.i("tag", "mimetype = " + mimetype);

                        //6、区分类型给hashMap填充数据
                        if (mimetype.equals("vnd.android.cursor.item/phone_v2")){
                            //数据非空判断
                            if (TextUtils.isEmpty(data)){
                                hashMap.put("phone",data);
                            }
                        }else if (mimetype.equals("vnd.android.cursor.item/name")){
                            if (TextUtils.isEmpty(data)){
                                hashMap.put("name", data);
                            }
                        }
                    }
                    indexCursor.close();
                    contactList.add(hashMap);
                }
                cursor.close();
                //7、消息机制,发送一个空消息告知主线程可以去使用子线程已经填充好的数据集合
                mHandler.sendEmptyMessage(0);
            }
        }.start();

    }

    private void initUI(){
        lv_contact = (ListView)findViewById(R.id.lv_contact);

        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1、获取点中条目的索引指向集合中的对象
                if (mAdapter != null){
                    HashMap<String, String> hashMap = mAdapter.getItem(position);
                    //2、获取当前条目指定集合对应的电话号码
                    String phone = hashMap.get("phone");
                    //3、此电话号码需要给第三个导航界面使用
                    Intent intent = new Intent();
                    intent.putExtra("phone",phone);
                    setResult(0,intent);
                    finish();
                }
            }
        });


    }


}
