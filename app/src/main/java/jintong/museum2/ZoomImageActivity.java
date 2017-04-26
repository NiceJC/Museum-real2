package jintong.museum2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import MyView.ZoomImageView;
import interfaces.OnclickFinish;

/**
 * 点击图片之后 切换到本Activity进行图片的缩放处理
 * Created by wjc on 2017/3/9.
 */

public class ZoomImageActivity extends BaseActivity implements View.OnClickListener {


    private ViewPager viewPager;

    private List<String> mImageURLS;

    private ImageView[] mImageViews;


    private TextView mCurrent;

    private TextView mCount;

    private int firstPosition;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏

        setContentView(R.layout.activity_zoom_image);


        initData();


        mImageViews = new ImageView[mImageURLS.size()];
        viewPager = (ViewPager) findViewById(R.id.zoom_viewpager);

        mCount = (TextView) findViewById(R.id.zoom_count);
        mCount.setText(mImageURLS.size() + "");
        mCurrent = (TextView) findViewById(R.id.zoom_current);

        viewPager.setOnClickListener(this);


        viewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                ZoomImageView imageView = new ZoomImageView(getApplicationContext());
                imageView.setmOnClickFinish(new OnclickFinish() {
                    @Override
                    public void onClickFinish() {
                        finish();
                        overridePendingTransition(R.anim.none, R.anim.out_zoom);
                    }
                });

                Glide.with(ZoomImageActivity.this).load(mImageURLS.get(position)).into(imageView);

                container.addView(imageView);


                mImageViews[position] = imageView;


                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mImageViews[position]);
            }

            @Override
            public int getCount() {
                return mImageURLS.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }


        });


        viewPager.setCurrentItem(firstPosition);
        mCurrent.setText(firstPosition + 1 + "");

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Glide.with(ZoomImageActivity.this).load(mImageURLS.get(position)).into(mImageViews[position]);
                mCurrent.setText(position + 1 + "");


            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });


    }

    private void initData() {

        mImageURLS = getIntent().getStringArrayListExtra("imageURLs");
        firstPosition = getIntent().getIntExtra("position", 0);

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.none, R.anim.out_zoom);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.zoom_viewpager:
                Toast.makeText(this, "ha", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.none, R.anim.out_zoom);


                break;
            default:
                break;

        }


    }
}
