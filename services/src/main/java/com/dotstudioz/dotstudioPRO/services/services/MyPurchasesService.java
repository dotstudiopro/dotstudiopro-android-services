package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.models.dto.MyPurchaseItemDTO;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightCategoriesDTO;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientInterface;
import com.dotstudioz.dotstudioPRO.services.services.retrofit.RestClientManager;
import com.dotstudioz.dotstudioPRO.services.util.CommonServiceUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by mohsin on 08-10-2016.
 */

public class MyPurchasesService {

    public IMyPurchasesService iMyPurchasesService;

    Context context;
    public MyPurchasesService(Context ctx) {
        context = ctx;
        if (ctx instanceof MyPurchasesService.IMyPurchasesService)
            iMyPurchasesService = (MyPurchasesService.IMyPurchasesService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IMyPurchasesService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setMyPurchasesServiceListener(IMyPurchasesService callback) {
        this.iMyPurchasesService = callback;
    }

    public void getMyPurchases1(String xAccessToken, String xClientToken, String MY_PURCHASES_URL, final List<SpotLightCategoriesDTO> spotLightCategoriesDTOList) {
        if (iMyPurchasesService == null) {
            if (context != null && context instanceof MyPurchasesService.IMyPurchasesService) {
                iMyPurchasesService = (MyPurchasesService.IMyPurchasesService) context;
            }
            if (iMyPurchasesService == null) {
                throw new RuntimeException(context.toString()+ " must implement IMyPurchasesService or setMyPurchasesServiceListener");
            }
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
        client.addHeader("x-access-token", xAccessToken);
        client.addHeader("x-client-token", xClientToken);

        Map<String, String> jsonParams = new HashMap<String, String>();
        //jsonParams.put("x-access-token", ApplicationConstants.xAccessToken);
        //jsonParams.put("token", ApplicationConstants.xAccessToken);
        //jsonParams.put("x-client-token", ApplicationConstants.CLIENT_TOKEN);

        RequestParams rp = new RequestParams(jsonParams);

        try {
            client.get(MY_PURCHASES_URL, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                    myPurchasesServiceResponse(responseBody, spotLightCategoriesDTOList);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject responseBody) {
                    if (responseBody != null) {

                        boolean isSuccess = true;
                        try {
                            isSuccess = responseBody.getBoolean("success");
                        } catch (JSONException e) {
                            //throws error, because on success there is no boolean returned, so
                            // we are assuming that it is a success
                            isSuccess = false;
                        }

                        if (!isSuccess) {
                            if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                                AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInMyPurchasePageString);
                                if(AccessTokenHandler.getInstance().foundAnyError)
                                    iMyPurchasesService.accessTokenExpired();
                                else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                    iMyPurchasesService.clientTokenExpired();
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            iMyPurchasesService.myPurchasesServiceError(e.getMessage());
        }
    }
    public void getMyPurchases(String xAccessToken, String xClientToken, String MY_PURCHASES_URL, final List<SpotLightCategoriesDTO> spotLightCategoriesDTOList) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setMaxRetriesAndTimeout(2, 30000);
        client.setTimeout(30000);
        client.addHeader("x-access-token", xAccessToken);
        client.addHeader("x-client-token", xClientToken);

        Map<String, String> jsonParams = new HashMap<String, String>();
        //jsonParams.put("x-access-token", ApplicationConstants.xAccessToken);
        //jsonParams.put("token", ApplicationConstants.xAccessToken);
        //jsonParams.put("x-client-token", ApplicationConstants.CLIENT_TOKEN);

        RequestParams rp = new RequestParams(jsonParams);

        try {
            try {
                //adding a front slash, because retro api call fails if it is not there at the end
                if(!MY_PURCHASES_URL.substring(MY_PURCHASES_URL.length()-1, MY_PURCHASES_URL.length()).equalsIgnoreCase("/")) {
                    MY_PURCHASES_URL = MY_PURCHASES_URL + "/";
                }
                RestClientInterface restClientInterface = RestClientManager.getClient(MY_PURCHASES_URL, ApplicationConstants.xAccessToken, ApplicationConstants.CLIENT_TOKEN, null).create(RestClientInterface.class);
                Call<Object> call1 = restClientInterface.requestGet(MY_PURCHASES_URL);
                call1.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                        try {
                            if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                                // iClientTokenService.clientTokenServiceError(t.getMessage());
                                boolean isSuccess = true;
                                JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.errorBody())));
                                try {

                                    if(responseBody.has("success"))
                                        isSuccess = responseBody.getBoolean("success");
                                    else
                                        isSuccess = false;

                                } catch (JSONException e) {
                                    //throws error, because on success there is no boolean returned, so
                                    // we are assuming that it is a success
                                    isSuccess = false;
                                }

                                if (!isSuccess) {

                                    try {
                                        if (responseBody.has("message")) {
                                            iMyPurchasesService.accessTokenExpired();
                                        }
                                    } catch (Exception e)
                                    {
                                        iMyPurchasesService.myPurchasesServiceError("");
                                    }
                                }
                                return;
                            }
                            if (response != null && response.isSuccessful() && response.body() != null) {
                                JSONArray responseBody = new JSONArray("" + (new Gson().toJson(response.body())));
                                myPurchasesServiceResponse(responseBody, spotLightCategoriesDTOList);

                            } else {
                                //TODO:Error Handling
                                // Toast.makeText(LoginActivity.this, INVALID_RESPONSE_MESSAGE, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            //   Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            iMyPurchasesService.myPurchasesServiceError(e.getMessage());
                        }

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        call.cancel();
                        iMyPurchasesService.myPurchasesServiceError(t.getMessage());
                    }
                });
            } catch (Exception e) {
                iMyPurchasesService.myPurchasesServiceError(e.getMessage());
            }
            /*client.get(MY_PURCHASES_URL, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray responseBody) {
                    myPurchasesServiceResponse(responseBody, spotLightCategoriesDTOList);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject responseBody) {
                    if (responseBody != null) {

                        boolean isSuccess = true;
                        try {
                            isSuccess = responseBody.getBoolean("success");
                        } catch (JSONException e) {
                            //throws error, because on success there is no boolean returned, so
                            // we are assuming that it is a success
                            isSuccess = false;
                        }

                        if (!isSuccess) {
                            if(AccessTokenHandler.getInstance().handleTokenExpiryConditions(responseBody)) {
                                AccessTokenHandler.getInstance().setFlagWhileCalingForToken(AccessTokenHandler.getInstance().fetchTokenCalledInMyPurchasePageString);
                                if(AccessTokenHandler.getInstance().foundAnyError)
                                    iMyPurchasesService.accessTokenExpired();
                                else if(AccessTokenHandler.getInstance().foundAnyErrorForClientToken)
                                    iMyPurchasesService.clientTokenExpired();
                            }
                        }
                    }
                }
            });*/
        } catch (Exception e) {
            iMyPurchasesService.myPurchasesServiceError(e.getMessage());
        }
    }

    public void myPurchasesServiceResponse(JSONArray responseBody, List<SpotLightCategoriesDTO> spotLightCategoriesDTOList) {
        try {
            ArrayList myPurchaseItemDTOList = new ArrayList<MyPurchaseItemDTO>();
            for (int i = 0; i < responseBody.length(); i++) {
                MyPurchaseItemDTO myPurchaseItemDTO = new MyPurchaseItemDTO();
                try {
                    myPurchaseItemDTO.setVideoID(((JSONObject) responseBody.get(i)).getString("video_id"));
                    myPurchaseItemDTO.setTitle(((JSONObject) responseBody.get(i)).getString("product_name"));
                    myPurchaseItemDTO.setVideoPrice(((JSONObject) responseBody.get(i)).getDouble("total_charge"));
                    myPurchaseItemDTO.setVideoPurchaseDate(((JSONObject) responseBody.get(i)).getJSONObject("rental_start").getString("date"));
                    //myPurchaseItemDTO.setStatus(((JSONObject) responseBody.get(i)).getBoolean("video_purchase_status"));

                    String string = ((JSONObject) responseBody.get(i)).getJSONObject("rental_end").getString("date");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date rentalEndDate = format.parse(string);
                    //Date rentalEndDAte = new Date(((JSONObject) responseBody.get(i)).getJSONObject("rental_end").getString("date"));
                    if (rentalEndDate.compareTo(new Date()) == 1) {
                        myPurchaseItemDTO.setStatus(true);
                    }

                    boolean imageFoundFlag = false;
                    String categoryName = ((JSONObject) responseBody.get(i)).getJSONArray("channels").getString(0).split("/")[0];
                    String channelName = ((JSONObject) responseBody.get(i)).getJSONArray("channels").getString(0).split("/")[1];

                    myPurchaseItemDTO.setCategory(categoryName);
                    myPurchaseItemDTO.setChannel(channelName);

                    if (!imageFoundFlag) {
                        for (int j = 0; j < spotLightCategoriesDTOList.size(); j++) {
                            if (spotLightCategoriesDTOList.get(j).getCategorySlug().toLowerCase().equals(categoryName.toLowerCase()) && !imageFoundFlag) {
                                for (int k = 0; k < spotLightCategoriesDTOList.get(j).getSpotLightChannelDTOList().size(); k++) {
                                    if (spotLightCategoriesDTOList.get(j).getSpotLightChannelDTOList().get(k).getSlug().equals(channelName) && !imageFoundFlag) {
                                        if (spotLightCategoriesDTOList.get(j).getSpotLightChannelDTOList().get(k).getPlaylist().size() == 0 &&
                                                spotLightCategoriesDTOList.get(j).getSpotLightChannelDTOList().get(k).getSeasonsList().size() == 0 && !imageFoundFlag) {
                                            if (spotLightCategoriesDTOList.get(j).getSpotLightChannelDTOList().get(k).getVideo().equals(((JSONObject) responseBody.get(i)).getString("video_id")) && !imageFoundFlag) {
                                                String posterDataURL = "";
                                                if(spotLightCategoriesDTOList.get(j).getSpotLightChannelDTOList().get(k).getImage()!=null ||
                                                        !spotLightCategoriesDTOList.get(j).getSpotLightChannelDTOList().get(k).getImage().equals("null"))
                                                    posterDataURL = spotLightCategoriesDTOList.get(j).getSpotLightChannelDTOList().get(k).getImage();
                                                else if(spotLightCategoriesDTOList.get(j).getSpotLightChannelDTOList().get(k).getSpotlightImage()!=null ||
                                                        !spotLightCategoriesDTOList.get(j).getSpotLightChannelDTOList().get(k).getSpotlightImage().equals("null"))
                                                    posterDataURL = spotLightCategoriesDTOList.get(j).getSpotLightChannelDTOList().get(k).getSpotlightImage();
                                                posterDataURL = CommonServiceUtils.replaceDotstudioproWithMyspotlightForImage(posterDataURL);
                                                myPurchaseItemDTO.setImageURL(posterDataURL);
                                                imageFoundFlag = true;
                                            }
                                        }/* else if(spotLightCategoriesDTOList.get(j).getSpotLightChannelDTOList().get(k).getPlaylist().size() > 0 &&
                                                spotLightCategoriesDTOList.get(j).getSpotLightChannelDTOList().get(k).getSeasonsList().size() == 0) {
                                            for(int l = 0; l < spotLightCategoriesDTOList.get(j).getSpotLightChannelDTOList().get(k).getPlaylist().size(); l++) {
                                                if(spotLightCategoriesDTOList.get(j).getSpotLightChannelDTOList().get(k).getPlaylist().get(l).equals(((JSONObject) responseBody.get(i)).getString("video_id")))
                                            }
                                        }*/
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
                }
                myPurchaseItemDTOList.add(myPurchaseItemDTO);

                Collections.sort(myPurchaseItemDTOList, MyPurchaseItemDTO.COMPARE_BY_STATUS);
            }

            iMyPurchasesService.myPurchasesServiceResponse(myPurchaseItemDTOList);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }


    public interface IMyPurchasesService {
        void myPurchasesServiceResponse(List<MyPurchaseItemDTO> myPurchaseItemDTOList);
        void myPurchasesServiceError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }
}
