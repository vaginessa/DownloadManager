package com.boco.main;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.webkit.MimeTypeMap;
/**
 * 
 * @author lishengjie
 *
 */
@SuppressLint("NewApi")
public class AppUpdateDownloadService extends Service {

	@SuppressLint("SdCardPath")
	private static final String SDCARD_PATH = "/sdcard/Download/";
	private static final String APP_NAME = "piis_user.apk";
	private DownloadManager downloadManager;

	public void onCreate() {
		super.onCreate();
		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		registerReceiver(receiver, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startDownload();
		return super.onStartCommand(intent, flags, startId);
	}

	@SuppressWarnings("deprecation")
	private void startDownload() {
		
		/**
		 * 多任务下载时请注意：可以在此处判断下载任务重复
		 * 
		 * downloadManager.enqueue(request); 方法调用后会返回一个DownloadId
		 * 
		 */
		
		String apkUrl = "http://img.meilishuo.net/css/images/AndroidShare/Meilishuo_3.6.1_10006.apk";
		// 开始下载
		Uri resource = Uri.parse(apkUrl);
		DownloadManager.Request request = new DownloadManager.Request(resource);
		// 设置下载网络
		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
				| Request.NETWORK_WIFI);
		// 是否允许使用漫游。
		request.setAllowedOverRoaming(true);

		// 设置文件类型
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap
				.getFileExtensionFromUrl(apkUrl));
		request.setMimeType(mimeString);

		// 在通知栏中显示 当前方法已过期，未找到新的方法
		request.setShowRunningNotification(true);
		// 设置下载保存目录：sdcard/download 设置文件名
		request.setDestinationInExternalPublicDir("/download/",
				"Meilishuo_3.6.1_10006.apk");
		// 设置下载任务的标题
		request.setTitle("美丽说");
		// 当前方法必须调用，否则下载任务不会开始，返回下载任务的ID
		downloadManager.enqueue(request);
		/*
		 * 此方法在下载音频或图片时需要设置，设置为true则可以将当前下载的资源共享给别的App
		 * request.setVisibleInDownloadsUi(true);
		 */

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}

	/**
	 * 下载监听
	 */
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 这里可以取得下载的id，这样就可以知道哪个文件下载完成了。适用与多个下载任务的监听
			Log.v("test",
					""
							+ intent.getLongExtra(
									DownloadManager.EXTRA_DOWNLOAD_ID, 0));
			Log.i("test", "下载完成发送广播···");
			queryDownloadStatus();
		}
	};

	/**
	 * 下载状态管理
	 */
	private void queryDownloadStatus() {
		DownloadManager.Query query = new DownloadManager.Query();
		Cursor c = downloadManager.query(query);
		if (c.moveToFirst()) {
			int status = c.getInt(c
					.getColumnIndex(DownloadManager.COLUMN_STATUS));
			switch (status) {
			case DownloadManager.STATUS_PAUSED:
				// 下载停止
			case DownloadManager.STATUS_PENDING:
				// 等待下载
			case DownloadManager.STATUS_RUNNING:
				// 正在下载
				break;
			case DownloadManager.STATUS_SUCCESSFUL:
				// 下载完成
				File file = new File(SDCARD_PATH + APP_NAME);
				openFile(file);
				break;
			case DownloadManager.STATUS_FAILED:
				// 清除已下载的内容，重新下载
				break;
			}
		}
	}

	/**
	 * 打开安装应用界面
	 * 
	 * @param file
	 *            安装文件
	 */
	private void openFile(File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

}
