package interfaces;

/**
 * Created by wjc on 2017/4/19.
 *
 * 用于处理 Bmob上传下载返回结果的回调接口
 * 不需要用到参数的时候
 */

public interface OnBmobReturnSuccess {


    void onSuccess();
    void onFail();

}
