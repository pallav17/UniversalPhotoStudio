/**
 * 
 */
package com.gmail.charleszq.picorner.task.px500;

import android.app.Activity;
import android.content.Context;

import com.github.yuyang226.j500px.J500px;
import com.github.yuyang226.j500px.J500pxException;
import com.gmail.charleszq.picorner.PicornerApplication;
import com.gmail.charleszq.picorner.task.AbstractContextAwareTask;
import com.gmail.charleszq.picorner.utils.J500pxHelper;

/**
 * @author charles(charleszq@gmail.com)
 * 
 */
public class Px500AddCommentTask extends
		AbstractContextAwareTask<String, Void, Boolean> {

	public Px500AddCommentTask(Context ctx) {
		super(ctx);
	}

	@Override
	protected Boolean doInBackground(String... params) {
		String photoId = params[0];
		String comment = params[1];

		PicornerApplication app = (PicornerApplication) ((Activity) mContext)
				.getApplication();
		J500px px = J500pxHelper.getJ500pxAuthedInstance(
				app.getPx500OauthToken(), app.getPx500OauthTokenSecret());
		try {
			px.getPhotosInterface().commentPhoto(photoId, comment);
			return true;
		} catch (J500pxException e) {
			return false;
		}

	}

}
