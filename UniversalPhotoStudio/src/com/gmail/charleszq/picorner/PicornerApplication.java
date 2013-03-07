/**
 * 
 */

package com.gmail.charleszq.picorner;

import java.io.File;

import org.jinstagram.auth.model.Token;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.gmail.charleszq.picorner.model.Author;
import com.gmail.charleszq.picorner.model.MediaObject;
import com.gmail.charleszq.picorner.offline.OfflineHandleService;
import com.gmail.charleszq.picorner.utils.IConstants;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Represents the main application.
 * 
 * @author charles
 */
public class PicornerApplication extends Application {

	private static final String TAG = PicornerApplication.class.getSimpleName();
	private static final String FIRST_TIME_KEY = "first.time"; //$NON-NLS-1$
	private static final String IS_LICENSED = "isLicensed"; //$NON-NLS-1$
	private static final String OFFLINE_SCHEDULED = "offline.download.scheduled"; //$NON-NLS-1$

	@Override
	public void onCreate() {
		super.onCreate();

		initializesImageLoader();
		enableHttpResponseCache();
		scheduleOfflineDownload(false);

	}

	private PendingIntent getOfflineServicePendingIntent() {
		Intent offlineIntent = new Intent(this, OfflineHandleService.class);
		PendingIntent photoPendingIntent = PendingIntent.getService(
				getApplicationContext(), 0, offlineIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		return photoPendingIntent;
	}

	/**
	 * Initializes the image loader.
	 */
	public void initializesImageLoader() {
		String cacheSize = getSharedPreferenceValue(
				IConstants.PREF_PHOTO_CACHE_SIZE,
				String.valueOf(IConstants.IMAGE_CACHE_SIZE));
		// initializes the image loader
		ImageLoader imageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPoolSize(9).memoryCache(new WeakMemoryCache())
				.threadPriority(Thread.NORM_PRIORITY + 1)
				.discCacheSize(Integer.parseInt(cacheSize)).build();
		imageLoader.init(config);
		if (BuildConfig.DEBUG)
			Log.d(TAG, "image cache size: " + cacheSize); //$NON-NLS-1$
	}

	/**
	 * Schedules the offline download time span.
	 */
	public void scheduleOfflineDownload(boolean cancel) {

		SharedPreferences sp = getSharedPreferences(IConstants.DEF_PREF_NAME,
				Context.MODE_APPEND);
		boolean scheduledBefore = sp.getBoolean(OFFLINE_SCHEDULED, false);
		if (scheduledBefore && !cancel) {
			return;
		}

		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		PendingIntent pendingIntent = getOfflineServicePendingIntent();
		if (cancel)
			am.cancel(pendingIntent);

		String span = getSharedPreferenceValue(
				IConstants.PREF_OFFLINE_TIMER_IN_HOURS, "24"); //$NON-NLS-1$

		// start 5 min from now, and repeat every 24 hours
		am.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis() + 5 * 60 * 1000L,
				Integer.valueOf(span) * 60 * 60 * 1000L, pendingIntent);
		// save the marker
		Editor editor = sp.edit();
		editor.putBoolean(OFFLINE_SCHEDULED, true);
		editor.commit();

		if (BuildConfig.DEBUG)
			Log.d(TAG, String.format(
					"offline download scheduled, once every %s hours.", span)); //$NON-NLS-1$
	}

	private void enableHttpResponseCache() {
		try {
			long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
			File httpCacheDir = new File(getCacheDir(), "http"); //$NON-NLS-1$
			android.net.http.HttpResponseCache.install(httpCacheDir,
					httpCacheSize);
		} catch (Exception httpResponseCacheNotAvailable) {
		}
	}

	public void saveFlickrAuthToken(OAuth oauth) {

		// save the token information.
		SharedPreferences sp = getSharedPreferences(IConstants.DEF_PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		String oauthToken = null;
		String tokenSecret = null;
		String userId = null;
		String userName = null;
		if (oauth != null) {
			oauthToken = oauth.getToken().getOauthToken();
			tokenSecret = oauth.getToken().getOauthTokenSecret();
			userId = oauth.getUser().getId();
			userName = oauth.getUser().getUsername();
		}
		editor.putString(IConstants.FLICKR_TOKEN, oauthToken);
		editor.putString(IConstants.FLICKR_TOKEN_SECRENT, tokenSecret);
		editor.putString(IConstants.FLICKR_USER_ID, userId);
		editor.putString(IConstants.FLICKR_USER_NAME, userName);
		editor.commit();
	}

	public void saveFlickrTokenSecret(String tokenSecrent) {
		SharedPreferences sp = getSharedPreferences(IConstants.DEF_PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(IConstants.FLICKR_TOKEN_SECRENT, tokenSecrent);
		editor.commit();
	}

	/**
	 * Returns the saved value in the shared preferences.
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	private String getSharedPreferenceValue(String key, String defaultValue) {
		SharedPreferences sp = getSharedPreferences(IConstants.DEF_PREF_NAME,
				Context.MODE_APPEND);
		String value = sp.getString(key, defaultValue);
		return value;
	}

	public Token getInstagramAuthToken() {
		String token = getSharedPreferenceValue(IConstants.IG_AUTH_TOKEN, null);
		if (token == null) {
			return null;
		}
		String secret = getSharedPreferenceValue(
				IConstants.IG_AUTH_TOKEN_SECRET, null);
		String rawResponse = getSharedPreferenceValue(
				IConstants.IG_AUTH_TOKEN_RAW_RES, null);
		Token t = new Token(token, secret, rawResponse);
		return t;
	}

	public void saveInstagramAuthToken(long userId, String token,
			String secret, String rawResponse, String url) {
		SharedPreferences sp = getSharedPreferences(IConstants.DEF_PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(IConstants.IG_USER_ID, String.valueOf(userId));
		editor.putString(IConstants.IG_AUTH_TOKEN, token);
		editor.putString(IConstants.IG_AUTH_TOKEN_SECRET, secret);
		editor.putString(IConstants.IG_AUTH_TOKEN_RAW_RES, rawResponse);
		editor.putString(IConstants.IG_USER_BUDDY_ICON_URL, url);
		editor.commit();
	}

	public boolean isFirstTime() {
		return getSharedPreferenceValue(FIRST_TIME_KEY, null) == null;
	}

	public boolean isLicensed() {
		return Boolean.valueOf(getSharedPreferenceValue(IS_LICENSED,
				Boolean.FALSE.toString()));
	}

	public void setFirstTimeFalse() {
		SharedPreferences sp = getSharedPreferences(IConstants.DEF_PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(FIRST_TIME_KEY, Boolean.TRUE.toString());
		editor.commit();
	}

	public void setLicensedTrue() {
		SharedPreferences sp = getSharedPreferences(IConstants.DEF_PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(IS_LICENSED, Boolean.TRUE.toString());
		editor.commit();
	}

	public boolean isMyOwnPhoto(MediaObject photo) {
		boolean result = false;

		Author a = photo.getAuthor();
		if (a != null) {
			switch (photo.getMediaSource()) {
			case FLICKR:
				result = a.getUserId().equals(SPUtil.getFlickrUserId(getApplicationContext()));
				break;
			case INSTAGRAM:
				result = a.getUserId().equals(SPUtil.getInstagramUserId(getApplicationContext()));
				break;
			case PX500:
				Author me = getPxUserProfile();
				if (me == null) {
					result = true;
				} else {
					result = a.getUserId().equals(me.getUserId());
				}
				break;
			}
		}
		return result;
	}

	public void savePx500TokenSecret(String oauthTokenSecret) {
		SharedPreferences sp = getSharedPreferences(IConstants.DEF_PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(IConstants.PX500_TOKEN_SECRET_KEY, oauthTokenSecret);
		editor.commit();
	}

	public void savePxAuthToken(
			com.github.yuyang226.j500px.oauth.OAuthToken token) {
		SharedPreferences sp = getSharedPreferences(IConstants.DEF_PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(IConstants.PX500_OAUTH_TOKEN_KEY,
				token.getOauthToken());
		editor.putString(IConstants.PX500_OAUTH_TOKEN_SECRET_KEY,
				token.getOauthTokenSecret());
		editor.commit();
	}

	public Author getPxUserProfile() {
		String userId = getSharedPreferenceValue(IConstants.PX_USER_ID, null);
		if (userId == null) {
			return null;
		}

		Author a = new Author();
		a.setUserId(userId);
		a.setUserName(getSharedPreferenceValue(IConstants.PX_USER_NAME, null));
		a.setBuddyIconUrl(getSharedPreferenceValue(
				IConstants.PX_USER_BUDDY_ICON_URL, null));
		return a;
	}

	public void savePxUserProfile(String id, String name, String url) {
		SharedPreferences sp = getSharedPreferences(IConstants.DEF_PREF_NAME,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(IConstants.PX_USER_ID, id);
		editor.putString(IConstants.PX_USER_NAME, name);
		editor.putString(IConstants.PX_USER_BUDDY_ICON_URL, url);
		editor.commit();
	}

	@Override
	public void onTerminate() {
		ImageLoader.getInstance().stop();
		super.onTerminate();
	}
}
