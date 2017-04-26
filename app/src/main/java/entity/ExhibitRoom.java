package entity;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 *
 *
 * 展厅的实体类
 * 每个博物馆包含若干展厅
 *
 *
 * Created by wjc on 2017/3/6.
 */

public class ExhibitRoom extends BmobObject {

    private int exhibitRoomID;//展厅ID

    private int museumID;//所属博物馆ID

    private String name;//展厅名称

    private String introduction;//展厅介绍

    private String imageURL;//展厅图片

    private int collectionNum;//展出藏品数量

    private List<Integer> coltIDs;//展出藏品ID




    public ExhibitRoom() {
    }

    public ExhibitRoom(int exhibitRoomID, int museumID, String name, String introduction, int collectionNum, List<Integer> coltIDs) {
        this.exhibitRoomID = exhibitRoomID;
        this.museumID = museumID;
        this.name = name;
        this.introduction = introduction;
        this.collectionNum = collectionNum;
        this.coltIDs = coltIDs;
    }

    @Override
    public String toString() {
        return "ExhibitRoom{" +
                "exhibitRoomID=" + exhibitRoomID +
                ", museumID=" + museumID +
                ", name='" + name + '\'' +
                ", introduction='" + introduction + '\'' +
                ", collectionNum=" + collectionNum +
                ", coltIDs=" + coltIDs +
                '}';
    }

    public int getExhibitRoomID() {
        return exhibitRoomID;
    }

    public void setExhibitRoomID(int exhibitRoomID) {
        this.exhibitRoomID = exhibitRoomID;
    }

    public int getMuseumID() {
        return museumID;
    }

    public void setMuseumID(int museumID) {
        this.museumID = museumID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(int collectionNum) {
        this.collectionNum = collectionNum;
    }

    public List<Integer> getColtIDs() {
        return coltIDs;
    }

    public void setColtIDs(List<Integer> coltIDs) {
        this.coltIDs = coltIDs;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
