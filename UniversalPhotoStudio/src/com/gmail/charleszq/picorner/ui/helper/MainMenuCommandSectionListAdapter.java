/**
 * 
 */
package com.gmail.charleszq.picorner.ui.helper;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.charleszq.picorner.R;
import com.gmail.charleszq.picorner.offline.IOfflineViewParameter;
import com.gmail.charleszq.picorner.offline.OfflineControlFileUtil;
import com.gmail.charleszq.picorner.offline.OfflineHandleService;
import com.gmail.charleszq.picorner.ui.command.ICommand;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author charles(charleszq@gmail.com)
 * 
 */
public class MainMenuCommandSectionListAdapter extends
		AbstractCommandSectionListAdapter {

	public MainMenuCommandSectionListAdapter(Context ctx, ImageLoader fetcher,
			boolean showHeaderMarker) {
		super(ctx, fetcher, showHeaderMarker);
	}

	public MainMenuCommandSectionListAdapter(Context ctx, ImageLoader fetcher) {
		super(ctx, fetcher);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		ICommand<?> command = (ICommand<?>) getItem(position);
		prepareBackView(view, command);
		return view;
	}

	private void prepareBackView(final View view, final ICommand<?> command) {
		final IOfflineViewParameter offline = (IOfflineViewParameter) command
				.getAdapter(IOfflineViewParameter.class);
		if (offline != null) {
			// prepare the back view
			View bv = view.findViewById(R.id.menu_item_back_view);
			if (bv == null) {
				bv = LayoutInflater.from(mContext).inflate(
						R.layout.main_menu_item_backview, null);
				((ViewGroup) view).addView(bv);
			}
			final View backView = bv;
			backView.setVisibility(View.INVISIBLE);

			// get the front view
			final View frontView = view
					.findViewById(R.id.main_menu_item_front_view);

			// hook up action item with click listener.
			prepareOfflineActionItem(backView, frontView, command, offline);

			// hook the action on the setting icon
			ImageView settingButton = (ImageView) view
					.findViewById(R.id.btn_offline_settings);
			settingButton.setVisibility(View.VISIBLE);
			settingButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					frontView.setVisibility(View.INVISIBLE);
					backView.setVisibility(View.VISIBLE);
					ObjectAnimator.ofFloat(backView, "alpha", 0f, 1f) //$NON-NLS-1$
							.setDuration(1000).start();
				}
			});
		}
	}

	private void prepareOfflineActionItem(final View backView,
			final View frontView, ICommand<?> command,
			final IOfflineViewParameter offline) {
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_offline_back:
					backView.setVisibility(View.INVISIBLE);
					frontView.setVisibility(View.VISIBLE);
					ObjectAnimator.ofFloat(frontView, "alpha", 0f, 1f) //$NON-NLS-1$
							.setDuration(1000).start();
					break;
				case R.id.btn_offline_refresh:
					boolean isOfflineEnabled = OfflineControlFileUtil
							.isOfflineViewEnabled(mContext,offline);
					if (!isOfflineEnabled) {
						Toast.makeText(
								mContext,
								mContext.getString(R.string.msg_pls_enable_offline_first),
								Toast.LENGTH_SHORT).show();
						break;
					}
					Intent serviceIntent = new Intent(mContext,
							OfflineHandleService.class);
					serviceIntent.putExtra(
							IOfflineViewParameter.OFFLINE_PARAM_INTENT_KEY,
							offline);
					serviceIntent
							.putExtra(
									IOfflineViewParameter.OFFLINE_PARAM_INTENT_ADD_REMOVE_REFRESH_KEY,
									OfflineHandleService.REFRESH_OFFLINE_PARAM);
					mContext.startService(serviceIntent);
					break;
				case R.id.btn_offline_slide_show:
					//TODO implement in next release.
					break;
				}
			}
		};

		TextView btnBack = (TextView) backView
				.findViewById(R.id.btn_offline_back);
		btnBack.setOnClickListener(listener);

		CheckBox btnOffline = (CheckBox) backView
				.findViewById(R.id.btn_enable_offline);
		btnOffline.setChecked(OfflineControlFileUtil
				.isOfflineViewEnabled(mContext,offline));
		btnOffline.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Intent serviceIntent = new Intent(mContext,
						OfflineHandleService.class);
				serviceIntent
						.putExtra(
								IOfflineViewParameter.OFFLINE_PARAM_INTENT_KEY,
								offline);
				serviceIntent
						.putExtra(
								IOfflineViewParameter.OFFLINE_PARAM_INTENT_ADD_REMOVE_REFRESH_KEY,
								isChecked ? OfflineHandleService.ADD_OFFLINE_PARAM
										: OfflineHandleService.REMOVE_OFFLINE_PARAM);
				mContext.startService(serviceIntent);
			}
		});

		TextView btnRefresh = (TextView) backView
				.findViewById(R.id.btn_offline_refresh);
		btnRefresh.setOnClickListener(listener);

		TextView btnSlideShow = (TextView) backView
				.findViewById(R.id.btn_offline_slide_show);
		btnSlideShow.setOnClickListener(listener);
	}
}