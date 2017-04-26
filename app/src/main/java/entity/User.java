package entity;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 *
 * 用户的实体类
 * Created by wjc on 2017/2/28.
 *
 */

public class User extends BmobUser {


    public static Integer REGISTERTYPE_PHONE=1;
    public static Integer REGISTERTYPE_EMAIL=2;
    public static Integer REGISTERTYPE_WECHAT=3;
    public static Integer REGISTERTYPE_QQ=4;


    private List<Integer> likeCollections; //喜欢的藏品的ID

    private List<Integer> watchMuseums; //关注的博物馆的ID

    private List<Integer> postBlogs; //发表的状态的ID

    private List<Integer> watchUsers; //关注的其他用户

    private List<Integer> fans; //粉丝



    private String nickName;  //用户昵称

    private Integer registerType; //注册的种类（手机注册或者QQ微信关联）

    private Boolean isMan; //性别


    private String age;

    private String portraitURL; //头像URL；



    public User() {
    }


    public User(List<Integer> likeCollections, List<Integer> watchMuseums, List<Integer> postBlogs, List<Integer> watchUsers, List<Integer> fans, String nickName, boolean isMan, String phoneNum) {
        this.likeCollections = likeCollections;
        this.watchMuseums = watchMuseums;
        this.postBlogs = postBlogs;
        this.watchUsers = watchUsers;
        this.fans = fans;
        this.nickName = nickName;
        this.isMan = isMan;

    }

    @Override
    public String toString() {
        return "User{" +
                "likeCollections=" + likeCollections +
                ", watchMuseums=" + watchMuseums +
                ", postBlogs=" + postBlogs +
                ", watchUsers=" + watchUsers +
                ", fans=" + fans +
                ", nickName='" + nickName + '\'' +
                ", registerType=" + registerType +
                ", isMan=" + isMan +

                ", portraitURL='" + portraitURL + '\'' +
                '}';
    }

    public List<Integer> getLikeCollections() {
        return likeCollections;
    }

    public void setLikeCollections(List<Integer> likeCollections) {
        this.likeCollections = likeCollections;
    }

    public List<Integer> getWatchMuseums() {
        return watchMuseums;
    }

    public void setWatchMuseums(List<Integer> watchMuseums) {
        this.watchMuseums = watchMuseums;
    }

    public List<Integer> getPostBlogs() {
        return postBlogs;
    }

    public void setPostBlogs(List<Integer> postBlogs) {
        this.postBlogs = postBlogs;
    }

    public List<Integer> getWatchUsers() {
        return watchUsers;
    }

    public void setWatchUsers(List<Integer> watchUsers) {
        this.watchUsers = watchUsers;
    }

    public List<Integer> getFans() {
        return fans;
    }

    public void setFans(List<Integer> fans) {
        this.fans = fans;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public Integer getRegisterType() {
        return registerType;
    }

    public void setRegisterType(Integer registerType) {
        this.registerType = registerType;
    }

    public String getPortraitURL() {
        return portraitURL;
    }

    public void setPortraitURL(String portraitURL) {
        this.portraitURL = portraitURL;
    }

    public Boolean getMan() {
        return isMan;
    }

    public void setMan(Boolean man) {
        isMan = man;
    }



    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
