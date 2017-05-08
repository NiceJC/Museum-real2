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

//    private int exhibitRoomID;//展厅ID

    private Museum toMuseum;//所属博物馆

    private String name;//展厅名称

    private String introduction;//展厅介绍

    private String imageURL;//展厅图片

    private Integer collectionNum;//展出藏品数量






    public ExhibitRoom() {
    }

    public Integer getCollectionNum() {
        return collectionNum;
    }

    public void setCollectionNum(Integer collectionNum) {
        this.collectionNum = collectionNum;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Museum getToMuseum() {
        return toMuseum;
    }

    public void setToMuseum(Museum toMuseum) {
        this.toMuseum = toMuseum;
    }
}
