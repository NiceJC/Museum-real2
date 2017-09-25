package bmobUtils;

import android.content.Context;
import android.util.Log;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import model.ExhibitRoom;
import model.Museum;
import interfaces.OnBmobReturnWithObj;
import util.ToastUtils;

import static util.ParameterBase.LIMIT_SIX;

/**
 * Created by wjc on 2017/5/12.
 */

public class BmobExhibitRoom  {



    private Context context;
    private String TAG;
    private static BmobExhibitRoom instance = null;

    private OnBmobReturnWithObj onBmobReturnWithObj;

    public void setOnBmobReturnWithObj(OnBmobReturnWithObj onBmobReturnWithObj) {
        this.onBmobReturnWithObj = onBmobReturnWithObj;
    }


    private BmobExhibitRoom(Context context) {
        this.context = context;
        TAG = "bmob blog";

    }

    //单例
    public static BmobExhibitRoom getInstance(Context context) {

        if (instance == null) {
            instance = new BmobExhibitRoom(context);

        }

        return instance;

    }



    //获取指定博物馆下的展馆
    public void getExhibitRoomsByMuseum (String museumID,int curPage) {

        BmobQuery<ExhibitRoom> query = new BmobQuery<ExhibitRoom>();
//        query.setLimit(5);
        query.setLimit(LIMIT_SIX);
        query.setSkip(LIMIT_SIX * curPage);

        Museum museum=new Museum();
        museum.setObjectId(museumID);
        query.addWhereEqualTo("toMuseum",new BmobPointer(museum));
        query.order("createdAt");
        query.findObjects(new FindListener<ExhibitRoom>() {
            @Override
            public void done(List<ExhibitRoom> list, BmobException e) {
                if (e == null) {

                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    ToastUtils.toast(context, e.getMessage());
                }

            }
        });
    }



    //根据ID查询单个ExhibitionRoom
    public void getExhibitRoomByID(String exhibitRoomID){

        BmobQuery<ExhibitRoom> query=new BmobQuery<ExhibitRoom>();
        query.getObject(exhibitRoomID, new QueryListener<ExhibitRoom>() {
            @Override
            public void done(ExhibitRoom exhibitRoom, BmobException e) {
                if(e==null){
                    onBmobReturnWithObj.onSuccess(exhibitRoom);

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });



    }



    /**
     * 关键词查询，暂时只针对名称的关键字
     */
    public void getBykeyWord(String keyWord) {
        BmobQuery<ExhibitRoom> query = new BmobQuery<ExhibitRoom>();
        query.setLimit(LIMIT_SIX);

        query.order("-createdAt");//先按点赞数降序，再按时间降序
        query.include("toMuseum");
        query.addWhereContains("name", keyWord);
        query.findObjects(new FindListener<ExhibitRoom>() {
            @Override
            public void done(List<ExhibitRoom> list, BmobException e) {
                if (e == null) {

                    onBmobReturnWithObj.onSuccess(list);
                } else {
                    ToastUtils.toast(context, e.getMessage());
                }

            }
        });
    }


    public void uploadExhibitRoom(ExhibitRoom exhibitRoom){

        exhibitRoom.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {


            }
        });
    }
}
