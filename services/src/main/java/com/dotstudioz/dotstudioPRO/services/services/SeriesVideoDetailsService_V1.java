package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.models.dto.AdDTO;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightCategoriesDTO;
import com.dotstudioz.dotstudioPRO.models.dto.VideoInfoDTO;

import com.dotstudioz.dotstudioPRO.services.util.CommonServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Admin on 17-01-2016.
 */
public class SeriesVideoDetailsService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public ISeriesVideoDetailsService_V1 iSeriesVideoDetailsService_V1;
    public interface ISeriesVideoDetailsService_V1 {
        void callBackFromFetchSeriesVideoDetails(ArrayList<VideoInfoDTO> videoInfoDTOArrayList);
        void fetchVideoPlaybackDetailsForSeries(String videoID);
        void getSeriesVideoDetailsServiceError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }

    ArrayList<VideoInfoDTO> videoInfoDTOArrayList = null;
    int numberOfVideosToFetchForSliderShowCase = 0;
    List<SpotLightCategoriesDTO> spotLightCategoriesDTOListForSliderShowcase;
    String channelLink = "";
    String channelID = "";
    public SeriesVideoDetailsService_V1(Context ctx, ArrayList<VideoInfoDTO> videoInfoDTOArrayList,
                                        String channelLink, String channelID) {
        this.videoInfoDTOArrayList = videoInfoDTOArrayList;
        this.channelID = channelID;
        this.channelLink = channelLink;

        if (ctx instanceof SeriesVideoDetailsService_V1.ISeriesVideoDetailsService_V1)
            iSeriesVideoDetailsService_V1 = (SeriesVideoDetailsService_V1.ISeriesVideoDetailsService_V1) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement ISeriesVideoDetailsService_V1");
    }

    public void fetchSeriesVideoDetails(String API_URL) {
        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
        if(ApplicationConstants.CLIENT_TOKEN != null && ApplicationConstants.CLIENT_TOKEN.length() > 0)
            headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));

        /*System.out.println("fetchSeriesVideoDetails==>ApplicationConstants.xAccessToken==>"+ApplicationConstants.xAccessToken);
        System.out.println("fetchSeriesVideoDetails==>ApplicationConstants.CLIENT_TOKEN==>"+ApplicationConstants.CLIENT_TOKEN);
        if(ApplicationConstants.TEMP_BOOLEAN_FLAG) {
            ApplicationConstants.CLIENT_TOKEN = ApplicationConstants.CLIENT_TOKEN.substring(0, ApplicationConstants.CLIENT_TOKEN.length() - 5);
            ApplicationConstants.TEMP_BOOLEAN_FLAG = false;
        }
        System.out.println("fetchSeriesVideoDetails==>ApplicationConstants.CLIENT_TOKEN==>"+ApplicationConstants.CLIENT_TOKEN);*/

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, null,
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInSeriesVideoPageString);
    /*    CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpClient(headerItemsArrayList, null,
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInSeriesVideoPageString);*/
    }

    boolean someVideoDataMissing = false;
    public void processJSONResponseObject(JSONObject response) {
        Log.d("processJSONResponse", response.toString());
        JSONObject obj = response;

        boolean isGeoBlocked = false;
        try {
            if(obj.has("geoCheck")) {
                if(!obj.getBoolean("geoCheck")) {
                    isGeoBlocked = true;
                }
            }
        } catch(Exception e) {
            isGeoBlocked = false;
        }

        if(!isGeoBlocked) {
            VideoInfoDTO videoInfoDTO = new VideoInfoDTO();
            videoInfoDTO.setChannelLink(channelLink);
            videoInfoDTO.setSingleVideo(false);

            try {
                try {
                    videoInfoDTO.setVideoID(obj.getString("_id"));
                } catch (JSONException e) {
                    someVideoDataMissing = true;
                }

                try {
                    videoInfoDTO.setCountry(obj.getString("country"));
                } catch (JSONException e) {
                    videoInfoDTO.setCountry("-");
                }

                try {
                    videoInfoDTO.setCompanyId(obj.getString("company_id"));
                } catch (JSONException e) {
                    videoInfoDTO.setCompanyId("");
                }

                if (obj.getString("source").toLowerCase().equals("youtube")) {
                    videoInfoDTO.setSource("Youtube");
                    videoInfoDTO.setIsYoutubeVideo(true);
                    try {
                        if (obj.has("youtube")) {
                            String youtubeVideoID = obj.getJSONObject("youtube").getString("id");
                            videoInfoDTO.setYoutubeVideoID(youtubeVideoID);
                        } else {
                            if (obj.has("video_m3u8")) {
                                String youtubeURL = obj.getString("video_m3u8");
                                youtubeURL = obj.getString("video_m3u8");
                                if (!youtubeURL.substring(0, 8).equals("https://"))
                                    youtubeURL = "https://" + youtubeURL;
                                if (youtubeURL != null && youtubeURL != "null" && youtubeURL.length() > 4) {
                                    String youtubeVideoID = youtubeURL.substring((youtubeURL.indexOf("?v=") + 3), youtubeURL.length());
                                    videoInfoDTO.setYoutubeVideoID(youtubeVideoID);
                                }
                            }
                        }
                    } catch (Exception e) {

                    }
                } else {
                    videoInfoDTO.setIsYoutubeVideo(false);
                }

                {
                    String sourceValue = "";

                    try {
                        sourceValue = obj.getString("source");
                    } catch (JSONException e) {
                        sourceValue = "";
                    }

                    String geoBlockString = "";
                    if (obj.has("geoblock")) {
                        geoBlockString = obj.getString("geoblock");
                    }
                    if (geoBlockString.length() > 0) {
                        if (!geoBlockString.toLowerCase().equals("all")) {
                            videoInfoDTO.setIsGeoBlocked(true);
                            videoInfoDTO.setGeoBlockedDisabledCountries(obj.getString("geoblock"));
                        } else {
                            videoInfoDTO.setIsGeoBlocked(false);
                        }
                    }

                    if (sourceValue.equals("Live")) {
                        videoInfoDTO.setSource("Live");
                        videoInfoDTO.setVideoToPlayURL(obj.getJSONObject("live").getString("url"));
                    } else {
                        if (!(videoInfoDTO.isYoutubeVideo() && videoInfoDTO.getYoutubeVideoID() != null && videoInfoDTO.getYoutubeVideoID().length() > 0)) {
                            videoInfoDTO.setSource("");
                            try {
                                String videoToPlayURL = obj.getString("video_m3u8");
                                if(videoToPlayURL.indexOf("https:") < 0 && videoToPlayURL.indexOf("http:") < 0) {
                                    if(videoToPlayURL.substring(0, 2).equalsIgnoreCase("//")) {
                                        videoToPlayURL = "https:" + videoToPlayURL;
                                    } else {
                                        videoToPlayURL = "https://" + videoToPlayURL;
                                    }
                                }
                                /*if (!videoToPlayURL.substring(0, 7).equals("http://"))
                                    videoToPlayURL = "https://" + videoToPlayURL;
                                if (!videoToPlayURL.substring(0, 4).equals("http"))
                                    videoToPlayURL = "https://" + videoToPlayURL;*/
                                videoInfoDTO.setVideoToPlayURL(videoToPlayURL);

                                try {
                                    //String serverURL = "https://k7q5a5e5.ssl.hwcdn.net/";
                                    String serverURL = "https://e7g8u6d9.ssl.hwcdn.net/";
                                    String vodDir = obj.getJSONObject("paths").getString("vodDir");
                                    String videoFileName = "/" + videoInfoDTO.getVideoID() + ".m3u8";
                                    serverURL = serverURL + vodDir + videoFileName;

                                    videoInfoDTO.setChromeCastVideoToPlayURL(serverURL);
                                    videoInfoDTO.setChromeCastVideoToPlayURLAvailable(true);
                                } catch (Exception e) {
                                    videoInfoDTO.setChromeCastVideoToPlayURLAvailable(false);
                                    e.printStackTrace();
                                }

                                //String serverURL = "http://cdn.dotstudiopro.com/";
                                /*String serverURL = "https://k7q5a5e5.ssl.hwcdn.net";
                                String vodDir = obj.getJSONObject("paths").getString("vodDir");
                                String videoFileName = "/" + videoInfoDTO.getVideoID() + ".m3u8";
                                activity.videoToPlayURL = serverURL + vodDir + videoFileName;
                                videoInfoDTO.setVideoToPlayURL(activity.videoToPlayURL);*/

                                //https://k7q5a5e5.ssl.hwcdn.net/files/company/53fd1266d66da833047b23c6/assets/videos/540f28fdd66da89e1ed70281/vod/540f28fdd66da89e1ed70281.m3u8
                            } catch (JSONException e) {
                                //videoToPlayURL = obj.getJSONObject("data").getString("video_m3u8");
                                //String serverURL = "https://k7q5a5e5.ssl.hwcdn.net/";
                                String serverURL = "http://cdn.dotstudiopro.com/";
                                String vodDir = obj.getJSONObject("paths").getString("vodDir");
                                String videoFileName = "/" + videoInfoDTO.getVideoID() + ".m3u8";
                                String videoToPlayURL = serverURL + vodDir + videoFileName;

                                videoInfoDTO.setVideoToPlayURL(videoToPlayURL);

                                e.printStackTrace();
                            }
                        }
                    }

                    try {
                        videoInfoDTO.setSlug(obj.getString("slug"));
                    } catch (Exception e) {
                        videoInfoDTO.setSlug("");
                        e.printStackTrace();
                    }

                    try {
                        videoInfoDTO.setServerSideAdsEnabled(obj.getBoolean("server_side_ads"));
                    } catch(Exception e) {
                        videoInfoDTO.setServerSideAdsEnabled(false);
                        e.printStackTrace();
                    }

                    try {
                        videoInfoDTO.setAccessValue(obj.getString("access"));
                        videoInfoDTO.setIsTeaserAvailable(false);

                        if (videoInfoDTO.getAccessValue().equals("paywall")) {
                            videoInfoDTO.setTeaserID(obj.getJSONObject("teaser_trailer").getString("_id"));
                            videoInfoDTO.setIsTeaserAvailable(true);
                        } else {
                            videoInfoDTO.setIsTeaserAvailable(false);
                        }
                    } catch (JSONException e) {
                        videoInfoDTO.setIsTeaserAvailable(false);
                    }

                    try {
                        videoInfoDTO.setRentalPrice(obj.getJSONObject("paywall").getString("rental_price"));
                    } catch (JSONException e) {
                        videoInfoDTO.setRentalPrice("");
                    }

                    try {
                        if(obj.getJSONObject("paywall").has("presell")) {
                            try {
                                JSONObject presell = obj.getJSONObject("paywall").getJSONObject("presell");
                                if(presell.has("stream_start")) {
                                    videoInfoDTO.getPresellDTO().streamStart = presell.getString("stream_start");
                                }
                                if(presell.has("stream_end")) {
                                    videoInfoDTO.getPresellDTO().streamEnd = presell.getString("stream_end");
                                }
                                if(presell.has("rental_price")) {
                                    videoInfoDTO.getPresellDTO().rentalPrice = presell.getString("rental_price");
                                }
                            } catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        videoInfoDTO.setRentalPeriod(obj.getJSONObject("paywall").getString("rental_period"));
                    } catch (JSONException e) {
                        videoInfoDTO.setRentalPeriod("");
                    }

                    /*try {
                        videoInfoDTO.setS(obj.getString("access"));
                    } catch (JSONException e) {
                        videoInfoDTO.setIsTeaserAvailable(false);
                    }*/

                    try {
                        videoInfoDTO.setAndroidZoneID(obj.getJSONObject("company").getJSONObject("adserver").getString("android"));
                    } catch (Exception e) {
                        videoInfoDTO.setAndroidZoneID("");
                        e.printStackTrace();
                    }

                    if (videoInfoDTO.isTeaserAvailable()) {
                        String teaserURL = "";
                        /*try {
                            teaserURL = ApplicationConstantURL.getInstance().TEASER_DOMAIN + obj.getJSONObject("teaser").getString("paths");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        try {
                            teaserURL = obj.getJSONObject("teaser_trailer").getString("url");
                            if(teaserURL.indexOf("https:") < 0 && teaserURL.indexOf("http:") < 0) {
                                if(teaserURL.substring(0, 2).equalsIgnoreCase("//")) {
                                    teaserURL = "https:" + teaserURL;
                                } else {
                                    teaserURL = "https://" + teaserURL;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (teaserURL.length() > 0 && teaserURL != null)
                            videoInfoDTO.setTeaserURL(teaserURL);
                        else
                            videoInfoDTO.setIsTeaserAvailable(false);
                    }

                    try {
                        String posterDataURL = obj.getString("thumb");
                        posterDataURL = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(posterDataURL);

                        videoInfoDTO.setThumb(posterDataURL);
                    } catch (JSONException e) {
                        videoInfoDTO.setThumb("");
                    }

                    try {
                        if (obj.has("teaser_trailer")) {
                            if (obj.getJSONObject("teaser_trailer").has("_id")) {
                                videoInfoDTO.setTeaserID(obj.getJSONObject("teaser_trailer").getString("_"));
                            } else {
                                videoInfoDTO.setTeaserID("");
                            }
                        }
                    } catch (JSONException e) {
                        videoInfoDTO.setTeaserID("");
                    }
                    try {
                        if (obj.has("teaser_trailer")) {
                            if (obj.getJSONObject("teaser_trailer").has("url")) {
                                videoInfoDTO.setTeaserTrailer(obj.getJSONObject("teaser_trailer").getString("url"));
                            } else {
                                videoInfoDTO.setTeaserTrailer("");
                            }
                        }
                    } catch (JSONException e) {
                        videoInfoDTO.setTeaserTrailer("");
                    }
                    try {
                        if (obj.has("teaser_trailer")) {
                            if (obj.getJSONObject("teaser_trailer").has("thumb")) {
                                videoInfoDTO.setTeaserTrailerThumb(obj.getJSONObject("teaser_trailer").getString("thumb"));
                            } else {
                                videoInfoDTO.setTeaserTrailerThumb("");
                            }
                        }
                    } catch (JSONException e) {
                        videoInfoDTO.setTeaserTrailerThumb("");
                    }

                    try {
                        if (obj.getJSONObject("ads").getString("pre").equals("yes"))
                            videoInfoDTO.setIsPreRollToBePlayed(true);
                        else
                            videoInfoDTO.setIsPreRollToBePlayed(false);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if(obj.has("ad_tag")) {
                            if(obj.getJSONObject("ad_tag").has("pre")) {
                                videoInfoDTO.setPreRollAdFixIssueVMAP(obj.getJSONObject("ad_tag").getString("pre"));
                            }
                        }
                        if(videoInfoDTO.getPreRollAdFixIssueVMAP() != null && videoInfoDTO.getPreRollAdFixIssueVMAP().length() > 0)
                            videoInfoDTO.setPreRollToBePlayed(true);
                        else
                            videoInfoDTO.setPreRollToBePlayed(false);
                    } catch(Exception e) {
                        e.printStackTrace();
                        videoInfoDTO.setPreRollToBePlayed(false);
                    }
                    try {

                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if(videoInfoDTO.isPreRollToBePlayed() && obj.has("ads")) {
                            if(obj.getJSONObject("ads").has("pre_offset")) {
                                if(obj.getJSONObject("ads").getJSONObject("pre_offset").has("pre_ads_number")) {
                                    videoInfoDTO.setNoOfPreRollToBePlayed(Integer.parseInt(obj.getJSONObject("ads").getJSONObject("pre_offset").getString("pre_ads_number")));
                                }
                                if(obj.getJSONObject("ads").getJSONObject("pre_offset").has("offsets")) {
                                    if(obj.getJSONObject("ads").getJSONObject("pre_offset").getJSONObject("offsets").has("linear")) {
                                        videoInfoDTO.setNoOfPreRollToBePlayed(obj.getJSONObject("ads").getJSONObject("pre_offset").getJSONObject("offsets").getJSONArray("linear").length());
                                    }
                                }
                            }
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if(obj.has("ad_tag")) {
                            if(obj.getJSONObject("ad_tag").has("post")) {
                                videoInfoDTO.setPostRollAdFixIssueVMAP(obj.getJSONObject("ad_tag").getString("post"));
                            }
                        }
                        if(videoInfoDTO.getPostRollAdFixIssueVMAP() != null && videoInfoDTO.getPostRollAdFixIssueVMAP().length() > 0)
                            videoInfoDTO.setPostRollToBePlayed(true);
                        else
                            videoInfoDTO.setPostRollToBePlayed(false);
                    } catch(Exception e) {
                        e.printStackTrace();
                        videoInfoDTO.setPostRollToBePlayed(false);
                    }
                    try {
                        if(videoInfoDTO.isPostRollToBePlayed() && obj.has("ads")) {
                            if(obj.getJSONObject("ads").has("post_offset")) {
                                if(obj.getJSONObject("ads").getJSONObject("post_offset").has("post_ads_number")) {
                                    videoInfoDTO.setNoOfPostRollToBePlayed(Integer.parseInt(obj.getJSONObject("ads").getJSONObject("post_offset").getString("post_ads_number")));
                                }
                            }
                            if(obj.getJSONObject("ads").getJSONObject("post_offset").has("offsets")) {
                                if(obj.getJSONObject("ads").getJSONObject("post_offset").getJSONObject("offsets").has("linear")) {
                                    videoInfoDTO.setNoOfPostRollToBePlayed(obj.getJSONObject("ads").getJSONObject("post_offset").getJSONObject("offsets").getJSONArray("linear").length());
                                }
                            }
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if(obj.has("ad_tag")) {
                            if(obj.getJSONObject("ad_tag").has("mid")) {
                                videoInfoDTO.setMidRollAdFixIssueVMAP(obj.getJSONObject("ad_tag").getString("mid"));
                                //videoInfoDTO.setMidRollAdFixIssueVMAP("https://redirector.gvt1.com/videoplayback/id/396d2e35a55f7bf8/itag/18/source/gfp_video_ads/requiressl/yes/acao/yes/mime/video%2Fmp4/ip/0.0.0.0/ipbits/0/expire/1494514576/sparams/ip,ipbits,expire,id,itag,source,requiressl,acao,mime/signature/7C921E4C17546B86F0B17C4E8C127EF61BF01C80.70229DB23921846D271069F4C2DC907777BB7F4C/key/ck2/file/file.mp4");
                            }
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        String midroll = obj.getJSONObject("ads").getString("mid");
                        if(obj.has("ads")) {
                            if(obj.getJSONObject("ads").has("mid")) {
                                if(obj.getJSONObject("ads").getString("mid").equals("yes")) {
                                    videoInfoDTO.setIsMidRollToBePlayed(true);
                                    if(obj.getJSONObject("ads").has("mid_offset")) {
                                        if(obj.getJSONObject("ads").getJSONObject("mid_offset").has("offsets")) {
                                            if(obj.getJSONObject("ads").getJSONObject("mid_offset").getJSONObject("offsets").has("linear")) {
                                                if(obj.getJSONObject("ads").getJSONObject("mid_offset").getJSONObject("offsets").getJSONArray("linear").length() > 0) {
                                                    for(int i = 0; i < obj.getJSONObject("ads").getJSONObject("mid_offset").getJSONObject("offsets").getJSONArray("linear").length(); i++) {
                                                        try {
                                                            JSONArray linearOffset = obj.getJSONObject("ads").getJSONObject("mid_offset").getJSONObject("offsets").getJSONArray("linear");
                                                            int[] linearOffsetInt = new int[linearOffset.length()];
                                                            int arrayLength = linearOffset.length();

                                                            int m = 0;
                                                            do {
                                                                String checkForDecimal = linearOffset.get(m).toString();
                                                                checkForDecimal = checkForDecimal.split("\\.")[0];
                                                                //activity.linearOffsetInt[i] = Integer.parseInt("" + activity.linearOffset.get(i));
                                                                linearOffsetInt[m] = Integer.parseInt(checkForDecimal);
                                                                m++;
                                                            } while (m < arrayLength);

                                                            Arrays.sort(linearOffsetInt);
                                                            videoInfoDTO.setLinearOffset(linearOffsetInt);

                                                        } catch (Exception e) {
                                                            videoInfoDTO.setIsMidRollToBePlayed(false);
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        /*if (midroll.equals("yes")) {
                            try {
                                videoInfoDTO.setIsMidRollToBePlayed(true);
                                activity.linearOffset = obj.getJSONObject("ads").getJSONObject("offsets").getJSONArray("linear");
                                activity.linearOffsetInt = new int[activity.linearOffset.length()];
                                int arrayLength = activity.linearOffset.length();

                                int i = 0;
                                do {
                                    String checkForDecimal = activity.linearOffset.get(i).toString();
                                    checkForDecimal = checkForDecimal.split("\\.")[0];
                                    //activity.linearOffsetInt[i] = Integer.parseInt("" + activity.linearOffset.get(i));
                                    activity.linearOffsetInt[i] = Integer.parseInt(checkForDecimal);
                                    i++;
                                } while (i < arrayLength);

                                Arrays.sort(activity.linearOffsetInt);
                                videoInfoDTO.setLinearOffset(activity.linearOffsetInt);

                            } catch (Exception e) {
                                videoInfoDTO.setIsMidRollToBePlayed(false);
                                e.printStackTrace();
                            }
                        }*/
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    //videoInfoDTO.setIsMidRollToBePlayed(false);

                    try {
                        if (obj.getJSONObject("ads").getString("post").equals("yes"))
                            videoInfoDTO.setIsPostRollToBePlayed(true);
                        else
                            videoInfoDTO.setIsPostRollToBePlayed(false);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    //videoInfoDTO.setPostRollToBePlayed(true);
                    //videoInfoDTO.setNoOfPostRollToBePlayed(2);

                    /*int numberOfAdsExpected = 0;
                    //adding the ad tags to the videoInfoDTO
                    if (videoInfoDTO.isPreRollToBePlayed())
                        numberOfAdsExpected++;
                    if (videoInfoDTO.isPostRollToBePlayed())
                        numberOfAdsExpected++;
                    if (videoInfoDTO.isMidRollToBePlayed()) {
                        if (videoInfoDTO.getLinearOffset().length > 0) {
                            numberOfAdsExpected = numberOfAdsExpected + videoInfoDTO.getLinearOffset().length;
                        }
                    }*/

                    //based on numberOfAdsExpected, we have to extract the information from the API response

                    /*try {
                        if(obj.has("androidads_breaks")) {
                            for (int i = 0; i < numberOfAdsExpected; i++) {
                                String parameterName = "adbreak"+(i+1);
                                AdDTO adDTO = new AdDTO();
                                adDTO.setType(obj.getJSONObject("androidads_breaks").getJSONObject(parameterName).getString("type"));
                                adDTO.setOffset(obj.getJSONObject("androidads_breaks").getJSONObject(parameterName).getString("offset"));
                                adDTO.setTag(obj.getJSONObject("androidads_breaks").getJSONObject(parameterName).getJSONArray("tag").get(0).toString());
                                videoInfoDTO.getAdDTOArrayList().add(adDTO);
                            }
                        }
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }*/

                    try {
                        if (obj.has("androidads_breaks")) {
                            Iterator<String> iterator = obj.getJSONObject("androidads_breaks").keys();
                            while (iterator.hasNext()) {
                                try {
                                    AdDTO adDTO = new AdDTO();
                                    String keyValue = iterator.next();
                                    adDTO.setType(obj.getJSONObject("androidads_breaks").getJSONObject(keyValue).getString("type"));
                                    adDTO.setOffset(obj.getJSONObject("androidads_breaks").getJSONObject(keyValue).getString("offset"));
                                    adDTO.setTag(obj.getJSONObject("androidads_breaks").getJSONObject(keyValue).getJSONArray("tag").get(0).toString());
                                    videoInfoDTO.getAdDTOArrayList().add(adDTO);
                                } catch (JSONException e) {/* Catching and continuing, so that we do not ignore the rest*/} catch (Exception e) {/* Catching and continuing, so that we do not ignore the rest*/}
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        for (int i = 0; i < videoInfoDTO.getAdDTOArrayList().size(); i++) {
                            if (videoInfoDTO.getAdDTOArrayList().get(i).getOffset().equals("pre"))
                                videoInfoDTO.setNoOfPreRollToBePlayed((videoInfoDTO.getNoOfPreRollToBePlayed() + 1));
                            else if (videoInfoDTO.getAdDTOArrayList().get(i).getOffset().equals("post"))
                                videoInfoDTO.setNoOfPostRollToBePlayed((videoInfoDTO.getNoOfPostRollToBePlayed() + 1));
                        }
                    } catch (Exception e) {
                    }

                    /*String videoDuration = obj.getString("duration");
                    float floatVideoDuration = Float.parseFloat(videoDuration);
                    int videoDurationInt = (int) floatVideoDuration;

                    videoInfoDTO.setVideoDuration(videoDurationInt);*/

                    try {
                        String duraString = obj.getString("duration");
                        float floatVideoDuration = Float.parseFloat(duraString);
                        int videoDurationInt = (int) floatVideoDuration;
                        videoInfoDTO.setVideoDuration(videoDurationInt);
                    } catch(Exception e) { videoInfoDTO.setVideoDuration(0); }
                    if(videoInfoDTO.getVideoPausedPoint() == 0) {
                        try {
                            int duraInt = obj.getInt("duration");
                            videoInfoDTO.setVideoDuration(duraInt);
                        } catch(Exception e) { videoInfoDTO.setVideoDuration(0); }
                    }
                    if(videoInfoDTO.getVideoPausedPoint() == 0) {
                        try {
                            float floatVideoDuration = (float)(obj.getDouble("duration"));
                            int videoDurationInt = (int) floatVideoDuration;
                            videoInfoDTO.setVideoDuration(videoDurationInt);
                        } catch(Exception e) { videoInfoDTO.setVideoDuration(0); }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                try {
                    videoInfoDTO.setVideoTitle(obj.getString("title"));
                } catch (Exception e) {
                    videoInfoDTO.setVideoTitle("");
                }

                try {
                    videoInfoDTO.setSeriesTitle(obj.getString("seriestitle"));
                } catch (Exception e) {
                    videoInfoDTO.setSeriesTitle("");
                }

                try {
                    videoInfoDTO.setDescription(obj.getString("description"));
                } catch (Exception e) {
                    videoInfoDTO.setDescription("");
                }

                String casting = "";
                String writterDirector = "";
                JSONArray castingArray = new JSONArray();
                try {
                    castingArray = obj.getJSONArray("actors");
                } catch (Exception e) {
                }
                for (int i = 0; i < castingArray.length(); i++) {
                    if (i == 0)
                        casting = "Starring: " + castingArray.get(i).toString();
                    else
                        casting = casting + ", " + castingArray.get(i).toString();
                }
                videoInfoDTO.setCasting("");
                if (casting.length() > 11)
                    videoInfoDTO.setCasting(casting);

                JSONArray writterDirectorArray = new JSONArray();
                try {
                    writterDirectorArray = obj.getJSONArray("directors");
                } catch (Exception e) {
                }
                for (int i = 0; i < writterDirectorArray.length(); i++) {
                    if (i == 0)
                        writterDirector = "Written & Directed by " + writterDirectorArray.get(i).toString();
                    else
                        writterDirector = writterDirector + ", " + writterDirectorArray.get(i).toString();
                }
                videoInfoDTO.setWritterDirector("");
                if (writterDirector.length() > 23)
                    videoInfoDTO.setWritterDirector(writterDirector);

                String ratingData = "";
                int ratingValue = 0;
                try {
                    ratingData = obj.getString("rating_average");
                    ratingValue = Integer.parseInt(ratingData);
                } catch (Exception e) {
                    ratingValue = 0;
                }
                videoInfoDTO.setRatingValue(ratingValue);

                try {
                    String posterDataURL = obj.getString("poster");
                    if (posterDataURL == null || posterDataURL.equals("null")) {
                        posterDataURL = obj.getString("thumb");
                        posterDataURL = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(posterDataURL);
                        videoInfoDTO.setPoster(posterDataURL);
                    } else if (posterDataURL != null) {
                        posterDataURL = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(posterDataURL);
                        videoInfoDTO.setPoster(posterDataURL);
                    }
                } catch (Exception e) {
                    videoInfoDTO.setPoster("default");

                    try {
                        String posterDataURL = obj.getString("thumb");
                        posterDataURL = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(posterDataURL);
                        videoInfoDTO.setThumb(posterDataURL);
                    } catch (Exception e1) {
                    }

                    e.printStackTrace();
                }

                videoInfoDTO.setChannelID(channelID);
                videoInfoDTO.setChannelLink(channelLink);

                try {
                    videoInfoDTO.setVideoYear(obj.getString("year"));
                } catch (JSONException e) {
                    videoInfoDTO.setVideoYear("-");
                }
                try {
                    videoInfoDTO.setVideoLanguage(obj.getString("language"));
                } catch (JSONException e) {
                    videoInfoDTO.setVideoLanguage("-");
                }
                try {
                    videoInfoDTO.setVideoRating(obj.getString("rating"));
                } catch (JSONException e) {
                    videoInfoDTO.setVideoRating("-");
                }
                try {
                    videoInfoDTO.setVideoCompanyName(obj.getJSONObject("company").getString("name").toUpperCase());
                } catch (JSONException e) {
                    videoInfoDTO.setVideoCompanyName("NOSEY");
                }

                try {
                    if (obj.has("videowatermarking")) {
                        if (obj.get("videowatermarking") instanceof String) {
                            if (obj.getString("videowatermarking").equals("true"))
                                videoInfoDTO.setVideoLogoWatermarkEnabled(true);
                            else
                                videoInfoDTO.setVideoLogoWatermarkEnabled(false);
                        } else {
                            if (obj.getBoolean("videowatermarking"))
                                videoInfoDTO.setVideoLogoWatermarkEnabled(true);
                            else
                                videoInfoDTO.setVideoLogoWatermarkEnabled(false);
                        }
                    }
                } catch (JSONException e) {
                    videoInfoDTO.setVideoLogoWatermarkEnabled(false);
                }

                if (videoInfoDTO.isVideoLogoWatermarkEnabled()) {
                    try {
                        videoInfoDTO.setVideoLogoWatermark(obj.getJSONObject("company").getJSONObject("watermark").getString("logo"));
                    } catch (JSONException e) {
                        videoInfoDTO.setVideoLogoWatermark("");
                    }

                    try {
                        videoInfoDTO.setVideoLogoWatermarkOpacity(Float.parseFloat(obj.getJSONObject("company").getJSONObject("watermark").getString("opacity")));
                    } catch (JSONException e) {
                        videoInfoDTO.setVideoLogoWatermarkOpacity(1f);
                    }

                    try {
                        videoInfoDTO.setVideoLogoWatermarkLink(obj.getJSONObject("company").getJSONObject("watermark").getString("link"));
                    } catch (JSONException e) {
                        videoInfoDTO.setVideoLogoWatermarkLink("");
                    }
                }

                try {
                    if (obj.has("closeCaption")) {
                        JSONArray closeCaptionJSONArray = obj.getJSONArray("closeCaption");
                        boolean alreadyFound = false;
                        for (int i = 0; i < closeCaptionJSONArray.length(); i++) {
                            if (!alreadyFound) {
                                if (((JSONObject) closeCaptionJSONArray.get(i)).has("srtExt") && (((JSONObject) closeCaptionJSONArray.get(i)).getString("srtExt").equals("srt"))) {
                                    if ((((JSONObject) closeCaptionJSONArray.get(i)).has("srtPath"))) {
                                        alreadyFound = true;
                                        videoInfoDTO.setClosedCaptionEnabled(true);
                                        videoInfoDTO.setClosedCaptionPath(((JSONObject) closeCaptionJSONArray.get(i)).getString("srtPath"));
                                    } else {
                                        videoInfoDTO.setClosedCaptionEnabled(false);
                                    }
                                } else {
                                    videoInfoDTO.setClosedCaptionEnabled(false);
                                }
                                videoInfoDTO.setVideoCompanyName(obj.getJSONObject("company").getString("name").toUpperCase());
                            }
                        }
                    } else {
                        videoInfoDTO.setClosedCaptionEnabled(false);
                    }
                } catch (JSONException e) {
                    videoInfoDTO.setClosedCaptionEnabled(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (!someVideoDataMissing) {
                videoInfoDTOArrayList.add(videoInfoDTO);
            } else {
                someVideoDataMissing = false;
            }

            try {
                //if (activity.videoInfoDTOFetchedSoFar == activity.seriesVideoCategoriesDTO.getVideosList().size()) {
                if (videoInfoDTOArrayList.size() == 1) {
                    if (!someVideoDataMissing) {
                        //moving the below piece of code into the MainActivity in the callBack methods
                        /*if (ApplicationConstants.CLIENT_TOKEN != null && ApplicationConstants.CLIENT_TOKEN.length() > 0) {
                            //Checking if there are any videos which has paywall enabled, and if so
                            // then checking if the logged in user has access to it or not, so not disabling the busy cursor
                            iSeriesVideoDetailsService_V1.fetchVideoPlaybackDetailsForSeries(videoInfoDTO.getVideoID());
                        }*/
                        iSeriesVideoDetailsService_V1.callBackFromFetchSeriesVideoDetails(videoInfoDTOArrayList);
                    } else {
                        iSeriesVideoDetailsService_V1.getSeriesVideoDetailsServiceError(ApplicationConstants.DATA_MISSING_CHANNEL);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            iSeriesVideoDetailsService_V1.getSeriesVideoDetailsServiceError(ApplicationConstants.GEO_BLOCKED_CONTENT);
        }
    }

    @Override
    public void onResultHandler(JSONObject response) {
        processJSONResponseObject(response);
    }
    @Override
    public void onErrorHandler(String ERROR) {
        iSeriesVideoDetailsService_V1.getSeriesVideoDetailsServiceError(ERROR);
    }
    @Override
    public void accessTokenExpired() {
        iSeriesVideoDetailsService_V1.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
        iSeriesVideoDetailsService_V1.clientTokenExpired();
    }
}