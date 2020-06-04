package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.models.dto.DurationDTO;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SubscriptionDTO;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class GetAllSubscriptionsService_V1 /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public IGetAllSubscriptionsService iGetAllSubscriptionsService;
    public interface IGetAllSubscriptionsService {
        void getAllSubscriptionsServiceResponse(
                ArrayList<SubscriptionDTO> subscriptionDTOArrayList
        );
        void getAllSubscriptionsError(String ERROR);
        void accessTokenExpired();
        void accessTokenRefreshed(String accessToken);
        void clientTokenExpired();
    }

    Context context;
    public GetAllSubscriptionsService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof IGetAllSubscriptionsService)
            iGetAllSubscriptionsService = (IGetAllSubscriptionsService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IGetAllSubscriptionsService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setGetAllSubscriptionsServiceListener(IGetAllSubscriptionsService callback) {
        this.iGetAllSubscriptionsService = callback;
    }

    private String xAccessToken;
    private String api;
    public void getAllSubscriptionsService(String xAccessToken, String API_URL) {
        this.xAccessToken = xAccessToken;
        this.api = API_URL;
        if (iGetAllSubscriptionsService == null) {
            if (context != null && context instanceof GetAllSubscriptionsService_V1.IGetAllSubscriptionsService) {
                iGetAllSubscriptionsService = (GetAllSubscriptionsService_V1.IGetAllSubscriptionsService) context;
            }
            if (iGetAllSubscriptionsService == null) {
                throw new RuntimeException(context.toString() + " must implement IGetAllSubscriptionsService or setGetAllSubscriptionsServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));

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
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    private CommonAsyncHttpClient_V1 commonAsyncHttpClientV1;
    private CommonAsyncHttpClient_V1 getCommonAsyncHttpClientV1() {
        if(commonAsyncHttpClientV1 == null) {
            commonAsyncHttpClientV1 = new CommonAsyncHttpClient_V1();
        }
        return commonAsyncHttpClientV1;
    }

    //@Override
    public void onResultHandler1(JSONObject response) {
        try {

            Log.d("GetAllSubscrip", "response==>"+response);
            if (response.has("data"))
                resultProcessingForCategories(response.getJSONArray("data"));
            else
                iGetAllSubscriptionsService.getAllSubscriptionsError("No Subscriptions available");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
    //@Override
    public void onErrorHandler1(String ERROR) {
        iGetAllSubscriptionsService.getAllSubscriptionsError(ERROR);
    }
    //@Override
    public void accessTokenExpired1() {
        if(!refreshAccessToken)
            refreshAccessToken();
        else
            iGetAllSubscriptionsService.accessTokenExpired();
    }
    //@Override
    public void clientTokenExpired1() {
        iGetAllSubscriptionsService.clientTokenExpired();
    }

    private ArrayList<SubscriptionDTO> subscriptionDTOArrayList = new ArrayList<SubscriptionDTO>();
    /*private void resultProcessingForCategories(JSONArray response) {
        subscriptionDTOArrayList = new ArrayList<SubscriptionDTO>();

        // Parsing json response
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject obj = response.getJSONObject(i);
                SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
                try {
                    if(obj.has("_id"))
                        subscriptionDTO.setId(obj.getString("_id"));
                } catch (JSONException e) {
                    subscriptionDTO.setId("");
                }
                try {
                    if(obj.has("company_id"))
                        subscriptionDTO.setCompanyId(obj.getString("company_id"));
                } catch (JSONException e) {
                    subscriptionDTO.setCompanyId("");
                }
                try {
                    if(obj.has("chargify_id"))
                        subscriptionDTO.setChargifyId(obj.getString("chargify_id"));
                } catch (JSONException e) {
                    subscriptionDTO.setChargifyId("");
                }
                try {
                    if(obj.has("entire_catalogue"))
                        subscriptionDTO.setEntireCatalogue(obj.getBoolean("entire_catalogue"));
                } catch (JSONException e) {
                    subscriptionDTO.setEntireCatalogue(false);
                }
                try {
                    if(obj.has("ads_enabled"))
                        subscriptionDTO.setAdsEnabled(obj.getBoolean("ads_enabled"));
                } catch (JSONException e) {
                    subscriptionDTO.setAdsEnabled(false);
                }
                try {
                    if(obj.has("status"))
                        subscriptionDTO.setStatus(obj.getString("status"));
                } catch (JSONException e) {
                    subscriptionDTO.setStatus("");
                }
                try {
                    if(obj.has("channels")) {
                        for(int j = 0; j < obj.getJSONArray("channels").length(); j++) {
                            subscriptionDTO.getChannels().add(""+obj.getJSONArray("channels").getString(j));
                        }
                    }
                } catch (JSONException e) {
                    subscriptionDTO.setTrial("");
                }
                try {
                    if(obj.has("duration")) {
                        DurationDTO durationDTO = new DurationDTO();
                        if(obj.getJSONObject("duration").has("interval")) {
                            subscriptionDTO.setInterval(obj.getJSONObject("duration").getInt("interval"));
                            durationDTO.setInterval(obj.getJSONObject("duration").getInt("interval"));
                        }
                        if(obj.getJSONObject("duration").has("interval_unit")) {
                            subscriptionDTO.setIntervalUnit(obj.getJSONObject("duration").getString("interval_unit"));
                            durationDTO.setIntervalUnit(obj.getJSONObject("duration").getString("interval_unit"));
                        }
                        subscriptionDTO.setDurationDTO(durationDTO);
                    }
                } catch (JSONException e) {
                    subscriptionDTO.setTrial("");
                }
                try {
                    if(obj.has("price"))
                        subscriptionDTO.setPrice(obj.getString("price"));
                } catch (JSONException e) {
                    subscriptionDTO.setPrice("");
                }
                try {
                    if(obj.has("name"))
                        subscriptionDTO.setName(obj.getString("name"));
                } catch (JSONException e) {
                    subscriptionDTO.setName("");
                }
                try {
                    if(obj.has("trial"))
                        subscriptionDTO.setTrial(obj.getString("trial"));
                } catch (JSONException e) {
                    subscriptionDTO.setTrial("");
                }

                subscriptionDTOArrayList.add(subscriptionDTO);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        iGetAllSubscriptionsService.getAllSubscriptionsServiceResponse(
                subscriptionDTOArrayList
        );
    }*/

    public ArrayList resultProcessingForCategories(JSONArray response) {
        subscriptionDTOArrayList = new ArrayList<SubscriptionDTO>();

        // Parsing json response
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject obj = response.getJSONObject(i);
                SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
                try {
                    if(obj.has("cancel_at_end_of_period"))
                        subscriptionDTO.setCancel_at_end_of_period(obj.getBoolean("cancel_at_end_of_period"));
                } catch (JSONException e) {
                    subscriptionDTO.setCancel_at_end_of_period(false);
                }
                try {
                    if(obj.has("current_period_ends_at"))
                        subscriptionDTO.setCurrent_period_ends_at(obj.getString("current_period_ends_at"));
                } catch (JSONException e) {
                    subscriptionDTO.setCurrent_period_ends_at("");
                }
                try {
                    if(obj.has("_id"))
                        subscriptionDTO.setId(obj.getString("_id"));
                } catch (JSONException e) {
                    subscriptionDTO.setId("");
                }
                try {
                    if(obj.has("company_id"))
                        subscriptionDTO.setCompanyId(obj.getString("company_id"));
                } catch (JSONException e) {
                    subscriptionDTO.setCompanyId("");
                }
                try {
                    if(obj.has("chargify_id"))
                        subscriptionDTO.setChargifyId(obj.getString("chargify_id"));
                } catch (JSONException e) {
                    subscriptionDTO.setChargifyId("");
                }
                try {
                    if(obj.has("apple_product_id"))
                        subscriptionDTO.setAppleProductId(obj.getString("apple_product_id"));
                } catch (JSONException e) {
                    subscriptionDTO.setAppleProductId("");
                }
                try {
                    if(obj.has("google_product_id"))
                        subscriptionDTO.setGoogleProductId(obj.getString("google_product_id"));
                } catch (JSONException e) {
                    subscriptionDTO.setGoogleProductId("");
                }
                try {
                    if(obj.has("roku_product_id"))
                        subscriptionDTO.setRokuProductId(obj.getString("roku_product_id"));
                } catch (JSONException e) {
                    subscriptionDTO.setRokuProductId("");
                }
                try {
                    if(obj.has("entire_catalogue"))
                        subscriptionDTO.setEntireCatalogue(obj.getBoolean("entire_catalogue"));
                } catch (JSONException e) {
                    subscriptionDTO.setEntireCatalogue(false);
                }
                try {
                    if(obj.has("cancel_at_end_of_period"))
                        subscriptionDTO.setCancel_at_end_of_period(obj.getBoolean("cancel_at_end_of_period"));
                } catch (JSONException e) {
                    subscriptionDTO.setCancel_at_end_of_period(false);
                }
                try {
                    if(obj.has("ads_enabled"))
                        subscriptionDTO.setAdsEnabled(obj.getBoolean("ads_enabled"));
                } catch (JSONException e) {
                    subscriptionDTO.setAdsEnabled(false);
                }
                try {
                    if(obj.has("current_period_ends_at"))
                        subscriptionDTO.setCurrent_period_ends_at(obj.getString("current_period_ends_at"));
                } catch (JSONException e) {
                    subscriptionDTO.setCurrent_period_ends_at("");
                }
                try {
                    if(obj.has("status"))
                        subscriptionDTO.setStatus(obj.getString("status"));
                } catch (JSONException e) {
                    subscriptionDTO.setStatus("");
                }
                try {
                    if(obj.has("channels")) {
                        for(int j = 0; j < obj.getJSONArray("channels").length(); j++) {
                            subscriptionDTO.getChannels().add(""+obj.getJSONArray("channels").getString(j));
                        }
                    }
                } catch (JSONException e) {
                    subscriptionDTO.setTrial("");
                }
                try {
                    if(obj.has("duration")) {
                        DurationDTO durationDTO = new DurationDTO();
                        if(obj.getJSONObject("duration").has("interval")) {
                            subscriptionDTO.setInterval(obj.getJSONObject("duration").getInt("interval"));
                            durationDTO.setInterval(obj.getJSONObject("duration").getInt("interval"));
                        }
                        if(obj.getJSONObject("duration").has("interval_unit")) {
                            subscriptionDTO.setIntervalUnit(obj.getJSONObject("duration").getString("interval_unit"));
                            durationDTO.setIntervalUnit(obj.getJSONObject("duration").getString("interval_unit"));
                        }
                        subscriptionDTO.setDurationDTO(durationDTO);
                    }
                } catch (JSONException e) {
                    subscriptionDTO.setTrial("");
                }
                try {
                    if(obj.has("price"))
                        subscriptionDTO.setPrice(obj.getString("price"));
                } catch (JSONException e) {
                    subscriptionDTO.setPrice("");
                }
                try {
                    if(obj.has("name"))
                        subscriptionDTO.setName(obj.getString("name"));
                } catch (JSONException e) {
                    subscriptionDTO.setName("");
                }
                try {
                    if(obj.has("description"))
                        subscriptionDTO.setDescription(obj.getString("description"));
                } catch (JSONException e) {
                    subscriptionDTO.setDescription("");
                }
                try {
                    if(obj.has("price_display"))
                        subscriptionDTO.setPriceDisplay(obj.getString("price_display"));
                } catch (JSONException e) {
                    subscriptionDTO.setPriceDisplay("");
                }
                try {
                    if(obj.has("is_most_popular"))
                        subscriptionDTO.setMostPopular(obj.getBoolean("is_most_popular"));
                } catch (JSONException e) {
                    subscriptionDTO.setMostPopular(false);
                }
                try {
                    if(obj.has("trial"))
                        subscriptionDTO.setTrial(obj.getString("trial"));
                } catch (JSONException e) {
                    subscriptionDTO.setTrial("");
                }

                subscriptionDTOArrayList.add(subscriptionDTO);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        iGetAllSubscriptionsService.getAllSubscriptionsServiceResponse(
                subscriptionDTOArrayList
        );

        return subscriptionDTOArrayList;
    }

    boolean refreshAccessToken = false;
    private void refreshAccessToken() {
        CompanyTokenService companyTokenService = new CompanyTokenService(context);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                    iGetAllSubscriptionsService.accessTokenRefreshed(ApplicationConstants.xAccessToken);
                    getAllSubscriptionsService(ApplicationConstants.xAccessToken, api);
                } catch (Exception e) {
                    e.printStackTrace();
                    iGetAllSubscriptionsService.accessTokenExpired();
                }
            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                iGetAllSubscriptionsService.accessTokenExpired();
            }
        });
        refreshAccessToken = true;
        companyTokenService.requestForToken(ApplicationConstants.COMPANY_KEY, ApplicationConstantURL.TOKEN_URL);
    }

    /*public ArrayList resultProcessingForCategories(ArrayList response) {
        subscriptionDTOArrayList = new ArrayList<SubscriptionDTO>();

        // Parsing json response
        for (int i = 0; i < response.size(); i++) {
            try {
                LinkedTreeMap obj = (LinkedTreeMap) response.get(i);
                SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
                try {
                    if(obj.containsKey("_id"))
                        subscriptionDTO.setId(""+obj.get("_id"));
                } catch (Exception e) {
                    subscriptionDTO.setId("");
                }
                try {
                    if(obj.containsKey("company_id"))
                        subscriptionDTO.setCompanyId(""+obj.get("company_id"));
                } catch (Exception e) {
                    subscriptionDTO.setCompanyId("");
                }
                try {
                    if(obj.containsKey("chargify_id"))
                        subscriptionDTO.setChargifyId(""+obj.get("chargify_id"));
                } catch (Exception e) {
                    subscriptionDTO.setChargifyId("");
                }
                try {
                    if(obj.containsKey("entire_catalogue"))
                        subscriptionDTO.setEntireCatalogue(Boolean.getBoolean(""+obj.get("entire_catalogue")));
                } catch (Exception e) {
                    subscriptionDTO.setEntireCatalogue(false);
                }
                try {
                    if(obj.containsKey("ads_enabled"))
                        subscriptionDTO.setAdsEnabled(Boolean.getBoolean(""+obj.get("ads_enabled")));
                } catch (Exception e) {
                    subscriptionDTO.setAdsEnabled(false);
                }
                try {
                    if(obj.containsKey("status"))
                        subscriptionDTO.setStatus(""+obj.get("status"));
                } catch (Exception e) {
                    subscriptionDTO.setStatus("");
                }
                *//*try {
                    if(obj.containsKey("channels")) {
                        for(int j = 0; j < obj.("channels").length(); j++) {
                            subscriptionDTO.getChannels().add(""+obj.getJSONArray("channels").getInt(j));
                        }
                    }
                } catch (JSONException e) {
                    subscriptionDTO.setTrial("");
                }*//*
                try {
                    if(obj.containsKey("duration")) {
                        ArrayList durationArrayList = (ArrayList) obj.get("duration");
                        for(int j = 0; j < durationArrayList.size(); j++) {

                        }
                        subscriptionDTO.setInterval(duratio.getInt("interval"));
                        subscriptionDTO.setIntervalUnit(obj.getJSONObject("duration").getString("interval_unit"));
                    }
                } catch (JSONException e) {
                    subscriptionDTO.setTrial("");
                }
                *//*try {
                    if(obj.has("duration")) {
                        DurationDTO durationDTO = new DurationDTO();
                        if(obj.getJSONObject("duration").has("interval")) {
                            subscriptionDTO.setInterval(obj.getJSONObject("duration").getInt("interval"));
                            durationDTO.setInterval(obj.getJSONObject("duration").getInt("interval"));
                        }
                        if(obj.getJSONObject("duration").has("interval_unit")) {
                            subscriptionDTO.setIntervalUnit(obj.getJSONObject("duration").getString("interval_unit"));
                            durationDTO.setIntervalUnit(obj.getJSONObject("duration").getString("interval_unit"));
                        }
                        subscriptionDTO.setDurationDTO(durationDTO);
                    }
                } catch (JSONException e) {
                    subscriptionDTO.setTrial("");
                }*//*
                try {
                    if(obj.has("price"))
                        subscriptionDTO.setPrice(obj.getString("price"));
                } catch (JSONException e) {
                    subscriptionDTO.setPrice("");
                }
                try {
                    if(obj.has("name"))
                        subscriptionDTO.setName(obj.getString("name"));
                } catch (JSONException e) {
                    subscriptionDTO.setName("");
                }
                try {
                    if(obj.has("trial"))
                        subscriptionDTO.setTrial(obj.getString("trial"));
                } catch (JSONException e) {
                    subscriptionDTO.setTrial("");
                }

                subscriptionDTOArrayList.add(subscriptionDTO);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return subscriptionDTOArrayList;
        *//*iGetAllSubscriptionsService.getAllSubscriptionsServiceResponse(
                subscriptionDTOArrayList
        );*//*
    }*/
}
