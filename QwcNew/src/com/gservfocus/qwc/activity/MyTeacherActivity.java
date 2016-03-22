package com.gservfocus.qwc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.QwcApplication;
import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.bean.Teacher;

public class MyTeacherActivity extends BaseActivity {

	Intent intent;
	ImageView selboy , selgirl , zhuce , main , close , girlss , boyss;
	Teacher teacher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.teather1);
		girlss = ((ImageView) findViewById(R.id.girlss));
		boyss = ((ImageView) findViewById(R.id.boyss));
		main = ((ImageView) findViewById(R.id.main));
		close = ((ImageView) findViewById(R.id.close));
		close.setOnClickListener(this);
		zhuce = ((ImageView) findViewById(R.id.zhuce));
		zhuce.setOnClickListener(this);
		selboy = ((ImageView) findViewById(R.id.selboy));
		selboy.setOnClickListener(this);
		selgirl = ((ImageView) findViewById(R.id.selgirl));
		selgirl.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		teacher = Teacher.getTeacher(this);
		super.onClick(view);
		switch (view.getId()) {
		case R.id.selboy:
			teacher.setBoy(true);
			teacher.setFirstLogin(false);
			Teacher.saveTeacher(this, teacher);
			changeView();
			break;
		case R.id.selgirl:
			teacher.setBoy(false);
			teacher.setFirstLogin(false);
			Teacher.saveTeacher(this, teacher);
			changeView();
			break;
		case R.id.close:
			finish();
			break;
		case R.id.zhuce:
			startActivity(new Intent(this,RegisterActivity.class));
			finish();
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			teacher = Teacher.getTeacher(this);
			teacher.setBoy(true);
			teacher.setFirstLogin(false);
			Teacher.saveTeacher(this, teacher);
			finish();
		}
		return false;
	}
	
	private void changeView() {
		// TODO Auto-generated method stub
		main.setVisibility(View.INVISIBLE);
		selboy.setVisibility(View.INVISIBLE);
		selgirl.setVisibility(View.INVISIBLE);
		close.setVisibility(View.VISIBLE);
		if(teacher.isBoy()){
			boyss.setVisibility(View.VISIBLE);
		}else if(!teacher.isBoy()){
			girlss.setVisibility(View.VISIBLE);
		}
		zhuce.setVisibility(View.VISIBLE);
	}

}
