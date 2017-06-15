package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;



/**
 * Created by zhuguohui on 2016/3/23.
 */
public class TRSRecyclerAdapter<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {
    List<T> data;
    Context context;
    private final LayoutInflater mInflater;
    private int layoutId;

    public TRSRecyclerAdapter(List<T> data, Context context, int layoutId) {
        this.data = data;
        this.layoutId = layoutId;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(layoutId, parent, false);
        return getInstanceOfH(view);
    }

    H getInstanceOfH(View view) {
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<H> type = (Class<H>) superClass.getActualTypeArguments()[1];
        try {
            Constructor<H> constructor = type.getConstructor(View.class);
            return constructor.newInstance(view);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public final void onBindViewHolder(H h, int position) {
        T t = data.get(position);
        bindData(t, h);
    }

    private void setDataToHolder(T t, H h) {
        Field[] fields = h.getClass().getDeclaredFields();
        Object o ;
        for (Field f : fields) {
            f.setAccessible(true);
            o = null;
            try {
                o = f.get(h);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (o instanceof TextView) {
                TextView tv = (TextView) o;
                Object value = getValueFromFiled(t, f.getName());
                if (value instanceof CharSequence) {
                    tv.setText((CharSequence) value);
                }
            } else if (o instanceof ImageView) {
                ImageView iv = (ImageView) o;
                Object value = getValueFromFiled(t, f.getName());
                if (value instanceof String) {
                    String url = (String) value;
                    Glide.with(context).load(url).into(iv);
                }
            }


        }
    }

    public Object getValueFromFiled(T t, String name) {
        try {
            Field hf = t.getClass().getDeclaredField(name);
            hf.setAccessible(true);
            return hf.get(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void bindData(T t, H h) {
        setDataToHolder(t, h);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

//
//    public class NewsAdapter extends TRSRecyclerAdapter<NewsItem,NewsAdapter.NewsViewHolder> {
//
//
//        public NewsAdapter(List<NewsItem> data, Context context) {
//            super(data, context, R.layout.item_news_layout);
//        }
//
//        public static class NewsViewHolder extends TRSViewHolder{
//            public TextView title;
//            public TextView time;
//            public ImageView image;
//            public NewsViewHolder(View itemView) {
//                super(itemView);
//            }
//        }
//    }
}