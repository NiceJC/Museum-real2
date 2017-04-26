package jintong.museum2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import adapter.PostImageAdapter;

/**
 * Created by wjc on 2017/4/25.
 */

public class PostActivity extends BaseActivity implements View.OnClickListener {


    private ImageView back;
    private TextView sendPost;
    private EditText editText;
    private GridView gridView;
    private List<Bitmap> datas;

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

        back= (ImageView) findViewById(R.id.post_back);
        sendPost= (TextView) findViewById(R.id.post_send);
        editText= (EditText) findViewById(R.id.post_text);
        gridView= (GridView) findViewById(R.id.post_gridView);


    }

    private void initData() {
        datas=new ArrayList<Bitmap>();
        datas.add(BitmapFactory.decodeResource(getResources(),R.drawable.first));

        datas.add(BitmapFactory.decodeResource(getResources(),R.drawable.hgportrait));

        datas.add(BitmapFactory.decodeResource(getResources(),R.drawable.first));

        datas.add(BitmapFactory.decodeResource(getResources(),R.drawable.hgportrait));

        datas.add(BitmapFactory.decodeResource(getResources(),R.drawable.plusp));


        PostImageAdapter adapter=new PostImageAdapter(PostActivity.this,datas);
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

                if (count > 0) {
                    editText.setSelected(true);
                    editText.setClickable(true);

                } else {
                    editText.setSelected(false);
                    editText.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onClick(View v) {

    }
}
