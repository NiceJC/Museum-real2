package entity;

import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 *
 *
 * 社区Blog的基本信息类
 * 一般来说，在社区主页展示
 * 排序的关键词是 创建时间
 * 在关注列表的，经过关注用户Author属性筛选后按照时间排序
 * Created by wjc on 2017/2/20.
 */

public class Blog extends BmobObject implements Serializable  {


     //关联关系  这一部分是与Bmob解不开了。。

    private User Author; //作者
//    private List<Comments> commentsList; //下方的评论

//    private Intent userID; //所属用户的ID
//    private int blogID; //博客的ID
//    private Integer commentsID;//所拥有的评论的 belongId

//    private String  iconURL; //  用户头像
//    private String userName; //用户昵称
//    private String time; //发布时间




    private String contentText;//内容文本
//    private Boolean isWatched;//是否当前用户已经关注
//    private Boolean isPraised;//是否已经点赞
    private Integer commentNums;//评论的数量
    private Integer praiseNums;//点赞的数量

    private BmobRelation likedUsers; //点赞过的用户



    private List<String> imageURLs;//发表的图片

    private Boolean isLiked;
    private Integer likeNum;

    public Blog() {
    }


    public User getAuthor() {
        return Author;
    }

    public void setAuthor(User author) {
        Author = author;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public Integer getCommentNums() {
        return commentNums;
    }

    public void setCommentNums(Integer commentNums) {
        this.commentNums = commentNums;
    }

    public Integer getPraiseNums() {
        return praiseNums;
    }

    public void setPraiseNums(Integer praiseNums) {
        this.praiseNums = praiseNums;
    }

    public List<String> getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(List<String> imageURLs) {
        this.imageURLs = imageURLs;
    }

    public BmobRelation getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(BmobRelation likedUsers) {
        this.likedUsers = likedUsers;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }
}

