package interfaces;

/**
 * Created by wjc on 2017/4/27.
 *
 * 用于处理 Bmob上传下载返回结果的回调接口
 * 需要对返回参数进行进一步处理的时候
 */

public interface OnBmobReturnWithObj {


    void onSuccess(Object Obj);
    void onFail(Object Obj);
}
