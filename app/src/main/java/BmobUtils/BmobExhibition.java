package BmobUtils;

import android.content.Context;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import entity.ExhibitRoom;
import entity.Exhibition;
import entity.Museum;
import entity.User;
import interfaces.OnBmobReturnWithObj;
import util.ToastUtils;

import static util.ParameterBase.LIMIT;

/**
 * 展览的服务端操作类
 * Created by wjc on 2017/5/3.
 */

public class BmobExhibition {

    private Context context;
    private String TAG;
    private static BmobExhibition instance = null;

    private OnBmobReturnWithObj onBmobReturnWithObj;

    public void setOnBmobReturnWithObj(OnBmobReturnWithObj onBmobReturnWithObj) {
        this.onBmobReturnWithObj = onBmobReturnWithObj;
    }


    private BmobExhibition(Context context) {
        this.context = context;
        TAG = "bmob blog";

    }

    //单例
    public static BmobExhibition getInstance(Context context) {

        if (instance == null) {
            instance = new BmobExhibition(context);

        }

        return instance;

    }

    /**
     * 刷新列表
     * 一次查询最多返回5条
     * 暂时按照创建时间排序
     * 将所属博物馆的信息一并查出
     */
    public void refreshExhibition() {

        BmobQuery<Exhibition> query = new BmobQuery<Exhibition>();


        query.setLimit(LIMIT);
        query.order("-createdAt");
        query.include("toMuseum");
        query.findObjects(new FindListener<Exhibition>() {
            @Override
            public void done(List<Exhibition> list, BmobException e) {
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
     * 传入当前的页数，以便在返回结果跳过
     */
    public void getMoreExhibition(int curPage) {

        BmobQuery<Exhibition> query = new BmobQuery<Exhibition>();
        query.setLimit(LIMIT);
        query.setSkip(LIMIT * curPage);
        query.order("-createdAt");
        query.include("toMuseum");
        query.findObjects(new FindListener<Exhibition>() {
            @Override
            public void done(List<Exhibition> list, BmobException e) {
                if (e == null) {

                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    ToastUtils.toast(context, e.getMessage());
                }

            }
        });

    }


    //根据ID查询单个Exhibition
    public void getExhibitionByID(String ID){

        BmobQuery<Exhibition> query=new BmobQuery<Exhibition>();
        query.getObject(ID, new QueryListener<Exhibition>() {
            @Override
            public void done(Exhibition exhibition, BmobException e) {
                if(e==null){
                    onBmobReturnWithObj.onSuccess(exhibition);

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });



    }



    /**
     * 关键词查询，暂时只针对名称的关键字
     */
    public void getBykeyWord(String keyWord) {
        BmobQuery<Exhibition> query = new BmobQuery<Exhibition>();
        query.setLimit(LIMIT);

        query.order("-createdAt");//先按点赞数降序，再按时间降序
        query.include("toMuseum");
        query.addWhereContains("exhibitName", keyWord);
        query.findObjects(new FindListener<Exhibition>() {
            @Override
            public void done(List<Exhibition> list, BmobException e) {
                if (e == null) {

                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    ToastUtils.toast(context, e.getMessage());
                }

            }
        });
    }


    public void getLikedExhibition(String userID){
        User user=new User();
        user.setObjectId(userID);
        BmobQuery<Exhibition> query=new BmobQuery<Exhibition>();
        query.addWhereRelatedTo("watchMuseums",new BmobPointer(user));
        query.findObjects(new FindListener<Exhibition>() {
            @Override
            public void done(List<Exhibition> list, BmobException e) {
                if(e==null){
                    onBmobReturnWithObj.onSuccess(list);
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });
    }
    //上传
//    public void uploadExhibition(Exhibition exhibition){
//
//        exhibition.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//
//
//            }
//        });
//
//
//    }


}





