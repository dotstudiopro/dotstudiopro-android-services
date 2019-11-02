package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;
import android.util.Log;

import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;
import com.dotstudioz.dotstudioPRO.models.dto.SubscriptionDTO;
import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class GetUserSubscriptionsService_V1 /*implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1*/ {

    public IGetUserSubscriptionsService iGetUserSubscriptionsService;
    public interface IGetUserSubscriptionsService {
        void getUserSubscriptionsServiceResponse(
                ArrayList<SubscriptionDTO> userSubscriptionDTOArrayList
        );
        void getUserSubscriptionsError(String ERROR);
        void accessTokenExpired1();
        void clientTokenExpired1();
    }

    Context context;
    public GetUserSubscriptionsService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof IGetUserSubscriptionsService)
            iGetUserSubscriptionsService = (IGetUserSubscriptionsService) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement IGetUserSubscriptionsService");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setGetUserSubscriptionsServiceListener(IGetUserSubscriptionsService callback) {
        this.iGetUserSubscriptionsService = callback;
    }

    public void getUserSubscriptionsService(String xAccessToken, String xClientToken, String API_URL) {
        if (iGetUserSubscriptionsService == null) {
            if (context != null && context instanceof GetUserSubscriptionsService_V1.IGetUserSubscriptionsService) {
                iGetUserSubscriptionsService = (GetUserSubscriptionsService_V1.IGetUserSubscriptionsService) context;
            }
            if (iGetUserSubscriptionsService == null) {
                throw new RuntimeException(context.toString() + " must implement IGetUserSubscriptionsService or setGetUserSubscriptionsServiceListener");
            }
        }

        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", xClientToken));

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
            Log.d("GetUserSubscription", "onResultHandler1==>"+response);
            if (response.has("subscriptions"))
                resultProcessingForSubscriptions(response.getJSONArray("subscriptions"));
            else
                iGetUserSubscriptionsService.getUserSubscriptionsError("No Subscriptions available");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    //@Override
    public void onErrorHandler1(String ERROR) {
        Log.d("onErrorHandler1", "onErrorHandler1==>"+ERROR);
        iGetUserSubscriptionsService.getUserSubscriptionsError(ERROR);
    }
    //@Override
    public void accessTokenExpired1() {
        iGetUserSubscriptionsService.accessTokenExpired1();
    }
    //@Override
    public void clientTokenExpired1() {
        iGetUserSubscriptionsService.clientTokenExpired1();
    }

    private ArrayList<SubscriptionDTO> userSubscriptionDTOArrayList = new ArrayList<SubscriptionDTO>();
    /*private void resultProcessingForSubscriptions(JSONArray response) {
        userSubscriptionDTOArrayList = new ArrayList<SubscriptionDTO>();

        // Parsing json response
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject parentObj = response.getJSONObject(i);
                if(parentObj.has("subscription")) {
                    JSONObject childObj = parentObj.getJSONObject("subscription");
                    if(childObj.has("product")) {
                        JSONObject obj = childObj.getJSONObject("product");
                        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
                        try {
                            if (obj.has("id"))
                                subscriptionDTO.setId(obj.getString("id"));
                        } catch (JSONException e) {
                            subscriptionDTO.setId("");
                        }
                        try {
                            if (obj.has("id"))
                                subscriptionDTO.setChargifyId(obj.getString("id"));
                        } catch (JSONException e) {
                            subscriptionDTO.setChargifyId("");
                        }
                        try {
                            if (obj.has("entire_catalogue"))
                                subscriptionDTO.setEntireCatalogue(obj.getBoolean("entire_catalogue"));
                        } catch (JSONException e) {
                            subscriptionDTO.setEntireCatalogue(false);
                        }
                        try {
                            if (obj.has("name"))
                                subscriptionDTO.setName(obj.getString("name"));
                        } catch (JSONException e) {
                            subscriptionDTO.setName("");
                        }
                        if (obj.has("interval")) {
                            subscriptionDTO.setInterval(obj.getInt("interval"));
                        }
                        if (obj.has("interval_unit")) {
                            subscriptionDTO.setIntervalUnit(obj.getString("interval_unit"));
                        }
                        if (obj.has("price_in_cents")) {
                            subscriptionDTO.setPrice(""+obj.getInt("price_in_cents"));
                        }

                        userSubscriptionDTOArrayList.add(subscriptionDTO);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        iGetUserSubscriptionsService.getUserSubscriptionsServiceResponse(
                userSubscriptionDTOArrayList
        );
    }*/

    public SubscriptionDTO resultProcessingForSubscription(JSONObject response) {
        // Parsing json response

        try {
            SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
            JSONObject childObj = response;

            try {
                if (childObj.has("platform")) {
                    subscriptionDTO.setPlatform(childObj.getString("platform"));
                }
            }catch (JSONException e)
            {
                subscriptionDTO.setPlatform("");
            }

            try {
                if(childObj.has("delayed_cancel_at"))
                    subscriptionDTO.setDelayed_cancel_at(childObj.getString("delayed_cancel_at"));
            } catch (JSONException e) {
                subscriptionDTO.setDelayed_cancel_at("");
            }

            try {
                if(childObj.has("cancel_at_end_of_period"))
                    subscriptionDTO.setCancel_at_end_of_period(childObj.getBoolean("cancel_at_end_of_period"));
            } catch (JSONException e) {
                subscriptionDTO.setCancel_at_end_of_period(false);
            }
            try {
                if(childObj.has("current_period_ends_at"))
                    subscriptionDTO.setCurrent_period_ends_at(childObj.getString("current_period_ends_at"));
            } catch (JSONException e) {
                subscriptionDTO.setCurrent_period_ends_at("");
            }
            if (childObj.has("total_revenue_in_cents"))
            {
                try {
                    Double total_revenue = childObj.getDouble("total_revenue_in_cents");
                    subscriptionDTO.setTotal_revenue(total_revenue);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            if(childObj.has("product")) {
                JSONObject obj = childObj.getJSONObject("product");

                try {
                    if (obj.has("id"))
                        subscriptionDTO.setId(obj.getString("id"));
                } catch (JSONException e) {
                    subscriptionDTO.setId("");
                }
                try {
                    if (obj.has("id"))
                        subscriptionDTO.setChargifyId(obj.getString("id"));
                } catch (JSONException e) {
                    subscriptionDTO.setChargifyId("");
                }
                try {
                    if (obj.has("entire_catalogue"))
                        subscriptionDTO.setEntireCatalogue(obj.getBoolean("entire_catalogue"));
                } catch (JSONException e) {
                    subscriptionDTO.setEntireCatalogue(false);
                }

                try {
                    if (obj.has("name"))
                        subscriptionDTO.setName(obj.getString("name"));
                } catch (JSONException e) {
                    subscriptionDTO.setName("");
                }
                if (obj.has("interval")) {
                    subscriptionDTO.setInterval(obj.getInt("interval"));
                }
                if (obj.has("handle")) {
                    subscriptionDTO.setHandle(obj.getString("handle"));
                }
                if (obj.has("interval_unit")) {
                    subscriptionDTO.setIntervalUnit(obj.getString("interval_unit"));
                }
                if (obj.has("price_in_cents")) {
                    subscriptionDTO.setPrice(""+obj.getInt("price_in_cents"));
                }

                return subscriptionDTO;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new SubscriptionDTO();

        /*iGetUserSubscriptionsService.getUserSubscriptionsServiceResponse(
                userSubscriptionDTOArrayList
        );*/
    }
    public ArrayList<SubscriptionDTO> resultProcessingForSubscriptions(JSONArray response) {
        userSubscriptionDTOArrayList = new ArrayList<SubscriptionDTO>();

        // Parsing json response
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject parentObj = response.getJSONObject(i);
                SubscriptionDTO subscriptionDTO = new SubscriptionDTO();

                if(parentObj.has("subscription")) {

                    JSONObject childObj = parentObj.getJSONObject("subscription");

                    try {
                        if(childObj.has("cancel_at_end_of_period"))
                            subscriptionDTO.setCancel_at_end_of_period(childObj.getBoolean("cancel_at_end_of_period"));
                    } catch (JSONException e) {
                        subscriptionDTO.setCancel_at_end_of_period(false);
                    }
                    try {
                        if (childObj.has("platform")) {
                            subscriptionDTO.setPlatform(childObj.getString("platform"));
                        }
                    }catch (JSONException e)
                    {
                        subscriptionDTO.setPlatform("");
                    }

                    try {
                        if(childObj.has("delayed_cancel_at"))
                            subscriptionDTO.setDelayed_cancel_at(childObj.getString("delayed_cancel_at"));
                    } catch (JSONException e) {
                        subscriptionDTO.setDelayed_cancel_at("");
                    }

                    try {
                        if(childObj.has("current_period_ends_at"))
                            subscriptionDTO.setCurrent_period_ends_at(childObj.getString("current_period_ends_at"));
                    } catch (JSONException e) {
                        subscriptionDTO.setCurrent_period_ends_at("");
                    }
                    if (childObj.has("total_revenue_in_cents"))
                    {
                        Double total_revenue = childObj.getDouble("total_revenue_in_cents");
                        subscriptionDTO.setTotal_revenue(total_revenue);
                    }

                    if(childObj.has("product")) {
                        JSONObject obj = childObj.getJSONObject("product");

                        try {
                            if (obj.has("id"))
                                subscriptionDTO.setId(obj.getString("id"));
                        } catch (JSONException e) {
                            subscriptionDTO.setId("");
                        }
                        try {
                            if (obj.has("id"))
                                subscriptionDTO.setChargifyId(obj.getString("id"));
                        } catch (JSONException e) {
                            subscriptionDTO.setChargifyId("");
                        }
                        try {
                            if (obj.has("entire_catalogue"))
                                subscriptionDTO.setEntireCatalogue(obj.getBoolean("entire_catalogue"));
                        } catch (JSONException e) {
                            subscriptionDTO.setEntireCatalogue(false);
                        }

                        try {
                            if (obj.has("name"))
                                subscriptionDTO.setName(obj.getString("name"));
                        } catch (JSONException e) {
                            subscriptionDTO.setName("");
                        }
                        if (obj.has("interval")) {
                            subscriptionDTO.setInterval(obj.getInt("interval"));
                        }
                        if (obj.has("handle")) {
                            subscriptionDTO.setHandle(obj.getString("handle"));
                        }
                        if (obj.has("interval_unit")) {
                            subscriptionDTO.setIntervalUnit(obj.getString("interval_unit"));
                        }
                        if (obj.has("price_in_cents")) {
                            subscriptionDTO.setPrice(""+obj.getInt("price_in_cents"));
                        }

                        userSubscriptionDTOArrayList.add(subscriptionDTO);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        iGetUserSubscriptionsService.getUserSubscriptionsServiceResponse(
                userSubscriptionDTOArrayList
        );

        return userSubscriptionDTOArrayList;
    }

    public ArrayList<SubscriptionDTO> resultProcessingForSubscriptions(JSONObject childObj) {
        userSubscriptionDTOArrayList = new ArrayList<SubscriptionDTO>();
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();


        try {
            try {
                if(childObj.has("cancel_at_end_of_period"))
                    subscriptionDTO.setCancel_at_end_of_period(childObj.getBoolean("cancel_at_end_of_period"));
            } catch (JSONException e) {
                subscriptionDTO.setCancel_at_end_of_period(false);
            }
            try {
                if (childObj.has("platform")) {
                    subscriptionDTO.setPlatform(childObj.getString("platform"));
                }
            }catch (JSONException e)
            {
                subscriptionDTO.setPlatform("");
            }

            try {
                if(childObj.has("delayed_cancel_at"))
                    subscriptionDTO.setDelayed_cancel_at(childObj.getString("delayed_cancel_at"));
            } catch (JSONException e) {
                subscriptionDTO.setDelayed_cancel_at("");
            }

            try {
                if(childObj.has("current_period_ends_at"))
                    subscriptionDTO.setCurrent_period_ends_at(childObj.getString("current_period_ends_at"));
            } catch (JSONException e) {
                subscriptionDTO.setCurrent_period_ends_at("");
            }
            if (childObj.has("total_revenue_in_cents"))
            {
                Double total_revenue = childObj.getDouble("total_revenue_in_cents");
                subscriptionDTO.setTotal_revenue(total_revenue);
            }


            if (childObj.has("product")) {
                JSONObject obj = childObj.getJSONObject("product");

                try {
                    if (obj.has("id"))
                        subscriptionDTO.setId(obj.getString("id"));
                } catch (JSONException e) {
                    subscriptionDTO.setId("");
                }
                try {
                    if (obj.has("id"))
                        subscriptionDTO.setChargifyId(obj.getString("id"));
                } catch (JSONException e) {
                    subscriptionDTO.setChargifyId("");
                }
                try {
                    if (obj.has("entire_catalogue"))
                        subscriptionDTO.setEntireCatalogue(obj.getBoolean("entire_catalogue"));
                } catch (JSONException e) {
                    subscriptionDTO.setEntireCatalogue(false);
                }

                try {
                    if (obj.has("name"))
                        subscriptionDTO.setName(obj.getString("name"));
                } catch (JSONException e) {
                    subscriptionDTO.setName("");
                }
                if (obj.has("interval")) {
                    subscriptionDTO.setInterval(obj.getInt("interval"));
                }
                if (obj.has("handle")) {
                    subscriptionDTO.setHandle(obj.getString("handle"));
                }
                if (obj.has("interval_unit")) {
                    subscriptionDTO.setIntervalUnit(obj.getString("interval_unit"));
                }
                if (obj.has("price_in_cents")) {
                    subscriptionDTO.setPrice("" + obj.getInt("price_in_cents"));
                }

                userSubscriptionDTOArrayList.add(subscriptionDTO);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return userSubscriptionDTOArrayList;
    }


    /**
     * {
     "success": true,
     "subscriptions": [
     {
     "subscription": {
     "id": 22006691,
     "state": "trialing",
     "balance_in_cents": 0,
     "total_revenue_in_cents": 0,
     "product_price_in_cents": 699,
     "product_version_number": 1,
     "current_period_ends_at": "2018-06-18T14:53:19-04:00",
     "next_assessment_at": "2018-06-18T14:53:19-04:00",
     "trial_started_at": "2018-05-18T14:53:19-04:00",
     "trial_ended_at": "2018-06-18T14:53:19-04:00",
     "activated_at": null,
     "expires_at": null,
     "created_at": "2018-05-18T14:53:19-04:00",
     "updated_at": "2018-05-18T14:53:20-04:00",
     "cancellation_message": null,
     "cancellation_method": null,
     "cancel_at_end_of_period": false,
     "canceled_at": null,
     "current_period_started_at": "2018-05-18T14:53:19-04:00",
     "previous_state": "trialing",
     "signup_payment_id": 241825431,
     "signup_revenue": "0.00",
     "delayed_cancel_at": null,
     "coupon_code": null,
     "coupon_codes": [],
     "payment_collection_method": "automatic",
     "snap_day": null,
     "customer": {
     "first_name": "Mohsin",
     "last_name": "Shaikh",
     "email": "shaikhmohsink@gmail.com",
     "cc_emails": null,
     "organization": null,
     "reference": "542c4f5497f8157c477b23c6",
     "id": 21686881,
     "created_at": "2018-05-18T14:53:19-04:00",
     "updated_at": "2018-05-18T14:53:19-04:00",
     "address": null,
     "address_2": null,
     "city": null,
     "state": null,
     "zip": null,
     "country": null,
     "phone": null,
     "verified": false,
     "portal_customer_created_at": null,
     "portal_invite_last_sent_at": null,
     "portal_invite_last_accepted_at": null,
     "tax_exempt": false
     },
     "product": {
     "id": 4654324,
     "name": "Revry Monthly",
     "handle": "revry_monthly",
     "description": null,
     "accounting_code": null,
     "price_in_cents": 699,
     "interval": 1,
     "interval_unit": "month",
     "initial_charge_in_cents": null,
     "expiration_interval": null,
     "expiration_interval_unit": "never",
     "trial_price_in_cents": 0,
     "trial_interval": 1,
     "trial_interval_unit": "month",
     "initial_charge_after_trial": false,
     "return_params": null,
     "request_credit_card": true,
     "require_credit_card": false,
     "created_at": "2018-05-10T00:56:07-04:00",
     "updated_at": "2018-05-10T00:56:07-04:00",
     "archived_at": null,
     "update_return_url": null,
     "tax_code": null,
     "update_return_params": null,
     "product_family": {
     "id": 1089216,
     "name": "Revry Subscriptions",
     "handle": "revry",
     "accounting_code": null,
     "description": ""
     },
     "public_signup_pages": [
     {
     "id": 351627,
     "url": "https://revry.chargifypay.com/subscribe/76zzwhrsp39m/revry_monthly"
     }
     ],
     "taxable": true,
     "version_number": 1
     },
     "credit_card": {
     "id": 15158828,
     "payment_type": "credit_card",
     "first_name": "Mohsin",
     "last_name": "Shaikh",
     "masked_card_number": "XXXX-XXXX-XXXX-1881",
     "card_type": "visa",
     "expiration_month": 3,
     "expiration_year": 2023,
     "billing_address": null,
     "billing_address_2": null,
     "billing_city": null,
     "billing_state": null,
     "billing_country": null,
     "billing_zip": null,
     "current_vault": "braintree_blue",
     "vault_token": "257325439",
     "customer_vault_token": null,
     "customer_id": 21686881
     },
     "payment_type": "credit_card",
     "referral_code": null,
     "next_product_id": null,
     "coupon_use_count": null,
     "coupon_uses_allowed": null,
     "reason_code": null,
     "automatically_resume_at": null
     }
     }
     ]
     }
     */
}
