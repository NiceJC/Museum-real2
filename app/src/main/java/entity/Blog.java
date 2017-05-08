package entity;

import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

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
    private int blogID; //博客的ID
    private int commentsID;//所拥有的评论的 belongId

    private String iconURL; //  用户头像
    private String userName; //用户昵称
    private String time; //发布时间
    private String contentText;//内容文本
    private Boolean isWatched;//是否当前用户已经关注
    private Boolean isPraised;//是否已经点赞
    private Integer commentNums;//评论的数量
    private Integer praiseNums;//点赞的数量
    private List<String> imageURLs;//发表的图片


    public Blog() {
    }



    public Blog(String iconURL, String userName, String time, String contentText, boolean isWatched, boolean isPraised, int commentNums, int praiseNums, ArrayList<String> imageURLs) {
        this.iconURL = iconURL;
        this.userName = userName;
        this.time = time;
        this.contentText = contentText;
        this.isWatched = isWatched;
        this.isPraised = isPraised;
        this.commentNums = commentNums;
        this.praiseNums = praiseNums;
        this.imageURLs = imageURLs;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "iconURL='" + iconURL + '\'' +
                ", userName='" + userName + '\'' +
                ", time='" + time + '\'' +
                ", contentText='" + contentText + '\'' +
                ", isWatched=" + isWatched +
                ", isPraised=" + isPraised +
                ", commentNums=" + commentNums +
                ", praiseNums=" + praiseNums +
                ", imageURLs=" + imageURLs +
                '}';
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }

    public boolean isPraised() {
        return isPraised;
    }

    public void setPraised(boolean praised) {
        isPraised = praised;
    }

    public int getCommentNums() {
        return commentNums;
    }

    public void setCommentNums(int commentNums) {
        this.commentNums = commentNums;
    }

    public int getPraiseNums() {
        return praiseNums;
    }

    public void setPraiseNums(int praiseNums) {
        this.praiseNums = praiseNums;
    }

    public User getAuthor() {
        return Author;
    }

    public void setAuthor(User author) {
        Author = author;
    }

    public int getBlogID() {
        return blogID;
    }

    public void setBlogID(int blogID) {
        this.blogID = blogID;
    }

    public int getCommentsID() {
        return commentsID;
    }

    public void setCommentsID(int commentsID) {
        this.commentsID = commentsID;
    }

    public List<String> getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(List<String> imageURLs) {
        this.imageURLs = imageURLs;
    }
}

