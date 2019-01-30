package com.dotstudioz.dotstudioPRO.services.constants;

/**
 * Created by Admin on 29-06-2015.
 */
public  class ApplicationConstantURL {

    private static ApplicationConstantURL ourInstance = new ApplicationConstantURL();

    public static ApplicationConstantURL getInstance() {
        return ourInstance;
    }

    private ApplicationConstantURL() {}

    public static String COUNTRY_CODE = "IN";

   //PRODUCTION_URL
    public static String API_DOMAIN; //PRODUCTION SERVER
    public static String API_DOMAIN_S; //PRODUCTION SERVER
    public static String BLOG_LIST_ABS;
    public static String BLOG_LIST_BY_CATEGORY;
    public static String SPOTLIGHT_DOMAIN;
    public static String API_DSPRO_DOMAIN_S;
    public static String API_SPOTLIGHT_DOMAIN;
    public static String IMAGES_DSPRO_DOMAIN_S;
    public static String TEASER_DOMAIN;
    public static String ADSERVER_DOMAIN;
    public static String MYSPOTLIGHT_DOMAIN;
    public static String COLLECTOR_DEV_DOMAIN_S;
    public static String COLLECTOR_DSPRO_DOMAIN_S;


    public  static String  TOKEN_URL = ApplicationConstantURL.API_DOMAIN_S + "/token";
    //public  static String TOKEN_URL = ApplicationConstantURL.API_DOMAIN_S + "/token";
    public  static String DEVICE_CODE_URL = ApplicationConstantURL.API_DOMAIN_S + "/device/codes/new";
    public  static String DEVICE_CODE_ACTIVATION_URL = ApplicationConstantURL.API_DOMAIN_S + "/device/codes/customer";
    public  static String DEVICE_CODE_VERIFICATION_URL = ApplicationConstantURL.API_DOMAIN_S + "/device/codes?code=";

    public  static String USER_LOGIN_URL = ApplicationConstantURL.API_DOMAIN_S + "/users/login";
    public  static String USER_DETAILS_URL = ApplicationConstantURL.API_DOMAIN_S + "/users/details";
    public  static String USER_REGISTER_URL = ApplicationConstantURL.API_DOMAIN_S + "/users/register";
    public  static String USER_TOKEN_REFRESH = ApplicationConstantURL.API_DOMAIN_S + "/users/token/refresh";
    public  static String LON_LAT_COUNTRY = ApplicationConstantURL.API_DOMAIN_S + "/country/analytics";
    public  static String CHANGE_PASSWORD_URL = ApplicationConstantURL.API_DOMAIN_S + "/users/password";
    //public  static String CHANGE_PASSWORD_URL = API_DSPRO_DOMAIN_S +"/v2/universalapi/customer_details/password";
    //public  static String MY_PURCHASES_URL = API_DSPRO_DOMAIN_S +"/v2/universalapi/collect_video_information";
    public  static String MY_PURCHASES_URL = ApplicationConstantURL.API_DOMAIN_S + "/users/orders/history";
    public  static String VIDEO_PURCHASE_STATUS = API_DSPRO_DOMAIN_S +"/v2/universalapi/paywall_status";
    public  static String CUSTOMER_REGISTER = API_DSPRO_DOMAIN_S +"/v2/universalapi/customer_register";

    public static String APP_VERSION_ANDROID_API = ApplicationConstantURL.API_DOMAIN_S + "/latestAppVersion/android";
    public static String APP_VERSION_ANDROID_API_S = ApplicationConstantURL.API_DOMAIN_S + "/latestAppVersion/android";
    public static String APP_VERSION_FIRE_TV_API = ApplicationConstantURL.API_DOMAIN_S + "/latestAppVersion/fire_tv";
    public static String APP_VERSION_FIRE_TV_API_S = ApplicationConstantURL.API_DOMAIN_S + "/latestAppVersion/fire_tv";
    public static String APP_VERSION_ANDROID_TV_API = ApplicationConstantURL.API_DOMAIN_S + "/latestAppVersion/android_tv";
    public static String APP_VERSION_ANDROID_TV_API_s = ApplicationConstantURL.API_DOMAIN_S + "/latestAppVersion/android_tv";

    public static String BLOG_LIST = BLOG_LIST_ABS+"/wp-json/dsp/v1/featured/blog/carousel";

    //public  static String AVATAR = SPOTLIGHT_DOMAIN + "/user/avatar/";
    public  static String AVATAR = ApplicationConstantURL.API_DOMAIN_S + "/users/avatar/";
    //public   static String CATEGORIES_LIST = SPOTLIGHT_DOMAIN + "/categories/list";
    public   static String HOMEPAGE_CATEGORIES_LIST = API_SPOTLIGHT_DOMAIN+"/wp-json/dsp/v1/homepage";

    public   static String SUBSCRIPTION_LIST = ApplicationConstantURL.API_DOMAIN_S + "/subscriptions/summary";
    public   static String ACTIVE_SUBSCRIPTIONS_LIST = ApplicationConstantURL.API_DOMAIN_S + "/subscriptions/users/active_subscriptions";
    public   static String CHECK_CHANNEL_SUBSCRIPTIONS_STATUS = ApplicationConstantURL.API_DOMAIN_S + "/subscriptions/check/";
    public   static String CREATE_BRAINTREE_CUSTOMER_FROM_NONCE = ApplicationConstantURL.API_DOMAIN_S + "/subscriptions/users/create_from_nonce";
    public   static String CREATE_CHARGIFY_CUSTOMER_USING_SUBSCRIPTION_ID = ApplicationConstantURL.API_DOMAIN_S + "/subscriptions/users/import/subscribe_to/";

    public   static String CATEGORIES_LIST = ApplicationConstantURL.API_DOMAIN_S + "/categories/"+COUNTRY_CODE+"/";
    //public   static String CATEGORIES_LIST ="/categories/"+COUNTRY_CODE+"/";
    //public  static String CHANNELS = SPOTLIGHT_DOMAIN + "json/channels";
    public  static String CHANNELS = ApplicationConstantURL.API_DOMAIN_S + "/channels/"+COUNTRY_CODE+"/";
    public  static String CHANNEL = ApplicationConstantURL.API_DOMAIN_S + "/channel/"+COUNTRY_CODE+"/";
    public  static String CHANNELS_DETAILS = ApplicationConstantURL.API_DOMAIN_S + "/channels/"+COUNTRY_CODE+"/";


    public  static String SEARCH_API_URL = ApplicationConstantURL.API_DOMAIN_S + "/search/";
    public  static String SEARCH_VIDEO_API_URL = ApplicationConstantURL.API_DOMAIN_S + "/search/videos/";
    public  static String SEARCH_SUGGESTER_API_URL = ApplicationConstantURL.API_DOMAIN_S + "/search/s/";
    public  static String SEARCH_BY_COMPANY_API_URL = SPOTLIGHT_DOMAIN + "/company/";
    public  static String SEARCH_BY_COMPANY_DATA_FORMAT = "/json";

    public  static String IMAGES = IMAGES_DSPRO_DOMAIN_S;
    //public  static String VIDEOS_API = API_DSPRO_DOMAIN_S +"/v2/cc89512b/json/videos/";
    //public  static String VIDEOS_API = API_DSPRO_DOMAIN_S +"/v2/cc89512b/json/videos/";
    public  static String VIDEOS_API = ApplicationConstantURL.API_DOMAIN_S + "/channels/"+COUNTRY_CODE+"/";

    public static String VIDEO_PLAY2_API = ApplicationConstantURL.API_DOMAIN_S + "/video/play2/";

    public  static String PLAYLIST_VIDEOS = API_DSPRO_DOMAIN_S +"/v2/cc89512b/json/playlists/";
    public  static String GET_FIRST_LAST_NAME_API = API_DSPRO_DOMAIN_S +"/v2/universalapi/customer_details";
    public  static String SAVE_FIRST_LAST_NAME_API = API_DSPRO_DOMAIN_S + "/v2/universalapi/customer_details/edit";
    public  static String FORGOT_PASSWORD_API = API_DSPRO_DOMAIN_S +"/v2/universalapi/forgotpassword";

    /*public  static String CREATE_ANALYTICS_USER_API = COLLECTOR_DEV_DOMAIN_S +"/users";
    public  static String SAVE_PLAYER_DATA_API = COLLECTOR_DEV_DOMAIN_S +"/plays";
    public  static String SAVE_APP_DATA_API = COLLECTOR_DEV_DOMAIN_S +"/players";*/
    public  static String CREATE_ANALYTICS_USER_API = COLLECTOR_DSPRO_DOMAIN_S +"/users";
    public  static String SAVE_PLAYER_DATA_API = COLLECTOR_DSPRO_DOMAIN_S +"/plays";
    public  static String SAVE_APP_DATA_API = COLLECTOR_DSPRO_DOMAIN_S +"/players";

    //public  static String CLIENT_TOKEN_API = MYSPOTLIGHT_DOMAIN +"/dotstudio_token";
    public  static String CLIENT_TOKEN_API = ApplicationConstantURL.API_DOMAIN_S + "/users/payment/token";
    //public  static String RENT_API = MYSPOTLIGHT_DOMAIN +"/rent";
    public  static String RENT_API = ApplicationConstantURL.API_DOMAIN_S + "/users/payment/purchase";
    public  static String RENT_API_ANDROID = ApplicationConstantURL.API_DOMAIN_S + "/users/payment/android";

    public  static String RECEIPT_VERIFY_API_FIRE_TV = ApplicationConstantURL.API_DOMAIN_S + "/users/payment/fire_tv";
    public  static String SHARING_DOMAIN_NAME = MYSPOTLIGHT_DOMAIN +"/watch";

    public  static String ADSERVER_API = ADSERVER_DOMAIN +"/adserver/www/delivery/fc.php?script=apVideo:vast2&zoneid=";

    public  static String VIDEO_PLAYBACK_DETAILS_API = ApplicationConstantURL.API_DOMAIN_S + "/users/videos/point/";
    public  static String MULTIPLE_VIDEO_PLAYBACK_DETAILS_API = ApplicationConstantURL.API_DOMAIN_S + "/users/videos/points/";
    public  static String LAST_WATCHED_VIDEOS_API = ApplicationConstantURL.API_DOMAIN_S + "/users/resumption/videos";

    public  static String ADD_TO_MY_LIST_API = ApplicationConstantURL.API_DOMAIN_S + "/watchlist/channels/add";
    public  static String DELETE_FROM_MY_LIST_API = ApplicationConstantURL.API_DOMAIN_S + "/watchlist/channels/delete";
    public  static String  GET_MY_LIST_API = ApplicationConstantURL.API_DOMAIN_S + "/watchlist/channels";

    public  static String RECOMMENDATION_API = ApplicationConstantURL.API_DOMAIN_S + "/search/recommendation";
    public  static String RECOMMENDATION_CHANNEL_API = ApplicationConstantURL.API_DOMAIN_S + "/search/recommendation/channel";

    public  static String ANALYTICS_TESTING_URL = ApplicationConstantURL.API_DOMAIN_S + "/testing/analytics";
    public  static String CLIENT_REFRESH_TOKEN = ApplicationConstantURL.API_DOMAIN_S + "/users/token/refresh";

    public  static String SUBSCRIPTION_API = ApplicationConstantURL.API_DOMAIN_S + "/subscriptions/google/parse";

    public  static String RECOMMENDATION_CHANNEL_FIRE_TV_API = "/channel/"+COUNTRY_CODE+"/recommendationsamazonfire";

    public void setAPIDomain()
    {
        ApplicationConstantURL.TOKEN_URL = ApplicationConstantURL.API_DOMAIN_S + "/token";
        //public  String TOKEN_URL = ApplicationConstantURL.API_DOMAIN_S + "/token";
        ApplicationConstantURL.DEVICE_CODE_URL = ApplicationConstantURL.API_DOMAIN_S + "/device/codes/new";
        ApplicationConstantURL.DEVICE_CODE_ACTIVATION_URL = ApplicationConstantURL.API_DOMAIN_S + "/device/codes/customer";
        ApplicationConstantURL.DEVICE_CODE_VERIFICATION_URL = ApplicationConstantURL.API_DOMAIN_S + "/device/codes?code=";

        ApplicationConstantURL.USER_LOGIN_URL = ApplicationConstantURL.API_DOMAIN_S + "/users/login";
        ApplicationConstantURL.USER_DETAILS_URL = ApplicationConstantURL.API_DOMAIN_S + "/users/details";
        ApplicationConstantURL.USER_REGISTER_URL = ApplicationConstantURL.API_DOMAIN_S + "/users/register";
        ApplicationConstantURL.USER_TOKEN_REFRESH = ApplicationConstantURL.API_DOMAIN_S + "/users/token/refresh";
        ApplicationConstantURL.LON_LAT_COUNTRY = ApplicationConstantURL.API_DOMAIN_S + "/country/analytics";
        ApplicationConstantURL.CHANGE_PASSWORD_URL = ApplicationConstantURL.API_DOMAIN_S + "/users/password";
        //this.CHANGE_PASSWORD_URL = API_DSPRO_DOMAIN_S +"/v2/universalapi/customer_details/password";
        //this.MY_PURCHASES_URL = API_DSPRO_DOMAIN_S +"/v2/universalapi/collect_video_information";
        ApplicationConstantURL.MY_PURCHASES_URL = ApplicationConstantURL.API_DOMAIN_S + "/users/orders/history";
        ApplicationConstantURL.VIDEO_PURCHASE_STATUS = API_DSPRO_DOMAIN_S +"/v2/universalapi/paywall_status";
        ApplicationConstantURL.CUSTOMER_REGISTER = API_DSPRO_DOMAIN_S +"/v2/universalapi/customer_register";

        ApplicationConstantURL.APP_VERSION_ANDROID_API = ApplicationConstantURL.API_DOMAIN_S + "/latestAppVersion/android";
        ApplicationConstantURL.APP_VERSION_ANDROID_API_S = ApplicationConstantURL.API_DOMAIN_S + "/latestAppVersion/android";
        ApplicationConstantURL.APP_VERSION_FIRE_TV_API = ApplicationConstantURL.API_DOMAIN_S + "/latestAppVersion/fire_tv";
        ApplicationConstantURL.APP_VERSION_FIRE_TV_API_S = ApplicationConstantURL.API_DOMAIN_S + "/latestAppVersion/fire_tv";
        ApplicationConstantURL.APP_VERSION_ANDROID_TV_API = ApplicationConstantURL.API_DOMAIN_S + "/latestAppVersion/android_tv";
        ApplicationConstantURL.APP_VERSION_ANDROID_TV_API_s = ApplicationConstantURL.API_DOMAIN_S + "/latestAppVersion/android_tv";

        ApplicationConstantURL.BLOG_LIST = BLOG_LIST_ABS + "/wp-json/dsp/v1/featured/blog/carousel";
        ApplicationConstantURL.BLOG_LIST_BY_CATEGORY = BLOG_LIST_BY_CATEGORY + "/wp-json/abs/v1/posts/bycategory/";

        //this.AVATAR = SPOTLIGHT_DOMAIN + "/user/avatar/";
        ApplicationConstantURL.AVATAR = ApplicationConstantURL.API_DOMAIN_S + "/users/avatar/";
        //this.CATEGORIES_LIST = SPOTLIGHT_DOMAIN + "/categories/list";
        ApplicationConstantURL.HOMEPAGE_CATEGORIES_LIST = API_SPOTLIGHT_DOMAIN+"/wp-json/dsp/v1/homepage";

        ApplicationConstantURL.SUBSCRIPTION_LIST = ApplicationConstantURL.API_DOMAIN_S + "/subscriptions/summary";
        ApplicationConstantURL.ACTIVE_SUBSCRIPTIONS_LIST = ApplicationConstantURL.API_DOMAIN_S + "/subscriptions/users/active_subscriptions";
        ApplicationConstantURL.CHECK_CHANNEL_SUBSCRIPTIONS_STATUS = ApplicationConstantURL.API_DOMAIN_S + "/subscriptions/check/";
        ApplicationConstantURL.CREATE_BRAINTREE_CUSTOMER_FROM_NONCE = ApplicationConstantURL.API_DOMAIN_S + "/subscriptions/users/create_from_nonce";
        ApplicationConstantURL.CREATE_CHARGIFY_CUSTOMER_USING_SUBSCRIPTION_ID = ApplicationConstantURL.API_DOMAIN_S + "/subscriptions/users/import/subscribe_to/";

        ApplicationConstantURL.CATEGORIES_LIST = ApplicationConstantURL.API_DOMAIN_S + "/categories/"+COUNTRY_CODE+"/";
        //this.CATEGORIES_LIST ="/categories/"+COUNTRY_CODE+"/";
        //this.CHANNELS = SPOTLIGHT_DOMAIN + "json/channels";
        ApplicationConstantURL.CHANNELS = ApplicationConstantURL.API_DOMAIN_S + "/channels/"+COUNTRY_CODE+"/";
        ApplicationConstantURL.CHANNEL = ApplicationConstantURL.API_DOMAIN_S + "/channel/"+COUNTRY_CODE+"/";
        ApplicationConstantURL.CHANNELS_DETAILS = ApplicationConstantURL.API_DOMAIN_S + "/channels/"+COUNTRY_CODE+"/";


        ApplicationConstantURL.SEARCH_API_URL = ApplicationConstantURL.API_DOMAIN_S + "/search/";
        ApplicationConstantURL.SEARCH_VIDEO_API_URL = ApplicationConstantURL.API_DOMAIN_S + "/search/videos/";
        ApplicationConstantURL.SEARCH_SUGGESTER_API_URL = ApplicationConstantURL.API_DOMAIN_S + "/search/s/";
        ApplicationConstantURL.SEARCH_BY_COMPANY_API_URL = SPOTLIGHT_DOMAIN + "/company/";
        ApplicationConstantURL.SEARCH_BY_COMPANY_DATA_FORMAT = "/json";

        ApplicationConstantURL.IMAGES = IMAGES_DSPRO_DOMAIN_S;
        //this.VIDEOS_API = API_DSPRO_DOMAIN_S +"/v2/cc89512b/json/videos/";
        //this.VIDEOS_API = API_DSPRO_DOMAIN_S +"/v2/cc89512b/json/videos/";
        ApplicationConstantURL.VIDEOS_API = ApplicationConstantURL.API_DOMAIN_S + "/channels/"+COUNTRY_CODE+"/";

        ApplicationConstantURL.VIDEO_PLAY2_API = ApplicationConstantURL.API_DOMAIN_S + "/video/play2/";

        ApplicationConstantURL.PLAYLIST_VIDEOS = API_DSPRO_DOMAIN_S +"/v2/cc89512b/json/playlists/";
        ApplicationConstantURL.GET_FIRST_LAST_NAME_API = API_DSPRO_DOMAIN_S +"/v2/universalapi/customer_details";
        ApplicationConstantURL.SAVE_FIRST_LAST_NAME_API = API_DSPRO_DOMAIN_S + "/v2/universalapi/customer_details/edit";
        ApplicationConstantURL.FORGOT_PASSWORD_API = API_DSPRO_DOMAIN_S +"/v2/universalapi/forgotpassword";

        /*this.CREATE_ANALYTICS_USER_API = COLLECTOR_DEV_DOMAIN_S +"/users";
        ApplicationConstantURL.SAVE_PLAYER_DATA_API = COLLECTOR_DEV_DOMAIN_S +"/plays";
        ApplicationConstantURL.SAVE_APP_DATA_API = COLLECTOR_DEV_DOMAIN_S +"/players";*/
        ApplicationConstantURL.CREATE_ANALYTICS_USER_API = COLLECTOR_DSPRO_DOMAIN_S +"/users";
        ApplicationConstantURL.SAVE_PLAYER_DATA_API = COLLECTOR_DSPRO_DOMAIN_S +"/plays";
        ApplicationConstantURL.SAVE_APP_DATA_API = COLLECTOR_DSPRO_DOMAIN_S +"/players";

        //this.CLIENT_TOKEN_API = MYSPOTLIGHT_DOMAIN +"/dotstudio_token";
        ApplicationConstantURL.CLIENT_TOKEN_API = ApplicationConstantURL.API_DOMAIN_S + "/users/payment/token";
        //this.RENT_API = MYSPOTLIGHT_DOMAIN +"/rent";
        ApplicationConstantURL.RENT_API = ApplicationConstantURL.API_DOMAIN_S + "/users/payment/purchase";
        ApplicationConstantURL.RENT_API_ANDROID = ApplicationConstantURL.API_DOMAIN_S + "/users/payment/android";

        ApplicationConstantURL.RECEIPT_VERIFY_API_FIRE_TV = ApplicationConstantURL.API_DOMAIN_S + "/users/payment/fire_tv";
        ApplicationConstantURL.RENT_API = ApplicationConstantURL.API_DOMAIN_S + "/users/payment/purchase";
        ApplicationConstantURL.SHARING_DOMAIN_NAME = MYSPOTLIGHT_DOMAIN +"/watch";

        ApplicationConstantURL.ADSERVER_API = ADSERVER_DOMAIN +"/adserver/www/delivery/fc.php?script=apVideo:vast2&zoneid=";

        ApplicationConstantURL.VIDEO_PLAYBACK_DETAILS_API = ApplicationConstantURL.API_DOMAIN_S + "/users/videos/point/";
        ApplicationConstantURL.MULTIPLE_VIDEO_PLAYBACK_DETAILS_API = ApplicationConstantURL.API_DOMAIN_S + "/users/videos/points/";
        ApplicationConstantURL.LAST_WATCHED_VIDEOS_API = ApplicationConstantURL.API_DOMAIN_S + "/users/resumption/videos";

        ApplicationConstantURL.ADD_TO_MY_LIST_API = ApplicationConstantURL.API_DOMAIN_S + "/watchlist/channels/add";
        ApplicationConstantURL.DELETE_FROM_MY_LIST_API = ApplicationConstantURL.API_DOMAIN_S + "/watchlist/channels/delete";
        ApplicationConstantURL.GET_MY_LIST_API = ApplicationConstantURL.API_DOMAIN_S + "/watchlist/channels";

        ApplicationConstantURL.RECOMMENDATION_API = ApplicationConstantURL.API_DOMAIN_S + "/search/recommendation";
        ApplicationConstantURL.RECOMMENDATION_CHANNEL_API = ApplicationConstantURL.API_DOMAIN_S + "/search/recommendation/channel";

        ApplicationConstantURL.ANALYTICS_TESTING_URL = ApplicationConstantURL.API_DOMAIN_S + "/testing/analytics";
        ApplicationConstantURL.CLIENT_REFRESH_TOKEN = ApplicationConstantURL.API_DOMAIN_S + "/users/token/refresh";

        ApplicationConstantURL.SUBSCRIPTION_API = ApplicationConstantURL.API_DOMAIN_S + "/subscriptions/google/parse";

        ApplicationConstantURL.RECOMMENDATION_CHANNEL_FIRE_TV_API = "/channel/"+COUNTRY_CODE+"/recommendationsamazonfire";
    }




    public void setCountryCode(String countryCode) {
        this.COUNTRY_CODE = countryCode;

        this.CATEGORIES_LIST = ApplicationConstantURL.API_DOMAIN_S + "/categories/"+countryCode+"/";
        this.CHANNEL = ApplicationConstantURL.API_DOMAIN_S + "/channel/"+countryCode+"/";
        this.CHANNELS = ApplicationConstantURL.API_DOMAIN_S + "/channels/"+countryCode+"/";
        this.CHANNELS_DETAILS = ApplicationConstantURL.API_DOMAIN_S + "/channels/"+countryCode+"/";
        this.VIDEOS_API = ApplicationConstantURL.API_DOMAIN_S + "/channels/"+countryCode+"/";
        this.RECOMMENDATION_CHANNEL_FIRE_TV_API = ApplicationConstantURL.API_DOMAIN_S + "/channel/"+countryCode+"/recommendationsamazonfire";

    }


}
