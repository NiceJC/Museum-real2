package BmobUtils;

import android.content.Context;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import entity.Blog;
import entity.Collection;
import entity.Comments;
import entity.Exhibition;
import entity.Museum;
import entity.User;
import interfaces.OnBmobReturnWithObj;
import util.ToastUtils;

import static entity.Comments.COMMENT_TO_BLOG;
import static entity.Comments.COMMENT_TO_COLLECTION;
import static entity.Comments.COMMENT_TO_EXHIBITION;
import static entity.Comments.COMMENT_TO_MUSEUM;

/**
 * 评论的Bmob操作
 * 包括评论的提交 查询 删除
 * 提交评论的时候，需要额外地将评论数+1 ，便于在Blog或者Collection列表上第一时间显示出评论数量
 * （好麻烦啊  早知道用BmobRelation了）
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
    public void getCommentToMuseum(String museumID) {
        BmobQuery<Comments> query = new BmobQuery<Comments>();

        Museum museum = new Museum();
        museum.setObjectId(museumID);
        query.addWhereEqualTo("museum", new BmobPointer(museum));
        query.include("author");

        query.findObjects(new FindListener<Comments>() {
            @Override
            public void done(List<Comments> list, BmobException e) {
                if (e == null) {
                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    ToastUtils.toast(context, e.getMessage());
                    Log.e(TAG, e.getMessage());
                }
            }
        });

    }


    //查询出展览对应的所有留言，并一并查查出留言的作者
    public void getCommentToExhibition(String exhibitID) {
        BmobQuery<Comments> query = new BmobQuery<Comments>();

        Exhibition exhibition = new Exhibition();
        exhibition.setObjectId(exhibitID);
        query.addWhereEqualTo("exhibition", new BmobPointer(exhibitID));
        query.include("author");

        query.findObjects(new FindListener<Comments>() {
            @Override
            public void done(List<Comments> list, BmobException e) {
                if (e == null) {
                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    ToastUtils.toast(context, e.getMessage());
                    Log.e(TAG, e.getMessage());
                }
            }
        });

    }


    //查询出文物对应的所有留言，并一并查查出留言的作者
    public void getCommentToColt(String coltID) {
        BmobQuery<Comments> query = new BmobQuery<Comments>();

        Collection collection = new Collection();
        collection.setObjectId(coltID);
        query.addWhereEqualTo("collection", new BmobPointer(collection));
        query.include("author");

        query.findObjects(new FindListener<Comments>() {
            @Override
            public void done(List<Comments> list, BmobException e) {
                if (e == null) {
                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    ToastUtils.toast(context, e.getMessage());
                    Log.e(TAG, e.getMessage());
                }
            }
        });

    }


    //查询出Blog对应的所有留言，并一并查查出留言的作者
    public void getCommentToBlog(String blogID) {
        BmobQuery<Comments> query = new BmobQuery<Comments>();

        Blog blog = new Blog();
        blog.setObjectId(blogID);
        query.addWhereEqualTo("blog", new BmobPointer(blog));
        query.include("author");

        query.findObjects(new FindListener<Comments>() {
            @Override
            public void done(List<Comments> list, BmobException e) {
                if (e == null) {
                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    ToastUtils.toast(context, e.getMessage());
                    Log.e(TAG, e.getMessage());
                }
            }
        });

    }

    //获取用户所有对于藏品的评论
    public void getCommentToColtByUser(String UserID){
        BmobQuery<Comments> query =new BmobQuery<>();
        User user=new User();
        user.setObjectId(BmobUser.getCurrentUser(User.class).getObjectId());

        query.addWhereEqualTo("author",new BmobPointer(user));
        query.addWhereEqualTo("commentType",COMMENT_TO_COLLECTION);
        query.include("collection");
        query.findObjects(new FindListener<Comments>() {
            @Override
            public void done(List<Comments> list, BmobException e) {
                if (e == null) {
                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    ToastUtils.toast(context, e.getMessage());
                    Log.e(TAG, e.getMessage());
                }
            }
        });


    }


    //提交评论或者留言 参数包括评论的主体类型，ID以及评论内容
    //type为blog的需要设置commentNum

    public void postComment(final int commentType, final String ID, String commentSting) {
        BmobQuery<Comments> query = new BmobQuery<Comments>();

        User user = BmobUser.getCurrentUser(User.class);
        Comments comments = new Comments();

        comments.setCommentText(commentSting);
        comments.setAuthor(user);
        switch (commentType) {
            case COMMENT_TO_MUSEUM:
                Museum museum = new Museum();
                museum.setObjectId(ID);
                comments.setMuseum(museum);
                comments.setCommentType(COMMENT_TO_MUSEUM);
                break;
            case COMMENT_TO_EXHIBITION:
                Exhibition exhibition = new Exhibition();
                exhibition.setObjectId(ID);
                comments.setExhibition(exhibition);
                comments.setCommentType(COMMENT_TO_EXHIBITION);
                break;
            case COMMENT_TO_COLLECTION:
                Collection collection = new Collection();
                collection.setObjectId(ID);
                comments.setCollection(collection);
                comments.setCommentType(COMMENT_TO_COLLECTION);
                break;
            case COMMENT_TO_BLOG:
                Blog blog = new Blog();
                blog.setObjectId(ID);
                comments.setBlog(blog);
                comments.setCommentType(COMMENT_TO_BLOG);
                break;
            default:
                break;
        }

        comments.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    onBmobReturnWithObj.onSuccess(null);

                    if(commentType==COMMENT_TO_BLOG){
                        updateBlogCommentCount(ID);

                    }

                    Log.i("bmob", "评论发表成功");
                } else {
                    onBmobReturnWithObj.onFail(null);
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }
        });


    }

    //重新调整评论数量，并存储
    //评论提交后，先查询Blog下的评论数量，返回后存储评论数
    public void updateBlogCommentCount(final String blogID) {
        BmobQuery<Comments> query = new BmobQuery<Comments>();
        Blog blog = new Blog();
        blog.setObjectId(blogID);
        query.addWhereEqualTo("blog", new BmobPointer(blog));

        query.findObjects(new FindListener<Comments>() {
            @Override
            public void done(List<Comments> list, BmobException e) {
                if (e == null) {
                    int count = list.size();
                    Blog blog = new Blog();
                    blog.setCommentNums(count);
                    blog.update(blogID, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.i("bmob", "更新log评论数量成功");
                            } else {
                                Log.i("bmob", "更新Blog评论数量失败：" + e.getMessage() + "," + e.getErrorCode());
                            }
                        }
                    });
                }
            }
        });
    }
}
