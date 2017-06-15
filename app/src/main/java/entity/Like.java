package entity;

/**
 *
 * 点赞或者关注
 * 可以针对Blog 可以针对藏品，博物馆 也可以针对用户
 * Created by wjc on 2017/3/15.
 */

public class Like {


    /**
     * 与点赞或者关注 的主体的关联关系
     * 用第三张表
     */
    private Museum museum; //被关注的博物馆
    private Exhibition exhibition;
    private Collection collection;
    private Blog blog;   //被点赞的Blog
    private User user;  //被关注的其他用户




    private User author;//点赞或者关注的作者


    public Like() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
