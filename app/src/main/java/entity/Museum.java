package entity;

import android.content.Intent;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobRelation;

/**
 *
 * 博物馆的实体类
 * Created by wjc on 2017/2/21.
 */

public class Museum extends BmobObject {



//    private int museumId; //博物馆ID

    private String museumName;  //名称

    private String museumInfo;  //介绍

    private String museumAddress; //详细地址

    private String locateCity; //所在城市


    private BmobGeoPoint geoPoint; //经纬度


    private String opening_time;  //开馆时间

    private String cost;  //费用

    private Integer watchNums; //关注人数

    private BmobRelation watchedUsers; //关注该博物馆的人

    private Boolean isWatched; //是否已经关注

    private List<String> imageURLs;  //图片URL  注：博物馆的第一张大图必须是裁成正方形的，不然适配效果很差

    private String iconURL; //头像小图标的URL

    private Integer hotValue; //热力值

    private Integer commentNum; //评论数量






    public Museum() {

    }

    public String getMuseumName() {
        return museumName;
    }

    public void setMuseumName(String museumName) {
        this.museumName = museumName;
    }

    public String getMuseumInfo() {
        return museumInfo;
    }

    public void setMuseumInfo(String museumInfo) {
        this.museumInfo = museumInfo;
    }

    public String getMuseumAddress() {
        return museumAddress;
    }

    public void setMuseumAddress(String museumAddress) {
        this.museumAddress = museumAddress;
    }

    public String getLocateCity() {
        return locateCity;
    }

    public void setLocateCity(String locateCity) {
        this.locateCity = locateCity;
    }

    public BmobGeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(BmobGeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public String getOpening_time() {
        return opening_time;
    }

    public void setOpening_time(String opening_time) {
        this.opening_time = opening_time;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Integer getWatchNums() {
        return watchNums;
    }

    public void setWatchNums(Integer watchNums) {
        this.watchNums = watchNums;
    }

    public Boolean getWatched() {
        return isWatched;
    }

    public void setWatched(Boolean watched) {
        isWatched = watched;
    }

    public List<String> getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(List<String> imageURLs) {
        this.imageURLs = imageURLs;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public Integer getHotValue() {
        return hotValue;
    }

    public void setHotValue(Integer hotValue) {
        this.hotValue = hotValue;
    }

    public BmobRelation getWatchedUsers() {
        return watchedUsers;
    }

    public void setWatchedUsers(BmobRelation watchedUsers) {
        this.watchedUsers = watchedUsers;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }
}
