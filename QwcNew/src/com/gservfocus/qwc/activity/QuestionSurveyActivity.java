package com.gservfocus.qwc.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;

public class QuestionSurveyActivity extends BaseActivity {

	private ImageView back;
	private TextView title;
	private WebView Survey;
	private static String Url = "http://qwcweb.gservfocus.com/ShowQuestion.aspx?UID="
			+ Constants.mAccount.getMobile();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_survey);
		intoView();
	}

	private void intoView() {
		// TODO Auto-generated method stub
		back = (ImageView) findViewById(R.id.imagereturn1);
		title = (TextView) findViewById(R.id.title);
		Survey = (WebView) findViewById(R.id.Survey);
		Survey.getSettings().setJavaScriptEnabled(true);
		Survey.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				//Survey.setVisibility(View.GONE);
			}

			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		Survey.loadUrl(Url);
		back.setOnClickListener(this);
		title.setText("ÎÊ¾íµ÷²é");
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch (view.getId()) {
		case R.id.imagereturn1:
			finish();
			break;
		}
	}
}
