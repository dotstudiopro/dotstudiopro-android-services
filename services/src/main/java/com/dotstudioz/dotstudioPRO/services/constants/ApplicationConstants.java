package com.dotstudioz.dotstudioPRO.services.constants;

import android.util.DisplayMetrics;
import android.widget.AutoCompleteTextView;

import com.dotstudioz.dotstudioPRO.models.dto.SearchSuggesterDTO;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mohsin on 15-10-2016.
 */

public class ApplicationConstants {

    public static String TAG;
    public static String APP_NAME;
    public static String APP_VERSION;
    public static String APP_CODE_NAME;
    public static String COMPANY_KEY;
    public static String EPISODE_LIST_COLOR_1;
    public static String EPISODE_LIST_COLOR_2;

    public static boolean isMute = false;
    public static int currentlySelectedPopularCategoriesPage = 0;
    public static boolean scrollStarted = false;
    public static boolean scrollEnded = true;
    public static int selectedIndexOfVideo = 0;

    public static int selectedIndexOfChannel = 0;
    public static boolean hasScrolled = false;
    public static boolean channelClickIntercepted = false;
    public static boolean hasScrollCompleted = false;
    public static boolean hasClickedChannelPosterItem = false;
    public static boolean selectedIndexOfChannelFlag = false;

    public static boolean channelRailScrolledFlag = false;

    public static String PLATFORM_ENABLED;
    public  String PLATFORM_ENABLE_ANDROID_MOBILE = "andriod";
    public  String PLATFORM_ENABLE_ANDROID_TV = "android_TV";
    public  String PLATFORM_ENABLE_FIRE_TV = "amazon_fire";
    //Set Page TYPE for analytics
    public  String ANDROID_PAGE_TYPE            = "android";
    public  String ANDROID_TV_PAGE_TYPE         = "androidtv";
    public  String FIRE_TV_PAGE_TYPE            = "firetv";

    private static ApplicationConstants ourInstance = new ApplicationConstants();

    public static ApplicationConstants getInstance() {
        return ourInstance;
    }

    public static void initializeInstance(String tag, String appName, String appVersion, String appCodeName, String companyKey, String color1, String color2) {
        TAG = tag;
        APP_NAME = appName;
        APP_VERSION = appVersion;
        APP_CODE_NAME = appCodeName;
        COMPANY_KEY = companyKey;
        EPISODE_LIST_COLOR_1 = color1;
        EPISODE_LIST_COLOR_2 = color2;
    }



    public static void setSharedPreferenceNames(
            String TOKEN_RESPONSE_SHARED_PREFERENCE,
            String IS_FB_USER_RESPONSE_SHARED_PREFERENCE,
            String FACEBOOK_RESPONSE_SHARED_PREFERENCE,
            String USER_DETAILS_RESPONSE_SHARED_PREFERENCE,
            String USER_EMAIL_DETAILS_RESPONSE_SHARED_PREFERENCE,
            String AUTH0_ID_TOKEN_RESPONSE_SHARED_PREFERENCE,
            String AUTH0_REFRESH_TOKEN_RESPONSE_SHARED_PREFERENCE,
            String LIVE_TICKER_PREFERENCE_RESPONSE_SHARED_PREFERENCE
    ) {
        /*ApplicationConstants.TOKEN_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.tokenResponse";
        ApplicationConstants.IS_FB_USER_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.isFBUserResponse";
        ApplicationConstants.FACEBOOK_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.facebookResponse";
        ApplicationConstants.USER_DETAILS_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.userDetails";
        ApplicationConstants.USER_EMAIL_DETAILS_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.userEmailDetails";
        ApplicationConstants.AUTH0_ID_TOKEN_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.auth0idtoken";
        ApplicationConstants.AUTH0_REFRESH_TOKEN_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.auth0refreshtoken";
        ApplicationConstants.LIVE_TICKER_PREFERENCE_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.liveTickerVisibilityPreference";*/

        ApplicationConstants.TOKEN_RESPONSE_SHARED_PREFERENCE = TOKEN_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.IS_FB_USER_RESPONSE_SHARED_PREFERENCE = IS_FB_USER_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.FACEBOOK_RESPONSE_SHARED_PREFERENCE = FACEBOOK_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.USER_DETAILS_RESPONSE_SHARED_PREFERENCE = USER_DETAILS_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.USER_EMAIL_DETAILS_RESPONSE_SHARED_PREFERENCE = USER_EMAIL_DETAILS_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.AUTH0_ID_TOKEN_RESPONSE_SHARED_PREFERENCE = AUTH0_ID_TOKEN_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.AUTH0_REFRESH_TOKEN_RESPONSE_SHARED_PREFERENCE = AUTH0_REFRESH_TOKEN_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.LIVE_TICKER_PREFERENCE_RESPONSE_SHARED_PREFERENCE = LIVE_TICKER_PREFERENCE_RESPONSE_SHARED_PREFERENCE;

    }

    public static void setSharedPreferenceNames(
            String TOKEN_RESPONSE_SHARED_PREFERENCE,
            String IS_FB_USER_RESPONSE_SHARED_PREFERENCE,
            String FACEBOOK_RESPONSE_SHARED_PREFERENCE,
            String USER_DETAILS_RESPONSE_SHARED_PREFERENCE,
            String USER_EMAIL_DETAILS_RESPONSE_SHARED_PREFERENCE,
            String AUTH0_ID_TOKEN_RESPONSE_SHARED_PREFERENCE,
            String AUTH0_REFRESH_TOKEN_RESPONSE_SHARED_PREFERENCE
    ) {
        /*ApplicationConstants.TOKEN_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.tokenResponse";
        ApplicationConstants.IS_FB_USER_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.isFBUserResponse";
        ApplicationConstants.FACEBOOK_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.facebookResponse";
        ApplicationConstants.USER_DETAILS_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.userDetails";
        ApplicationConstants.USER_EMAIL_DETAILS_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.userEmailDetails";
        ApplicationConstants.AUTH0_ID_TOKEN_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.auth0idtoken";
        ApplicationConstants.AUTH0_REFRESH_TOKEN_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.auth0refreshtoken";
        ApplicationConstants.LIVE_TICKER_PREFERENCE_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.liveTickerVisibilityPreference";*/

        ApplicationConstants.TOKEN_RESPONSE_SHARED_PREFERENCE = TOKEN_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.IS_FB_USER_RESPONSE_SHARED_PREFERENCE = IS_FB_USER_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.FACEBOOK_RESPONSE_SHARED_PREFERENCE = FACEBOOK_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.USER_DETAILS_RESPONSE_SHARED_PREFERENCE = USER_DETAILS_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.USER_EMAIL_DETAILS_RESPONSE_SHARED_PREFERENCE = USER_EMAIL_DETAILS_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.AUTH0_ID_TOKEN_RESPONSE_SHARED_PREFERENCE = AUTH0_ID_TOKEN_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.AUTH0_REFRESH_TOKEN_RESPONSE_SHARED_PREFERENCE = AUTH0_REFRESH_TOKEN_RESPONSE_SHARED_PREFERENCE;

    }

    public static void setSharedPreferenceNames(
            String TOKEN_RESPONSE_SHARED_PREFERENCE,
            String IS_FB_USER_RESPONSE_SHARED_PREFERENCE,
            String FACEBOOK_RESPONSE_SHARED_PREFERENCE,
            String USER_DETAILS_RESPONSE_SHARED_PREFERENCE,
            String USER_EMAIL_DETAILS_RESPONSE_SHARED_PREFERENCE,
            String AUTH0_ID_TOKEN_RESPONSE_SHARED_PREFERENCE,
            String AUTH0_REFRESH_TOKEN_RESPONSE_SHARED_PREFERENCE,
            String USER_ID_FOR_REWARDS_POINTS_SHARED_PREFERENCE,
            String USER_NAME_FOR_REWARDS_POINTS_SHARED_PREFERENCE
    ) {
        /*ApplicationConstants.TOKEN_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.tokenResponse";
        ApplicationConstants.IS_FB_USER_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.isFBUserResponse";
        ApplicationConstants.FACEBOOK_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.facebookResponse";
        ApplicationConstants.USER_DETAILS_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.userDetails";
        ApplicationConstants.USER_EMAIL_DETAILS_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.userEmailDetails";
        ApplicationConstants.AUTH0_ID_TOKEN_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.auth0idtoken";
        ApplicationConstants.AUTH0_REFRESH_TOKEN_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.auth0refreshtoken";
        ApplicationConstants.LIVE_TICKER_PREFERENCE_RESPONSE_SHARED_PREFERENCE = "com.dotstudioz.dotstudioPRO.myspotllighttv.liveTickerVisibilityPreference";*/

        ApplicationConstants.TOKEN_RESPONSE_SHARED_PREFERENCE = TOKEN_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.IS_FB_USER_RESPONSE_SHARED_PREFERENCE = IS_FB_USER_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.FACEBOOK_RESPONSE_SHARED_PREFERENCE = FACEBOOK_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.USER_DETAILS_RESPONSE_SHARED_PREFERENCE = USER_DETAILS_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.USER_EMAIL_DETAILS_RESPONSE_SHARED_PREFERENCE = USER_EMAIL_DETAILS_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.AUTH0_ID_TOKEN_RESPONSE_SHARED_PREFERENCE = AUTH0_ID_TOKEN_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.AUTH0_REFRESH_TOKEN_RESPONSE_SHARED_PREFERENCE = AUTH0_REFRESH_TOKEN_RESPONSE_SHARED_PREFERENCE;
        ApplicationConstants.USER_ID_FOR_REWARDS_POINTS_SHARED_PREFERENCE = USER_ID_FOR_REWARDS_POINTS_SHARED_PREFERENCE;
        ApplicationConstants.USER_NAME_FOR_REWARDS_POINTS_SHARED_PREFERENCE = USER_NAME_FOR_REWARDS_POINTS_SHARED_PREFERENCE;

    }

    private ApplicationConstants() {}

    //public static final String ROKU_APP_ID = "72969";//mySpotlightTV
    //public static final String ROKU_APP_ID = "129877";//FamiLeague
    public static final String ROKU_APP_ID = "193120";//CelebrityPage

    public static final String AIRPLAY_APP_ID = "1190636061";//Nosey

    public static final String FIRETV_APP_ID = "com.dotstudioz.dotstudioPRO.myspotlighttv";//MySpotlightTV

    //public static final String COMPANY_KEY = "3b293100a3d779b4593b7b832e2745555f27dc08";
    //public static String COMPANY_KEY = "aef7ade8aa14b0ac910c92bfa622b91a78d75f22"; // MySpotlight
    //public static final String COMPANY_KEY = "9faead783db73758aeb3e7000711ce62f506334f"; // FUBU
    //public static final String COMPANY_KEY = "f37062d9a65543a46f2ba13299ba77a370a1c4eb"; // BTVR

    public static String xAccessToken;
    public static String CLIENT_TOKEN;
    public static String COMPANY_KEY_NOT_API_KEY;
    public static Date TIME_NOW_DATE_FROM_LOCAL;
    public static Date TIME_NOW_DATE_FROM_SERVER;
    public static String TIME_NOW_DATE_ACTUAL_DATA_FROM_SERVER;

    public static boolean TEMP_BOOLEAN_FLAG = true;

    public static boolean CHANNEL_TYPE_FULL = false;

    public static boolean FIRST_LAUNCH = true;
    public static boolean CHANNEL_TYPE_LEAN = true;

    public static String ADVERTISING_ID_CLIENT;

    public static AutoCompleteTextView searchViewEditText;
    public static String DIRECTORS_SEARCH_SUGGESTER_STRING = "directors";
    public static String ACTORS_SEARCH_SUGGESTER_STRING = "actors";
    public static String TITLE_SEARCH_SUGGESTER_STRING = "title";
    public static boolean isActorsSet = false;
    public static boolean isDirectorsSet = false;
    public static boolean isTitleSet = false;
    public static ArrayList<SearchSuggesterDTO> searchSuggesterDTOArrayList = new ArrayList<>();

    public static String PREVIOUS_LOADED_VIEW = "";
    public static String CURRNET_LOADED_VIEW = "";
    public static String LOGIN_VIEW = "LOGIN_VIEW";
    public static String CATEGORIES_VIEW = "CATEGORIES_VIEW";
    public static String SUBSCRIBE_VIEW = "SUBSCRIBE_VIEW";
    public static String SUBSCRIBE_VIEW_2 = "SUBSCRIBE_VIEW_2";
    public static String MARKET_VIEW = "MARKET_VIEW";
    public static String TELEGRAM_VIEW = "TELEGRAM_VIEW";
    public static String CHANNEL_VIEW = "CHANNEL_VIEW";
    public static String HOME_VIEW = "HOME_VIEW";
    public static String POPULAR_CATEGORIES_VIEW = "POPULAR_CATEGORIES_VIEW";
    public static String BLOG_VIEW = "BLOG_VIEW";
    public static String ROSTER_VIEW = "ROSTER_VIEW";
    public static String COLLECTION_VIEW = "COLLECTION_VIEW";
    public static String RADIO_VIEW = "RADIO_VIEW";
    public static String NEWS_VIEW = "NEWS_VIEW";
    public static String MAGAZINE_VIEW = "MAGAZINE_VIEW";
    public static String MY_LIST_VIEW = "MY_LIST_VIEW";
    public static String SINGLE_VIEW = "SINGLE_VIEW";
    public static String SERIES_VIEW = "SERIES_VIEW";
    public static String COMMENTS_VIEW = "COMMENTS_VIEW";

    public static String SETTINGS_VIEW = "SETTINGS_VIEW";
    public static String SUBSCRIPTIONS_VIEW = "SUBSCRIPTIONS_VIEW";
    public static String CARD_VIEW = "CARD_VIEW";
    public static String LIVE_SCHEDULE_VIEW = "LIVE_SCHEDULE_VIEW";
    public static String STATIC_WEB_PAGE_VIEW = "STATIC_WEB_PAGE_VIEW";
    public static String MORE_SETTINGS_VIEW = "MORE_SETTINGS_VIEW";
    public static String MENU_VIEW = "MENU_VIEW";
    public static String CASTING_VIEW = "CASTING_VIEW";
    public static String MY_ACCOUNT_VIEW = "MY_ACCOUNT_VIEW";
    public static String MY_PURCHASES_VIEW = "MY_PURCHASES_VIEW";
    public static String PREFERENCE_VIEW = "PREFERENCE_VIEW";
    public static String HELP_VIEW = "HELP_VIEW";
    public static String ABOUT_VIEW = "ABOUT_VIEW";
    public static String TERMS_AND_CONDITIONS_VIEW = "TERMS_AND_CONDITIONS_VIEW";
    public static String PRIVACY_POLICY_VIEW = "PRIVACY_POLICY_VIEW";
    public static String COPYRIGHTS_VIEW = "COPYRIGHTS_VIEW";
    public static String RSS_TERMS_AND_CONDITIONS_VIEW = "RSS_TERMS_AND_CONDITIONS_VIEW";
    public static String SEARCH_VIEW = "SEARCH_VIEW";
    public static String COMPANY_VIEW = "COMPANY_VIEW";

    public static String PORTRAIT_MODE = "PORTRAIT_MODE";
    public static String LANDSCAPE_MODE = "LANDSCAPE_MODE";

    public static int TEXT_INPUT_HEIGHT = 0;
    public static int TOP_PADDING_FOR_MAIN_ACTION_BAR = 0;
    public static int BOTTOM_PADDING_FOR_TAB_BAR = 0;
    public static String TOKEN_RESPONSE_SHARED_PREFERENCE;
    public static String IS_FB_USER_RESPONSE_SHARED_PREFERENCE;
    public static String FACEBOOK_RESPONSE_SHARED_PREFERENCE;
    public static String USER_DETAILS_RESPONSE_SHARED_PREFERENCE;
    public static String USER_EMAIL_DETAILS_RESPONSE_SHARED_PREFERENCE;
    public static String AUTH0_ID_TOKEN_RESPONSE_SHARED_PREFERENCE;
    public static String AUTH0_REFRESH_TOKEN_RESPONSE_SHARED_PREFERENCE;
    public static String LIVE_TICKER_PREFERENCE_RESPONSE_SHARED_PREFERENCE;
    public static String USER_ID_FOR_REWARDS_POINTS_SHARED_PREFERENCE;
    public static String USER_NAME_FOR_REWARDS_POINTS_SHARED_PREFERENCE;


    public static String TOKEN_RESPONSE_SHARED_PREFERENCE_KEY = "xAccessToken";
    public static String IS_FB_USER_RESPONSE_SHARED_PREFERENCE_KEY = "isFBUser";
    public static String FACEBOOK_RESPONSE_SHARED_PREFERENCE_KEY = "authenticDetails";
    public static String USER_DETAILS_RESPONSE_SHARED_PREFERENCE_KEY = "authenticDetails";
    public static String USER_EMAIL_DETAILS_RESPONSE_SHARED_PREFERENCE_KEY = "userEmailId";
    public static String AUTH0_ID_TOKEN_RESPONSE_SHARED_PREFERENCE_KEY = "auth0idtoken";
    public static String AUTH0_REFRESH_TOKEN_RESPONSE_SHARED_PREFERENCE_KEY = "auth0refreshtoken";
    public static String LIVE_TICKER_PREFERENCE_RESPONSE_SHARED_PREFERENCE_KEY = "liveTickerVisibilityPreference";
    public static String USER_ID_FOR_REWARDS_POINTS_SHARED_PREFERENCE_KEY = "userID";
    public static String USER_NAME_FOR_REWARDS_POINTS_SHARED_PREFERENCE_KEY = "userName";

    public static String userEmailId = "";
    public static String userIdString = "";
    public static String userFirstName = "";
    public static String userLastName = "";
    public static String userAvatarPath = "";
    public static String emailIdUsedToLogin = "";
    public static boolean isFBUser = false;
    public static JSONObject facebookResponse;
    public static JSONObject finalObjectResult;


    public static boolean backClickedOnAdPlayer = false;


    public static DisplayMetrics displaymetrics;

    public static final String DATA_MISSING_VIDEO = "Data missing for this video!";
    public static final String DATA_MISSING_CHANNEL = "Data missing for this channel!";
    public static final String DATA_MISSING_SLIDER_SHOWCASE = "Data missing for this Featured!";
    public static final String GEO_BLOCKED_CONTENT = "This content is geoblocked in your region.";

    public static String WATCH_AGAIN = "Watch Again";
    public static String WATCH_AGAIN_CAPS = "WATCH AGAIN";
    public static String WATCH_AGAIN_SLUG = "watchagain";
    public static String CONTINUE_WATCHING_CAPS = "CONTINUE WATCHING";
    public static String CONTINUE_WATCHING = "Continue Watching";
    public static String CONTINUE_WATCHING_SLUG = "continuewatching";
}
