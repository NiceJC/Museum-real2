package BmobUtils;

import android.content.Context;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import entity.Exhibition;
import entity.Museum;
import entity.User;
import interfaces.OnBmobReturnWithObj;
import util.ToastUtils;

import static util.ParameterBase.LIMIT;

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




    public void getMuseumByID(String museumID){


        BmobQuery<Museum> query=new BmobQuery<Museum>();
        query.getObject(museumID, new QueryListener<Museum>() {
            @Override
            public void done(Museum museum, BmobException e) {
                if(e==null){
                    onBmobReturnWithObj.onSuccess(museum);

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });


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
    public void getMoreMuseum (int curPage){

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


//    //上传
//    public void uploadMuseum(Museum museum){
//
//        museum.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//
//
//            }
//        });
//
//
//    }




    /**
     * 关注博物馆
     *
     * 将关注的博物馆添加进用户的BmobRelation
     *
     * 同时将用户添加进Museum的BmobRelation
     *
     */
    public void watchMuseum(String museumID){
        User user= BmobUser.getCurrentUser(User.class);
        Museum museum=new Museum();
        museum.setObjectId(museumID);

        BmobRelation relation=new BmobRelation();
        relation.add(museum);
        user.setWatchMuseums(relation);

        BmobRelation relation1=new BmobRelation();
        relation1.add(user);
        museum.setWatchedUsers(relation1);


        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","关注博物馆成功");
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });

        museum.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });


    }

    /**
     * 取消关注博物馆
     *
     */
    public void cancelWatchMuseum(String museumID){
        User user=BmobUser.getCurrentUser(User.class);
        Museum museum=new Museum();
        museum.setObjectId(museumID);

        BmobRelation relation=new BmobRelation();
        relation.remove(museum);
        user.setWatchMuseums(relation);

        BmobRelation relation1=new BmobRelation();
        relation1.remove(user);
        museum.setWatchedUsers(relation1);


        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","取消关注博物馆成功");
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });

        museum.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }


    /**
     * 查询某个用户关注的所有博物馆
     */

    public void getLikedMuseums(String userID){
        User user=new User();
        user.setObjectId(userID);
        BmobQuery<Museum> query=new BmobQuery<Museum>();
        query.addWhereRelatedTo("watchMuseums",new BmobPointer(user));
        query.findObjects(new FindListener<Museum>() {
            @Override
            public void done(List<Museum> list, BmobException e) {
                if(e==null){
                    onBmobReturnWithObj.onSuccess(list);
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });
    }




}
