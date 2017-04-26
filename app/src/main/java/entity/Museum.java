package entity;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 *
 * 博物馆的实体类
 * Created by wjc on 2017/2/21.
 */

public class Museum extends BmobObject {



    private int museumId; //博物馆ID

    private String museumName;  //名称

    private String museumInfo;  //介绍

    private String museumAddress; //详细地址

    private String locateCity; //所在城市

    private double latitude; //纬度

    private double longitude; //经度


    private String opening_time;  //开馆时间

    private String cost;  //费用

    private int watchNums; //关注人数

    private boolean isWatched; //是否已经关注

    private List<String> imageURLs;  //图片URL  注：博物馆的第一张大图必须是裁成正方形的，不然适配效果很差

    private String iconURL; //头像小图标的URL







    public Museum() {

    }

    public Museum(int museumId, String museumName, String museumInfo, String museumAddress, String opening_time, String cost, int watchNums, List<String> imageURLs) {
        this.museumId = museumId;
        this.museumName = museumName;
        this.museumInfo = museumInfo;
        this.museumAddress = museumAddress;
        this.opening_time = opening_time;
        this.cost = cost;
        this.watchNums = watchNums;
        this.imageURLs = imageURLs;
    }


    @Override
    public String toString() {
        return "Museum{" +
                "museumId=" + museumId +
                ", museumName='" + museumName + '\'' +
                ", museumInfo='" + museumInfo + '\'' +
                ", museumAddress='" + museumAddress + '\'' +
                ", opening_time='" + opening_time + '\'' +
                ", cost='" + cost + '\'' +
                ", watchNums=" + watchNums +
                ", imageURLs=" + imageURLs +
                '}';
    }

    public int getMuseumId() {
        return museumId;
    }

    public void setMuseumId(int museumId) {
        this.museumId = museumId;
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

    public int getWatchNums() {
        return watchNums;
    }

    public void setWatchNums(int watchNums) {
        this.watchNums = watchNums;
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

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }
}
