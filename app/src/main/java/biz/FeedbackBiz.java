package biz;

import bmobUtils.BmobFeedback;
import interfaces.OnBmobReturnWithObj;
import interfaces.OnFeedBackListener;

/**
 * Created by wjc on 2017/9/14.
 */

public class FeedbackBiz implements IFeedbackBiz {
    @Override
    public void commitFeedback(String content, String contact, final OnFeedBackListener onFeedBackListener) {

        BmobFeedback bmobFeedback=BmobFeedback.getInstance();
        bmobFeedback.setOnBmobReturnWithObj(new OnBmobReturnWithObj() {
            @Override
            public void onSuccess(Object Obj) {

                onFeedBackListener.onSuccess(Obj);
            }

            @Override
            public void onFail(Object Obj) {

                onFeedBackListener.onFail(Obj);
            }
        });

        bmobFeedback.commitFeedback(content,contact);


    }
}
