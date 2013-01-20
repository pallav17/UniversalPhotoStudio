/**
 * 
 */
package com.gmail.charleszq.picorner.offline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

/**
 * @author charleszq
 * 
 */
public class BatteryStatusReceiver extends BroadcastReceiver {

	private static final String TAG = BatteryStatusReceiver.class
			.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {

		int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		float batteryPct = level / (float) scale;
		Log.d(TAG, "battery percentage: " + batteryPct); //$NON-NLS-1$

		if (batteryPct * 100 > 20f) {
			Intent service = new Intent(context, OfflineHandleService.class);
			context.startService(service);
		}
	}

}