package biz;

import interfaces.OnBmobReturnWithObj;
import interfaces.OnFeedBackListener;

/**
 * 闲的蛋疼
 * 把反馈页面 用mvp的框架写写看
 * Created by wjc on 2017/9/14.
 */

public interface IFeedbackBiz {

    public void commitFeedback(String content, String contact, OnFeedBackListener onFeedBackListener);

}
