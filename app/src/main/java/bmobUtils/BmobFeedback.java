package bmobUtils;

import android.util.Log;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import interfaces.OnBmobReturnWithObj;
import model.FeedBack;
import model.User;

/**
 * Created by wjc on 2017/9/14.
 */

public class BmobFeedback {


    private static BmobFeedback instance=null;

    private OnBmobReturnWithObj onBmobReturnWithObj;
    private BmobFeedback() {
    }

    public void setOnBmobReturnWithObj(OnBmobReturnWithObj onBmobReturnWithObj) {
        this.onBmobReturnWithObj = onBmobReturnWithObj;
    }

    public static BmobFeedback getInstance(){
        if(instance==null){
            synchronized (BmobFeedback.class){
                if (instance==null){
                    instance=new BmobFeedback();
                }
            }
        }
        return instance;
    }



    public void commitFeedback(String content,String contact){

        User user= BmobUser.getCurrentUser(User.class);
        FeedBack feedBack=new FeedBack();
        feedBack.setAuthor(user);
        feedBack.setContact(contact);
        feedBack.setFeedbackString(content);
        feedBack.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "保存成功");
                    onBmobReturnWithObj.onSuccess(s);
                } else {
                    Log.i("bmob", "保存失败：" + e.getMessage());
                    onBmobReturnWithObj.onFail(e.getMessage());
                }

            }
        });



    }





}
