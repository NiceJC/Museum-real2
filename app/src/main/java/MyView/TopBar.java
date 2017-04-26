package MyView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import jintong.museum2.R;

/**
 * 自定义控件TopBar  暂时弃用
 */
public class TopBar extends RelativeLayout {

    private Button leftButton,rightButton;
    private TextView tvTittle;


    private int leftIcon;

    private int rightIcon;


    private float titleTextSize;
    private int tittleTextColor;
    private String tittle;

    private LayoutParams leftParams,rightParams,tittleParams;

    private topbarClickListener listener;

    //回调接口，具体的点击事件可以在调用方实现接口的时候设定
    public interface topbarClickListener{
        public void leftClick();
        public void rightClick();

    }

    //为两个Buttton定义点击事件
    public void setOnTopbarClickListener(topbarClickListener listener){

        this.listener=listener;
    }


    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        //使用TypedArray保存在xml文件中自定义的值
        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.TopBar);
        //默认值为0

        leftIcon=ta.getResourceId(R.styleable.TopBar_leftIcon,0);

        rightIcon=ta.getResourceId(R.styleable.TopBar_rightIcon,0);


        titleTextSize=ta.getDimension(R.styleable.TopBar_tittleTextSize,0);

        tittleTextColor=ta.getColor(R.styleable.TopBar_tittleTextColor,0);
        tittle=ta.getString(R.styleable.TopBar_tittle);

        //在使用完TypedArray之后，进行回收，避免浪费资源和一些其他的错误
        ta.recycle();

        //实例化所需的子控件
        leftButton=new Button(context);
        rightButton=new Button(context);
        tvTittle=new TextView(context);

        //将xml中定义过的属性赋值给控件，也可以新加入一些属性作为默认属性设置

        leftButton.setBackgroundResource(leftIcon);



        rightButton.setBackgroundResource(rightIcon);

        tvTittle.setTextColor(tittleTextColor);
        tvTittle.setTextSize(titleTextSize);
        tvTittle.setText(tittle);
        tvTittle.setGravity(Gravity.CENTER);



        //所有的布局属性都是设置在LayoutParams
        //设定自定义子控件的宽高参数，以及在ViewGroup(RelativeLayout)中的位置
        leftParams=new LayoutParams(60,60);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);//这里的TRUE是一个int

        rightParams=new LayoutParams(60,60);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);

        tittleParams=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        tittleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);


        //将自控件加入ViewGroup中
        addView(leftButton,leftParams);
        addView(rightButton,rightParams);
        addView(tvTittle,tittleParams);


        setBackgroundColor(0xFFF33333);

        //这里的点击事件实际是由Topbar的调用方来实现的
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftClick();
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rightClick();
            }
        });

    }
}
