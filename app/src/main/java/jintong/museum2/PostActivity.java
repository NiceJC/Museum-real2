package jintong.museum2;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import BmobUtils.BmobFileUtil;
import BmobUtils.BmobSocialUtil;
import adapter.PostImageAdapter;
import interfaces.OnBmobReturnSuccess;
import interfaces.OnBmobReturnWithObj;
import interfaces.OnItemClickListener;
import util.ToastUtils;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //配合状态浸入，这句一定在setContentView之后
        //透明状态栏，API小于19时。。。。。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

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
            @Override
            public void onItemClick(View view, int position) {
                choosePortraitFromNative();

            }

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
            Uri seletedImage = intent.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(seletedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            final String picturePath = cursor.getString(columnIndex);
            datas.add(picturePath);

            adapter.notifyDataSetChanged();

            hasPic = true;
            sendPost.setSelected(true);
            sendPost.setClickable(true);

            cursor.close();

        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    //从本地相册选取头像照片
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
                /**
                 * 提交文字与图片
                 * 先上传图片，上传成功后将返回的url连同文字一起上传
                 */

                final String text = editText.getText().toString();

                String[] paths = new String[datas.size()];
                for (int i = 0; i < datas.size(); i++) {
                    paths[i] = datas.get(i);

                }

                BmobFileUtil.getInstance(this).setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
                    @Override
                    public void onSuccess(Object Obj) {
                        //上传图片成功，将返回的url和文字再次上传
                        List<String> urls = (List<String>) Obj;
                        BmobSocialUtil.getInstance(PostActivity.this).postBlog(text,datas);




                    }

                    @Override
                    public void onFail(Object Obj) {

                    }
                });
                BmobFileUtil.getInstance(this).uploadBatch(paths);


                Log.e("TAG", text);
                for (int i = 0; i < datas.size(); i++) {
                    Log.e("TAG", datas.get(i));
                }


                break;
            default:
                break;


        }


    }


}
