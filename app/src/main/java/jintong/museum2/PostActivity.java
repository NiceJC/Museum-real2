package jintong.museum2;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.ArrayList;
import java.util.List;

import bmobUtils.BmobFileUtil;
import bmobUtils.BmobSocialUtil;
import adapter.PostImageAdapter;
import cn.bmob.v3.BmobUser;
import interfaces.OnBmobReturnWithObj;
import interfaces.OnItemClickListener;
import model.User;
import util.ImageAbsolutePathUtil;
import util.ToastUtils;

import static jintong.museum2.MainActivity.REQUEST_CODE;

/**
 * Created by wjc on 2017/4/25.
 * <p>
 * 总体思路是
 * 1、监听文字内容与图片内容是否都为空，如果都为空，设置发送按钮为不可点击状态
 * 2、点击图片区的 + 调用系统的图片服务，从相册中选择图片，选中图片后，返回图片的Path
 * 并使用Glide将图片显示在图片框中，
 * 3、点击发送，将文字与图片一并发送到服务端
 */

public class PostActivity extends BaseActivity implements View.OnClickListener {


    private ImageView back;
    private TextView sendPost;
    private EditText editText;
    private GridView gridView;
    private List<String> datas;  //用于存储
    private PostImageAdapter adapter;

    private String textContent; //提交的文字内容

    private boolean hasText = false; //是否有文字
    private boolean hasPic = false;  //是否有图片

    private LoadToast loadToast;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        initView();
        initData();
        initEvents();

    }

    private void initView() {

        back = (ImageView) findViewById(R.id.post_back);
        sendPost = (TextView) findViewById(R.id.post_send);
        editText = (EditText) findViewById(R.id.post_text);
        gridView = (GridView) findViewById(R.id.post_gridView);


    }

    private void initData() {
        datas = new ArrayList<String>();


        adapter = new PostImageAdapter(PostActivity.this, datas);
        adapter.setOnItemClickListener(new OnItemClickListener() {

            //加号点击，添加图片，暂时职能从本地选取，先确认动态权限的获取
            @Override
            public void onItemClick(View view, int position) {

                checkLocatePermission();

            }

            //接口名没改，其实是选取图片右上角的删除点击
            @Override
            public void OnItemLongClick(View view, int position) {

                datas.remove(position);
                adapter.notifyDataSetChanged();
                if (position == 0) {
                    hasPic = false;
                    if (!hasText) {
                        sendPost.setSelected(false);
                        sendPost.setClickable(false);
                    }

                }

            }
        });

        gridView.setAdapter(adapter);

    }

    private void initEvents() {

        back.setOnClickListener(this);

        sendPost.setOnClickListener(this);

        //根据文字框中内容的有无，设置发送按钮是否可点击
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    hasText = true;
                    sendPost.setSelected(true);
                    sendPost.setClickable(true);

                } else {
                    hasText = false;
                    if (!hasPic) {
                        sendPost.setSelected(false);
                        sendPost.setClickable(false);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {//取消
            ToastUtils.toast(getApplication(), "取消");
            return;
        }

        if (requestCode == 1 && intent != null) {
            Uri selectedImage = intent.getData();

            //获取选中图片的绝对路径
            String picturePath= ImageAbsolutePathUtil.getImageAbsolutePath(this,selectedImage);



            datas.add(picturePath);
            adapter.notifyDataSetChanged();
            hasPic = true;
            sendPost.setSelected(true);
            sendPost.setClickable(true); 

        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    //从本地相册选取照片
    private void choosePortraitFromNative() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //如果你想在Activity中得到新打开Activity关闭后返回的数据，
        //你需要使用系统提供的startActivityForResult(Intent intent,int requestCode)方法打开新的Activity
        startActivityForResult(intent, 1);
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.post_back:
                finish();
                overridePendingTransition(R.anim.none, R.anim.out_to_right);
                break;
            case R.id.post_send:
                loadToast=ToastUtils.getLoadingToast(this);
                loadToast.show();
                /**
                 * 提交文字与图片
                 * 先上传图片，上传成功后将返回的url连同文字一起上传
                 */



                final String text = editText.getText().toString();

                final BmobSocialUtil bmobSocialUtil=BmobSocialUtil.getInstance(PostActivity.this);

                //先判断图片数量，如果为0，只需上传文字内容
                if(datas.size()==0){

                    bmobSocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                        @Override
                        public void onSuccess(Object Obj) {
                            ToastUtils.toast(PostActivity.this,"发表成功");
                            finish();
                            overridePendingTransition(R.anim.none,R.anim.out_to_right);

                            loadToast.success();
                        }

                        @Override
                        public void onFail(Object Obj) {

                            loadToast.error();
                        }
                    });
                    bmobSocialUtil.postBlog(BmobUser.getCurrentUser(User.class),text,null);

                }else {


                    String[] paths = new String[datas.size()];
                    for (int i = 0; i < datas.size(); i++) {
                        paths[i] = datas.get(i);

                    }

                    BmobFileUtil.getInstance(this).setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                        @Override
                        public void onSuccess(Object Obj) {
                            bmobSocialUtil.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                                @Override
                                public void onSuccess(Object Obj) {
                                    ToastUtils.toast(PostActivity.this,"发表成功");

                                    finish();
                                    overridePendingTransition(R.anim.none,R.anim.out_to_right);

                                    loadToast.success();
                                }

                                @Override
                                public void onFail(Object Obj) {

                                    loadToast.error();
                                }
                            });


                            //上传图片成功，将返回的url和文字再次上传
                            List<String> urls = (List<String>) Obj;

                            bmobSocialUtil.postBlog(BmobUser.getCurrentUser(User.class),text, urls);


                        }

                        @Override
                        public void onFail(Object Obj) {

                        }
                    });
                    BmobFileUtil.getInstance(this).uploadBatch(paths);

                }
                break;
            default:
                break;


        }


    }


    private void checkLocatePermission() {

        boolean isGranted= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        if(isGranted){

            choosePortraitFromNative();
        }else{

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
        }




    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length >0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //用户同意授权
                    choosePortraitFromNative();
                }else{
                    //用户拒绝授权
                    ToastUtils.toast(this,"没有存储读写权限将导致读取照片失败，用户可以前往应用权限进行设置");
                }
                break;
        }
    }




}
