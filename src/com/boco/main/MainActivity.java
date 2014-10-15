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
				 * ��ȡmanifest�еİ汾���ݣ�����ʹ��versionCode
				 * �ڴӷ�������ȡ�����°汾��versionCode,�Ƚ�
				 * 
				 * PackageManager manager = MainActivity.this.getPackageManager();
				try {
					PackageInfo info = manager.getPackageInfo(
							MainActivity.this.getPackageName(), 0);
					String appVersion = info.versionName; // �汾��
					currentVersionCode = info.versionCode; // �汾��
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
	 * ������ʾ�Ի���
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��⵽�°汾");
		builder.setMessage("�Ƿ����ظ���?");
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (existSDCard()) {
					// �������ط���
					Intent i = new Intent();
					i.setAction("com.boco.huipai.user.APP_UPDATE_DOWNLOAD_SERVICE");
					startService(i);
				} else {
					Toast.makeText(MainActivity.this, "SD���޷�����,��ʱ���ܸ��£�",
							Toast.LENGTH_SHORT).show();
				}

			}
		}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.show();
	}

	/**
	 * ��֤�ڴ濨�Ƿ����
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
