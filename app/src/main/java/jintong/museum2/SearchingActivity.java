package jintong.museum2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import MyView.FlowLayout;
import MyView.NoScrollViewPager;
import fragment.SearchingFragmentF1;
import fragment.SearchingFragmentF2;
import interfaces.OnclickFinish;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.button;
import static android.R.attr.key;

/**
 * 搜索页面
 * Created by wjc on 2017/3/20.
 */

public class SearchingActivity extends BaseActivity implements View.OnClickListener {

    private Set<String> wordsSet;
    private ImageView back;
    private ImageView search;
    private ImageView clearEditText;


    private EditText editText;
    private List<String> usedWords;

    private NoScrollViewPager viewPager;

    private FragmentPagerAdapter mAdapter;
    private SearchingFragmentF1 fragment1;
    private SearchingFragmentF2 fragment2;
    private List<Fragment> fragments=new ArrayList<Fragment>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searching);

        //配合状态浸入，这句一定在setContentView之后
        //透明状态栏，API小于19时。。。。。
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);



        initView();
        initData();
        setData();
        initEvents();
    }


    private void initView() {

        back = (ImageView) findViewById(R.id.searching_back);

        search = (ImageView) findViewById(R.id.searching_button);
        clearEditText = (ImageView) findViewById(R.id.searching_cancel);

        editText = (EditText) findViewById(R.id.searching_text);

        viewPager= (NoScrollViewPager) findViewById(R.id.searching_viewpager);

    }

    private void initData() {
        if(fragment1==null){
            fragment1=new SearchingFragmentF1();
            fragments.add(fragment1);
        }
        if(fragment2==null){
            fragment2=new SearchingFragmentF2();
            fragments.add(fragment2);
        }

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        viewPager.setAdapter(mAdapter);

    }

    private void setData() {


    }

    private void initEvents() {


        fragment1.setOnclickFinish(new OnclickFinish() {
            @Override
            public void onClickFinish() {
                viewPager.setCurrentItem(1);
            }
        });
        back.setOnClickListener(this);
        search.setOnClickListener(this);
        clearEditText.setOnClickListener(this);

        editText.setOnClickListener(this);
        back.setOnClickListener(this);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count > 0) {
                    clearEditText.setVisibility(View.VISIBLE);

                } else {
                    clearEditText.setVisibility(View.GONE);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searching_back:
                finish();
                overridePendingTransition(R.anim.none, R.anim.out_to_right);
                break;

            case R.id.searching_button:


                String keyWord = editText.getText().toString();
                if (!keyWord.equals("")) {
                    searching(keyWord);

                } else {
                    Toast.makeText(SearchingActivity.this, "请输入关键词", Toast.LENGTH_SHORT).show();



                }


                break;
            case R.id.searching_cancel:
                editText.setText("");
                break;


            case R.id.searching_clear:

                break;

            case R.id.searching_text:

                break;


            default:
                break;

        }


    }

    /**
     * 在SP文件中存档并执行搜索
     *
     * @param keyword
     */
    private void searching(String keyword) {
        Toast.makeText(SearchingActivity.this, "searching", Toast.LENGTH_SHORT).show();
        saveWordsToSP(keyword);

        viewPager.setCurrentItem(1);



    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.none, R.anim.out_to_right);
    }



    private void saveWordsToSP(String keyWord) {


        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        wordsSet = preferences.getStringSet("usedWords", new HashSet<String>());
        wordsSet.add(keyWord);
        editor.putStringSet("usedWords", wordsSet);

        editor.commit();

    }






}
