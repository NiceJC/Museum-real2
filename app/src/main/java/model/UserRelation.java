package model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by wjc on 2017/9/19.
 */

public class UserRelation  extends BmobObject{
    private String UserID;
    private User master;
    private BmobRelation fans;
    private BmobRelation followings;

    public UserRelation() {
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public User getMaster() {
        return master;
    }

    public void setMaster(User master) {
        this.master = master;
    }

    public BmobRelation getFans() {
        return fans;
    }

    public void setFans(BmobRelation fans) {
        this.fans = fans;
    }

    public BmobRelation getFollowings() {
        return followings;
    }

    public void setFollowings(BmobRelation followings) {
        this.followings = followings;
    }
}
