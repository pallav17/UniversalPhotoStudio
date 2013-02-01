/**
 * 
 */
package com.gmail.charleszq.picorner.ui.flickr;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.gmail.charleszq.picorner.model.Author;
import com.gmail.charleszq.picorner.task.IGeneralTaskDoneListener;
import com.gmail.charleszq.picorner.task.flickr.FlickrFriendsTask;
import com.gmail.charleszq.picorner.ui.helper.AbstractContactsView;

/**
 * @author charles(charleszq@gmail.com)
 * 
 */
public class FlickrContactsView extends AbstractContactsView {
	
	private FlickrFriendsTask task;

	@Override
	protected void getContactList(Context ctx) {
		task = new FlickrFriendsTask(ctx);
		task.addTaskDoneListener(new IGeneralTaskDoneListener<List<Author>>() {

			@Override
			public void onTaskDone(List<Author> result) {
				if (result != null) {
					mAdapter.populateFriends(result);
					mSpace.setVisibility(View.GONE);
				}
			}
		});
		task.execute();
	}

	@Override
	protected void onCancel() {
		if( task != null )
			task.cancel(true);
	}

}
