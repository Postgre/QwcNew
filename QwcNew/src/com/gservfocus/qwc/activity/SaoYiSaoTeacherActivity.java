package com.gservfocus.qwc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.gservfocus.qwc.R;
import com.gservfocus.qwc.activity.util.BaseActivity;
import com.gservfocus.qwc.bean.Teacher;

public class SaoYiSaoTeacherActivity extends BaseActivity {

	ImageView saoyisaonan , saoyisaonv , close , login , reg;
	Teacher teacher;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teacher3);
		findView();
	}
	
	private void findView() {
		// TODO Auto-generated method stub
		teacher = Teacher.getTeacher(this);
		saoyisaonan = (ImageView) findViewById(R.id.saoyisaonan);
		saoyisaonv = (ImageView) findViewById(R.id.saoyisaonv);
		if(teacher.isBoy()){
			saoyisaonan.setVisibility(View.VISIBLE);
		}else{
			saoyisaonv.setVisibility(View.VISIBLE);
		}
		close = (ImageView) findViewById(R.id.close);
		close.setOnClickListener(this);
		login = (ImageView) findViewById(R.id.login);
		login.setOnClickListener(this);
		reg = (ImageView) findViewById(R.id.reg);
		reg.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		switch(view.getId()){
		case R.id.close:
			finish();
			break;
		case R.id.login:
			startActivity(new Intent(this , LoginActivity.class));
			finish();
			break;
		case R.id.reg:
			startActivity(new Intent(this , RegisterActivity.class));
			finish();
			break;
		case KeyEvent.KEYCODE_BACK:
			startActivity(new Intent(this, CaptureActivity.class));
			finish();
			break;
		}
	}
}
