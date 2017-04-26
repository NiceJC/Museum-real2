package BmobUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.net.URL;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import entity.User;
import interfaces.OnBmobReturnSuccess;
import util.ToastUtils;

/**
 * Created by wjc on 2017/4/24.
 */

public class BmobFileUtil  {



    private Context context;
    private String TAG;
    private static BmobFileUtil instance=null;

    private OnBmobReturnSuccess onBmobReturnSuccess;

    public void setOnBmobReturnSuccess(OnBmobReturnSuccess onBmobReturnSuccess) {
        this.onBmobReturnSuccess = onBmobReturnSuccess;
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    onBmobReturnSuccess.onSuccess();
                    break;
                case 2:
                    onBmobReturnSuccess.onFail();
                    break;
                default:
                    break;

            }
        }
    };

    private BmobFileUtil(Context context){
        this.context=context;
        TAG="bmob file";

    }
    //单例
    public static BmobFileUtil getInstance(Context context){

        if (instance==null){
            instance=new BmobFileUtil(context);

        }

        return instance;

    }

    //上传本地文件到服务器,并更新数据表  成功后返回一个URL
    public void uploadFile(String path){
        final BmobFile bmobFile=new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {

                if(e==null){

                    String url=bmobFile.getFileUrl();
                    Log.d(TAG,"上传文件成功"+url);

                    User newUser=new User();
                    newUser.setPortraitURL(url);
                    User user=BmobUser.getCurrentUser(User.class);
                    newUser.update(user.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                ToastUtils.toast(context,"更新用户信息成功");
                            }else{
                                ToastUtils.toast(context,"更新用户信息失败:" + e.getMessage());
                            }
                        }
                    });

                }else{
                    Log.d(TAG,"上传文件失败"+e.getMessage());
                    ToastUtils.toast(context,"上传文件失败"+e.getMessage());
                }

            }
        });
    }

    //根据URL，删除文件
    public void deleteFile(String url){
        BmobFile file=new BmobFile();
        file.setUrl(url);
        file.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.d(TAG,"文件删除成功");
                }else{
                    Log.d(TAG,"文件删除失败："+e.getErrorCode()+","+e.getMessage());
                    ToastUtils.toast(context,"文件删除失败："+e.getErrorCode()+","+e.getMessage());
                }
            }
        });
    }



}
