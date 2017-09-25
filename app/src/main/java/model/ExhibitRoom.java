package model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

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

    private BmobFile image1;//展厅图片
//    private String imageURL2;//展厅图片
//    private String imageURL3;//展厅图片
//    private String imageURL4;//展厅图片


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


    public Museum getToMuseum() {
        return toMuseum;
    }

    public void setToMuseum(Museum toMuseum) {
        this.toMuseum = toMuseum;
    }

    public BmobFile getImage1() {
        return image1;
    }

    public void setImage1(BmobFile image1) {
        this.image1 = image1;
    }
}

