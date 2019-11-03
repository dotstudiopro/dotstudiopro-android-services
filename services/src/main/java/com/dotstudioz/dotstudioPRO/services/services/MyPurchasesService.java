package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.MyPurchaseItemDTO;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightCategoriesDTO;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.accesstoken.ClientTokenRefreshClass;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.services.util.CommonServiceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by mohsin on 08-10-2016.
 */

public class MyPurchasesService /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

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

    List<SpotLightCategoriesDTO> spotLightCategoriesDTOListGeneric;
    String xAccessToken; String xClientToken; String api;
    public void getMyPurchases(String xAccessToken, String xClientToken, String MY_PURCHASES_URL, final List<SpotLightCategoriesDTO> spotLightCategoriesDTOList) {
        this.xAccessToken = xAccessToken;
        this.xClientToken = xClientToken;
        this.api = MY_PURCHASES_URL;
        spotLightCategoriesDTOListGeneric = spotLightCategoriesDTOList;
        try {
            try {
                //adding a front slash, because retro api call fails if it is not there at the end
                if(!MY_PURCHASES_URL.substring(MY_PURCHASES_URL.length()-1, MY_PURCHASES_URL.length()).equalsIgnoreCase("/")) {
                    MY_PURCHASES_URL = MY_PURCHASES_URL + "/";
                }

                ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
                headerItemsArrayList.add(new ParameterItem("x-access-token", ApplicationConstants.xAccessToken));
                headerItemsArrayList.add(new ParameterItem("x-client-token", ApplicationConstants.CLIENT_TOKEN));

                getCommonAsyncHttpClientV1().setCommonAsyncHttpClient_V1Listener(new CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1() {
                    @Override
                    public void onResultHandler(JSONObject response) {
                        onResultHandler1(response);
                    }

                    @Override
                    public void onErrorHandler(String ERROR) {
                        onErrorHandler1(ERROR);
                    }

                    @Override
                    public void accessTokenExpired() {
                        accessTokenExpired1();
                    }

                    @Override
                    public void clientTokenExpired() {
                        clientTokenExpired1();
                    }
                });
                getCommonAsyncHttpClientV1().getAsyncHttpsClient(headerItemsArrayList, null,
                        MY_PURCHASES_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);

                /*RestClientInterface restClientInterface = RestClientManager.getClient(MY_PURCHASES_URL, ApplicationConstants.xAccessToken, ApplicationConstants.CLIENT_TOKEN, null).create(RestClientInterface.class);
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
                                            iMyPurchasesService.accessTokenExpired1();
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
                });*/
            } catch (Exception e) {
                iMyPurchasesService.myPurchasesServiceError(e.getMessage());
            }
        } catch (Exception e) {
            iMyPurchasesService.myPurchasesServiceError(e.getMessage());
        }
    }

    private CommonAsyncHttpClient_V1 commonAsyncHttpClientV1;
    private CommonAsyncHttpClient_V1 getCommonAsyncHttpClientV1() {
        if(commonAsyncHttpClientV1 == null) {
            commonAsyncHttpClientV1 = new CommonAsyncHttpClient_V1();
        }
        return commonAsyncHttpClientV1;
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

    //@Override
    public void onResultHandler1(JSONObject response) {
        try {
            JSONArray responseBody = response.getJSONArray("result");
            myPurchasesServiceResponse(responseBody, spotLightCategoriesDTOListGeneric);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //@Override
    public void onErrorHandler1(String ERROR) {
        iMyPurchasesService.myPurchasesServiceError(ERROR);
    }

    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iMyPurchasesService.accessTokenExpired1();
    }

    //@Override
    public void clientTokenExpired1() {
        if(refreshClientToken)
            refreshClientToken();
        else
            iMyPurchasesService.clientTokenExpired1();
    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    getMyPurchases(ApplicationConstants.xAccessToken, xClientToken, api, spotLightCategoriesDTOListGeneric);
                } catch (Exception e) {
                    e.printStackTrace();
                    iMyPurchasesService.accessTokenExpired1();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iMyPurchasesService.accessTokenExpired1();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }

    boolean refreshClientToken = false;
    private void refreshClientToken() {
        ClientTokenRefreshClass clientTokenRefreshClass = new ClientTokenRefreshClass(context);
        clientTokenRefreshClass.setClientTokenRefreshListener(new ClientTokenRefreshClass.IClientTokenRefresh() {
            @Override
            public void clientTokenResponse(String ACTUAL_RESPONSE) {
                try {
                    String idToken = ACTUAL_RESPONSE;
                    ApplicationConstants.CLIENT_TOKEN = idToken;
                    getMyPurchases(ApplicationConstants.xAccessToken, idToken, api, spotLightCategoriesDTOListGeneric);
                } catch(Exception e) {
                    e.printStackTrace();
                    iMyPurchasesService.clientTokenExpired1();
                }
            }

            @Override
            public void clientTokenError(String ERROR) {
                iMyPurchasesService.clientTokenExpired1();
            }
        });
        clientTokenRefreshClass.refreshExistingClientToken(ApplicationConstants.xAccessToken, ApplicationConstants.CLIENT_TOKEN);
    }


    public interface IMyPurchasesService {
        void myPurchasesServiceResponse(List<MyPurchaseItemDTO> myPurchaseItemDTOList);
        void myPurchasesServiceError(String ERROR);
        void accessTokenExpired1();
        void clientTokenExpired1();
    }
}
