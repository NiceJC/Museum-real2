package BmobUtils;

import android.content.Context;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import entity.Collection;
import entity.Exhibition;
import interfaces.OnBmobReturnWithObj;
import util.ToastUtils;

import static BmobUtils.BmobUtilBase.HOTLIMIT;
import static BmobUtils.BmobUtilBase.LIMIT;

/**
 *
 * 藏品的服务器操作类
 *
 * Created by wjc on 2017/5/3.
 */

public class BmobColt {


    private Context context;
    private String TAG;
    private static BmobColt instance = null;

    private OnBmobReturnWithObj onBmobReturnWithObj;

    public void setOnBmobReturnWithObj(OnBmobReturnWithObj onBmobReturnWithObj) {
        this.onBmobReturnWithObj = onBmobReturnWithObj;
    }


    private BmobColt(Context context) {
        this.context = context;
        TAG = "bmob blog";

    }

    //单例
    public static BmobColt getInstance(Context context) {

        if (instance == null) {
            instance = new BmobColt(context);

        }

        return instance;

    }

    //获取热门藏品，以点赞数的多少为准
    public void getHotColt(){

        BmobQuery<Collection> query = new BmobQuery<Collection>();
        query.setLimit(HOTLIMIT);

        query.order("-coltLikeNum,-createdAt");//先按点赞数降序，再按时间降序
        query.include("coltToMuseum");
        query.findObjects(new FindListener<Collection>() {
            @Override
            public void done(List<Collection> list, BmobException e) {
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
     */
    public void getBykeyWord(String keyWord){
        BmobQuery<Collection> query = new BmobQuery<Collection>();
        query.setLimit(LIMIT);

        query.order("-coltLikeNum,-createdAt");//先按点赞数降序，再按时间降序
        query.include("coltToMuseum");
        query.addWhereContains("coltName",keyWord);
        query.findObjects(new FindListener<Collection>() {
            @Override
            public void done(List<Collection> list, BmobException e) {
                if (e == null) {

                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    ToastUtils.toast(context, e.getMessage());
                }

            }
        });
    }



}
