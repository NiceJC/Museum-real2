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
import entity.Blog;
import entity.Collection;
import entity.ExhibitRoom;
import entity.Exhibition;
import entity.Museum;
import entity.User;
import interfaces.OnBmobReturnWithObj;
import util.ToastUtils;

import static util.ParameterBase.EXHIBIROOM_ID;
import static util.ParameterBase.EXHIBITION_ID;
import static util.ParameterBase.HOTLIMIT;
import static util.ParameterBase.LIMIT;
import static util.ParameterBase.LIMIT_TEN;
import static util.ParameterBase.MUSEUM_ID;

/**
 * 藏品的服务器操作类
 * <p>
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

    //获取热门藏品，以浏览数的多少为准
    public void getHotColt(int curPage) {

        BmobQuery<Collection> query = new BmobQuery<Collection>();
        query.setLimit(HOTLIMIT);
        query.setSkip(HOTLIMIT*curPage);

        query.order("-hotValue,-createdAt");//先按点赞数降序，再按时间降序
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

    //根据所属ID，以及belongType进行查询
    public void getByBelongID(String id, String idType,int curPage) {

        BmobQuery<Collection> query=new BmobQuery<Collection>();
        switch (idType) {
            case EXHIBITION_ID:

                Exhibition exhibition=new Exhibition();
                exhibition.setObjectId(id);
                query.addWhereEqualTo("toExhibition",new BmobPointer(exhibition));
                break;

            case EXHIBIROOM_ID:
                ExhibitRoom exhibitRoom=new ExhibitRoom();
                exhibitRoom.setObjectId(id);
                query.addWhereEqualTo("coltShowRoom",new BmobPointer(exhibitRoom));
                break;

            case MUSEUM_ID:
                Museum museum=new Museum();
                museum.setObjectId(id);
                query.addWhereEqualTo("coltToMuseum",new BmobPointer(museum));
                break;

            default:
                break;
        }


        query.setLimit(LIMIT);
        query.setSkip(LIMIT * curPage);
        query.order("-createdAt");
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
     * 关键词查询，暂时只针对名称的关键字
     */
    public void getBykeyWord(String keyWord) {
        BmobQuery<Collection> query = new BmobQuery<Collection>();
        query.setLimit(LIMIT);

        query.order("-coltLikeNum,-createdAt");//先按点赞数降序，再按时间降序
        query.include("coltToMuseum");
        query.addWhereContains("coltName", keyWord);
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
     *  根据藏品分类进行查询
     */
    public void getByType(int type,int curPage){
        BmobQuery<Collection> query=new BmobQuery<>();
        query.setLimit(LIMIT_TEN);
        query.addWhereEqualTo("coltSort",type);
        query.setSkip(LIMIT_TEN*curPage);
        query.order("-coltLikeNum,-createdAt");//先按点赞数降序，再按时间降序

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
    public void getByColtID(String coltID){

        BmobQuery<Collection> query=new BmobQuery<Collection>();
        query.getObject(coltID, new QueryListener<Collection>() {
            @Override
            public void done(Collection collection, BmobException e) {
                if (e == null) {

                    onBmobReturnWithObj.onSuccess(collection);
                } else {
                    ToastUtils.toast(context, e.getMessage());
                }
            }
        });



    }



    //热力值的增加  一般来说 点击进一次详情页，增加一
    public void incrementHotValue(String coltID){
        Collection collection=new Collection();
        collection.increment("hotValue");
        collection.update(coltID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });

    }


    //获取某个用户喜欢的所有藏品
    public void getLikedColt(String userID){
        User user=new User();
        user.setObjectId(userID);
        BmobQuery<Collection> query=new BmobQuery<Collection>();
        query.addWhereRelatedTo("likeCollections",new BmobPointer(user));
        query.findObjects(new FindListener<Collection>() {
            @Override
            public void done(List<Collection> list, BmobException e) {
                if(e==null){
                    onBmobReturnWithObj.onSuccess(list);
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });
    }

    //获取收藏某个藏品的所有用户
    public void getLikedUsers(String colt_ID){
        Collection collection=new Collection();
        collection.setObjectId(colt_ID);
        BmobQuery<User> query=new BmobQuery<User>();
        query.addWhereRelatedTo("likedUser",new BmobPointer(collection));
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
     * 喜欢藏品
     *
     *
     */
    public void likeColt(String coltID){
        User user= BmobUser.getCurrentUser(User.class);
        Collection collection=new Collection();
        collection.setObjectId(coltID);

        BmobRelation relation=new BmobRelation();
        relation.add(collection);
        user.setLikeCollections(relation);

        BmobRelation relation1=new BmobRelation();
        relation1.add(user);
        collection.setLikedUser(relation1);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    onBmobReturnWithObj.onSuccess(null);
                    Log.i("bmob","喜欢colt成功");
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });

        collection.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });

    }
    /**
     * 取消喜欢藏品
     *
     *
     */
    public void cancellikeColt(String coltID){
        User user= BmobUser.getCurrentUser(User.class);
        Collection collection=new Collection();
        collection.setObjectId(coltID);

        BmobRelation relation=new BmobRelation();
        relation.remove(collection);
        user.setLikeCollections(relation);

        BmobRelation relation1=new BmobRelation();
        relation1.remove(user);
        collection.setLikedUser(relation1);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    onBmobReturnWithObj.onSuccess(null);
                    Log.i("bmob","取消喜欢collection成功");
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });

        collection.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });

    }



}
