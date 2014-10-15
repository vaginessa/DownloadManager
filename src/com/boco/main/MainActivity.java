package com.boco.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
/**
 * 
 * @author lishengjie
 *
 */
@SuppressLint({ "NewApi", "SdCardPath" })
public class MainActivity extends Activity {
	private Button appCheck;
//	private int currentVersionCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}
	private void init() {
		appCheck = (Button) findViewById(R.id.check);
		appCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				/*
				 * 获取manifest中的版本数据，我是使用versionCode
				 * 在从服务器获取到最新版本的versionCode,比较
				 * 
				 * PackageManager manager = MainActivity.this.getPackageManager();
				try {
					PackageInfo info = manager.getPackageInfo(
							MainActivity.this.getPackageName(), 0);
					String appVersion = info.versionName; // 版本名
					currentVersionCode = info.versionCode; // 版本号
					System.out.println(currentVersionCode + " " + appVersion);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				*/
				
				showUpdateDialog();
			}
		});
	}

	/**
	 * 更新提示对话框
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("检测到新版本");
		builder.setMessage("是否下载更新?");
		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (existSDCard()) {
					// 启动下载服务
					Intent i = new Intent();
					i.setAction("com.boco.huipai.user.APP_UPDATE_DOWNLOAD_SERVICE");
					startService(i);
				} else {
					Toast.makeText(MainActivity.this, "SD卡无法访问,暂时不能更新！",
							Toast.LENGTH_SHORT).show();
				}

			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.show();
	}

	/**
	 * 验证内存卡是否可用
	 * 
	 * @return
	 */
	public boolean existSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

}
