/**
 * 
 */
package com.gmail.charleszq.picorner.ui.px500;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.github.yuyang226.j500px.photos.PhotoCategory;
import com.gmail.charleszq.picorner.model.Author;
import com.gmail.charleszq.picorner.task.IGeneralTaskDoneListener;
import com.gmail.charleszq.picorner.task.px500.Px500FetchFriendsTask;
import com.gmail.charleszq.picorner.ui.command.px500.AbstractPx500PhotoListCommand;
import com.gmail.charleszq.picorner.ui.helper.AbstractHiddenListView;

/**
 * @author charles(charleszq@gmail.com)
 * 
 */
public class Px500FriendsView extends AbstractHiddenListView {

	private Px500FetchFriendsTask task;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gmail.charleszq.picorner.ui.AbstractContactsView#getContactList(android
	 * .content.Context)
	 */
	@Override
	protected void getData(Context ctx) {
		task = new Px500FetchFriendsTask(ctx);
		task.addTaskDoneListener(new IGeneralTaskDoneListener<List<Author>>() {

			@Override
			public void onTaskDone(List<Author> result) {
				if (result != null) {
					mAdapter.populateData(result);
					mSpace.setVisibility(View.GONE);
				}
			}
		});
		task.execute();
	}

	@Override
	protected void onCancel() {
		if (task != null)
			task.cancel(true);
	}

	@Override
	public void onItemClick(AdapterView<?> parentView, View view, int position,
			long id) {
		if( AbstractPx500PhotoListCommand.class.isInstance(mCommand)) {
			((AbstractPx500PhotoListCommand) mCommand)
					.setPhotoCategory(PhotoCategory.Uncategorized);
		}
		super.onItemClick(parentView, view, position, id);
	}
	
	

}
