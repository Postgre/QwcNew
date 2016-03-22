package com.gservfocus.qwc.net.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.gservfocus.qwc.Constants;
import com.gservfocus.qwc.activity.MainActivity;
import com.gservfocus.qwc.bean.VersionInfo;
import com.gservfocus.qwc.net.ApkUpdaterApi;
import com.gservfocus.qwc.net.WSError;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

@SuppressLint("HandlerLeak")
public class ApkUpdaterApiImpl implements ApkUpdaterApi {
	private Activity context;
	ProgressDialog pd = null;
	private VersionInfo versionInfo;
	private Thread myThread;
	private static int DOWNLOAD_BEGIN = 0x1;
	private static int DOWNLOAD_END = 0x2;
	private static int DOWNLOAD_EXIST = 0x3;
	private static int DOWNLOAD_ISUPDATE = 0x4;
	private Handler handler = new Handler() {

		
		public void handleMessage(Message msg) {

			if (msg.what == DOWNLOAD_BEGIN) {
				int process = msg.getData().getInt("process", 0);
				//int sum = msg.getData().getInt("sum", 1);
				pd.setProgress(process);

			} else if (msg.what == DOWNLOAD_END) {
				pd.cancel();
			}else if(msg.what == DOWNLOAD_EXIST){
				isUpdatePrompt(versionInfo);
			}else if(msg.what == DOWNLOAD_ISUPDATE){
				context.sendBroadcast(new Intent(MainActivity.CheckUpdateBroadcastReceiver.UPDATE_ACTON));
			}

		}

	};

	public ApkUpdaterApiImpl(Activity context) {
		this.context = context;
		myThread = new Thread(new CheckVersionTask());
	}
	
	/**
	 * ���ÿ�ʼ������
	 * */
	public void setCheckUpdate(){
		myThread.start();
	}
	
	
	
	public void installApk(String apkName) {
		// TODO Auto-generated method stub
		File file = new File(apkName);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW); // �����ҳ��Action(����)
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(Uri.fromFile(file), type); // ������������
		context.startActivity(intent);
	}

	
	public void uninstallApk(String apkName) {
		// TODO Auto-generated method stub
		Uri packageURI = Uri
				.parse("package:gcsms.chinese.art.test.ChineseArtPalaceTest-android2.3"); // ����+������
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		context.startActivity(uninstallIntent);
	}

	
	public void downloadApk(final String url, final String localPath) {
		// �����ļ� ���Ŀ�ĵ�
		pd = new ProgressDialog(context);
		pd.setTitle("��������" + url.substring(url.lastIndexOf('/') + 1));
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCancelable(true);
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		File file = new File(localPath);
		if (!file.exists())
			file.mkdirs();
		new Thread() {
			public void run() {
				String appName = url.substring(url.lastIndexOf('/') + 1);
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse httpResponse = new DefaultHttpClient()
							.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						InputStream is = httpResponse.getEntity().getContent();
						// ��ʼ����apk�ļ�
						int length = (int) httpResponse.getEntity()
								.getContentLength();
						pd.setMax(length / 1024);
						FileOutputStream fos = new FileOutputStream(localPath
								+ "/" + appName);
						byte[] buffer = new byte[1024];
						int count = 0;
						int sum = 0;

						while ((count = is.read(buffer)) != -1) {
							fos.write(buffer, 0, count);
							sum = sum + count;
							Message msg = new Message();
							Bundle b = new Bundle();// �������
							b.putInt("process", sum / 1024);
							b.putInt("sum", length / 1024);
							msg.setData(b);
							msg.what = DOWNLOAD_BEGIN;
							handler.sendMessage(msg);
						}
						fos.close();
						is.close();
						handler.sendEmptyMessage(DOWNLOAD_END);
						installApk(localPath + "/" + appName);
					}
				} catch (Exception e) {
				}
			}
		}.start();
	}

	
	public int getVersionCode() {
		int verCode = -1;
		String verPack = "";
		try {
			verPack = context.getPackageName();
			verCode = context.getPackageManager().getPackageInfo(verPack, 0).versionCode;
		} catch (NameNotFoundException e) {
			return -1;
		}
		return verCode;
	}

	
	public String getPackName() {
		return context.getPackageName();
	}

	
	public void isUpdatePrompt(final VersionInfo versionInfo) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("�����°汾����Ҫ���ڸ�����")
				.setTitle("�汾����")
				.setCancelable(true)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						downloadApk(versionInfo.getPROD_DOWNLOAD_URL(),
								Constants.getDatabasePath()
										+ Constants.APK_UPDATER_DIR);
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						//context.finish();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/*
	 * �ӷ�������ȡxml���������бȶ԰汾��
	 */
	public class CheckVersionTask implements Runnable {
		public void run() {
			ApkUpdaterApi apkHelper = ApkUpdaterApiImpl.this;
			int currentVersion = apkHelper.getVersionCode();
			String currentPackageName = apkHelper.getPackName();
			VersionInfo version;
			try {
				version = new QwcApiImpl().getCurrentVersionInfo(""
						+ currentVersion, currentPackageName);
				if (version != null
						&& !TextUtils.isEmpty(version.getPROD_VERSION())) {
					if (Integer.valueOf(version.getPROD_VERSION()) > currentVersion) {
						versionInfo = version;
						handler.sendEmptyMessage(DOWNLOAD_EXIST);
						//apkHelper.isUpdatePrompt(version);
					}else{
						handler.sendEmptyMessage(DOWNLOAD_ISUPDATE);
					}
				}else{
					handler.sendEmptyMessage(DOWNLOAD_ISUPDATE);
				}
			} catch (WSError e) {
				e.printStackTrace();
			}
		}
	}
}
