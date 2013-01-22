/**
 * 
 */
package com.gmail.charleszq.picorner.offline;

import java.util.ArrayList;
import java.util.List;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gmail.charleszq.picorner.BuildConfig;
import com.gmail.charleszq.picorner.R;

/**
 * @author charles(charleszq@gmail.com)
 * 
 */
public class OfflineHandleService extends IntentService {

	/**
	 * The log tag
	 */
	private static final String TAG = OfflineHandleService.class
			.getSimpleName();

	private static final int DOWNLOAD_NOTIF_ID = 100001;

	public static final int ADD_OFFLINE_PARAM = 1;
	public static final int REMOVE_OFFLINE_PARAM = 2;
	public static final int REFRESH_OFFLINE_PARAM = 3;

	/**
	 * @param name
	 */
	public OfflineHandleService() {
		super("PiCorner offline view photo service"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		IOfflineViewParameter param = (IOfflineViewParameter) intent
				.getSerializableExtra(IOfflineViewParameter.OFFLINE_PARAM_INTENT_KEY);
		int actionType = intent
				.getIntExtra(
						IOfflineViewParameter.OFFLINE_PARAM_INTENT_ADD_REMOVE_REFRESH_KEY,
						ADD_OFFLINE_PARAM);
		if (param == null) {
			Log.d(TAG, "charging, start the download process."); //$NON-NLS-1$
			downlaodPhotos(param);
		} else {
			if (actionType != REFRESH_OFFLINE_PARAM)
				manageRepository(param, actionType == ADD_OFFLINE_PARAM ? true
						: false);
			else {
				if (BuildConfig.DEBUG)
					Log.d(TAG, "Refresh the given offline parameter."); //$NON-NLS-1$
				downlaodPhotos(param);
			}
		}
	}

	private void downlaodPhotos(IOfflineViewParameter offline) {

		// check network connection type
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork.isConnectedOrConnecting();
		boolean isWifi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
		if (!isConnected || !isWifi) {
			Log.d(TAG, "Not wifi, don't start the offline download process."); //$NON-NLS-1$
			return;
		}

		// shows the notification on status bar.
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(getString(R.string.app_name))
				.setContentText(getString(R.string.msg_offline_downloading));
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(DOWNLOAD_NOTIF_ID,
				mBuilder.getNotification());

		List<IOfflineViewParameter> params = OfflineControlFileUtil
				.getExistingOfflineParameters();
		if (params == null) {
			Log.w(TAG, "repository file not found."); //$NON-NLS-1$
			return;
		}

		if (offline != null) {
			if (params.contains(offline)) {
				int pos = params.indexOf(offline);
				offline = params.get(pos);
				((AbstractOfflineParameter) offline).setLastUpdateTime(System
						.currentTimeMillis());
				processOfflineParameter(offline, true);
			} else {
				// not enabled, just return
				return;
			}
		} else {
			for (IOfflineViewParameter param : params) {
				this.processOfflineParameter(param, false);
			}
		}
		try {
			OfflineControlFileUtil.save(params);
		} catch (Exception e) {
			if (BuildConfig.DEBUG)
				Log.w(TAG,
						"error to save offline control file: " + e.getMessage()); //$NON-NLS-1$
		}
	}

	/**
	 * Manages the repository.
	 * 
	 * @param param
	 * @param add
	 */
	private void manageRepository(IOfflineViewParameter param, boolean add) {
		Log.d(TAG, param.toString());
		List<IOfflineViewParameter> params = OfflineControlFileUtil
				.getExistingOfflineParameters();
		if (params == null) {
			params = new ArrayList<IOfflineViewParameter>();
		}

		if (add) {
			if (!params.contains(param))
				params.add(0, param);
		} else {
			params.remove(param);
		}
		try {
			OfflineControlFileUtil.save(params);
		} catch (Exception e) {
			Log.w(TAG, e.getMessage());
		}
	}

	/**
	 * Starts the process to get the photo collection information, and download
	 * large image
	 * 
	 * @param param
	 */
	private void processOfflineParameter(IOfflineViewParameter param,
			boolean doitnow) {
		if (doitnow || longerThanFiveHours(param)) {
			// do it.
			IOfflinePhotoCollectionProcessor p = param
					.getPhotoCollectionProcessor();
			((AbstractOfflineParameter) param).setLastUpdateTime(System
					.currentTimeMillis());
			p.process(this, param);
		}
	}

	private boolean longerThanFiveHours(IOfflineViewParameter param) {
		long lastUpdateTime = param.getLastUpdateTime();
		if (lastUpdateTime == 0) {
			if (BuildConfig.DEBUG)
				Log.d(TAG,
						"the offline parameter has no last update time saved."); //$NON-NLS-1$
			return true;
		}

		long delta = System.currentTimeMillis() - lastUpdateTime;
		boolean ret = delta > 5 * 60 * 60 * 1000;
		if (BuildConfig.DEBUG)
			Log.d(TAG, "longer than 5 hours? " + Boolean.toString(ret)); //$NON-NLS-1$
		return ret;
	}

	@Override
	public void onDestroy() {
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(DOWNLOAD_NOTIF_ID);
		super.onDestroy();
	}

}
