package entity;

import android.content.Intent;
import android.content.res.ColorStateList;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 *
 * 展览（主题展）的实体类
 * Created by wjc on 2017/3/2.
 */

public class Exhibition extends BmobObject {



//    private int exhibitionID; //展览ID

    private String exhibitName; //展览名称

//    private List<String> imageURLs;

    private BmobFile image1; //展览的图片


    private Museum toMuseum; //所属的博物馆

//    private String museumName; //博物馆名称
//
//    private double latitude; //纬度    这里的经纬度和城市 也就是博物馆的
//
//    private double longitude; //经度
//
//    private String locateCity; //所在城市



//    private int museumID; //博物馆id

    private String exhibitIntru; //展览介绍

    private String time; //展览时间

//    private List<Integer> collections; //参加本展览的藏品ID

    private  String Cost; //花费

    private Integer coltNum; //参加主体展览的展品数量

    private Integer likeNum; //感兴趣的人数

    private Integer hotValue; //热力值







    public Exhibition() {
    }

    public String getExhibitName() {
        return exhibitName;
    }

    public void setExhibitName(String exhibitName) {
        this.exhibitName = exhibitName;
    }

    public BmobFile getImage1() {
        return image1;
    }

    public void setImage1(BmobFile image1) {
        this.image1 = image1;
    }

    public Museum getToMuseum() {
        return toMuseum;
    }

    public void setToMuseum(Museum toMuseum) {
        this.toMuseum = toMuseum;
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

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public Integer getColtNum() {
        return coltNum;
    }

    public void setColtNum(Integer coltNum) {
        this.coltNum = coltNum;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getHotValue() {
        return hotValue;
    }

    public void setHotValue(Integer hotValue) {
        this.hotValue = hotValue;
    }
}
