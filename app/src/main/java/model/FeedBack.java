package model;

import cn.bmob.v3.BmobObject;

/**
 * Created by wjc on 2017/9/14.
 */

public class FeedBack extends BmobObject {

    private String feedbackString;
    private User author;
    private String contact;


    public FeedBack() {
    }

    public String getFeedbackString() {
        return feedbackString;
    }

    public void setFeedbackString(String feedbackString) {
        this.feedbackString = feedbackString;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
