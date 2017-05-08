package BmobUtils;

import android.content.Context;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import entity.Blog;
import entity.Collection;
import entity.Comments;
import entity.Exhibition;
import entity.Museum;
import entity.User;
import interfaces.OnBmobReturnWithObj;

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
     * 给博物馆留言
     * 将本地用户关联到评论
     */
    public void commentMuseum(String museumID,String commentText){

        User user=BmobUser.getCurrentUser(User.class);
        Museum museum=new Museum();
        museum.setObjectId(museumID);

        Comments comments=new Comments();
        comments.setCommentText(commentText);
        comments.setMuseum(museum);
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
     * 给展览留言
     * 将本地用户关联到评论
     */
    public void commentExhibition(String exhibitionID,String commentText){
        User user=BmobUser.getCurrentUser(User.class);
        Exhibition exhibition=new Exhibition();
        exhibition.setObjectId(exhibitionID);

        Comments comments=new Comments();
        comments.setCommentText(commentText);
        comments.setExhibition(exhibition);
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
     * 给藏品留言
     * 将本地用户关联到评论
     */
    public void commentCollection(String coltID,String commentText){

        User user=BmobUser.getCurrentUser(User.class);
        Collection collection=new Collection();
        collection.setObjectId(coltID);

        Comments comments=new Comments();
        comments.setCommentText(commentText);
        comments.setCollection(collection);
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

}