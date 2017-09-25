package model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 *
 * 用户的实体类
 * Created by wjc on 2017/2/28.
 *
 */

public class User extends BmobUser {

//
//    public static Integer REGISTERTYPE_PHONE=1;
//    public static Integer REGISTERTYPE_EMAIL=2;
//    public static Integer REGISTERTYPE_WECHAT=3;
//    public static Integer REGISTERTYPE_QQ=4;


    private BmobRelation likeCollections; //喜欢的藏品

    private BmobRelation watchMuseums; //关注的博物馆

    private BmobRelation watchExhibitions; //关注的主题展览

//    private BmobRelation watchUsers; //关注的其他用户
//
    private Integer watchUserNum;  //关注的其他用户的数量

    private BmobRelation likeBlogs; //点赞的Blog

//    private BmobRelation fans; //粉丝（被关注）
//
    private Integer fansNum; //粉丝的数量


    private String userRelationID;

    private String nickName;  //用户昵称

    private Integer registerType; //注册的种类（手机注册或者QQ微信关联）

    private Boolean isMan; //性别


    private String age;

    private String portraitURL; //头像URL；

    private String shortIntroducon; //简短的一句话介绍


    public User() {
    }



    public BmobRelation getLikeCollections() {
        return likeCollections;
    }

    public void setLikeCollections(BmobRelation likeCollections) {
        this.likeCollections = likeCollections;
    }

    public BmobRelation getWatchMuseums() {
        return watchMuseums;
    }

    public void setWatchMuseums(BmobRelation watchMuseums) {
        this.watchMuseums = watchMuseums;
    }

//    public BmobRelation getWatchUsers() {
//        return watchUsers;
//    }
//
//    public void setWatchUsers(BmobRelation watchUsers) {
//        this.watchUsers = watchUsers;
//    }
//
//    public BmobRelation getFans() {
//        return fans;
//    }
//
//    public void setFans(BmobRelation fans) {
//        this.fans = fans;
//    }

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

    public String getPortraitURL() {
        return portraitURL;
    }

    public void setPortraitURL(String portraitURL) {
        this.portraitURL = portraitURL;
    }

    public BmobRelation getLikeBlogs() {
        return likeBlogs;
    }

    public void setLikeBlogs(BmobRelation likeBlogs) {
        this.likeBlogs = likeBlogs;
    }

    public BmobRelation getWatchExhibitions() {
        return watchExhibitions;
    }

    public void setWatchExhibitions(BmobRelation watchExhibitions) {
        this.watchExhibitions = watchExhibitions;
    }


    public Integer getWatchUserNum() {
        return watchUserNum;
    }

    public void setWatchUserNum(Integer watchUserNum) {
        this.watchUserNum = watchUserNum;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public String getUserRelationID() {
        return userRelationID;
    }

    public void setUserRelationID(String userRelationID) {
        this.userRelationID = userRelationID;
    }

    public String getShortIntroducon() {
        return shortIntroducon;
    }

    public void setShortIntroducon(String shortIntroducon) {
        this.shortIntroducon = shortIntroducon;
    }
}
