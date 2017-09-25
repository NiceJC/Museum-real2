package jintong.museum2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import net.steamcrafted.loadtoast.LoadToast;

import presenter.FeedbackPresenter;
import util.ToastUtils;
import view.IFeedbackView;

/**
 * Created by wjc on 2017/9/14.
 */

public class FeedBackActivity  extends BaseActivity implements IFeedbackView{

    private ImageView back;
    private EditText content,contact;
    private Button commit;
    private LoadToast loadToast;
    private FeedbackPresenter feedbackPresenter=new FeedbackPresenter(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);

        initView();



    }


    private void initView() {

        back= (ImageView) findViewById(R.id.feedback_back);
        contact= (EditText) findViewById(R.id.feedback_contact);
        content= (EditText) findViewById(R.id.feedback_content);
        commit= (Button) findViewById(R.id.feedback_commit);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                feedbackPresenter.back();

            }
        });

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackPresenter.commit();
            }
        });



    }


    @Override
    public String getContentString() {
        return content.getText().toString();
    }

    @Override
    public String getContact() {
        return contact.getText().toString();
    }

    @Override
    public void finishActivity() {
        finish();
        overridePendingTransition(R.anim.none,R.anim.out_to_right);
    }

    @Override
    public void showLoading() {
        loadToast=ToastUtils.getLoadingToast(this);
        loadToast.show();
    }

    @Override
    public void loadSuccess() {

        loadToast.success();
    }

    @Override
    public void loadFail() {
       loadToast.error();
    }


    @Override
    public void showToast(String toastString) {
        ToastUtils.toast(this,toastString);
    }
}
