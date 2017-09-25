package bmobUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;
import model.User;
import interfaces.OnBmobReturnSuccess;
import interfaces.OnBmobReturnWithObj;
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

    private OnBmobReturnWithObj onBmobReturnWithObj;

    public void setOnBmobReturnWithObj(OnBmobReturnWithObj onBmobReturnWithObj) {
        this.onBmobReturnWithObj = onBmobReturnWithObj;
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

    //批量上传文件
    public void uploadBatch(final String[] paths){
        BmobFile.uploadBatch(paths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                if(paths.length==urls.size()){
                    //数量相等，表示全部上传成功


                    onBmobReturnWithObj.onSuccess(urls);

                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {


                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }

            @Override
            public void onError(int i, String s) {

                ToastUtils.toast(context,"错误码"+i +",错误描述："+s);
            }
        });




        }





    //上传本地文件到服务器 .成功后返回一个URL,并将URL更新到用户数据表
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
                                onBmobReturnWithObj.onSuccess(null);
                            }else{
                                onBmobReturnWithObj.onFail(e.getMessage());
                                Log.e("bmob",e.getMessage());
                            }
                        }
                    });

                }else{
                    onBmobReturnWithObj.onFail(e.getMessage());
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
