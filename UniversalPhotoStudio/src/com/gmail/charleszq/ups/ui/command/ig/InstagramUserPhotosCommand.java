/**
 * 
 */
package com.gmail.charleszq.ups.ui.command.ig;

import org.jinstagram.auth.model.Token;

import android.app.Activity;
import android.content.Context;

import com.gmail.charleszq.ups.R;
import com.gmail.charleszq.ups.UPSApplication;
import com.gmail.charleszq.ups.model.Author;
import com.gmail.charleszq.ups.service.IPhotoService;
import com.gmail.charleszq.ups.service.ig.InstagramUserPhotosService;
import com.gmail.charleszq.ups.ui.command.PhotoListCommand;

/**
 * @author charles(charleszq@gmail.com)
 * 
 */
public class InstagramUserPhotosCommand extends PhotoListCommand {

	private Author mUser;

	/**
	 * @param context
	 */
	public InstagramUserPhotosCommand(Context context, Author igUser) {
		super(context);
		this.mUser = igUser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gmail.charleszq.ups.ui.command.ICommand#getIconResourceId()
	 */
	@Override
	public int getIconResourceId() {
		return R.drawable.ic_action_my_ig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gmail.charleszq.ups.ui.command.ICommand#getLabel()
	 */
	@Override
	public String getLabel() {
		return mContext.getString(R.string.ig_my_photos);
	}

	@Override
	public Object getAdapter(Class<?> adapterClass) {
		if (adapterClass == IPhotoService.class) {
			if (mCurrentPhotoService == null) {
				UPSApplication app = (UPSApplication) ((Activity) mContext)
						.getApplication();
				Token token = app.getInstagramAuthToken();
				mCurrentPhotoService = new InstagramUserPhotosService(token,
						Long.parseLong(mUser.getUserId()));
			}
			return mCurrentPhotoService;
		}
		return super.getAdapter(adapterClass);
	}
	
	@Override
	public String getDescription() {
		String s = mContext.getString(R.string.cd_ig_user_photos);
		return String.format(s, mUser.getUserName());
	}
}