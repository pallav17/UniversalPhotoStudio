/**
 * 
 */
package com.gmail.charleszq.picorner.ui.command.flickr;

import java.util.Comparator;

import android.content.Context;
import android.util.Log;

import com.gmail.charleszq.picorner.BuildConfig;
import com.gmail.charleszq.picorner.R;
import com.gmail.charleszq.picorner.model.FlickrTagSearchParameter;
import com.gmail.charleszq.picorner.service.IPhotoService;
import com.gmail.charleszq.picorner.service.flickr.FlickrTagSearchService;
import com.gmail.charleszq.picorner.ui.command.PhotoListCommand;
import com.gmail.charleszq.picorner.ui.flickr.FlickrTagSearchView;
import com.gmail.charleszq.picorner.ui.helper.IHiddenView;

/**
 * @author charles(charleszq@gmail.com)
 * 
 */
public class FlickrTagSearchCommand extends PhotoListCommand {

	private IHiddenView					mHiddenView;
	private FlickrTagSearchParameter	mCurrentSearchParameter;

	/**
	 * @param context
	 */
	public FlickrTagSearchCommand(Context context) {
		super(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gmail.charleszq.picorner.ui.command.ICommand#getIconResourceId()
	 */
	@Override
	public int getIconResourceId() {
		return android.R.drawable.ic_menu_search;
	}

	@Override
	public Object getAdapter(Class<?> adapterClass) {
		if (adapterClass == IHiddenView.class) {
			if (mHiddenView == null) {
				mHiddenView = new FlickrTagSearchView();
			}
			return mHiddenView;
		}
		if (adapterClass == IPhotoService.class) {
			if (mCurrentPhotoService == null) {
				mCurrentPhotoService = new FlickrTagSearchService();
			}
			((FlickrTagSearchService) mCurrentPhotoService)
					.setSearchParameter(mCurrentSearchParameter);
			return mCurrentPhotoService;
		}
		if (adapterClass == Comparator.class) {
			return this.mCurrentSearchParameter;
		}
		return super.getAdapter(adapterClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gmail.charleszq.picorner.ui.command.ICommand#getLabel()
	 */
	@Override
	public String getLabel() {
		return mContext.getString(R.string.cmd_name_flickr_tag_search);
	}

	@Override
	public boolean execute(Object... params) {
		mCurrentSearchParameter = (FlickrTagSearchParameter) params[0];
		if (BuildConfig.DEBUG)
			Log.d(TAG, mCurrentSearchParameter.toString());
		// parent execute method requires the first parameter is page no
		return super.execute();
	}

	@Override
	public String getDescription() {
		String s = mContext.getString(R.string.cd_flickr_tag_search);
		s = String.format(s,
				mCurrentSearchParameter == null ? "" : mCurrentSearchParameter.getTags()); //$NON-NLS-1$
		return s;
	}

}
