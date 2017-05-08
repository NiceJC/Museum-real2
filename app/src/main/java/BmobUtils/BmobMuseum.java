package BmobUtils;

import android.content.Context;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import entity.Exhibition;
import entity.Museum;
import interfaces.OnBmobReturnWithObj;
import util.ToastUtils;

import static BmobUtils.BmobUtilBase.LIMIT;

/**
 *
 * Created by wjc on 2017/5/3.
 */

public class BmobMuseum {


    private Context context;
    private String TAG;
    private static BmobMuseum instance = null;

    private OnBmobReturnWithObj onBmobReturnWithObj;

    public void setOnBmobReturnWithObj(OnBmobReturnWithObj onBmobReturnWithObj) {
        this.onBmobReturnWithObj = onBmobReturnWithObj;
    }


    private BmobMuseum(Context context) {
        this.context = context;
        TAG = "bmob blog";

    }

    //单例
    public static BmobMuseum getInstance(Context context) {

        if (instance == null) {
            instance = new BmobMuseum(context);

        }

        return instance;

    }

    /**
     *
     *     刷新列表
     *     一次查询最多返回5条
     *     暂时按照创建时间排序
     */
    public void refreshExhibition () {

        BmobQuery<Museum> query = new BmobQuery<Museum>();
        query.setLimit(5);
        query.order("-createdAt");
        query.findObjects(new FindListener<Museum>() {
            @Override
            public void done(List<Museum> list, BmobException e) {
                if (e == null) {

                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    ToastUtils.toast(context, e.getMessage());
                }

            }
        });
    }

    /**
     * 获取更多
     *
     *
     * 传入当前的页数，以便在返回结果跳过
     *
     */
    public void getMoreExhibition (int curPage){

        BmobQuery<Museum> query = new BmobQuery<Museum>();
        query.setLimit(LIMIT);
        query.setSkip(LIMIT*curPage);
        query.order("-createdAt");
        query.findObjects(new FindListener<Museum>() {
            @Override
            public void done(List<Museum> list, BmobException e) {
                if (e == null) {

                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    ToastUtils.toast(context, e.getMessage());
                }

            }
        });

    }


    /**
     *关键词查询，暂时只针对名称的关键字
     *
     */
    public void getBykeyWord(String keyWord){
        BmobQuery<Museum> query = new BmobQuery<Museum>();
        query.setLimit(LIMIT);

        query.order("-createdAt");//先按点赞数降序，再按时间降序

        query.addWhereContains("museumName",keyWord);
        query.findObjects(new FindListener<Museum>() {
            @Override
            public void done(List<Museum> list, BmobException e) {
                if (e == null) {

                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    ToastUtils.toast(context, e.getMessage());
                }

            }
        });
    }










}
