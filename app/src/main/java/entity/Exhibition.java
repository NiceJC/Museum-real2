package entity;

import android.content.Intent;
import android.content.res.ColorStateList;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 *
 * 展览（主题展）的实体类
 * Created by wjc on 2017/3/2.
 */

public class Exhibition extends BmobObject {


    /**
     * 以下几项是需要在首页第一时间展示的
     */
    private int exhibitionID; //展览ID

    private String exhibitName; //展览名称

    private List<String> imageURLs; //展览的图片

    private String museumName; //博物馆名称

    private double latitude; //纬度    这里的经纬度和城市 也就是博物馆的

    private double longitude; //经度

    private String locateCity; //所在城市


    /**
     * 以下几项可以在点击之后进一步查询返回
     */
    private int museumID; //博物馆id

    private String exhibitIntru; //展览介绍

    private String time; //展览时间

    private List<Integer> collections; //参加本展览的藏品ID

    private  String Cost; //花费

    private int likeNum; //感兴趣的人数







    public Exhibition() {
    }

    public Exhibition(int exhibitionID, String exhibitName, List<String> imageURLs, String museumName, double latitude, double longitude, String locateCity, int museumID, String exhibitIntru, String time, List<Integer> collections, String cost, int likeNum) {
        this.exhibitionID = exhibitionID;
        this.exhibitName = exhibitName;
        this.imageURLs = imageURLs;
        this.museumName = museumName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locateCity = locateCity;
        this.museumID = museumID;
        this.exhibitIntru = exhibitIntru;
        this.time = time;
        this.collections = collections;
        Cost = cost;
        this.likeNum = likeNum;
    }


    @Override
    public String toString() {
        return "Exhibition{" +
                "exhibitionID=" + exhibitionID +
                ", exhibitName='" + exhibitName + '\'' +
                ", imageURLs=" + imageURLs +
                ", museumName='" + museumName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", locateCity='" + locateCity + '\'' +
                ", museumID=" + museumID +
                ", exhibitIntru='" + exhibitIntru + '\'' +
                ", time='" + time + '\'' +
                ", collections=" + collections +
                ", Cost='" + Cost + '\'' +
                ", likeNum=" + likeNum +
                '}';
    }

    public int getExhibitionID() {
        return exhibitionID;
    }

    public void setExhibitionID(int exhibitionID) {
        this.exhibitionID = exhibitionID;
    }

    public String getExhibitName() {
        return exhibitName;
    }

    public void setExhibitName(String exhibitName) {
        this.exhibitName = exhibitName;
    }

    public List<String> getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(List<String> imageURLs) {
        this.imageURLs = imageURLs;
    }

    public String getMuseumName() {
        return museumName;
    }

    public void setMuseumName(String museumName) {
        this.museumName = museumName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocateCity() {
        return locateCity;
    }

    public void setLocateCity(String locateCity) {
        this.locateCity = locateCity;
    }

    public int getMuseumID() {
        return museumID;
    }

    public void setMuseumID(int museumID) {
        this.museumID = museumID;
    }

    public String getExhibitIntru() {
        return exhibitIntru;
    }

    public void setExhibitIntru(String exhibitIntru) {
        this.exhibitIntru = exhibitIntru;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Integer> getCollections() {
        return collections;
    }

    public void setCollections(List<Integer> collections) {
        this.collections = collections;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }
}
