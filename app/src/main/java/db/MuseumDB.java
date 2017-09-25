package db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import model.Blog;
import model.Collection;
import model.Exhibition;
import model.Museum;
import model.User;

/**
 * Created by wjc on 2017/7/27.
 *
 * 数据库的实例化入口，采用单例模式
 *
 * 对外提供针对数据库的基本增删改查操作
 *
 *
 */

public class MuseumDB {
    private static final String DB_NAME="museum_db";
    private static final int VERSION=1;
    private static volatile MuseumDB museumDB;
    private SQLiteDatabase db;


    private MuseumDB(Context context) {
        MuseumOpenHelper dbHelper=new MuseumOpenHelper(context,DB_NAME,null,VERSION);
        db=dbHelper.getWritableDatabase();
    }


    public static MuseumDB getInstance(Context context){
        if(museumDB==null){
            synchronized (MuseumDB.class){
                if(museumDB==null){
                    museumDB=new MuseumDB(context);
                }
            }
        }
        return  museumDB;
    }


    /**
     *
     * 增加单条数据
     *
     */

    public void saveLikedBlog(Blog blog){
        if(blog!=null){
            db.execSQL("INSERT INTO like_blog (_id,blog_id)VALUES (null,?)", new String[]{blog.getObjectId()});
        }
    }


    public void saveLikedMuseum(Museum museum){
        if(museum!=null){
            db.execSQL("INSERT INTO watch_museum (_id,museum_id,museum_name,museum_image_url)VALUES (null,?,?,?)",
                    new String[]{museum.getObjectId(),museum.getMuseumName(),museum.getImageURLs().get(0)});
        }
    }

    public void saveLikedExhibit(Exhibition exhibition){
        if(exhibition!=null){
            db.execSQL("INSERT INTO watch_exhibit (_id,exhibit_id,exhibit_name,exhibit_image_url)VALUES (null,?,?,?)",
                    new String[]{exhibition.getObjectId(),exhibition.getExhibitName(),exhibition.getImage1().getFileUrl()});
        }
    }
    public void saveLikedColt(Collection collection){
        if(collection!=null){
            db.execSQL("INSERT INTO watch_colt (_id,colt_id,colt_name,colt_image_url)VALUES (null,?,?,?)",
                    new String[]{collection.getObjectId(),collection.getColtName(),collection.getImage1().getFileUrl()});
        }
    }

    /**
     * 删除单条数据
     */
    public void deleteLikedBlog(Blog blog){
        if(blog!=null){
            db.execSQL("DELETE FROM like_blog WHERE blog_id=?",new String[]{blog.getObjectId()});
        }


    }





    /**
     * 查
     * @return
     */
    public List<Blog> loadLikedBlogs(){
        List<Blog> blogList=new ArrayList<Blog>();
        Cursor cursor=db.rawQuery("SELECT * FROM like_blog",null);

        if(cursor.moveToFirst()){
            do{
                Blog blog=new Blog();
                blog.setObjectId(cursor.getString(cursor.getColumnIndex("blog_id")));
                blogList.add(blog);
            }while (cursor.moveToNext());

        }
        if(cursor!=null){
            cursor.close();
        }
        return  blogList;
    }


    public List<Museum> loadLikedMuseums(){
        List<Museum> museumList=new ArrayList<Museum>();
        Cursor cursor=db.rawQuery("SELECT * FROM watch_museum",null);

        if(cursor.moveToFirst()){
            do {
                Museum museum=new Museum();
                museum.setObjectId(cursor.getString(cursor.getColumnIndex("")));
                museum.setMuseumName(cursor.getString(cursor.getColumnIndex("")));
                List<String> urlList=new ArrayList<>();
                urlList.add(cursor.getString(cursor.getColumnIndex("")));
                museum.setImageURLs(urlList);
                museumList.add(museum);
            }while (cursor.moveToNext());
        }
        if(cursor!=null){
            cursor.close();
        }
        return museumList;
    }

    public List<Exhibition> loadLikedExhibit(){
        List<Exhibition> exhibitionList=new ArrayList<Exhibition>();
        Cursor cursor=db.rawQuery("SELECT * FROM watch_exhibit",null);

        if(cursor.moveToFirst()){
            do {
                Exhibition exhibition=new Exhibition();
                exhibition.setObjectId(cursor.getString(cursor.getColumnIndex("")));
                exhibition.setExhibitName(cursor.getString(cursor.getColumnIndex("")));
                exhibition.setImage1Url(cursor.getString(cursor.getColumnIndex("")));
                exhibitionList.add(exhibition);
            }while (cursor.moveToNext());
        }
        if(cursor!=null){
            cursor.close();
        }
        return exhibitionList;
    }

    public List<Collection> loadLikedColt(){
        List<Collection> collectionList=new ArrayList<Collection>();
        Cursor cursor=db.rawQuery("SELECT * FROM watch_colt",null);

        if(cursor.moveToFirst()){
            do {
                Collection collection=new Collection();
                collection.setObjectId(cursor.getString(cursor.getColumnIndex("")));
                collection.setColtName(cursor.getString(cursor.getColumnIndex("")));
                collection.setImage1Url(cursor.getString(cursor.getColumnIndex("")));
                collectionList.add(collection);
            }while (cursor.moveToNext());
        }
        if(cursor!=null){
            cursor.close();
        }
        return collectionList;
    }


    /**
     * 从服务器请求数据 同步到数据库  因为个人收藏的数据量不大，所以这里选择清空后重新插入
     */


    public void updataLikedBlog(List<Blog> blogList){
        if(blogList!=null){
            db.beginTransaction();
            db.execSQL("DELETE FROM like_blog");
            if(blogList.size()!=0){
                for (Blog blog:blogList
                     ) {

                    saveLikedBlog(blog);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }




    public void saveFan(User user) {

        if (user != null) {
            db.execSQL("INSERT INTO fans(_id,user_id)VALUES (null,?)",
                    new String[]{user.getObjectId()});
        }
    }

    public void saveFollowing(User user){

        if(user!=null){
            db.execSQL("INSERT INTO followings(_id,user_id)VALUES (null,?)",
                    new String[]{user.getObjectId()});
        }

    }

    public List<User> loadFans(){
        List<User> userList=new ArrayList<User>();
        Cursor cursor=db.rawQuery("SELECT * FROM fans",null);

        if(cursor.moveToFirst()){
            do {
                User user=new User();
                user.setObjectId(cursor.getString(cursor.getColumnIndex("user_id")));

                userList.add(user);
            }while (cursor.moveToNext());
        }
        if(cursor!=null){
            cursor.close();
        }
        return userList;
    }

    public List<User> loadFollowings(){
        List<User> userList=new ArrayList<User>();
        Cursor cursor=db.rawQuery("SELECT * FROM followings",null);

        if(cursor.moveToFirst()){
            do {
                User user=new User();
                user.setObjectId(cursor.getString(cursor.getColumnIndex("user_id")));

                userList.add(user);
            }while (cursor.moveToNext());
        }
        if(cursor!=null){
            cursor.close();
        }
        return userList;
    }




    public void updateFans(List<User> fansList){
        if(fansList!=null){
            db.beginTransaction();
            db.execSQL("DELETE FROM fans");
            if(fansList.size()!=0){
                for (User user:fansList
                        ) {

                    saveFan(user);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }

    }
    public void updateFollowings(List<User> followingList){
        if(followingList!=null){
            db.beginTransaction();
            db.execSQL("DELETE FROM followings");
            if(followingList.size()!=0){
                for (User user:followingList
                        ) {

                    saveFollowing(user);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }

    }



    /**
     * 查询传入用户属否已经被本地用户关注
     */
    public boolean getIfFollwed(final String userID){


        boolean isWatched =false;
        for (User user :
                loadFollowings()) {
            if(user.getObjectId().equals(userID)){
                isWatched=true;
            }
        }

        return isWatched;
    }


    public void updataLikedMuseum(List<Museum> museumList){
        if(museumList!=null){
            db.beginTransaction();
            db.execSQL("DELETE FROM watch_museum");
            if(museumList.size()!=0){

                for (Museum museum:museumList
                        ) {

                    saveLikedMuseum(museum);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public void updataLikedExhibit(List<Exhibition> exhibitList){
        if(exhibitList!=null){
            db.beginTransaction();
            db.execSQL("DELETE FROM watch_exhibit");
            if(exhibitList.size()!=0){

                for (Exhibition exhibition:exhibitList
                        ) {

                    saveLikedExhibit(exhibition);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }

    public void updataLikedColt(List<Collection> coltList){
        if(coltList!=null){
            db.beginTransaction();
            db.execSQL("DELETE FROM watch_colt");
            if(coltList.size()!=0){

                for (Collection collection:coltList
                        ) {
                    saveLikedColt(collection);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        }
    }



}



