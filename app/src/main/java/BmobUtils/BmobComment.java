package BmobUtils;

import android.content.Context;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import entity.Blog;
import entity.Collection;
import entity.Comments;
import entity.Exhibition;
import entity.Museum;
import interfaces.OnBmobReturnWithObj;
import util.ToastUtils;

/**
 * Created by wjc on 2017/5/4.
 */

public class BmobComment {

    private Context context;
    private String TAG;
    private static BmobComment instance = null;

    private OnBmobReturnWithObj onBmobReturnWithObj;

    public void setOnBmobReturnWithObj(OnBmobReturnWithObj onBmobReturnWithObj) {
        this.onBmobReturnWithObj = onBmobReturnWithObj;
    }


    private BmobComment(Context context) {
        this.context = context;
        TAG = "bmob blog";

    }

    //单例
    public static BmobComment getInstance(Context context) {

        if (instance == null) {
            instance = new BmobComment(context);

        }

        return instance;

    }



    //查询出博物馆对应的所有留言，并一并查查出留言的作者
    public void getCommentToMuseum(String museumID){
        BmobQuery<Comments> query=new BmobQuery<Comments>();

        Museum museum=new Museum();
        museum.setObjectId(museumID);
        query.addWhereEqualTo("museum",new BmobPointer(museum));
        query.include("author");

        query.findObjects(new FindListener<Comments>() {
            @Override
            public void done(List<Comments> list, BmobException e) {
                if(e==null){
                    onBmobReturnWithObj.onSuccess(list);
                }else{
                    ToastUtils.toast(context,e.getMessage());
                    Log.e(TAG,e.getMessage());
                }
            }
        });

    }


    //查询出展览对应的所有留言，并一并查查出留言的作者
    public void getCommentToExhibition(String exhibitID){
        BmobQuery<Exhibition> query=new BmobQuery<Exhibition>();

        Exhibition exhibition=new Exhibition();
        exhibition.setObjectId(exhibitID);
        query.addWhereEqualTo("exhibition",new BmobPointer(exhibitID));
        query.include("author");

        query.findObjects(new FindListener<Exhibition>() {
            @Override
            public void done(List<Exhibition> list, BmobException e) {
                if(e==null){
                    onBmobReturnWithObj.onSuccess(list);
                }else{
                    ToastUtils.toast(context,e.getMessage());
                    Log.e(TAG,e.getMessage());
                }
            }
        });

    }


    //查询出文物对应的所有留言，并一并查查出留言的作者
    public void getCommentToColt(String coltID){
        BmobQuery<Collection> query=new BmobQuery<Collection>();

        Collection collection=new Collection();
        collection.setObjectId(coltID);
        query.addWhereEqualTo("collection",new BmobPointer(collection));
        query.include("author");

        query.findObjects(new FindListener<Collection>() {
            @Override
            public void done(List<Collection> list, BmobException e) {
                if(e==null){
                    onBmobReturnWithObj.onSuccess(list);
                }else{
                    ToastUtils.toast(context,e.getMessage());
                    Log.e(TAG,e.getMessage());
                }
            }
        });

    }


    //查询出Blog对应的所有留言，并一并查查出留言的作者
    public void getCommentToBlog(String blogID){
        BmobQuery<Blog> query=new BmobQuery<Blog>();

        Blog blog=new Blog();
        blog.setObjectId(blogID);
        query.addWhereEqualTo("blog",new BmobPointer(blog));
        query.include("author");

        query.findObjects(new FindListener<Blog>() {
            @Override
            public void done(List<Blog> list, BmobException e) {
                if(e==null){
                    onBmobReturnWithObj.onSuccess(list);
                }else{
                    ToastUtils.toast(context,e.getMessage());
                    Log.e(TAG,e.getMessage());
                }
            }
        });

    }





}
