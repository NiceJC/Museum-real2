package entity;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 *
 * 藏品的实体类
 * Created by wjc on 2017/2/21.
 */

public class Collection extends BmobObject implements Serializable {

    public static final int TYPE_BRONSE=1; //青铜器
    public static final int TYPE_CHINA=2; //瓷器
    public static final int TYPE_JADE=3; //玉石器
    public static final int TYPE_LACQUER=4; //漆器
    public static final int TYPE_PAINT=5; //书画类
    public static final int TYPE_OTHERS=6; //其他类
    
    private int coltID; //藏品Id

    private String coltName;  //藏品名称

    private String coltSize; // 藏品尺寸说明

    private String coltDynasty; // 藏品朝代

    private String coltIntru; //藏品介绍

    private  int coltSort; // 藏品分类

    private int coltToMuseumId; // 藏品所在博物馆ID

    private String coltToMuseumName; //藏品所在博物馆名称

    private String coltShowRoom; // 藏品展厅

    private int coltLikeNum; //藏品的点赞数量   new

    private int coltCommentBolongID; //评论的ID

    private int coltCommentNum; //评论的数量

    private List<String> coltImageURLs; //藏品图片




    public Collection() {
    }

    public Collection(int coltID, String coltName, String coltSize, String coltDynasty, String coltIntru, int coltSort, int coltToMuseumId, String coltToMuseumName, String coltShowRoom, int coltLikeNum, List<String> coltImageURLs) {
        this.coltID = coltID;
        this.coltName = coltName;
        this.coltSize = coltSize;
        this.coltDynasty = coltDynasty;
        this.coltIntru = coltIntru;
        this.coltSort = coltSort;
        this.coltToMuseumId = coltToMuseumId;
        this.coltToMuseumName = coltToMuseumName;
        this.coltShowRoom = coltShowRoom;
        this.coltLikeNum = coltLikeNum;
        this.coltImageURLs = coltImageURLs;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "coltID=" + coltID +
                ", coltName='" + coltName + '\'' +
                ", coltSize='" + coltSize + '\'' +
                ", coltDynasty='" + coltDynasty + '\'' +
                ", coltIntru='" + coltIntru + '\'' +
                ", coltSort=" + coltSort +
                ", coltToMuseumId=" + coltToMuseumId +
                ", coltToMuseumName='" + coltToMuseumName + '\'' +
                ", coltShowRoom='" + coltShowRoom + '\'' +
                ", coltLikeNum=" + coltLikeNum +
                ", coltImageURLs=" + coltImageURLs +
                '}';
    }

    public int getColtID() {
        return coltID;
    }

    public void setColtID(int coltID) {
        this.coltID = coltID;
    }

    public String getColtName() {
        return coltName;
    }

    public void setColtName(String coltName) {
        this.coltName = coltName;
    }

    public String getColtSize() {
        return coltSize;
    }

    public void setColtSize(String coltSize) {
        this.coltSize = coltSize;
    }

    public String getColtDynasty() {
        return coltDynasty;
    }

    public void setColtDynasty(String coltDynasty) {
        this.coltDynasty = coltDynasty;
    }

    public String getColtIntru() {
        return coltIntru;
    }

    public void setColtIntru(String coltIntru) {
        this.coltIntru = coltIntru;
    }

    public int getColtSort() {
        return coltSort;
    }

    public void setColtSort(int coltSort) {
        this.coltSort = coltSort;
    }

    public int getColtToMuseumId() {
        return coltToMuseumId;
    }

    public void setColtToMuseumId(int coltToMuseumId) {
        this.coltToMuseumId = coltToMuseumId;
    }

    public String getColtToMuseumName() {
        return coltToMuseumName;
    }

    public void setColtToMuseumName(String coltToMuseumName) {
        this.coltToMuseumName = coltToMuseumName;
    }

    public String getColtShowRoom() {
        return coltShowRoom;
    }

    public void setColtShowRoom(String coltShowRoom) {
        this.coltShowRoom = coltShowRoom;
    }

    public int getColtLikeNum() {
        return coltLikeNum;
    }

    public void setColtLikeNum(int coltLikeNum) {
        this.coltLikeNum = coltLikeNum;
    }

    public List<String> getColtImageURLs() {
        return coltImageURLs;
    }

    public void setColtImageURLs(List<String> coltImageURLs) {
        this.coltImageURLs = coltImageURLs;
    }

    public int getColtCommentBolongID() {
        return coltCommentBolongID;
    }

    public void setColtCommentBolongID(int coltCommentBolongID) {
        this.coltCommentBolongID = coltCommentBolongID;
    }

    public int getColtCommentNum() {
        return coltCommentNum;
    }

    public void setColtCommentNum(int coltCommentNum) {
        this.coltCommentNum = coltCommentNum;
    }
}
