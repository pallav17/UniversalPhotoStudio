/**
 * 
 */
package com.gmail.charleszq.ups.ui.command.px500;

import android.content.Context;

import com.gmail.charleszq.ups.R;
import com.gmail.charleszq.ups.service.IPhotoService;
import com.gmail.charleszq.ups.service.px500.Px500EditorsPhotosService;

/**
 * @author charles(charleszq@gmail.com)
 * 
 */
public class PxEditorsPhotosCommand extends AbstractPx500PhotoListCommand {

	/**
	 * @param context
	 */
	public PxEditorsPhotosCommand(Context context) {
		super(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gmail.charleszq.ups.ui.command.ICommand#getIconResourceId()
	 */
	@Override
	public int getIconResourceId() {
		return R.drawable.ic_action_px500_editors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gmail.charleszq.ups.ui.command.ICommand#getLabel()
	 */
	@Override
	public String getLabel() {
		return mContext.getString(R.string.px_editors);
	}
	
	@Override
	public Object getAdapter(Class<?> adapterClass) {
		if (adapterClass == IPhotoService.class) {
			return new Px500EditorsPhotosService();
		}
		return super.getAdapter(adapterClass);
	}

	@Override
	public String getDescription() {
		return mContext.getString(R.string.cd_500px_editor);
	}
}
