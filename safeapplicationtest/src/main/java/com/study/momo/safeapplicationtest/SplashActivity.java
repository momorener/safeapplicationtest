package com.study.momo.safeapplicationtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.study.momo.safeapplicationtest.utils.ConstantValue;
import com.study.momo.safeapplicationtest.utils.SpUtils;
import com.study.momo.safeapplicationtest.utils.ToastUtil;
import com.study.momo.safeapplicationtest.utils.StreamUtil;

import com.lidroid.xutils.exception.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {
    protected static final String tag = "SplashActivity";
    protected static final int UPDATE_VERSION = 100;
    protected static final int ENTER_HOME = 101;
    protected static final int URL_ERROR = 102;
    protected static final int IO_ERROR = 103;
    protected static final int JSON_ERROR = 104;

    private TextView tv_vision_name;
    private int mLocalVersionCode;
    private String mVersionDes;
    private String mDownloadUrl;
    private RelativeLayout rl_root;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_VERSION:
                    //弹出对话框，提示用户更新
                    showUpdateDialog();
                    break;
                case URL_ERROR:
                    ToastUtil.show(SplashActivity.this, "URL异常");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtil.show(getApplicationContext(), "读取异常");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtil.show(getApplicationContext(), "json解析异常");
                    enterHome();
                    break;
                case ENTER_HOME:
                    //进入程序主界面 activuty跳转过程
                    enterHome();
                    break;
                default:
                    break;
            }

        }
    } ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //取出当前activity头
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        //初始化ui
        initUI();
        //初始化数据
        initDate();
        //初始化动画
        initAnimation();
        //初始化数据库
        initDB();

    }
    private void initDB(){
        //1、归属地数据拷贝过程
        initAddressDB("address.db");
    }

    /**
     * 拷贝数据库值files文件夹下
     * @param dbName 数据库名称
     */
    private void initAddressDB(String dbName){
        //1、在files文件夹下创建同名dbName数据库文件过程
        File files = getFilesDir();
        File file = new File(files, dbName);
        if (file.exists()){
            return;
        }
        InputStream stream = null;
        FileOutputStream fos = null;
        //2、读取第三方资产目录assets下的文件
        try{
            stream = getAssets().open(dbName);
            //3、将读取的内容写入到指定文件夹文件中去
            fos = new FileOutputStream(file);
            //4、每次的读取内容大小
            byte[] bs = new byte[1024];
            int temp = -1;
            while( (temp = stream.read(bs)) != -1){
                fos.write(bs, 0 ,temp);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (stream != null && fos != null){
                try{
                    stream.close();
                    fos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

    }



    /**
     * 添加淡入效果
     */
    private void initAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(3000);
        rl_root.startAnimation(alphaAnimation);

    }

    /**
     * 弹出对话框，提示用户更新
     */
    private void showUpdateDialog(){
        //对话框是用来与activity存在的
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.update);
        builder.setTitle("版本更新");
        builder.setMessage(mVersionDes);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载apk,apk链接地址
                downloadApk();
            }
        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消对话框，进入主界面
                enterHome();
            }
        });

        //点击取消时间监听
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });

        //点击取消事件监听
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //即使用户点击取消,也需要让其进入应用程序主界面
                enterHome();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void downloadApk(){
        //apk下载链接地址，放置apk的所在路径
        //判断sd卡是否有用，是否挂上
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //获取sd路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator +"safetest.apk";

            Log.i(tag, path);

            //发送请求，获取apk，并放置在指定位置
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功(下载过后放置在sd卡中的apk)
                    Log.i(tag,"下载成功");
                    File file = responseInfo.result;
                    //提示用户更新
                    installApk(file);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    //下载失败
                    Log.i(tag,"下载失败");

                }

                @Override
                public void onStart() {
                    Log.i(tag,"开始下载");
                    super.onStart();
                }

                //下载过程中的方法


                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    Log.i(tag,"下载中。。。");
                    super.onLoading(total, current, isUploading);
                }
            });

        }

    }

    /**
     * 安装对应apk
     * @param file
     */
    protected void installApk(File file){
        //系统应用界面  通过源码 下载入口
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        /*//文件作为数据源
        intent.setData(Uri.fromFile(file));
        //设置安装类型
        intent.setType("application/vnd.android.package-archive");*/
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        startActivity(intent);
    }

    /**
     * 进入应用程序主界面
     */
    private void enterHome(){
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        //开启新的界面后将导航界面关闭（导航界面只可见一次）
        finish();
    }

    /**
    获取数据方法
    */
    private void initDate(){
        //1、应用版本名称
        tv_vision_name.setText("版本名称:" + getVersionName());
        //2、检测版本（本地版本号和服务器版本号对比）是否有更新，如果有更新，提示用户下载
        mLocalVersionCode = getVersionCode();//本地版本号
        if (SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false)){
            checkVersion();
       }else {
            //  enterHome();
            //消息机制
            //在发送消息4秒后去处理ENTER_HOME状态码指向的消息
            mHandler.sendEmptyMessageDelayed(ENTER_HOME, 4000);

        }

    }
    /**
     * 检测版本号
     */
    private void checkVersion(){
        new Thread(){
            @Override
            public void run() {

                //获取消息对象
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();

                //发送请求获取数据,参数为请求json的链接地址
                //模拟器访问电脑tomcat 10.0.2.2
                try {
                    //封装url地址
                    URL url = new URL("http://172.20.71.116:8080/update.json");
                    //开启链接
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    //设置常见参数(请求头)
                    connection.setConnectTimeout(2000);//请求超时
                    connection.setReadTimeout(2000);//读取超时
                    //connection.setRequestMethod("GET");//默认为get方式

                    if (connection.getResponseCode() == 200){
                        //以流的形式将数据获取下来
                        InputStream is = connection.getInputStream();
                        //将流转换成字符串（工具类封装）
                        String json = StreamUtil.streamToString(is);
                        Log.i(tag,json);
                        //json解析
                        JSONObject jsonObject = new JSONObject(json);
                        String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        Log.i(tag, versionName);
                        Log.i(tag, mVersionDes);
                        Log.i(tag, versionCode);
                        Log.i(tag, mDownloadUrl);
                        Log.i(tag, mLocalVersionCode + "  老版本");

                        //比对版本号（服务器版本号>本地版本号，提示用户更新）
                        if (mLocalVersionCode < Integer.parseInt(versionCode)){
                            //提示用户更新，弹出对话框（UI）消息机制
                            msg.what = UPDATE_VERSION;

                        }else {
                            msg.what = ENTER_HOME;
                        }

                    }
                    super.run();
                }catch(MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                }catch (IOException e) {
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
                }finally {
                    //指定睡眠时间，请求网络的时长超过4秒则不做处理
                    //指定睡眠时间，请求网络的时长小于4秒强制让其睡眠满4秒
                    long endTime = System.currentTimeMillis();

                    if (endTime - startTime < 4000) {
                        try {
                            Thread.sleep(4000 - (endTime - startTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    mHandler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 返回版本号
     * @return
     *      非0代表获取失败
     */
    private int getVersionCode(){
        try {
            PackageManager pm = getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;

        }catch (Exception e){
            e.printStackTrace();

        }
        return 0;
    }

    /**
     *
    获取版本名称:清单文件
    @return 应用版本名称  返回null代表异常
     */
    private String getVersionName(){
        try {
            PackageManager pm = getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;

        }catch (Exception e){
            e.printStackTrace();

        }
        return null;

    }
    /*
    初始化UI方法
     */
    private void initUI(){
        tv_vision_name = (TextView) findViewById(R.id.tv_version_name);

        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
    }


}
