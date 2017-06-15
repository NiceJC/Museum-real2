package entity;

import cn.bmob.v3.BmobObject;

/**
 * 评论
 * 可以针对博物馆  展览  藏品  Blog
 * 查询评论时  结合评论的针对类别以及所属 博物馆 or 展览 or 藏品 or Blog 的ID
 * <p>
 * Created by wjc on 2017/3/15.
 */

public class Comments extends BmobObject {

    public static final int COMMENT_TO_MUSEUM = 1;  //针对博物馆
    public static final int COMMENT_TO_EXHIBITION = 2;//针对展览
    public static final int COMMENT_TO_COLLECTION = 3;//针对藏品
    public static final int COMMENT_TO_BLOG = 4;//针对发表的状态


    //用于从Intent中获取评论类型，主体ID的 key
    public static final String COMMENT_TYPE="type"; //评论的针对类型
    public static final String COMMENT_BELONG_ID="belongID" ;//主体的ID

    /**
     * 与评论主体的关联关系
     */
    private Museum museum;
    private Exhibition exhibition;
    private Collection collection;
    private Blog blog;
    private String commentText; //评论正文
    private User author;//评论的作者

//    private long commentTime; //评论时间


//    private int authorID;//评论作者ID；
//    private String authorName;//作者昵称
//    private String authorIconURL;//作者头像URL


    public Comments() {
    }







    public Museum getMuseum() {
        return museum;
    }

    public void setMuseum(Museum museum) {
        this.museum = museum;
    }

    public Exhibition getExhibition() {
        return exhibition;
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
    //
//
//    public Comments(int commentID, int belongID, int belongType, String commentText, long commentTime) {
//        this.commentID = commentID;
//        this.belongID = belongID;
//        this.belongType = belongType;
//        this.commentText = commentText;
//        this.commentTime = commentTime;
//    }
//
//    @Override
//    public String toString() {
//        return "Comments{" +
//                "commentID=" + commentID +
//                ", belongID=" + belongID +
//                ", belongType=" + belongType +
//                ", commentText='" + commentText + '\'' +
//                ", commentTime=" + commentTime +
//                '}';
//    }
//
//    public int getCommentID() {
//        return commentID;
//    }
//
//    public void setCommentID(int commentID) {
//        this.commentID = commentID;
//    }
//
//    public int getBelongID() {
//        return belongID;
//    }
//
//    public void setBelongID(int belongID) {
//        this.belongID = belongID;
//    }
//
//    public int getBelongType() {
//        return belongType;
//    }
//
//    public void setBelongType(int belongType) {
//        this.belongType = belongType;
//    }
//
//    public String getCommentText() {
//        return commentText;
//    }
//
//    public void setCommentText(String commentText) {
//        this.commentText = commentText;
//    }
//
//    public long getCommentTime() {
//        return commentTime;
//    }
//
//    public void setCommentTime(long commentTime) {
//        this.commentTime = commentTime;
//    }
//
//    public int getAuthorID() {
//        return authorID;
//    }
//
//    public void setAuthorID(int authorID) {
//        this.authorID = authorID;
//    }
//
//    public String getAuthorName() {
//        return authorName;
//    }
//
//    public void setAuthorName(String authorName) {
//        this.authorName = authorName;
//    }
//
//    public String getAuthorIconURL() {
//        return authorIconURL;
//    }
//
//    public void setAuthorIconURL(String authorIconURL) {
//        this.authorIconURL = authorIconURL;
//    }
}
