/**
 * 
 */
package com.gmail.charleszq.picorner.service.ig;

import org.jinstagram.AdvancedInstagram;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;

import android.util.Log;

import com.gmail.charleszq.picorner.model.MediaObjectCollection;
import com.gmail.charleszq.picorner.utils.InstagramHelper;
import com.gmail.charleszq.picorner.utils.ModelUtils;

/**
 * @author Charles(charleszq@gmail.com)
 * 
 */
public class InstagramPopularsService extends AbstractInstagramPhotoListService {

	@Override
	public MediaObjectCollection getPhotos(int pageSize, int pageNo)
			throws Exception {
		Log.d(TAG, String.format("page size %s and page# %s", pageSize, pageNo)); //$NON-NLS-1$
		MediaObjectCollection pc = new MediaObjectCollection();
		AdvancedInstagram ig = InstagramHelper.getInstance().getInstagram();

		MediaFeed mf = null;
		if (pageNo <= 0 || mPagination == null) {
			mf = ig.getPopularMedia(pageSize);
		} else {
			mf = ig.getNextPage(mPagination, pageSize);
		}

		if (mf != null) {
			mPagination = mf.getPagination();
			for (MediaFeedData feed : mf.getData()) {
				pc.addPhoto(ModelUtils.convertInstagramPhoto(feed));
			}
		}

		return pc;
	}

}
