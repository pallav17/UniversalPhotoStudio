/**
 * 
 */
package com.gmail.charleszq.picorner.ui.helper;

import android.app.Service;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.gmail.charleszq.picorner.R;
import com.gmail.charleszq.picorner.model.FlickrTagSearchParameter;
import com.gmail.charleszq.picorner.model.FlickrTagSearchParameter.FlickrTagSearchMode;
import com.gmail.charleszq.picorner.ui.command.ICommand;

/**
 * @author charles(charleszq@gmail.com)
 * 
 */
public class FlickrTagSearchView extends LinearLayout implements IHiddenView {

	private Button mCancelButton;
	private Button mSearchButton;
	private EditText mTagText;
	private ICommand<?> mCommand;
	private IHiddenViewActionListener mHideViewCancelListener;
	private RadioButton mRadioAnd, mRadioOr;

	private FlickrTagSearchParameter mSearchParameter;

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == mCancelButton) {
				onAction(ACTION_CANCEL);
			} else if (v == mSearchButton) {
				doSearch();
			} else if (v == mRadioAnd) {
				mSearchParameter.setSearchMode(FlickrTagSearchMode.ALL);
			} else if (v == mRadioOr) {
				mSearchParameter.setSearchMode(FlickrTagSearchMode.ANY);
			}
		}
	};

	/**
	 * @param context
	 */
	public FlickrTagSearchView(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public FlickrTagSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public FlickrTagSearchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void init(ICommand<?> command, IHiddenViewActionListener listener) {
		this.mCommand = command;
		this.mHideViewCancelListener = listener;
		mSearchParameter = new FlickrTagSearchParameter();

		mTagText = (EditText) findViewById(R.id.txt_flickr_tag_search);
		mTagText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					doSearch();
					return true;
				}
				return false;
			}
		});

		mCancelButton = (Button) findViewById(R.id.btn_cancel_search);
		mCancelButton.setOnClickListener(mOnClickListener);

		mSearchButton = (Button) findViewById(R.id.btn_search);
		mSearchButton.setOnClickListener(mOnClickListener);

		mRadioAnd = (RadioButton) findViewById(R.id.radio_and);
		mRadioAnd.setOnClickListener(mOnClickListener);
		mRadioOr = (RadioButton) findViewById(R.id.radio_or);
		mRadioOr.setOnClickListener(mOnClickListener);
	}

	private void doSearch() {
		String s = mTagText.getText().toString();
		if (s == null || s.trim().length() == 0) {
			Toast.makeText(
					getContext(),
					getContext().getString(
							R.string.msg_flickr_tag_search_empty_tag),
					Toast.LENGTH_LONG).show();
			return;
		}

		mSearchParameter.setTags(s);
		onAction(ACTION_DO, mSearchParameter);
	}

	@Override
	public void onAction(int action, Object... data) {
		// hide the soft keyboard
		InputMethodManager imm = (InputMethodManager) getContext()
				.getSystemService(Service.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mTagText.getWindowToken(), 0);

		// notify the listener.
		mHideViewCancelListener.onAction(action, mCommand, this, data);
	}

}