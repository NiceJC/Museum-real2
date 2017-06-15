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
import cn.bmob.v3.socketio.callback.StringCallback;
import entity.Blog;
import entity.Collection;
import entity.Comments;
import entity.Exhibition;
import entity.Museum;
import entity.User;
import interfaces.OnBmobReturnWithObj;

import static util.ParameterBase.LIMIT;
import static util.ParameterBase.LIMIT_TEN;

/**
 * Created by wjc on 2017/4/27.
 */

public class BmobSocialUtil {

    private Context context;
    private String TAG;
    private static BmobSocialUtil instance = null;

    private OnBmobReturnWithObj onBmobReturnWithObj;

    public void setOnBmobReturnWithObj(OnBmobReturnWithObj onBmobReturnWithObj) {
        this.onBmobReturnWithObj = onBmobReturnWithObj;
    }


    private BmobSocialUtil(Context context) {
        this.context = context;
        TAG = "bmob blog";

    }

    //单例
    public static BmobSocialUtil getInstance(Context context) {

        if (instance == null) {
            instance = new BmobSocialUtil(context);

        }

        return instance;

    }


    /**
     * 上传Blog
     *，参数包括 文字和图片的 包含url的List
     * 关联本地用户作为作者
     */
    public void postBlog(String textContent, List<String> urls) {

        User user = BmobUser.getCurrentUser(User.class);
        Blog blog = new Blog();
        blog.setContentText(textContent);
        blog.setImageURLs(urls);
        blog.setCommentNums(0);
        blog.setPraiseNums(0);

        blog.setAuthor(user);
        blog.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "保存成功");
                    onBmobReturnWithObj.onSuccess(s);
                } else {
                    Log.i("bmob", "保存失败：" + e.getMessage());
                    onBmobReturnWithObj.onFail(e.getMessage());
                }
            }
        });
    }

    /**
     * 删除某条Blog
     */
    public void deleteBlog(String blogID){
        Blog blog=new Blog();
        blog.setObjectId(blogID);
        blog.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "保存成功");
                    onBmobReturnWithObj.onSuccess(null);
                } else {
                    Log.i("bmob", "保存失败：" + e.getMessage());
                    onBmobReturnWithObj.onFail(e.getMessage());
                }
            }
        });
    }

    /**
     * 删除某条评论
     *
     */
    public void deleteComment(String commentID){

        Comments comments=new Comments();
        comments.setObjectId(commentID);
        comments.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "保存成功");
                    onBmobReturnWithObj.onSuccess(null);
                } else {
                    Log.i("bmob", "保存失败：" + e.getMessage());
                    onBmobReturnWithObj.onFail(e.getMessage());
                }
            }
        });


    }

    /**
     * 评论Blog
     * 将本地用户关联到评论
     * 评论的write权限 仅限评论人和被评论主体的作者
     */

    public void commentBlog(String blogID,String commentText){
        User user=BmobUser.getCurrentUser(User.class);
        Blog blog=new Blog();
        blog.setObjectId(blogID);

        Comments comments=new Comments();

        comments.setCommentText(commentText);
        comments.setBlog(blog);
        comments.setAuthor(user);
        comments.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "保存成功");
                    onBmobReturnWithObj.onSuccess(s);
                } else {
                    Log.i("bmob", "保存失败：" + e.getMessage());
                    onBmobReturnWithObj.onFail(e.getMessage());
                }
            }
        });
    }
    /**
     * 点赞Blog
     * 先查询Blog的likeUser列表，是否已经包括本地用户
     *
     */
    public void likeBlog(String blogID){
        User user=BmobUser.getCurrentUser(User.class);
        Blog blog=new Blog();
        blog.setObjectId(blogID);

        BmobRelation relation=new BmobRelation();
        relation.add(blog);
        user.setLikeBlogs(relation);

        BmobRelation relation1=new BmobRelation();
        relation1.add(user);
        blog.setLikedUsers(relation1);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","点赞blog成功");
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });

        blog.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });

    }

    /**
     * 取消点赞Blog
     *
     */
    public void cancelLikeBlog(String blogID){
        User user=BmobUser.getCurrentUser(User.class);
        Blog blog=new Blog();
        blog.setObjectId(blogID);

        BmobRelation relation=new BmobRelation();
        relation.remove(blog);
        user.setLikeBlogs(relation);

        BmobRelation relation1=new BmobRelation();
        relation1.remove(user);
        blog.setLikedUsers(relation1);

        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","取消点赞blog成功");
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });

        blog.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });

    }


    /**
     * 获取关注的用户
     *
     */
    public void getFollowing(String userID){
        User user=new User();
        user.setObjectId(userID);
        BmobQuery<User> query=new BmobQuery<User>();
        query.addWhereRelatedTo("watchUsers",new BmobPointer(user));
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
     * 获取所有粉丝
     */
    public void getFans(String userID){


        User user=new User();
        user.setObjectId(userID);
        BmobQuery<User> query=new BmobQuery<User>();
        query.addWhereRelatedTo("fans",new BmobPointer(user));
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
     * 关注用户
     *
     * 将关注的其他用户添加进用户的BmobRelation
     *
     * 同时将用户添加进被关注用户的粉丝列表
     *
     */
    public void watchUsers(String userID){
        User localUser=BmobUser.getCurrentUser(User.class);
        User user=new User();
        user.setObjectId(userID);

        BmobRelation relation=new BmobRelation();
        relation.add(user);
        localUser.setWatchUsers(relation);

        BmobRelation relation1=new BmobRelation();
        relation1.add(localUser);
        user.setFans(relation1);


        localUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","关注用户成功");
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });

        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }

    /**
     * 取关用户
     *
     * 将关注的其他用户从用户的BmobRelation移除
     *
     * 同时将用户从被关注用户的粉丝列表移除
     *
     */
    public void cancelWatchUsers(String userID){
        User localUser=BmobUser.getCurrentUser(User.class);
        User user=new User();
        user.setObjectId(userID);

        BmobRelation relation=new BmobRelation();
        relation.remove(user);
        localUser.setWatchUsers(relation);

        BmobRelation relation1=new BmobRelation();
        relation1.remove(localUser);
        user.setFans(relation1);


        localUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("bmob","取关用户成功");
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });

        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
    }

//
//    /**
//     * 给博物馆留言
//     * 将本地用户关联到评论
//     */
//    public void commentMuseum(String museumID,String commentText){
//
//        User user=BmobUser.getCurrentUser(User.class);
//        Museum museum=new Museum();
//        museum.setObjectId(museumID);
//
//        Comments comments=new Comments();
//        comments.setCommentText(commentText);
//        comments.setMuseum(museum);
//        comments.setAuthor(user);
//        comments.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if (e == null) {
//                    Log.i("bmob", "保存成功");
//                    onBmobReturnWithObj.onSuccess(s);
//                } else {
//                    Log.i("bmob", "保存失败：" + e.getMessage());
//                    onBmobReturnWithObj.onFail(e.getMessage());
//                }
//            }
//        });
//    }
//    /**
//     * 给展览留言
//     * 将本地用户关联到评论
//     */
//    public void commentExhibition(String exhibitionID,String commentText){
//        User user=BmobUser.getCurrentUser(User.class);
//        Exhibition exhibition=new Exhibition();
//        exhibition.setObjectId(exhibitionID);
//
//        Comments comments=new Comments();
//        comments.setCommentText(commentText);
//        comments.setExhibition(exhibition);
//        comments.setAuthor(user);
//        comments.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if (e == null) {
//                    Log.i("bmob", "保存成功");
//                    onBmobReturnWithObj.onSuccess(s);
//                } else {
//                    Log.i("bmob", "保存失败：" + e.getMessage());
//                    onBmobReturnWithObj.onFail(e.getMessage());
//                }
//            }
//        });
//
//
//    }
//
//    /**
//     * 给藏品留言
//     * 将本地用户关联到评论
//     */
//    public void commentCollection(String coltID,String commentText){
//
//        User user=BmobUser.getCurrentUser(User.class);
//        Collection collection=new Collection();
//        collection.setObjectId(coltID);
//
//        Comments comments=new Comments();
//        comments.setCommentText(commentText);
//        comments.setCollection(collection);
//        comments.setAuthor(user);
//        comments.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if (e == null) {
//                    Log.i("bmob", "保存成功");
//                    onBmobReturnWithObj.onSuccess(s);
//                } else {
//                    Log.i("bmob", "保存失败：" + e.getMessage());
//                    onBmobReturnWithObj.onFail(e.getMessage());
//                }
//            }
//        });
//    }


    /**
     *
     * 根据ID获取单个Blog
     */
public  void getBlogByID(String blogID){

    BmobQuery<Blog> query=new BmobQuery<Blog>();
    query.include("Author");

    query.getObject(blogID, new QueryListener<Blog>() {
        @Override
        public void done(Blog blog, BmobException e) {
            if(e==null){

                onBmobReturnWithObj.onSuccess(blog);
            }else{
                onBmobReturnWithObj.onFail(null);

                Log.i("bmob","获取单个Blog失败："+e.getMessage()+","+e.getErrorCode());
            }
        }
    });

}

    //获取给某个Blog点赞的所有用户
    public void getBlogLikesByID(String blogID){
        BmobQuery<User> query=new BmobQuery<User>();
        Blog blog=new Blog();
        blog.setObjectId(blogID);
        query.addWhereRelatedTo("likedUsers",new BmobPointer(blog));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){
                    onBmobReturnWithObj.onSuccess(list);
                    Log.i("bmob","查询个数："+list.size());
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });



    }


    /**
     * 获取某个用户post的所有Blog
     */

    public void getBlogByAuthor(String userID){
        BmobQuery<Blog> query=new BmobQuery<Blog>();
        User user=new User();
        user.setObjectId(userID);
        query.addWhereEqualTo("Author",user);
        query.findObjects(new FindListener<Blog>() {
            @Override
            public void done(List<Blog> list, BmobException e) {
                if(e==null){
                    onBmobReturnWithObj.onSuccess(list);
                    Log.i("bmob","查询个数："+list.size());
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });



    }



    /**
     * 获取最新鲜的Blog
     *
     */
    public void getRecentBlog(int curPage){
        BmobQuery<Blog> query=new BmobQuery<Blog>();
        query.setLimit(LIMIT_TEN);
        query.include("Author");
        query.order("-createdAt");
        query.setSkip(LIMIT_TEN*curPage);

        query.findObjects(new FindListener<Blog>() {
            @Override
            public void done(List<Blog> list, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "获取"+list.size()+"条Blog");
                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    Log.i("bmob", "保存失败：" + e.getMessage());
                    onBmobReturnWithObj.onFail(e.getMessage());
                }
            }
        });


    }
    /**
     * 获取关注人的Blog
     */
    public void geWatchedBlog(int curPage){

        BmobQuery<Blog> query=new BmobQuery<Blog>();
        User user=BmobUser.getCurrentUser(User.class);
        user.getWatchMuseums();
        user.getWatchUsers();

        query.include("Author");
        query.setLimit(LIMIT);
        query.order("-createdAt");
        query.setSkip(LIMIT*curPage);
        query.findObjects(new FindListener<Blog>() {
            @Override
            public void done(List<Blog> list, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "获取"+LIMIT+"条Blog");
                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    Log.i("bmob", "保存失败：" + e.getMessage());
                    onBmobReturnWithObj.onFail(e.getMessage());
                }
            }
        });
    }
}