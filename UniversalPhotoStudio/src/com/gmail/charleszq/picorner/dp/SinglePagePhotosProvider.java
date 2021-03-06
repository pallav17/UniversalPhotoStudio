/**
 * 
 */
package com.gmail.charleszq.picorner.dp;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.gmail.charleszq.picorner.model.MediaObject;
import com.gmail.charleszq.picorner.model.MediaObjectCollection;

/**
 * @author Charles(charleszq@gmail.com)
 * 
 */
public class SinglePagePhotosProvider implements IPhotosProvider {

	/**
	 * auto gen sid
	 */
	private static final long serialVersionUID = 1771926524223469593L;

	/**
	 * Log tag
	 */
	private static final String TAG = SinglePagePhotosProvider.class.getSimpleName();

	/**
	 * The photo list.
	 */
	private List<MediaObject> mPhotos;
	
	/**
	 * The current source which populate photos into this, usually, the command.
	 */
	transient private Object mCurrentSource = null;
	transient private Object mComparator = null;

	public SinglePagePhotosProvider(MediaObjectCollection photos) {
		loadData(photos, null, null );
	}

	@Override
	public MediaObject getMediaObject(int index) {
		return mPhotos.get(index);
	}

	@Override
	public int getCurrentSize() {
		return mPhotos.size();
	}

	@Override
	public void loadData(MediaObjectCollection list, Object source, Object comparator ) {
		if (list == null) {
			return;
		}
		if (mPhotos == null)
			mPhotos = new ArrayList<MediaObject>();
		
		boolean reload = false;
		if (source != mCurrentSource) {
			reload = true;
		} else {
			if( mComparator != null )
				reload = !mComparator.equals(comparator);
			else if( comparator != null )
				reload = !comparator.equals(mComparator);
		}
		
		if( reload ) {
			Log.d(TAG, String.format(
					"before clear previous photos, there were %s in it", //$NON-NLS-1$
					mPhotos.size()));
			mPhotos.clear();
			mCurrentSource = source;
			mComparator = comparator;
		}
		for( MediaObject p : list.getPhotos() ) {
			if(mPhotos.contains(p)) {
				Log.d(TAG, "Duplicate photo."); //$NON-NLS-1$
				continue;
			}
			mPhotos.add(p);
		}
		Log.d(TAG, String.format("now there are %s photos.", mPhotos.size())); //$NON-NLS-1$
	}
}
