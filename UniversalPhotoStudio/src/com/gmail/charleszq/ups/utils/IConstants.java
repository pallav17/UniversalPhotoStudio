/**
 * 
 */
package com.gmail.charleszq.ups.utils;

/**
 * @author charleszq
 * 
 */
public interface IConstants {

	/**
	 * For each service call, what's the page size, by default, it's 10 times of
	 * the UI page size.
	 */
	static final int SERVICE_PAGE_SIZE = 100;
	
	static final String SD_CARD_FOLDER_NAME = "ups_flickr"; //$NON-NLS-1$
	static final String SHARE_TEMP_FILE_NAME = "ups.share.png"; //$NON-NLS-1$

	/**
	 * the flickr oauth scheme
	 */
	static final String ID_SCHEME = "ups-flickr-oauth"; //$NON-NLS-1$
	static final String DEF_PREF_NAME = "ups_flickr"; //$NON-NLS-1$
	
	static final String IMAGE_CACHE_DIR = "cmdicon"; //$NON-NLS-1$
	
	/**
	 * Instagram oauth scheme, these constants cannot be modified, they are defined in the
	 * client app of instagram.
	 */
	static final String ID_IG_SCHEME = "schema"; //$NON-NLS-1$
	static final String IG_AUTHORITY = "upsigoauth"; //$NON-NLS-1$
	static final String IG_CALL_BACK_STR = "schema://upsigoauth"; //$NON-NLS-1$
	static final String IG_USER_ID = "ig.user.id"; //$NON-NLS-1$
	static final String IG_AUTH_TOKEN = "ig.auth.token"; //$NON-NLS-1$
	static final String IG_AUTH_TOKEN_SECRET = "ig.auth.token.secret"; //$NON-NLS-1$
	static final String IG_AUTH_TOKEN_RAW_RES = "ig.auth.token.raw.response"; //$NON-NLS-1$
	
	//flickr
	static final String FLICKR_TOKEN_SECRENT = "token.secret"; //$NON-NLS-1$
	static final String FLICKR_TOKEN = "flickr.token"; //$NON-NLS-1$
	static final String FLICKR_USER_ID = "flickr.user.id"; //$NON-NLS-1$
	static final String FLICKR_USER_NAME = "flickr.user.name"; //$NON-NLS-1$
	
	//instagram
	static final String INSTAGRAM_CLIENT_ID = "2588f081b4c2432cbfcc5d27aef4fecb"; //$NON-NLS-1$
	static final String INSTAGRAM_CLIENT_SECRET = "76ab8f0afb954671bf28ec80fca75ad6"; //$NON-NLS-1$
	

}
