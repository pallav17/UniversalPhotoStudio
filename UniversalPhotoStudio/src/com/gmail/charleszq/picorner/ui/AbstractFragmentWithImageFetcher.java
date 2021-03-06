/**
 * 
 */
package com.gmail.charleszq.picorner.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author charles(charleszq@gmail.com)
 * 
 */
public abstract class AbstractFragmentWithImageFetcher extends Fragment {

	protected ImageLoader mImageFetcher;
	protected String TAG = getClass().getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageFetcher = ImageLoader.getInstance();
	}
}
