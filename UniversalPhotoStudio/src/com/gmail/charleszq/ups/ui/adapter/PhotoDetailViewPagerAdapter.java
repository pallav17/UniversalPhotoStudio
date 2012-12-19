package com.gmail.charleszq.ups.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gmail.charleszq.ups.R;
import com.gmail.charleszq.ups.model.MediaObject;
import com.gmail.charleszq.ups.ui.PhotoDetailCommentsFragment;
import com.gmail.charleszq.ups.ui.PhotoDetailGeneralFragment;
import com.gmail.charleszq.ups.ui.PhotoDetailLikesFragment;
import com.gmail.charleszq.ups.ui.PhotoDetailMapFragment;
import com.gmail.charleszq.ups.ui.flickr.FlickrExifDataFragment;
import com.gmail.charleszq.ups.ui.px500.Px500ExifDataFragment;

public class PhotoDetailViewPagerAdapter extends FragmentPagerAdapter {

	private MediaObject mPhoto;
	private Context mContext;

	public PhotoDetailViewPagerAdapter(FragmentManager fragmentManager,
			MediaObject photo, Context context) {
		super(fragmentManager);
		mPhoto = photo;
		mContext = context;
	}

	@Override
	public Fragment getItem(int pos) {
		List<Fragment> fragments = new ArrayList<Fragment>();
		switch (mPhoto.getMediaSource()) {
		case FLICKR:
			fragments.add(PhotoDetailGeneralFragment.newInstance(mPhoto));
			fragments.add(PhotoDetailCommentsFragment.newInstance(mPhoto));
			fragments.add(FlickrExifDataFragment.newInstance(mPhoto));
			fragments.add(PhotoDetailLikesFragment.newInstance(mPhoto));
			fragments.add(PhotoDetailMapFragment.newMyInstance(mPhoto));
			break;
		case INSTAGRAM:
			fragments.add(PhotoDetailGeneralFragment.newInstance(mPhoto));
			fragments.add(PhotoDetailCommentsFragment.newInstance(mPhoto));
			fragments.add(PhotoDetailLikesFragment.newInstance(mPhoto));
			fragments.add(PhotoDetailMapFragment.newMyInstance(mPhoto));
			break;
		case PX500:
			fragments.add(PhotoDetailGeneralFragment.newInstance(mPhoto));
			fragments.add(PhotoDetailCommentsFragment.newInstance(mPhoto));
			fragments.add(Px500ExifDataFragment.newInstance(mPhoto));
			fragments.add(PhotoDetailMapFragment.newMyInstance(mPhoto));
			break;
		}
		return fragments.get(pos);
	}

	@Override
	public int getCount() {

		int count = 0;
		switch (mPhoto.getMediaSource()) {
		case INSTAGRAM:
			count = 4;
			break;
		case FLICKR:
			count = 5;
			break;
		case PX500:
			count = 4;
			if (mPhoto.getExifs().isEmpty()) {
				count = 3;
			}
			break;
		}
		if (mPhoto.getLocation() == null) {
			count--;
		}
		return count >= 0 ? count : 0;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		List<CharSequence> titles = new ArrayList<CharSequence>();
		switch (mPhoto.getMediaSource()) {
		case FLICKR:
			titles.add(mContext.getString(R.string.flickr_detail_general_title));
			titles.add(mContext
					.getString(R.string.flickr_detail_comments_title));
			titles.add(mContext.getString(R.string.flickr_detail_exif_title));
			titles.add(mContext.getString(R.string.ig_like_users));
			titles.add(mContext.getString(R.string.flickr_detail_map));
			break;
		case INSTAGRAM:
			titles.add(mContext.getString(R.string.flickr_detail_general_title));
			titles.add(mContext
					.getString(R.string.flickr_detail_comments_title));
			titles.add(mContext.getString(R.string.ig_like_users));
			titles.add(mContext.getString(R.string.flickr_detail_map));
			break;
		case PX500:
			titles.add(mContext.getString(R.string.flickr_detail_general_title));
			titles.add(mContext
					.getString(R.string.flickr_detail_comments_title));
			if (!mPhoto.getExifs().isEmpty())
				titles.add(mContext
						.getString(R.string.flickr_detail_exif_title));
			titles.add(mContext.getString(R.string.flickr_detail_map));
			break;
		}
		return titles.get(position);
	}

}
