package bmobUtils;

import android.content.Context;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import db.MuseumDB;
import interfaces.OnBmobReturnWithObj;
import model.User;
import model.UserRelation;

import static util.ParameterBase.LIMIT_TEN;

/**
 * 处理用户之间的关注取关  以及人物关系提取
 * Created by wjc on 2017/9/20.
 */

public class BmobUserRelation {


    private OnBmobReturnWithObj onBmobReturnWithObj;

    private Context context;


    private static BmobUserRelation instance=null;


    private static BmobUserRelation instance2=null;

    private BmobUserRelation() {

    }

    public BmobUserRelation( Context context) {

        this.context = context;
    }

    public void setOnBmobReturnWithObj(OnBmobReturnWithObj onBmobReturnWithObj) {
        this.onBmobReturnWithObj = onBmobReturnWithObj;
    }

    public static BmobUserRelation getInstance(Context context){

        if(instance==null){
            synchronized (BmobUserRelation.class){
                if(instance==null){
                    instance=new BmobUserRelation(context);
                }

            }

        }

        return instance;


    }
    public static BmobUserRelation getInstance2(Context context){

        if(instance2==null){
            synchronized (BmobUserRelation.class){
                if(instance2==null){
                    instance2=new BmobUserRelation(context);
                }

            }

        }

        return instance2;


    }



    /**
     * 关注用户
     * <p>
     * 将关注的其他用户添加进用户的BmobRelation
     * <p>
     * 同时将用户添加进被关注用户的粉丝列表
     */
    public void watchUsers(final User localUser, User watchedUser) {


        UserRelation localUserRelation=new UserRelation();

        BmobRelation relation = new BmobRelation();
        relation.add(watchedUser);
        localUserRelation.setFollowings(relation);

        UserRelation userRelation=new UserRelation();
        BmobRelation relation1 = new BmobRelation();
        relation1.add(localUser);
        userRelation.setFans(relation1);


        localUserRelation.update(localUser.getUserRelationID(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    updateFollowingNum(localUser.getUserRelationID());
                    onBmobReturnWithObj.onSuccess(null);
                    Log.i("bmob", "关注用户成功");
                } else {
                    onBmobReturnWithObj.onFail(e.getMessage());
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }
        });

        userRelation.update(watchedUser.getUserRelationID(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {


                    Log.i("bmob", "关注用户成功");
                } else {

                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }
        });

    }

    /**
     * 取关用户
     * <p>
     * 将关注的其他用户从用户的BmobRelation移除
     * <p>
     * 同时将用户从被关注用户的粉丝列表移除
     */
    public void unWatchUsers(final User localUser, User watchedUser) {


        UserRelation localUserRelation=new UserRelation();

        BmobRelation relation = new BmobRelation();
        relation.remove(watchedUser);
        localUserRelation.setFollowings(relation);

        UserRelation userRelation=new UserRelation();
        BmobRelation relation1 = new BmobRelation();
        relation1.remove(localUser);
        userRelation.setFans(relation1);


        localUserRelation.update(localUser.getUserRelationID(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    updateFollowingNum(localUser.getUserRelationID());
                    onBmobReturnWithObj.onSuccess(null);
                    Log.i("bmob", "取关用户成功");
                } else {
                    onBmobReturnWithObj.onFail(e.getMessage());
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }
        });

        userRelation.update(watchedUser.getUserRelationID(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {


                    Log.i("bmob", "被取关成功");
                } else {

                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }
        });

    }



    /**
     * 网络分页获取关注
     */
    public void getFollowing(int curPage,String userRelationID) {
        UserRelation userRelation=new UserRelation();
        userRelation.setObjectId(userRelationID);


        BmobQuery<User> query = new BmobQuery<User>();
        query.order("-createdAt");
        query.setSkip(LIMIT_TEN * curPage);
        query.addWhereRelatedTo("followings",new BmobPointer(userRelation));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    onBmobReturnWithObj.onSuccess(list);


                } else {
                    onBmobReturnWithObj.onFail(null);
                    Log.i("获取关注失败", "失败：" + e.getMessage());
                }

            }
        });
    }
    /**
     * 网络分页获取粉丝
     */
    public void getFans(int curPage,String userRelationID) {
        UserRelation userRelation=new UserRelation();
        userRelation.setObjectId(userRelationID);


        BmobQuery<User> query = new BmobQuery<User>();
        query.order("-createdAt");
        query.setSkip(LIMIT_TEN * curPage);
        query.addWhereRelatedTo("fans",new BmobPointer(userRelation));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    onBmobReturnWithObj.onSuccess(list);


                } else {
                    onBmobReturnWithObj.onFail(null);
                    Log.i("获取粉丝失败", "失败：" + e.getMessage());
                }

            }
        });
    }


    //从数据库获取粉丝
    public List<User> getFansFromDB() {

       return MuseumDB.getInstance(context).loadFans();

    }
    //从数据库获取关注
    public List<User> getFollowingsFromDB() {

        return MuseumDB.getInstance(context).loadFollowings();

    }



    public void getFansAndFollowingsNum(String userRelationID){

        if(userRelationID==null){
            return;
        }
        final UserRelation userRelation=new UserRelation();
        userRelation.setObjectId(userRelationID);

        BmobQuery<User> query=new BmobQuery<User>();
        query.addWhereRelatedTo("followings",new BmobPointer(userRelation));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {

                    final int followingNum=list.size();
                    BmobQuery<User> query2=new BmobQuery<User>();
                    query2.addWhereRelatedTo("fans",new BmobPointer(userRelation));
                    query2.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null) {
                               int fansNum=list.size();
                                int[] ints=new int[]{fansNum,followingNum};
                                onBmobReturnWithObj.onSuccess(ints);


                            } else {
                                onBmobReturnWithObj.onFail(null);
                                Log.i("获取粉丝失败", "失败：" + e.getMessage());
                            }



                        }
                    });

                } else {
                    Log.i("获取关注失败", "失败：" + e.getMessage());
                }
            }
        });




    }



    //将最新的关注数量更新到本地数据库
    public void updateFollowingNum(String userRelationID){

        UserRelation userRelation=new UserRelation();
        userRelation.setObjectId(userRelationID);

        BmobQuery<User> query=new BmobQuery<User>();
        query.addWhereRelatedTo("followings",new BmobPointer(userRelation));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    MuseumDB.getInstance(context).updateFollowings(list);

                } else {
                    Log.i("获取关注失败", "失败：" + e.getMessage());
                }
            }
        });

    }
    //将最新的关注数量更新到本地数据库
    public void updateFollowingAndFansNum(String userRelationID){

        if(userRelationID==null){
            return;
        }
        UserRelation userRelation=new UserRelation();
        userRelation.setObjectId(userRelationID);

        BmobQuery<User> query=new BmobQuery<User>();
        query.addWhereRelatedTo("followings",new BmobPointer(userRelation));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    MuseumDB.getInstance(context).updateFollowings(list);


                } else {
                    Log.i("获取关注失败", "失败：" + e.getMessage());
                }



            }
        });

        BmobQuery<User> query2=new BmobQuery<User>();
        query2.addWhereRelatedTo("fans",new BmobPointer(userRelation));
        query2.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    MuseumDB.getInstance(context).updateFans(list);

                } else {
                    Log.i("获取粉丝失败", "失败：" + e.getMessage());
                }



            }
        });

    }


    public void addUserRelationPointer(final User user){
        final UserRelation userRelation=new UserRelation();
        userRelation.setUserID(user.getObjectId());
        userRelation.setMaster(user);
        userRelation.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    User newUser=new User();

                    newUser.setUserRelationID(s);
                    newUser.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {

                        }
                    });


                } else {
                    Log.i("关系存储失败", "失败：" + e.getMessage());
                }


            }
        });



    }




    public void checkUserRelationPointer(User user ){
//        fetchUserInfo();





        if(user.getUserRelationID()==null){
            addUserRelationPointer(user);
        }









    }




}
