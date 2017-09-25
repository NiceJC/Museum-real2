package jintong.museum2;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import bmobUtils.BmobFileUtil;
import bmobUtils.BmobUserInfo;
import cn.bmob.v3.BmobUser;
import interfaces.OnBmobReturnWithObj;
import model.User;
import interfaces.OnBmobReturnSuccess;
import interfaces.OnItemSelectedListener;
import util.DialogUtil;
import util.SysUtils;
import util.ToastUtils;

import static jintong.museum2.MainActivity.REQUEST_CODE;

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

    private Uri cropUri;
    private Uri cameraUri;
    private int screenWidth;
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

    private static final String IMAGE_AFTER_CROP_NAME="portrait_after_crop";

    private static final int CODE_NATIVE_PIC = 1; //本地

    private static final int CODE_CAMERA_PIC = 2; //拍照

    private static final int CODE_RESULT_PIC = 3; //最终裁剪后的结果

    private static int output_X = 600; //裁剪后的图片尺寸
    private static int output_Y = 600;


    private static final String FROM_NATIVE = "从相册选取头像";
    private static final String FROM_CAMERA = "拍照选取头像";


    private  File f;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "oncreate2");

        setContentView(R.layout.activity_setup);


        initView();
        initData();
        setData();
        initEvents();


    }

//
//    //获取Glide自动保存的图片的path
//    private void getImagePath() {
//
//        FutureTarget<File> future = Glide.with(this)
//                .load("http://bmob-cdn-4183.b0.upaiyun.com/2017/02/20/2dcd5037401d841b8026fb38b4847ac4.jpg")
//                .downloadOnly(200, 200);
//
//        try {
//            File cacheFile = future.get();
//            String path = cacheFile.getAbsolutePath();
//            Log.e("TAG", path);
//
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//
//
//    }

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
                /**
                 * 处理一下6.0以上的动态权限问题先
                 */

                screenWidth = SysUtils.getScreenWidth(SetUpActivity.this);

                checkLocatePermission();


                break;
            case R.id.setup_change_logOut:

                clickLogout();

                break;
            case R.id.setup_change_gender:

                Boolean isMan = BmobUser.getCurrentUser(User.class).getMan();
                if (isMan == null) {
                    isMan = true;
                }
                changeGender(isMan);
                break;
            case R.id.setup_change_userName:
                String nickName = BmobUser.getCurrentUser(User.class).getNickName();
                if (nickName == null) {
                    nickName = "";
                }
                changeNickName(nickName);

                break;

            case R.id.setup_change_age:

                String age = BmobUser.getCurrentUser(User.class).getAge();
                if (age == null) {
                    age = "00后";
                }
                changeAge(age);
                break;
            case R.id.setup_change_feedBack:
                Intent intent=new Intent(this,FeedBackActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,R.anim.none);

                break;
            case R.id.setup_check_version:
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.toast(SetUpActivity.this,"当前已是最新版本");
                    }
                },1000);


     

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

        File cameraImage = new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME);
        if(cameraImage.exists())
        {
            cameraImage.delete();
            try
            {
                cameraImage.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (Build.VERSION.SDK_INT>=24)
        {
            cameraUri = FileProvider.getUriForFile(SetUpActivity.this,"jintong.museum2.fileprovider",cameraImage);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


        }
        else
        {
            cameraUri = Uri.fromFile(cameraImage);
        }

        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
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
        //创建File对象，用于存储拍照后的图片
        File cropImage = new File(Environment.getExternalStorageDirectory(),IMAGE_AFTER_CROP_NAME);
        if(cropImage.exists())
        {
            cropImage.delete();
            try
            {
                cropImage.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        if (Build.VERSION.SDK_INT>=24)
        {
            cropUri = FileProvider.getUriForFile(SetUpActivity.this,"jintong.museum2.fileprovider",cropImage);

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        else
        {
            cropUri = Uri.fromFile(cropImage);
        }



        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, CODE_RESULT_PIC);


    }

    //设置图片并上传
    public void setImageAndUpload(Intent intent) {

        try {



            final LoadToast loadToast=ToastUtils.getLoadingToast(this);

            loadToast.setText("修改中...");
            loadToast.show();


            final Bitmap photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(cropUri));



            //新建文件夹 先选好路径 再调用mkdir函数 现在是根目录下面的photo文件夹
            File nf = new File(Environment.getExternalStorageDirectory() + "/photo");
            nf.mkdir();
            //在根目录下面的photo文件夹下 创建jpg文件
            String fileName="/"+System.currentTimeMillis()+"portrait.jpg";
             f = new File(Environment.getExternalStorageDirectory() + "/photo", fileName);
//            + "/Ask" + "/okkk.jpg"

            FileOutputStream out = null;
            //打开输出流 将图片数据填入文件中
            out = new FileOutputStream(f);
            photo.compress(Bitmap.CompressFormat.PNG, 90, out);

            out.flush();
            out.close();


            BmobFileUtil bmobFileUtil= BmobFileUtil.getInstance(this);

            bmobFileUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                @Override
                public void onSuccess(Object Obj) {
                    loadToast.success();

                    Glide.with(SetUpActivity.this).load(f).into(mIcon);
                }

                @Override
                public void onFail(Object Obj) {

                    loadToast.error();
                }
            });


            bmobFileUtil.uploadFile(Environment.getExternalStorageDirectory() + "/photo"+fileName);



        } catch (IOException e) {
            e.printStackTrace();

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



                        User user=BmobUser.getCurrentUser(User.class);
                        if(user==null){
                            Log.e("退出登录前","kong ");

                        }else{
                            Log.e("退出登陆前", user.getObjectId()+"   " +user.getNickName());
                        }
                        //退出登录，清除本地缓存用户
                        BmobUser.logOut();


                        User user2=BmobUser.getCurrentUser(User.class);
                        if(user2==null){
                            Log.e("退出登录后","kong ");

                        }else{
                            Log.e("退出登陆后", user2.getObjectId()+"   " +user2.getNickName());
                        }

                        ToastUtils.toast(SetUpActivity.this, "当前用户已退出登录");
                        Intent intent = new Intent(SetUpActivity.this, LoginActivity.class);



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
    public void changeGender(Boolean isMan) {

        new AlertDialog.Builder(SetUpActivity.this)
                .setTitle("请选择")
                .setSingleChoiceItems(new String[]{"男", "女"}, isMan ? 0 : 1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Boolean isMan;
                        if (which == 0) {
                            isMan = true;
                        } else {
                            isMan = false;
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
    public void changeAge(String age) {
        int ageInt = 0;
        switch (age) {
            case "00后":
                ageInt = 0;
                break;
            case "90后":
                ageInt = 1;
                break;
            case "80后":
                ageInt = 2;
                break;
            case "70后":
                ageInt = 3;
                break;
            case "60后":
                ageInt = 4;
                break;
            case "50后":
                ageInt = 5;
                break;
            case "其他":
                ageInt = 6;
                break;
            default:
                break;

        }


        new AlertDialog.Builder(SetUpActivity.this)
                .setTitle("请选择")
                .setSingleChoiceItems(new String[]{"00后", "90后", "80后", "70后", "60后", "50后", "其他"}, ageInt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String newAge = null;
                        switch (which) {
                            case 0:
                                newAge = "00后";
                                break;
                            case 1:
                                newAge = "90后";
                                break;
                            case 2:
                                newAge = "80后";
                                break;
                            case 3:
                                newAge = "70后";
                                break;
                            case 4:
                                newAge = "60后";
                                break;
                            case 5:
                                newAge = "50后";
                                break;
                            case 6:
                                newAge = "其他";
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
        String age = user.getAge();
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
        if (age != null) {
            mAge.setText(age);
        }
        if (phoneNum != null) {



                StringBuilder sb  =new StringBuilder();
                for (int i = 0; i < phoneNum.length(); i++) {
                    char c = phoneNum.charAt(i);
                    if (i >= 3 && i <= 6) {
                        sb.append('*');
                    } else {
                        sb.append(c);
                    }
                }
            mPhone.setText(sb.toString());


        }


    }

    private void checkLocatePermission() {

        boolean isGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
        if (isGranted) {

            DialogUtil.showItemSelectDialog(SetUpActivity.this, screenWidth / 25 * 24, onItemSelectedListener, FROM_NATIVE, FROM_CAMERA);

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, REQUEST_CODE);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户同意授权
                    DialogUtil.showItemSelectDialog(SetUpActivity.this, screenWidth / 25 * 24, onItemSelectedListener, FROM_NATIVE, FROM_CAMERA);

                } else {
                    //用户拒绝授权
                    ToastUtils.toast(this, "没有权限将可能出现异常，用户可以前往应用权限进行设置");
                }
                break;
        }
    }


}
