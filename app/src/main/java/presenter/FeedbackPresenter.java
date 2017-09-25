package presenter;

import biz.FeedbackBiz;
import biz.IFeedbackBiz;
import interfaces.OnFeedBackListener;
import view.IFeedbackView;

/**
 * Created by wjc on 2017/9/14.
 */

public class FeedbackPresenter {

    private IFeedbackBiz feedbackBiz;
    private IFeedbackView feedbackView;

    public FeedbackPresenter(IFeedbackView feedbackView) {
        this.feedbackView = feedbackView;
        this.feedbackBiz=new FeedbackBiz();
    }

    public void commit(){
        String content=feedbackView.getContentString();
        String contact=feedbackView.getContact();
        if(content==null||content.equals("")){
            feedbackView.showToast("请先填写您的反馈意见");
            return;
        }
        feedbackView.showLoading();
        feedbackBiz.commitFeedback(content, contact, new OnFeedBackListener() {
            @Override
            public void onSuccess(Object object) {
                feedbackView.loadSuccess();
                feedbackView.showToast("反馈提交成功");
                feedbackView.finishActivity();
            }

            @Override
            public void onFail(Object object) {

                feedbackView.loadFail();
                feedbackView.showToast(object.toString());
            }
        });
    }
    public void back(){
        feedbackView.finishActivity();

    }



}
