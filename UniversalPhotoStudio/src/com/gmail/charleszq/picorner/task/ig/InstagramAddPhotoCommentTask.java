/**
 * 
 */
package com.gmail.charleszq.picorner.task.ig;

import org.jinstagram.Instagram;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.gmail.charleszq.picorner.PicornerApplication;
import com.gmail.charleszq.picorner.task.AbstractContextAwareTask;
import com.gmail.charleszq.picorner.utils.InstagramHelper;

/**
 * @author charles(charleszq@gmail.com)
 * 
 */
public class InstagramAddPhotoCommentTask extends
		AbstractContextAwareTask<String, Integer, Boolean> {

	public InstagramAddPhotoCommentTask(Context ctx) {
		super(ctx);
	}

	@Override
	protected Boolean doInBackground(String... params) {
		String photoId = params[0];
		String comment = params[1];

		int index = photoId.indexOf("_"); //$NON-NLS-1$
		if (index != -1) {
			photoId = photoId.substring(0, index);
			Log.d(TAG, "instagram media id: " + photoId); //$NON-NLS-1$
		}

		PicornerApplication app = (PicornerApplication) ((Activity) mContext)
				.getApplication();
		Instagram ig = InstagramHelper.getInstance().getAuthedInstagram(
				app.getInstagramAuthToken());
		try {
			ig.setMediaComments(Long.valueOf(photoId), comment);
			return true;
		} catch (Exception e) {
			Log.w(TAG,"cannot add comment: " + e.getMessage()); //$NON-NLS-1$
		}
		return false;
	}

}
