package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SubscriptionDTO;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by mohsin on 08-10-2016.
 */

public class CheckChannelSubscriptionStatusService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public ICheckChannelSubscriptionStatusService iCheckChannelSubscriptionStatusService;
    public interface ICheckChannelSubscriptionStatusService {
        void checkChannelSubscriptionStatusServiceResponse(boolean unlockedFlag, boolean adsEnabledFlag);
        void checkChannelSubscriptionStatusServiceError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }

    Context context;
    public CheckChannelSubscriptionStatusService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof ICheckChannelSubscriptionStatusService)
            iCheckChannelSubscriptionStatusService = (ICheckChannelSubscriptionStatusService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement ICheckChannelSubscriptionStatusService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setCheckChannelSubscriptionStatusServiceListener(ICheckChannelSubscriptionStatusService callback) {
        this.iCheckChannelSubscriptionStatusService = callback;
    }

    public void checkChannelSubscriptionStatusService(String xAccessToken, String xClientToken, String API_URL) {
        if (iCheckChannelSubscriptionStatusService == null) {
            if (context != null && context instanceof CheckChannelSubscriptionStatusService_V1.ICheckChannelSubscriptionStatusService) {
                iCheckChannelSubscriptionStatusService = (CheckChannelSubscriptionStatusService_V1.ICheckChannelSubscriptionStatusService) context;
            }
            if (iCheckChannelSubscriptionStatusService == null) {
                throw new RuntimeException(context.toString()+ " must implement ICheckChannelSubscriptionStatusService or setCheckChannelSubscriptionStatusServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", xAccessToken));

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, null,
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }
    @Override
    public void onResultHandler(JSONObject response) {
        if(response != null)
            Log.d("onResultHandler", "onResultHandler==>"+response.toString());
        try {
            if (response != null)
                resultProcessingForSubscriptions(response);
            else
                iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceResponse(false, true);
        } catch(Exception e) {
            e.printStackTrace();
            iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceResponse(false, true);
        }
    }
    @Override
    public void onErrorHandler(String ERROR) {
        Log.d("onErrorHandler", "onErrorHandler==>"+ERROR);
        iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceError(ERROR);
    }
    @Override
    public void accessTokenExpired() {
        iCheckChannelSubscriptionStatusService.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
        iCheckChannelSubscriptionStatusService.clientTokenExpired();
    }

    private ArrayList<SubscriptionDTO> userSubscriptionDTOArrayList = new ArrayList<SubscriptionDTO>();
    public boolean resultProcessingForSubscriptions(JSONObject response) {
        try {
            if (response != null) {
                if (response.has("unlocked")) {
                    return response.getBoolean("unlocked");
                } else {
                    return false;
                }
            }
        } catch(Exception e) {
            return false;
        }

        return false;
    }

    /**
     * expecting 3 parameters in the response
     * success=true/false
     * unlocked=true/false
     * ads_enabled=true/false
     * @param response boolean flag
     * @param paramName it could be either unlocked or adsenabled
     * @return
     */
    public boolean resultProcessingForSubscriptions(JSONObject response, String paramName) {
        try {
            if (response != null) {
                if (response.has(paramName)) {
                    return response.getBoolean(paramName);
                } else {
                    return false;
                }
            }
        } catch(Exception e) {
            return false;
        }

        return false;
    }

    public boolean resultProcessingForPurchase(JSONObject response) {
         try {
                if (response != null) {
                    // if let bSuccess = infoDict["success"] as? Bool, bSuccess == true {
                    if (response.has("success")) {
                        if(response.getBoolean("success"))
                        {
                            if (response.has("unlocked")) {
                                return response.getBoolean("unlocked");
                            } else {
                                return false;
                            }
                        }else{
                            return false;
                        }
                    }else{
                        return false;
                    }
                }
            } catch(Exception e) {
                return false;
            }

            return false;
    }


    /*private void resultProcessingForSubscriptions(JSONObject response) {
        try {
            if (response != null) {
                if (response.has("unlocked")) {
                    iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceResponse(response.getBoolean("unlocked"));
                }
            }
        } catch(Exception e) {
            iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceResponse(false);
        }
    }*/


    /**
     *
     *
     {
     "success": true,
     "unlocked": false,
     "ads_enabled": false
     }
     */


    /**
     * call to check subscription status of a channel
     * Usage in MainActivity in Revry:-
     * public int noOfChannelSubscriptionStatusCallsMade = 0;
     *     public ArrayList<ChannelSubscriptionStatusDTO> checkChannelSubscriptionStatusCallArrayList = new ArrayList();
     *     public void checkChannelSubscriptionStatusCall() {
     *         if(noOfChannelSubscriptionStatusCallsMade < checkChannelSubscriptionStatusCallArrayList.size()) {
     *             checkChannelSubscriptionStatusService(checkChannelSubscriptionStatusCallArrayList.get(noOfChannelSubscriptionStatusCallsMade).dsproChannelId);
     *
     *             noOfChannelSubscriptionStatusCallsMade++;
     *         } else {
     *             //check if everything is ok, and mark the flags appropriately
     *             //selectedChannelIDUnLocked = true or false;
     *             if(calledFromPostProcessingMissingChannelDataServiceResponse) {
     *                 postProcessingMissingChannelDataServiceResponseFromSubscriptionStatusCheck(fetchMissingChannelSelectedChannelID, fetchMissingChannelSpotLightChannelDTO, fetchMissingChannelSpotLightCategoriesDTO, fetchMissingChannelResponseObject, fetchMissingChannelMissingVideoInfoDTOList);
     *             } else if(calledFromPostProcessingMissingChildChannelDataServiceResponse) {
     *                 postProcessingMissingChildChannelDataServiceResponseFromSubscriptionStatusCheck(fetchMissingChannelSelectedChannelID, fetchMissingChannelSpotLightChannelDTO, fetchMissingChannelSpotLightCategoriesDTO);
     *             }
     *             calledFromPostProcessingMissingChannelDataServiceResponse = false;
     *             calledFromPostProcessingMissingChildChannelDataServiceResponse = false;
     *         }
     *     }
     *     CheckChannelSubscriptionStatusService_V1 checkChannelSubscriptionStatusServiceV1;
     *     public void checkChannelSubscriptionStatusService(String idToPass) {
     *         if(ApplicationConstants.CLIENT_TOKEN != null && ApplicationConstants.CLIENT_TOKEN.length() > 0) {
     *             checkChannelSubscriptionStatusServiceV1 = new CheckChannelSubscriptionStatusService_V1(this);
     *
     *             //check status of the subscription
     *             apiInterface = APIClient.getClient(ApplicationConstants.xAccessToken, ApplicationConstants.CLIENT_TOKEN).create(APIInterface.class);
     *
     *             if(idToPass != null && idToPass.length() > 0) {
     *                 Call<Object> call1 = apiInterface.checkSubscriptions(idToPass);
     *                 checkChannelSubscriptionStatusServiceV1.checkChannelSubscription(call1);
     *             } else {
     *                 checkChannelSubscriptionStatusCallResponse(false);
     *             }
     *         } else {
     *             checkChannelSubscriptionStatusCallResponse(false);
     *         }
     *     }
     *     @Override
     *     public void checkChannelSubscriptionStatusServiceResponse(boolean flag) {
     *         checkChannelSubscriptionStatusCallResponse(flag);
     *     }
     *     @Override
     *     public void checkChannelSubscriptionStatusServiceError(String ERROR) {
     *         if(ERROR != null) {
     *             Log.d("checkChannelSubscriptionStatusServiceError", "ERROR==>"+ERROR);
     *         }
     *         checkChannelSubscriptionStatusCallResponse(false);
     *     }
     *     public void checkChannelSubscriptionStatusCallResponse(boolean flag) {
     *         checkChannelSubscriptionStatusCallArrayList.get(noOfChannelSubscriptionStatusCallsMade).unlocked = flag;
     *         checkChannelSubscriptionStatusCallArrayList.get(noOfChannelSubscriptionStatusCallsMade).assigned = true;
     *         noOfChannelSubscriptionStatusCallsMade++;
     *
     *         checkChannelSubscriptionStatusCall();
     *     }
     * @param call1
     */
    public void checkChannelSubscription(Call call1) {
        call1.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {
                    if(response.body() != null) {
                        Log.d("checkChanSubs", "" + (new Gson().toJson(response.body())));
                        JSONObject obj = new JSONObject("" + (new Gson().toJson(response.body())));
                        try {
                            if (obj.has("success") && obj.getBoolean("success")) {
                                iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceResponse(resultProcessingForSubscriptions(obj, "unlocked"), resultProcessingForSubscriptions(obj, "ads_enabled"));
                            } else {
                                iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceResponse(false, true);
                            }
                        } catch(Exception e) {
                            e.printStackTrace();
                            iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceResponse(false, true);
                        }
                    } else {
                        iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceResponse(false, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceResponse(false, true);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                iCheckChannelSubscriptionStatusService.checkChannelSubscriptionStatusServiceResponse(false, true);
            }
        });
    }
}
