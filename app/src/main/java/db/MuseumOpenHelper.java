package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *
 * 用来保存用户的个人信息
 * 包括
 *      个人的用户设置信息
 *      关注的博物馆、展览、藏品
 *      点赞的blog
 *
 *
 * Created by wjc on 2017/7/26.
 */

public class MuseumOpenHelper extends SQLiteOpenHelper {

    //用户点赞的Blog，显示blog列表是否已经点赞的时候直接对比本地blog，就不需要反复从服务器拉取数据比对
    private static final String CREATE_LIKE_BLOG="CREATE TABLE like_blog(" +
                                                    "_id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                                                    "blog_id VARCHAR UNIQUE" +
                                                    ")";
    //用户关注的
    private static final String CREATE_WATCH_MUSEUM="CREATE TABLE watch_museum(" +
                                                    "_id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                                                    "museum_id VARCHAR UNIQUE," +
                                                    "museum_name VARCHAR,"+
                                                    "museum_image_url TEXT"+
                                                    ")";

    private static final String CREATE_WATCH_EXHIBIT="CREATE TABLE watch_exhibit(" +
                                                    "_id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                                                    "exhibit_id VARCHAR UNIQUE," +
                                                    "exhibit_name VARCHAR,"+
                                                    "exhibit_image_url TEXT"+
                                                    ")";

    private static final String CREATE_WAHCH_COLT="CREATE TABLE watch_colt(" +
                                                    "_id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                                                    "colt_id VARCHAR UNIQUE," +
                                                    "colt_name VARCHAR,"+
                                                    "colt_image_url TEXT"+
                                                    ")";
    private static final String CREATE_FANS="CREATE TABLE fans(" +
                                                "_id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                                                "user_id VARCHAR UNIQUE" +
                                                ")";
    private static final String CREATE_FOLLOWINGS="CREATE TABLE followings(" +
                                                "_id INTEGER PRIMARY KEY AUTOINCREMENT ," +
                                                "user_id VARCHAR UNIQUE" +
                                                ")";




    public MuseumOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_LIKE_BLOG);
        db.execSQL(CREATE_WATCH_MUSEUM);
        db.execSQL(CREATE_WATCH_EXHIBIT);
        db.execSQL(CREATE_WAHCH_COLT);
        db.execSQL(CREATE_FANS);
        db.execSQL(CREATE_FOLLOWINGS);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
