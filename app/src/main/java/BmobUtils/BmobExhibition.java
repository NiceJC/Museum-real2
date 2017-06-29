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
        query.include("toMuseum");
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


    //查询某用户关注的所有展览
    public void getLikedExhibition(String userID){
        User user=new User();
        user.setObjectId(userID);
        BmobQuery<Exhibition> query=new BmobQuery<Exhibition>();
        query.addWhereRelatedTo("watchExhibitions",new BmobPointer(user));
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


    //获取关注某个展览的所有用户
    public void getFansOfExhibition(String exhibitID){
        Exhibition exhibition=new Exhibition();
        exhibition.setObjectId(exhibitID);

        BmobQuery<User> query=new BmobQuery<>();
        query.addWhereRelatedTo("watchedUsers",new BmobPointer(exhibition));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){
                    onBmobReturnWithObj.onSuccess(list);
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });
    }


    /**
     * 关注展览
     *
     * 将关注的展览添加进用户的BmobRelation
     *
     * 同时将用户添加进Exhibition的BmobRelation
     *
     */
    public void watchExhibit(String exhibitID){
        User user= BmobUser.getCurrentUser(User.class);
        Exhibition exhibition=new Exhibition();
        exhibition.setObjectId(exhibitID);

        BmobRelation relation=new BmobRelation();
        relation.add(exhibition);
        user.setWatchExhibitions(relation);

        BmobRelation relation1=new BmobRelation();
        relation1.add(user);
        exhibition.setWatchedUsers(relation1);


        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","关注展馆成功");
                    onBmobReturnWithObj.onSuccess(null);
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                    onBmobReturnWithObj.onFail(null);
                }
            }
        });

        exhibition.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });


    }

    /**
     * 取消关注博物馆
     *
     */
    public void cancelWatchExhibit(String exhibitID){
        User user=BmobUser.getCurrentUser(User.class);
        Exhibition  exhibition=new Exhibition();
        exhibition.setObjectId(exhibitID);

        BmobRelation relation=new BmobRelation();
        relation.remove(exhibition);
        user.setWatchExhibitions(relation);

        BmobRelation relation1=new BmobRelation();
        relation1.remove(user);
        exhibition.setWatchedUsers(relation1);


        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","取消关注展览成功");
                    onBmobReturnWithObj.onSuccess(null);
                }else{

                    Log.i("bmob","失败："+e.getMessage());
                    onBmobReturnWithObj.onFail(null);
                }
            }
        });

        exhibition.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {

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





