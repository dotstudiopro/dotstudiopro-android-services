package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.models.dto.LiveTickerDataDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 17-01-2016.
 */
public class LiveTickerDataService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public ILiveTickerDataService_V1 iLiveTickerDataService_V1;
    public interface ILiveTickerDataService_V1 {
        void fetchLiveTickerDataServiceResponse(ArrayList<LiveTickerDataDTO> liveTickerDataDTOArrayList);
        void fetchLiveTickerDataServiceError(String ERROR);
    }

    Context context;
    public LiveTickerDataService_V1(Context ctx) {
        context = ctx;
        if (ctx instanceof LiveTickerDataService_V1.ILiveTickerDataService_V1)
            iLiveTickerDataService_V1 = (LiveTickerDataService_V1.ILiveTickerDataService_V1) ctx;
        /*else
            throw new RuntimeException(ctx.toString()+ " must implement ILiveTickerDataService_V1");*/
    }

    // Assign the listener implementing events interface that will receive the events
    public void setLiveTickerDataService_V1Listener(ILiveTickerDataService_V1 callback) {
        this.iLiveTickerDataService_V1 = callback;
    }

    public void fetchLiveTickerData(String API_URL) {
        if (iLiveTickerDataService_V1 == null) {
            if (context != null && context instanceof LiveTickerDataService_V1.ILiveTickerDataService_V1) {
                iLiveTickerDataService_V1 = (LiveTickerDataService_V1.ILiveTickerDataService_V1) context;
            }
            if (iLiveTickerDataService_V1 == null) {
                throw new RuntimeException(context.toString()+ " must implement ILiveTickerDataService_V1 or setLiveTickerDataService_V1Listener");
            }
        }

        System.out.println("Calling fetchLiveTickerData==>"+API_URL);
        /*CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(null, null,
                API_URL, "");*/

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(null, null,
                API_URL, "");

        /*RestClientInterface restClientInterface = RestClientManager.getClient(API_URL, null, null, null).create(RestClientInterface.class);
        Call<Object> call1 = restClientInterface.requestGet(API_URL);
        call1.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, retrofit2.Response<Object> response) {
                try {
                    if (response != null && !response.isSuccessful() && response.errorBody() != null) {
                        return;
                    }
                    if (response != null && response.isSuccessful() && response.body() != null) {
                        JSONObject responseBody = new JSONObject("" + (new Gson().toJson(response.body())));
                        processJSONResponseObject(responseBody);

                    }
                } catch (Exception e) {
                    //   Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    iLiveTickerDataService_V1.fetchLiveTickerDataServiceError(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                call.cancel();
                iLiveTickerDataService_V1.fetchLiveTickerDataServiceError(t.getMessage());

            }
        });*/

    }

    public void processJSONResponseObject(JSONObject response) {
        ArrayList<LiveTickerDataDTO> liveTickerDataDTOArrayList = new ArrayList<>();
        if(response.has("result")) {
            try {
                JSONArray result = response.getJSONArray("result");

                if (result != null && result.length() > 0) {
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            LiveTickerDataDTO liveTickerDataDTO = new LiveTickerDataDTO();
                            liveTickerDataDTO.setId(((JSONObject) result.get(i)).getString("id"));
                            liveTickerDataDTO.setName(((JSONObject) result.get(i)).getString("name"));
                            liveTickerDataDTO.setSymbol(((JSONObject) result.get(i)).getString("symbol"));
                            liveTickerDataDTO.setPrice_usd(((JSONObject) result.get(i)).getString("price_usd"));
                            liveTickerDataDTO.setPercent_change_1h(((JSONObject) result.get(i)).getString("percent_change_1h"));
                            liveTickerDataDTO.setPercent_change_24h(((JSONObject) result.get(i)).getString("percent_change_24h"));

                            try {
                                double value = Double.parseDouble(liveTickerDataDTO.getPercent_change_24h());
                                if (value < 0) {
                                    liveTickerDataDTO.setRed(true);
                                } else {
                                    liveTickerDataDTO.setGreen(true);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            liveTickerDataDTOArrayList.add(liveTickerDataDTO);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    iLiveTickerDataService_V1.fetchLiveTickerDataServiceResponse(liveTickerDataDTOArrayList);
                } else {
                    iLiveTickerDataService_V1.fetchLiveTickerDataServiceResponse(liveTickerDataDTOArrayList);
                }
            } catch(JSONException e) {
                e.printStackTrace();
                iLiveTickerDataService_V1.fetchLiveTickerDataServiceResponse(liveTickerDataDTOArrayList);
            }
        } else {
            iLiveTickerDataService_V1.fetchLiveTickerDataServiceResponse(liveTickerDataDTOArrayList);
        }
    }

    @Override
    public void onResultHandler(JSONObject response) {
        processJSONResponseObject(response);
    }
    @Override
    public void onErrorHandler(String ERROR) {
        iLiveTickerDataService_V1.fetchLiveTickerDataServiceError(ERROR);
    }
    @Override
    public void accessTokenExpired() {
    }
    @Override
    public void clientTokenExpired() {
    }
}