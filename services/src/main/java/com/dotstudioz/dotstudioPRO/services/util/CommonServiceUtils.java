package com.dotstudioz.dotstudioPRO.services.util;

        import android.app.Activity;
        import android.content.ComponentName;
        import android.content.Context;
        import android.content.Intent;
        import android.content.res.Configuration;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.graphics.PorterDuff;
        import android.graphics.Rect;
        import android.graphics.Typeface;
        import android.media.AudioManager;
        import android.net.Uri;
        import android.os.AsyncTask;

        import android.support.design.widget.NavigationView;
        import android.support.v4.media.session.MediaButtonReceiver;
        import android.text.SpannableString;
        import android.util.DisplayMetrics;
        import android.util.Log;

        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.MotionEvent;

        import android.view.SubMenu;
        import android.view.TouchDelegate;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.HorizontalScrollView;

        import android.widget.LinearLayout;
        import android.widget.MediaController;
        import android.widget.RelativeLayout;
        import android.widget.ScrollView;
        import android.widget.SeekBar;


        import com.dotstudioz.dotstudioPRO.models.dto.VideoInfoDTO;


        import java.io.InputStream;
        import java.net.URL;
        import java.util.Set;


/**
 * Created by Admin on 16-01-2016.
 */
public class CommonServiceUtils {

    private static CommonServiceUtils commonServiceUtils = new CommonServiceUtils();

    private CommonServiceUtils() {
    }

    public static synchronized CommonServiceUtils getInstance() {
        if (commonServiceUtils != null)
            return commonServiceUtils;
        else
            return new CommonServiceUtils();
    }

    public static boolean isTabletDevice(Activity activity) {
        int screenLayout = activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        boolean isScreenLarge = (screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE);
        boolean isScreenXlarge = (screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean isScreenNormal = (screenLayout == Configuration.SCREENLAYOUT_SIZE_NORMAL);
        //return (isScreenXlarge);
        //return (isScreenLarge || isScreenXlarge);
        //return (isScreenLarge || isScreenXlarge || isScreenNormal);
        //return true;
        return false;
    }

    public static boolean isActuallyTabletDevice(Activity activity) {
        int screenLayout = activity.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        boolean isScreenLarge = (screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE);
        boolean isScreenXlarge = (screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean isScreenNormal = (screenLayout == Configuration.SCREENLAYOUT_SIZE_NORMAL);
        //return (isScreenXlarge);
        return (isScreenLarge || isScreenXlarge);
        //return (isScreenLarge || isScreenXlarge || isScreenNormal);
        //return true;
    }

    public static void enabledScrollForChildOnly(HorizontalScrollView mySmartScrollView) {
        mySmartScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View __v, MotionEvent __event) {
                if (__event.getAction() == MotionEvent.ACTION_DOWN) {
                    //  Disallow the touch request for parent scroll on touch of child view
                    requestDisallowParentInterceptTouchEvent(__v, true);
                } else if (__event.getAction() == MotionEvent.ACTION_UP || __event.getAction() == MotionEvent.ACTION_CANCEL) {
                    // Re-allows parent events
                    requestDisallowParentInterceptTouchEvent(__v, false);
                }
                return false;
            }
        });
    }

    public static void requestDisallowParentInterceptTouchEvent(View __v, Boolean __disallowIntercept) {
        while (__v.getParent() != null && __v.getParent() instanceof View) {
            if (__v.getParent() instanceof ScrollView) {
                __v.getParent().requestDisallowInterceptTouchEvent(__disallowIntercept);
            }
            __v = (View) __v.getParent();
        }
    }

    public static String replaceDotstudioproWithMyspotlightForImage(String imageString) {
        try {
            if (imageString.contains("http://image.dotstudiopro.com"))
                imageString = imageString.replace("http://image.dotstudiopro.com", "http://image.myspotlight.tv");
            else if (imageString.contains("https://image.dotstudiopro.com"))
                imageString = imageString.replace("https://image.dotstudiopro.com", "http://image.myspotlight.tv");
        } catch (Exception e) {
            e.printStackTrace();
            imageString = "";
        }

        return imageString;
    }

    public static String getIDOnlyFromImageURL(String channSlug) {
        channSlug = channSlug.substring((channSlug.lastIndexOf("/") + 1), channSlug.length());
        return channSlug;
    }

    public static boolean isTheVideoGeoBlocked(VideoInfoDTO videoInfoDTO, String countryCode) {
        try {
            boolean isGeoBlockedFlag = false;
            if (videoInfoDTO.isGeoBlocked()) {
                isGeoBlockedFlag = true;
                if (!videoInfoDTO.getGeoBlockedDisabledCountries().toLowerCase().contains(countryCode.toLowerCase())) {
                    isGeoBlockedFlag = false;
                }
            }
            return isGeoBlockedFlag;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public void styleMediaController(View view, int color) {
        try {
            if (view instanceof MediaController) {
                MediaController v = (MediaController) view;
                for (int i = 0; i < v.getChildCount(); i++) {
                    styleMediaController(v.getChildAt(i), color);
                }
            } else if (view instanceof LinearLayout) {
                LinearLayout ll = (LinearLayout) view;
                //ll.setBackgroundColor(adjustAlpha(color, 0.1f));
                ll.setBackgroundColor(adjustAlpha(Color.parseColor("#000000"), 0.1f));
                for (int i = 0; i < ll.getChildCount(); i++) {
                    styleMediaController(ll.getChildAt(i), color);
                }
            } else if (view instanceof SeekBar) {
                /*((SeekBar) view)
                        .getProgressDrawable()
                        .mutate()
                        .setColorFilter(
                                getResources().getColor(
                                        Color.RED),
                                PorterDuff.Mode.SRC_IN);*/
                try {
                    Log.d("CommonServiceUtils", "styleMediaController non exception handler!!!");
                    ((SeekBar) view).getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                    ((SeekBar) view).getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                } catch (Exception e) {
                    Log.d("CommonServiceUtils", "styleMediaController exception handler!!!");
                    e.printStackTrace();
                }
                //Drawable thumb = ((SeekBar) view).getThumb().mutate();
                /*if (thumb instanceof android.support.v4.graphics.drawable.DrawableWrapper) {
                    //compat mode, requires support library v4
                    ((android.support.v4.graphics.drawable.DrawableWrapper) thumb).setCompatTint(getResources()
                            .getColor(R.color.MediaPlayerThumbColor));
                } else {
                    //lollipop devices
                    thumb.setColorFilter(
                            getResources().getColor(R.color.MediaPlayerThumbColor),
                            PorterDuff.Mode.SRC_IN);
                }*/
            }
        } catch(Exception e) {
            e.printStackTrace();
            Log.d("CommonServiceUtils", "styleMediaController");
        }
    }
//    public static void setFontToMenuItems(NavigationView navigationView, Typeface typeface) {
//        Menu m = navigationView.getMenu();
//        for (int i = 0; i < m.size(); i++) {
//            MenuItem mi = m.getItem(i);
//
//            //for aapplying a font to subMenu ...
//            SubMenu subMenu = mi.getSubMenu();
//            if (subMenu != null && subMenu.size() > 0) {
//                for (int j = 0; j < subMenu.size(); j++) {
//                    MenuItem subMenuItem = subMenu.getItem(j);
//                    applyFontToMenuItem(subMenuItem, typeface);
//                }
//            }
//
//            //the method we have create in activity
//            applyFontToMenuItem(mi, typeface);
//        }
//    }

   /* public static void applyFontToMenuItem(MenuItem mi, Typeface typeface) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", typeface), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }*/

    public static void increaseClickAreaFor(final View view, final int sizeValue) {
        final View parent = (View) view.getParent();  // button: the view you want to enlarge hit area
        parent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                view.getHitRect(rect);
                rect.top -= sizeValue;    // increase top hit area
                rect.left -= sizeValue;   // increase left hit area
                rect.bottom += sizeValue; // increase bottom hit area
                rect.right += sizeValue;  // increase right hit area
                parent.setTouchDelegate(new TouchDelegate(rect, view));
            }
        });
    }

    public static void sendAppToBackground(Activity activity) {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(homeIntent);
    }

    public String getPosterURL(VideoInfoDTO videoInfoDTO, DisplayMetrics displaymetrics) {
        int width1 = displaymetrics.widthPixels;
        int imageHeight = ((width1 * 9) / 16);

        String posterDataURL = "";

        if (videoInfoDTO != null && videoInfoDTO.getPoster() != null &&
                !videoInfoDTO.getPoster().equals("null") &&
                videoInfoDTO.getPoster().equals("default")) {
            try {
                if (videoInfoDTO.getThumb() != null && !videoInfoDTO.getThumb().equals("null") && videoInfoDTO.getThumb().length() > 0) {
                    posterDataURL = videoInfoDTO.getThumb();
                }
            } catch (Exception e) {
            }
        } else if (videoInfoDTO != null && videoInfoDTO.getPoster() != null && !videoInfoDTO.getPoster().equals("null") && videoInfoDTO.getPoster().length() > 10) {
            try {
                posterDataURL = videoInfoDTO.getPoster();
            } catch (Exception e) {
            }
        } else if (videoInfoDTO != null && videoInfoDTO.getThumb() != null && !videoInfoDTO.getThumb().equals("null") && videoInfoDTO.getThumb().length() > 10) {
            try {
                posterDataURL = videoInfoDTO.getThumb();
            } catch (Exception e) {
            }
        }

        if (posterDataURL != null && posterDataURL.length() > 0) {
            posterDataURL = checkAndSetPosterDataURLForSingleVideo(posterDataURL, videoInfoDTO);
            posterDataURL = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(posterDataURL);
            posterDataURL = posterDataURL + "/" + width1 + "/" + imageHeight;
            return posterDataURL;
        }

        return "";
    }

    private String checkAndSetPosterDataURLForSingleVideo(String posterDataURL, VideoInfoDTO videoInfoDTO) {
        if (videoInfoDTO.isSingleVideo())
            posterDataURL = videoInfoDTO.getChannelSpotlightImage();
        return posterDataURL;
    }

//    public String getCompanyKeyNotAPIKey(String xAccessToken) {
//        return ApplicationConstants.COMPANY_KEY_NOT_API_KEY;
//    }

//    public void setCompanyKeyNotAPIKey(String xAccessToken) {
//        try {
//            Base64 decoder = new Base64(true);
//            byte[] secret = decoder.decodeBase64(xAccessToken.split("\\.")[1]);
//            String s = new String(secret);
//            try {
//                JSONObject accessTokenJSONObject = new JSONObject(s);
//                if (accessTokenJSONObject.has("iss")) {
//                    ApplicationConstants.COMPANY_KEY_NOT_API_KEY = accessTokenJSONObject.getString("iss");
//                }
//            } catch (JSONException e) {
//                ApplicationConstants.COMPANY_KEY_NOT_API_KEY = "";
//                //e.printStackTrace();
//            }
//        } catch (Exception e) {
//            ApplicationConstants.COMPANY_KEY_NOT_API_KEY = "";
//            //e.printStackTrace();
//        }
//    }

    public int getWidthForAd(DisplayMetrics displaymetrics) {
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        int actualWidth = (width < height) ? height : width;

        return actualWidth;
    }

    public int getHeightForAd(DisplayMetrics displaymetrics) {
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;

        int actualHeight = (width < height) ? width : height;

        return actualHeight;
    }

    public int adjustAlpha(int color, float factor) {
        try {
            int alpha = Math.round(Color.alpha(color) * factor);
            int red = Color.red(color);
            int green = Color.green(color);
            int blue = Color.blue(color);
            return Color.argb(alpha, red, green, blue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return color;
    }

    /*public void styleMediaController(View view, int color) {
        if (view instanceof MediaController) {
            MediaController v = (MediaController) view;
            for (int i = 0; i < v.getChildCount(); i++) {
                styleMediaController(v.getChildAt(i), color);
            }
        } else if (view instanceof LinearLayout) {
            LinearLayout ll = (LinearLayout) view;
            //ll.setBackgroundColor(adjustAlpha(color, 0.1f));
            ll.setBackgroundColor(adjustAlpha(Color.parseColor("#000000"), 0.1f));
            for (int i = 0; i < ll.getChildCount(); i++) {
                styleMediaController(ll.getChildAt(i), color);
            }
        } else if (view instanceof SeekBar) {
            *//*((SeekBar) view)
                    .getProgressDrawable()
                    .mutate()
                    .setColorFilter(
                            getResources().getColor(
                                    Color.RED),
                            PorterDuff.Mode.SRC_IN);*//*
            ((SeekBar) view).getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            ((SeekBar) view).getThumb().setColorFilter(color, PorterDuff.Mode.SRC_IN);
            //Drawable thumb = ((SeekBar) view).getThumb().mutate();
            *//*if (thumb instanceof android.support.v4.graphics.drawable.DrawableWrapper) {
                //compat mode, requires support library v4
                ((android.support.v4.graphics.drawable.DrawableWrapper) thumb).setCompatTint(getResources()
                        .getColor(R.color.MediaPlayerThumbColor));
            } else {
                //lollipop devices
                thumb.setColorFilter(
                        getResources().getColor(R.color.MediaPlayerThumbColor),
                        PorterDuff.Mode.SRC_IN);
            }*//*
        }
    }
*/
    public View getExtraSpaceView(Context context, int height) {
        View extraSpaceView = new RelativeLayout(context);
        extraSpaceView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        return extraSpaceView;
    }

    public void sysout(String className, String methodName, String message) {
        System.out.println(className + " ( " + methodName + " ) ==>" + message);
    }

   /* public String getYearLangCountryDurationString(VideoInfoDTO vidInfoDTO) {
        String videoYearString = "";
        if (vidInfoDTO.getVideoYear() != null && !vidInfoDTO.getVideoYear().equals("-") && !vidInfoDTO.getVideoYear().equals("null"))
            videoYearString = vidInfoDTO.getVideoYear();
        else
            videoYearString = "";

        String videoLanguageString = "";
        if (vidInfoDTO.getVideoLanguage() != null && !vidInfoDTO.getVideoLanguage().equals("-") && !vidInfoDTO.getVideoLanguage().equals("null")) {
            if (videoYearString != null && videoYearString.length() > 0)
                videoLanguageString = " | ";
            videoLanguageString = videoLanguageString + vidInfoDTO.getVideoLanguage();
        } else
            videoLanguageString = "";

        String videoCountryString = "";
        if (vidInfoDTO.getCountry() != null && !vidInfoDTO.getCountry().equals("-") && !vidInfoDTO.getCountry().equals("null")) {
            if ((videoYearString != null && videoYearString.length() > 0) ||
                    (videoLanguageString != null && videoLanguageString.length() > 0))
                videoCountryString = " | ";
            videoCountryString = videoCountryString + vidInfoDTO.getCountry();
        } else
            videoCountryString = "";

        String videoDuraString = "";
        if ((videoYearString != null && !videoYearString.equals("-") && videoYearString.length() > 0) ||
                (videoLanguageString != null && videoLanguageString.length() > 0) ||
                (videoCountryString != null && videoCountryString.length() > 0))
            videoDuraString = " | ";
        videoDuraString = videoDuraString + "" + CommonCoreLibraryUtils.convertMilliToString(vidInfoDTO.getVideoDuration() * 1000);


        String yearLangCountryDurationString = "";
        try {
            yearLangCountryDurationString = videoYearString + videoLanguageString + videoCountryString + videoDuraString;
        } catch (Exception e) {
        }

        return yearLangCountryDurationString;
    }*/

    /*public String getYearLangDurationString(VideoInfoDTO vidInfoDTO) {
        String videoYearString = "";
        if (vidInfoDTO.getVideoYear() != null && !vidInfoDTO.getVideoYear().equals("-") && !vidInfoDTO.getVideoYear().equals("null"))
            videoYearString = vidInfoDTO.getVideoYear();
        else
            videoYearString = "";

        String videoLanguageString = "";
        if (vidInfoDTO.getVideoLanguage() != null && !vidInfoDTO.getVideoLanguage().equals("-") && !vidInfoDTO.getVideoLanguage().equals("null")) {
            if (videoYearString != null && videoYearString.length() > 0)
                videoLanguageString = " | ";
            videoLanguageString = videoLanguageString + vidInfoDTO.getVideoLanguage();
        } else
            videoLanguageString = "";

        String videoCountryString = "";
        if (vidInfoDTO.getCountry() != null && !vidInfoDTO.getCountry().equals("-") && !vidInfoDTO.getCountry().equals("null")) {
            if ((videoYearString != null && videoYearString.length() > 0) ||
                    (videoLanguageString != null && videoLanguageString.length() > 0))
                videoCountryString = " | ";
            videoCountryString = videoCountryString + vidInfoDTO.getCountry();
        } else
            videoCountryString = "";

        String videoDuraString = "";
        if ((videoYearString != null && !videoYearString.equals("-") && videoYearString.length() > 0) ||
                (videoLanguageString != null && videoLanguageString.length() > 0) ||
                (videoCountryString != null && videoCountryString.length() > 0))
            videoDuraString = " | ";
        videoDuraString = videoDuraString + "" + CommonCoreLibraryUtils.convertMilliToString(vidInfoDTO.getVideoDuration() * 1000);


        String yearLangCountryDurationString = "";
        try {
            yearLangCountryDurationString = videoYearString + videoLanguageString + videoDuraString;
        } catch (Exception e) {
        }

        return yearLangCountryDurationString;
    }*/

    public String getVideoYearRatingLanguage(VideoInfoDTO currentVideoInfoDTO) {
        String videoYear = "";
        String videoRating = "";
        String videoLanguage = "";

        try {
            videoYear = currentVideoInfoDTO.getVideoYear();
        } catch (Exception e) {
            videoYear = "";
        }
        try {
            videoRating = currentVideoInfoDTO.getVideoRating();
        } catch (Exception e) {
            videoRating = "";
        }
        try {
            videoLanguage = currentVideoInfoDTO.getVideoLanguage();
        } catch (Exception e) {
            videoLanguage = "";
        }

        String videoText1String = "";
        if (videoYear != null && !videoYear.equals("-") && videoYear.length() > 0) {
            videoText1String = videoYear;
        }
        if (videoRating != null && !videoRating.equals("-") && videoRating.length() > 0) {
            videoText1String = videoText1String + " | ";
        }
        if (videoRating != null && !videoRating.equals("-") && videoRating.length() > 0) {
            videoText1String = videoText1String + videoRating;
        }
        if (videoLanguage != null && !videoLanguage.equals("-") && videoLanguage.length() > 0) {
            videoText1String = videoText1String + " | ";
        }
        if (videoLanguage != null && !videoLanguage.equals("-") && videoLanguage.length() > 0) {
            videoText1String = videoText1String + videoLanguage;
        }
        if (currentVideoInfoDTO.getVideoCompanyName() != null && !currentVideoInfoDTO.getVideoCompanyName().equals("-") && currentVideoInfoDTO.getVideoCompanyName().length() > 0 && videoText1String != null && videoText1String.length() > 0) {
            videoText1String = videoText1String + " | ";
        }

        return videoText1String;
    }

    public void getAudioFocus(final Context context) {
        try {
            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            // Request audio focus for playback
            int result = am.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                                                  @Override
                                                  public void onAudioFocusChange(int i) {
                                                      AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                                                      switch (i) {

                                                          case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                                                              // Lower the volume while ducking.
                                                              break;
                                                          case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                                                              break;

                                                          case (AudioManager.AUDIOFOCUS_LOSS):
                                                              break;

                                                          case (AudioManager.AUDIOFOCUS_GAIN):
                                                              // Return the volume to normal and resume if paused.
                                                              break;
                                                          default:
                                                              break;
                                                      }
                                                  }
                                              },
                    // Use the music stream.
                    AudioManager.STREAM_MUSIC,
                    // Request permanent focus.
                    AudioManager.AUDIOFOCUS_GAIN);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                // other app had stopped playing song now , so u can do u stuff now .
                am.registerMediaButtonEventReceiver(new ComponentName(context.getPackageName(), MediaButtonReceiver.class.getName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getImageDimensions(Uri uri, Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            imageBitmap = new DownloadImageTask(uri.toString()).execute().get();

            if (imageBitmap != null) {
                String returnString = imageBitmap.getWidth() + "|" + imageBitmap.getHeight();
                return returnString;
            }
            return "1|1";
        } catch (Exception e) {
            e.printStackTrace();
            return "1|1";
        }
    }

    public Bitmap imageBitmap;

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        public String urlDisplay = "";

        public DownloadImageTask(String url) {
            urlDisplay = url;
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap mIcon11 = null;
            try {
                System.out.println("urlDisplay==>" + urlDisplay);
                InputStream in = new URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                //e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null)
                imageBitmap = result;
        }
    }

    public String attachHttpsProtocolToUrl(String url) {
        if (url != null) {
            if (url.indexOf("https:") < 0 && url.indexOf("http:") < 0) {
                if (url.substring(0, 2).equalsIgnoreCase("//")) {
                    url = "https:" + url;
                } else {
                    url = "https://" + url;
                }
            } else if (url.indexOf("https:////") == 0) {
                url = url.replace("https:////", "https://");
            } else if (url.indexOf("http:////") == 0) {
                url = url.replace("http:////", "http://");
            }
            if (url != null && url.length() > 0) {
                return url;
            }
        }

        return "";
    }

    public static String getPlatformNameFromTechnicalName(String tName) {
        try {
            String mName = tName;
            switch (tName) {
                case "roku_mrss":
                    mName = "Roku mrss";
                    break;
                case "vewd_mrss":
                    mName = "Vewd mrss";
                    break;
                case "xumo_mrss":
                    mName = "Xumo mrss";
                    break;
                case "android_TV":
                    mName = "Android TV";
                    break;
                case "amazon_fire":
                    mName = "Amazon Fire TV";
                    break;
                case "andriod":
                    mName = "Android";
                    break;
                case "ios":
                    mName = "iOS";
                    break;
                case "roku":
                    mName = "Roku TV";
                    break;
                case "apple_tv":
                    mName = "Apple TV";
                    break;
                case "website":
                    mName = "Website";
                    break;

                default:
                    mName = tName;
            }
            return mName;
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * Utlity method to convert set into comma separated string and convert the valid alert message
     * @param stringSet ==> platforms set technical values
     * @return Form the Message for showing it into alert
     */
    public static String getStringPlatformWithChannel(Set<String> stringSet) {

        StringBuilder strPlatforms = new
                StringBuilder();
        Object[] array = stringSet.toArray();
        for (int index = 0; index < array.length; index++) {
            if (index == 0) {
                strPlatforms.append(getPlatformNameFromTechnicalName(array[index].toString()));
            } else if (index == (array.length - 1)) {
                strPlatforms.append(" and ").append(getPlatformNameFromTechnicalName(array[index].toString()));

            } else {
                strPlatforms.append(",").append(getPlatformNameFromTechnicalName(array[index].toString()));
            }
        }
        return strPlatforms.toString();
    }

}
