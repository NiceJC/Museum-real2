package fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import MyView.FlowLayout;
import interfaces.OnclickFinish;
import jintong.museum2.R;
import jintong.museum2.SearchingActivity;

/**
 * Created by wjc on 2017/3/22.
 */

public class SearchingFragmentF1 extends Fragment {
    private List<String> hotWords;

    private List<String> usedWords;

    private Set<String> wordsSet;
    private TextView clearUsedWords;
    private LayoutInflater inflater2;
    private FlowLayout usedKeyWords; //保存历史搜索关键字
    private FlowLayout hotKeyWords; //保存热门搜索关键字

    private OnclickFinish onclickFinish;
    View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment_1, container, false);
        inflater2 = LayoutInflater.from(getActivity());

        initView();
        initData();
        setData();
        initEvents();


        return view;
    }

    private void initView() {
        clearUsedWords = (TextView) view.findViewById(R.id.searching_clear);
        hotKeyWords = (FlowLayout) view.findViewById(R.id.flow_useful_keywords);
        usedKeyWords = (FlowLayout) view.findViewById(R.id.flow_used_keywords);

    }

    private void initData() {
        hotWords = new ArrayList<String>();
        hotWords.add("青花瓷");
        hotWords.add("青铜器");
        hotWords.add("油画");
        hotWords.add("诗词");
        hotWords.add("圣旨");
        hotWords.add("丝绸");

        usedWords = getWordsFromSP();
    }

    private void setData() {


        for (int i = 0; i < hotWords.size(); i++) {
            TextView tv = (TextView) inflater2.inflate(R.layout.key_words_text, hotKeyWords, false);
            tv.setText(hotWords.get(i));
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searching(hotWords.get(finalI));
                }
            });

            hotKeyWords.addView(tv);
        }

        for (int i = 0; i < usedWords.size(); i++) {

            TextView tv = (TextView) inflater2.inflate(R.layout.key_words_text, usedKeyWords, false);
            tv.setText(usedWords.get(i));
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searching(usedWords.get(finalI));
                }
            });

            usedKeyWords.addView(tv);
        }
    }

    private void initEvents() {
        clearUsedWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usedWords.size() > 0) {
                    dialog();
                } else {
                    Toast.makeText(getActivity(), "无历史记录", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    /**
     * 清空搜索历史的确定提示框
     * <p>
     * 清空历史关键字并且清空SP
     */
    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确定清空搜索历史吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                usedKeyWords.removeAllViews();
                usedWords.clear();
                clearKeyWordsSP();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void clearKeyWordsSP() {

        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        wordsSet = new HashSet<String>();


        editor.putStringSet("usedWords", wordsSet);

        editor.commit();
    }

    private List<String> getWordsFromSP() {

        List<String> usedWords = new ArrayList<String>();
        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        wordsSet = preferences.getStringSet("usedWords", new HashSet<String>());

        Iterator<String> it = wordsSet.iterator();
        while (it.hasNext()) {
            String keyWord = it.next();
            usedWords.add(keyWord);

        }


        return usedWords;


    }

    private void saveWordsToSP(String keyWord) {


        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        wordsSet = preferences.getStringSet("usedWords", new HashSet<String>());
        wordsSet.add(keyWord);
        editor.putStringSet("usedWords", wordsSet);

        usedWords.add(keyWord);
//        usedKeyWords.notify();


        editor.commit();

    }

    /**
     * 在SP文件中存档并执行搜索
     *
     * @param keyword
     */
    private void searching(String keyword) {
        Toast.makeText(getActivity(), "searching", Toast.LENGTH_SHORT).show();
        saveWordsToSP(keyword);

        onclickFinish.onClickFinish();



    }

    public void setOnclickFinish(OnclickFinish onclickFinish){
        this.onclickFinish=onclickFinish;

    }
}
