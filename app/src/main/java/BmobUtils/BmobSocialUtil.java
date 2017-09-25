package bmobUtils;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
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
import model.Blog;
import model.Comments;
import model.User;
import interfaces.OnBmobReturnWithObj;

import static util.ParameterBase.LIMIT_SIX;
import static util.ParameterBase.LIMIT_TEN;

/**
 * Created by wjc on 2017/4/27.
 */

public class BmobSocialUtil {

    private Context context;
    private String TAG;
    private static BmobSocialUtil instance1 = null;
    private static BmobSocialUtil instance2 = null;


    private OnBmobReturnWithObj onBmobReturnWithObj;

    public void setOnBmobReturnWithObj(OnBmobReturnWithObj onBmobReturnWithObj) {
        this.onBmobReturnWithObj = onBmobReturnWithObj;
    }


    private BmobSocialUtil(Context context) {
        this.context = context;
        TAG = "bmob blog";

    }

    //单例 1
    public static BmobSocialUtil getInstance(Context context) {

        if (instance1 == null) {
            instance1 = new BmobSocialUtil(context);

        }

        return instance1;
    }


    //单例 2
    public static BmobSocialUtil getInstance2(Context context) {

        if (instance2 == null) {
            instance2 = new BmobSocialUtil(context);

        }

        return instance2;
    }



    /**
     * 上传Blog
     * ，参数包括 文字和图片的 包含url的List
     * 关联本地用户作为作者
     */
    public void postBlog(User author,String textContent, List<String> urls) {

        Blog blog = new Blog();
        blog.setContentText(textContent);
        blog.setImageURLs(urls);
        blog.setCommentNums(0);
        blog.setLikeNum(0);
        blog.setAuthorID(author.getObjectId());
        blog.setAuthor(author);
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
    public void deleteBlog(String blogID) {
        Blog blog = new Blog();
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
     */
    public void deleteComment(String commentID) {

        Comments comments = new Comments();
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

    public void commentBlog(String blogID, String commentText) {
        User user = BmobUser.getCurrentUser(User.class);
        Blog blog = new Blog();
        blog.setObjectId(blogID);

        Comments comments = new Comments();

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
     * 点赞Blog,
     * 并且为保证likeNum的准确性，
     * 在更新完多对多关联字段后，统计并更新likeNum的值
     * 先查询Blog的likeUser列表，
     */
    public void likeBlog(final String blogID) {
        User user = BmobUser.getCurrentUser(User.class);
        Blog blog = new Blog();
        blog.setObjectId(blogID);

        BmobRelation relation = new BmobRelation();
        relation.add(blog);
        user.setLikeBlogs(relation);

        BmobRelation relation1 = new BmobRelation();
        relation1.add(user);
        blog.setLikedUsers(relation1);

        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });

        blog.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Blog blog = new Blog();
                    blog.setObjectId(blogID);

                    BmobQuery<User> query = new BmobQuery<User>();
                    query.addWhereRelatedTo("likedUsers", new BmobPointer(blog));

                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null) {
                                onBmobReturnWithObj.onSuccess(list.size());

                                Blog blog = new Blog();


                                blog.setLikeNum(list.size());
                                blog.update(blogID, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {

                                    }
                                });
                            } else {
                                onBmobReturnWithObj.onFail(null);
                                Log.i("bmob", "失败：" + e.getMessage());
                            }
                        }
                    });


                } else {
                    onBmobReturnWithObj.onFail(null);
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }
        });

    }

    /**
     * 取消点赞Blog
     */
    public void cancelLikeBlog(final String blogID) {
        User user = BmobUser.getCurrentUser(User.class);
        Blog blog = new Blog();
        blog.setObjectId(blogID);

        BmobRelation relation = new BmobRelation();
        relation.remove(blog);
        user.setLikeBlogs(relation);

        BmobRelation relation1 = new BmobRelation();
        relation1.remove(user);
        blog.setLikedUsers(relation1);

        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });

        blog.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Blog blog = new Blog();
                    blog.setObjectId(blogID);

                    BmobQuery<User> query = new BmobQuery<User>();
                    query.addWhereRelatedTo("likedUsers", new BmobPointer(blog));

                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null) {
                                onBmobReturnWithObj.onSuccess(list.size());
                                Blog blog = new Blog();


                                blog.setLikeNum(list.size());
                                blog.update(blogID, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {

                                    }
                                });
                            } else {
                                onBmobReturnWithObj.onFail(null);
                                Log.e("bmob", "查询点赞人失败：" + e.getMessage());
                            }
                        }
                    });


                } else {
                    onBmobReturnWithObj.onFail(null);
                    Log.e("bmob", "更新点赞失败：" + e.getMessage());
                }
            }
        });

    }



    /**
     * 根据ID获取单个Blog
     */
    public void getBlogByID(String blogID) {

        BmobQuery<Blog> query = new BmobQuery<Blog>();
        query.include("Author");

        query.getObject(blogID, new QueryListener<Blog>() {
            @Override
            public void done(Blog blog, BmobException e) {
                if (e == null) {

                    onBmobReturnWithObj.onSuccess(blog);
                } else {
                    onBmobReturnWithObj.onFail(null);

                    Log.i("bmob", "获取单个Blog失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }

    //获取给某个Blog点赞的所有用户
    public void getBlogLikesByID(String blogID) {
        BmobQuery<User> query = new BmobQuery<User>();
        Blog blog = new Blog();
        blog.setObjectId(blogID);
        query.addWhereRelatedTo("likedUsers", new BmobPointer(blog));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    onBmobReturnWithObj.onSuccess(list);
                    Log.i("bmob", "查询个数：" + list.size());
                } else {
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }
        });
    }


    //获取某个用户点赞的所有Blog
    public void getlikedBlogsByUser(String userID) {
        BmobQuery<Blog> query = new BmobQuery<Blog>();
        User user = new User();
        user.setObjectId(userID);
        query.addWhereRelatedTo("likeBlogs", new BmobPointer(user));
        query.findObjects(new FindListener<Blog>() {
            @Override
            public void done(List<Blog> list, BmobException e) {
                if (e == null) {
                    onBmobReturnWithObj.onSuccess(list);
                    Log.i("bmob", "查询个数：" + list.size());
                } else {
                    onBmobReturnWithObj.onSuccess(null);
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }
        });
    }

    /**
     * 获取某个用户post的所有Blog
     */

    public void getBlogByAuthor(String userID) {
        BmobQuery<Blog> query = new BmobQuery<Blog>();
        User user = new User();
        user.setObjectId(userID);
        query.addWhereEqualTo("Author", user);
        query.findObjects(new FindListener<Blog>() {
            @Override
            public void done(List<Blog> list, BmobException e) {
                if (e == null) {
                    onBmobReturnWithObj.onSuccess(list);
                    Log.i("bmob", "查询个数：" + list.size());
                } else {
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }
        });


    }


    /**
     * 获取最新鲜的Blog
     */
    public void getRecentBlog(int curPage) {
        BmobQuery<Blog> query = new BmobQuery<Blog>();
        query.setLimit(LIMIT_TEN);
        query.include("Author");
        query.order("-createdAt");
        query.setSkip(LIMIT_TEN * curPage);

        query.findObjects(new FindListener<Blog>() {
            @Override
            public void done(List<Blog> list, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "获取" + list.size() + "条Blog");
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
     * String[] names = {"Barbie", "Joe", "Julia"};
     query.addWhereContainedIn("playerName", Arrays.asList(names));
     */
    public void geWatchedBlog(int curPage,List<String> watchedUsersID) {


        BmobQuery<Blog> query = new BmobQuery<Blog>();

        query.addWhereContainedIn("authorID",watchedUsersID);

        query.include("Author");
        query.setLimit(LIMIT_SIX);
        query.order("-createdAt");
        query.setSkip(LIMIT_SIX * curPage);
        query.findObjects(new FindListener<Blog>() {
            @Override
            public void done(List<Blog> list, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "获取" + list.size() + "条Blog");
                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    Log.i("bmob", "获取blog失败：" + e.getMessage());
                    onBmobReturnWithObj.onFail(e.getMessage());
                }
            }
        });
    }


    /**
     * 获取某个人的基本信息
     */

    public void getUserInfoByID(String userID){
        BmobQuery<User> query=new BmobQuery<User>();
        query.getObject(userID, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {

                if(e==null){
                    if (e == null) {

                        onBmobReturnWithObj.onSuccess(user);
                    } else {

                        onBmobReturnWithObj.onFail(e.getMessage());
                    }
                }

            }
        });




    }

}