package jintong.museum2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import BmobUtils.BmobFileUtil;
import BmobUtils.BmobRegisterAndLogin;
import BmobUtils.BmobUserInfo;
import cn.bmob.v3.BmobUser;
import entity.User;
import interfaces.OnBmobReturnSuccess;
import interfaces.OnItemSelectedListener;
import util.DialogUtil;
import util.SysUtils;
import util.ToastUtils;

import static BmobUtils.BmobRegisterAndLogin.chekIfLogin;

/**
 * Created by wjc on 2017/2/27.
 */

public class SetUpActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBack;

    private ImageView mIcon;
    private LinearLayout mClickIcon;

    private TextView mNickName;
    private LinearLayout mClickNickName;

    private TextView mGender;
    private LinearLayout mClickGender;

    private TextView mAge;
    private LinearLayout mClickAge;

    private TextView mVersion;
    private LinearLayout mClickVersion;

    private TextView mPhone;
    private LinearLayout mClickPhone;

    private LinearLayout mClickAboutUs;

    private LinearLayout mClickFeedBack;

    private LinearLayout mClickLogOut;

    private User currentUser;

    private OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void getSelectedItem(String content) {

            if (content.equals(FROM_NATIVE)) {
                choosePortraitFromNative();
            } else if (content.equals(FROM_CAMERA)) {
                choosePortraitFromCamera();


            }

        }
    };

    private static final String IMAGE_FILE_NAME = "portrait";

    private static final int CODE_NATIVE_PIC = 1; //本地

    private static final int CODE_CAMERA_PIC = 2; //拍照

    private static final int CODE_RESULT_PIC = 3; //最终裁剪后的结果

    private static int output_X = 600; //裁剪后的图片尺寸
    private static int output_Y = 600;


    private static final String FROM_NATIVE = "从相册选取头像";
    private static final String FROM_CAMERA = "拍照选取头像";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "oncreate2");

        setContentView(R.layout.activity_setup);

        //配合状态浸入，这句一定在setContentView之后
        //透明状态栏，API小于19时。。。。。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        initView();
        initData();
        setData();
        initEvents();


    }


    private void getImagePath() {

        FutureTarget<File> future = Glide.with(this)
                .load("http://bmob-cdn-4183.b0.upaiyun.com/2017/02/20/2dcd5037401d841b8026fb38b4847ac4.jpg")
                .downloadOnly(200, 200);

        try {
            File cacheFile = future.get();
            String path = cacheFile.getAbsolutePath();
            Log.e("TAG", path);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

    private void initView() {

        mBack = (ImageView) findViewById(R.id.setup_back);
        mIcon = (ImageView) findViewById(R.id.setup_icon);
        mNickName = (TextView) findViewById(R.id.setup_userName);
        mGender = (TextView) findViewById(R.id.setup_gender);
        mAge = (TextView) findViewById(R.id.setup_age);
        mVersion = (TextView) findViewById(R.id.setup_version);
        mPhone = (TextView) findViewById(R.id.setup_phone);

        mClickIcon = (LinearLayout) findViewById(R.id.setup_change_icon);
        mClickNickName = (LinearLayout) findViewById(R.id.setup_change_userName);
        mClickGender = (LinearLayout) findViewById(R.id.setup_change_gender);
        mClickAge = (LinearLayout) findViewById(R.id.setup_change_age);
        mClickVersion = (LinearLayout) findViewById(R.id.setup_check_version);
        mClickPhone = (LinearLayout) findViewById(R.id.setup_change_phone);
        mClickAboutUs = (LinearLayout) findViewById(R.id.setup_change_aboutUs);
        mClickFeedBack = (LinearLayout) findViewById(R.id.setup_change_feedBack);
        mClickLogOut = (LinearLayout) findViewById(R.id.setup_change_logOut);


    }


    private void initData() {

        currentUser = BmobUser.getCurrentUser(User.class);
        Log.d("currntUser", currentUser.toString());


    }

    private void setData() {

        updateUserMessage();

    }

    private void initEvents() {
        mBack.setOnClickListener(this);
        mClickIcon.setOnClickListener(this);
        mClickNickName.setOnClickListener(this);
        mClickGender.setOnClickListener(this);
        mClickAge.setOnClickListener(this);
        mClickVersion.setOnClickListener(this);
        mClickPhone.setOnClickListener(this);
        mClickAboutUs.setOnClickListener(this);
        mClickFeedBack.setOnClickListener(this);
        mClickLogOut.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setup_back:
                finish();
                overridePendingTransition(R.anim.none, R.anim.out_to_right);
                break;
            case R.id.setup_change_icon:
                int screenWidth = SysUtils.getScreenWidth(SetUpActivity.this);

                DialogUtil.showItemSelectDialog(SetUpActivity.this, screenWidth / 25 * 24, onItemSelectedListener, FROM_NATIVE, FROM_CAMERA);


                break;
            case R.id.setup_change_logOut:

                clickLogout();

                break;
            case R.id.setup_change_gender:

                Boolean isMan=BmobUser.getCurrentUser(User.class).getMan();
                if(isMan==null){
                    isMan=true;
                }
                changeGender(isMan);
                break;
            case R.id.setup_change_userName:
                String nickName=BmobUser.getCurrentUser(User.class).getNickName();
                if(nickName==null){
                    nickName="";
                }
                changeNickName(nickName);

                break;

            case R.id.setup_change_age:

                String age=BmobUser.getCurrentUser(User.class).getAge();
                if(age==null){
                    age="00后";
                }
                changeAge(age);
                break;
            default:
                break;


        }

    }

    //从本地相册选取头像照片
    private void choosePortraitFromNative() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //如果你想在Activity中得到新打开Activity关闭后返回的数据，
        //你需要使用系统提供的startActivityForResult(Intent intent,int requestCode)方法打开新的Activity
        startActivityForResult(intent, CODE_NATIVE_PIC);
    }


    private void choosePortraitFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }

        startActivityForResult(intent, CODE_CAMERA_PIC);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {//取消
            ToastUtils.toast(getApplication(), "取消");
            return;
        }

        switch (requestCode) {
            case CODE_NATIVE_PIC://如果是来自本地的
                cropRawPhoto(intent.getData());//直接裁剪图片
                break;

            case CODE_CAMERA_PIC:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {

                    ToastUtils.toast(getApplication(), "没有SDCard");

                }

                break;

            case CODE_RESULT_PIC:
                if (intent != null) {
                    setImageAndUpload(intent);//设置图片
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }


    /**
     * 裁剪原始图片
     *
     * @param uri
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");


        //设置裁剪
        intent.putExtra("crop", "true");

        //设置宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        //设置裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);

        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_PIC);


    }

    //设置图片并上传
    public void setImageAndUpload(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            mIcon.setImageBitmap(photo);
            //新建文件夹 先选好路径 再调用mkdir函数 现在是根目录下面的Ask文件夹
            File nf = new File(Environment.getExternalStorageDirectory() + "/Ask");
            nf.mkdir();
            //在根目录下面的ASk文件夹下 创建okkk.jpg文件
            File f = new File(Environment.getExternalStorageDirectory() + "/Ask", "okkk.jpg");


            FileOutputStream out = null;
            try {//打开输出流 将图片数据填入文件中
                out = new FileOutputStream(f);
                photo.compress(Bitmap.CompressFormat.PNG, 90, out);

                try {
                    out.flush();
                    out.close();


                    BmobFileUtil.getInstance(this).uploadFile(Environment.getExternalStorageDirectory() + "/Ask" + "/okkk.jpg");


                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }


    }


    /**
     * 退出登录
     */
    public void clickLogout() {

        new AlertDialog.Builder(this).setTitle("退出登录").setMessage("确定退出当前账号吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //退出登录，清除本地缓存用户
                        BmobUser.logOut();

                        ToastUtils.toast(SetUpActivity.this, "当前用户已退出登录");
                        Intent intent = new Intent(SetUpActivity.this, LoginActivity.class);

                        BmobUser.logOut();

                        finish();
                        startActivity(intent);

                        overridePendingTransition(R.anim.in_from_right, R.anim.none);

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }


    /**
     * 修改昵称
     */

    public void changeNickName(String nickName) {

        final EditText editText = new EditText(this);
        editText.setText(nickName);
//        editText.setBackgroundResource(R.drawable.register_edittext_bg);
//        editText.setGravity(Gravity.CENTER);
//        editText.setPadding(10,10,10,10);
        new AlertDialog.Builder(this)
                .setTitle("新昵称")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newNickName = editText.getText().toString();

                        BmobUserInfo.getInstance(SetUpActivity.this).setOnBmobReturnSuccess(new OnBmobReturnSuccess() {
                            @Override
                            public void onSuccess() {

                                updateUserMessage();
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                        BmobUserInfo.getInstance(SetUpActivity.this).setNickName(newNickName);

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .show();

    }

    //选择性别
    public void changeGender(Boolean isMan ){

        new AlertDialog.Builder(SetUpActivity.this)
                .setTitle("请选择")
                .setSingleChoiceItems(new String[]{"男", "女"}, isMan ? 0 : 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Boolean isMan;
                        if(which==0){
                            isMan=true;
                        }else{
                            isMan=false;
                        }

                        BmobUserInfo.getInstance(SetUpActivity.this).setOnBmobReturnSuccess(new OnBmobReturnSuccess() {
                            @Override
                            public void onSuccess() {
                                updateUserMessage();
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                        BmobUserInfo.getInstance(SetUpActivity.this).setGender(isMan);


                        dialog.dismiss();



                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    //选择年龄
    public void changeAge(String age){
        int ageInt=0;
        switch (age){
            case "00后":
                ageInt=0;
                break;
            case "90后":
                ageInt=1;
                break;
            case "80后":
                ageInt=2;
                break;
            case "70后":
                ageInt=3;
                break;
            case "60后":
                ageInt=4;
                break;
            case "50后":
                ageInt=5;
                break;
            case "其他":
                ageInt=6;
                break;
            default:
                break;

        }



        new AlertDialog.Builder(SetUpActivity.this)
                .setTitle("请选择")
                .setSingleChoiceItems(new String[]{"00后", "90后","80后","70后","60后","50后","其他"},ageInt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newAge=null;
                        switch (which){
                            case 0:
                                newAge="00后";
                                break;
                            case 1:
                                newAge="90后";
                                break;
                            case 2:
                                newAge="80后";
                                break;
                            case 3:
                                newAge="70后";
                                break;
                            case 4:
                                newAge="60后";
                                break;
                            case 5:
                                newAge="50后";
                                break;
                            case 6:
                                newAge="其他";
                                break;

                            default:
                                break;

                        }



                        BmobUserInfo.getInstance(SetUpActivity.this).setOnBmobReturnSuccess(new OnBmobReturnSuccess() {
                            @Override
                            public void onSuccess() {
                                updateUserMessage();
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                        BmobUserInfo.getInstance(SetUpActivity.this).setAge(newAge);


                        dialog.dismiss();



                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();



    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }


    //根据传入的User 更新用户信息
    public void updateUserMessage() {

        User user = BmobUser.getCurrentUser(User.class);

        String porTraitUrl = user.getPortraitURL();
        String nickName = user.getNickName();
        Boolean isMan = user.getMan();
        String age=user.getAge();
        String phoneNum = user.getMobilePhoneNumber();


        if (porTraitUrl != null) {
            Glide.with(this).load(porTraitUrl).into(mIcon);
        }
        if (isMan != null) {
            mGender.setText(isMan ? "男" : "女");
        }
        if (nickName != null) {
            mNickName.setText(nickName);
        }
        if(age!=null){
            mAge.setText(age);
        }
        if (phoneNum != null) {
            mPhone.setText(phoneNum);
        }


    }


}
