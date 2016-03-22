package com.gservfocus.qwc.activity;

import java.util.concurrent.Callable;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.activity.util.Callback;
import com.gservfocus.qwc.net.WSError;
import com.gservfocus.qwc.net.impl.QwcApiImpl;

public class CommentActivity extends BaseActivity {

	private RatingBar tasteRatingBar;
	private RatingBar servicesRatingBar;
	private RatingBar conditionRatingBar;
	private EditText commentContentET;
	private Button submitBtn;
	private String agicolaID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		// setting title
		((TextView) findViewById(R.id.title)).setText("评论");
		// back
		findViewById(R.id.imagereturn1).setOnClickListener(this);

		tasteRatingBar = (RatingBar) findViewById(R.id.commentTasteRatingBar);
		servicesRatingBar = (RatingBar) findViewById(R.id.commentServicesRatingBar);
		conditionRatingBar = (RatingBar) findViewById(R.id.commentConditionRatingBar);
		commentContentET = (EditText) findViewById(R.id.commentContentEt);
		submitBtn = (Button) findViewById(R.id.commentSubmitBtn);
		submitBtn.setOnClickListener(this);
		agicolaID = getIntent().getStringExtra("agicolaID");
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);
		switch (view.getId()) {
		case R.id.commentSubmitBtn:
			if (!TextUtils.isEmpty(commentContentET.getText().toString())
					&& commentContentET.getText().toString().length() >= 10) {
				comment(commentContentET.getText().toString(), agicolaID, ""
						+ (int) tasteRatingBar.getRating(), ""
						+ (int) servicesRatingBar.getRating(), ""
						+ (int) conditionRatingBar.getRating());
				finish();
			} else
				showToast(getString(R.string.input_limit_prompt));
			break;
		case R.id.imagereturn1:
			finish();
			break;
		default:
			break;
		}
	}

	private void comment(final String content, final String id,
			final String taste, final String services, final String condition) {
		this.doAsync("评论中，请稍候...", new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub
				// 1,农家乐 2，信息
				try {
					return new QwcApiImpl().userComment(
							id,
							content,
							(Constants.mAccount.getMobile() != null ? Constants.mAccount
									.getMobile() : Constants.mAccount
									.getUserId()), "1", "" + services, ""
									+ taste, "" + condition);
				} catch (WSError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		}, new Callback<Boolean>() {

			@Override
			public void onCallback(Boolean pCallbackValue) {
				// TODO Auto-generated method stub
				if (pCallbackValue) {
					showToast("评论成功！");
					finish();
				} else {
					showToast("评论失败！");
				}
			}
		}, new Callback<Exception>() {
			public void onCallback(Exception pCallbackValue) {
			}
		});
	}
}
