/**
 * 
 */
package com.gmail.charleszq.picorner.ui.helper;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.gmail.charleszq.picorner.R;
import com.gmail.charleszq.picorner.model.Author;
import com.gmail.charleszq.picorner.ui.command.ICommand;

/**
 * @author charles(charleszq@gmail.com)
 * 
 */
public abstract class AbstractContactsView extends AbstractHiddenView implements
		OnItemClickListener {

	protected ListView			mListView;
	protected Button			mCancelButton;
	protected FriendListAdapter	mAdapter;
	protected View				mView;
	protected SearchView		mSearchView;
	
	private SearchView.OnQueryTextListener	mQueryTextListener	= new SearchView.OnQueryTextListener() {

		@Override
		public boolean onQueryTextSubmit(
				String query) {
			if (query == null
					|| query.trim()
							.length() == 0)
				return false;
			FriendListFilter filter = new FriendListFilter(mAdapter);
			filter.filter(query);
			return true;
		}

		@Override
		public boolean onQueryTextChange(
				String newText) {
			if (newText == null
					|| newText
							.trim()
							.length() == 0) {
				mAdapter.mFilteredOutFriends.clear();
				mAdapter.mFilteredOutFriends.addAll(mAdapter.mFriends);
				mAdapter.notifyDataSetChanged();
				return true;
			} else
				return false;
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gmail.charleszq.picorner.ui.helper.IHiddenView#init(com.gmail.charleszq
	 * .picorner.ui.command.ICommand,
	 * com.gmail.charleszq.picorner.ui.helper.IHiddenView
	 * .IHiddenViewActionListener)
	 */
	@Override
	public void init(ICommand<?> command, IHiddenViewActionListener listener) {
		super.init(command, listener);
		Context ctx = (Context) command.getAdapter(Context.class);
		mView = getView(ctx);
		View emptyView = mView.findViewById(R.id.empty_friend_view);

		mListView = (ListView) mView.findViewById(R.id.list_f_friends);
		mAdapter = new FriendListAdapter(ctx, command);
		mListView.setEmptyView(emptyView);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);

		mCancelButton = (Button) mView.findViewById(R.id.btn_cancel_friends);
		mCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onAction(ACTION_CANCEL);
				mCancelButton.setVisibility(View.INVISIBLE);
			}
		});

		// filter
		mSearchView = (SearchView) mView.findViewById(R.id.contact_filter);
		mSearchView.setOnQueryTextListener(mQueryTextListener);
		// hide the soft keyboard
		InputMethodManager imm = (InputMethodManager) ctx
				.getSystemService(Service.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

		getContactList(ctx);
	}

	@Override
	public void onItemClick(AdapterView<?> parentView, View view, int position,
			long id) {
		Author friend = (Author) mAdapter.getItem(position);
		onAction(ACTION_JUST_CMD, friend);
	}

	@Override
	public View getView(Context ctx) {
		if (mView == null) {
			mView = LayoutInflater.from(ctx).inflate(R.layout.contacts_list,
					null);
		}
		return mView;
	}

	protected abstract void getContactList(Context ctx);
}