package view;

/**
 * Created by wjc on 2017/9/14.
 */

public interface IFeedbackView {

    String getContentString();
    String getContact();
    void finishActivity();
    void showLoading();
    void loadSuccess();
    void loadFail();
    void showToast(String toastString);


}
