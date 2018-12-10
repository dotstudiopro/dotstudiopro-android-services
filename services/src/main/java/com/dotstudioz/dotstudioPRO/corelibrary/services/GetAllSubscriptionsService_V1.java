package com.dotstudioz.dotstudioPRO.corelibrary.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.corelibrary.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.CustomFieldDTO;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.DurationDTO;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.SpotLightCategoriesDTO;
import com.dotstudioz.dotstudioPRO.corelibrary.dto.SubscriptionDTO;

import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class GetAllSubscriptionsService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public IGetAllSubscriptionsService iGetAllSubscriptionsService;
    public interface IGetAllSubscriptionsService {
        void getAllSubscriptionsServiceResponse(
                ArrayList<SubscriptionDTO> subscriptionDTOArrayList
        );
        void getAllSubscriptionsError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }

    public GetAllSubscriptionsService_V1(Context ctx) {
        if (ctx instanceof IGetAllSubscriptionsService)
            iGetAllSubscriptionsService = (IGetAllSubscriptionsService) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IGetAllSubscriptionsService");
    }

    public void getAllSubscriptionsService(String xAccessToken, String API_URL) {
        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, null,
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }
    @Override
    public void onResultHandler(JSONObject response) {
        try {

            System.out.println("response==>"+response);
            if (response.has("data"))
                resultProcessingForCategories(response.getJSONArray("data"));
            else
                iGetAllSubscriptionsService.getAllSubscriptionsError("No Subscriptions available");
        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onErrorHandler(String ERROR) {
        iGetAllSubscriptionsService.getAllSubscriptionsError(ERROR);
    }
    @Override
    public void accessTokenExpired() {
        iGetAllSubscriptionsService.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
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
        /*iGetAllSubscriptionsService.getAllSubscriptionsServiceResponse(
                subscriptionDTOArrayList
        );*/
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
