/**
 * 
 */
package com.gmail.charleszq.picorner.ui.command;

import android.content.Context;

/**
 * @author Charles(charleszq@gmail.com)
 * 
 */
public class MenuSectionHeaderCommand extends AbstractCommand<String> {

	private String mLabel;
	private boolean mFiltering = false;

	/**
	 * @param context
	 */
	public MenuSectionHeaderCommand(Context context, String label) {
		this(context, label, false);
	}
	
	public MenuSectionHeaderCommand(Context context, String label, boolean hide) {
		super(context);
		this.mLabel = label;
		this.mFiltering = hide;
	}

	@Override
	public boolean execute(Object... objects) {
		return false;
	}

	@Override
	public int getIconResourceId() {
		return 0;
	}

	@Override
	public String getLabel() {
		return mLabel;
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.MENU_HEADER_CMD;
	}
	
	public boolean isFiltering() {
		return mFiltering;
	}

	@Override
	public Object getAdapter(Class<?> adapterClass) {
		if( adapterClass == Boolean.class ) {
			return mFiltering;
		}
		return super.getAdapter(adapterClass);
	}
	
	/**
	 * Sets the mark to say now this menu item was filtering its sub items or not. usually, this will be called
	 * in the main menu text filter, that is, when doing the text fitler, we will need say that each header is now
	 * filtering.
	 * @param filtering
	 */
	public void setFiltering(boolean filtering) {
		this.mFiltering = filtering;
	}
	

}
