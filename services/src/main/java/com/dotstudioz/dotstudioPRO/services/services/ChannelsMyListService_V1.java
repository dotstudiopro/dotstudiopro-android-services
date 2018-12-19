package com.dotstudioz.dotstudioPRO.services.services;

import android.content.Context;

import com.dotstudioz.dotstudioPRO.services.accesstoken.AccessTokenHandler;
import com.dotstudioz.dotstudioPRO.models.dto.ChannelMyListDTO;
import com.dotstudioz.dotstudioPRO.models.dto.ChannelsMyListDTOForMyList;
import com.dotstudioz.dotstudioPRO.models.dto.ParameterItem;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mohsin on 08-10-2016.
 */

public class ChannelsMyListService_V1 implements CommonAsyncHttpClient_V1.ICommonAsyncHttpClient_V1 {

    public IChannelsMyListService iChannelsMyListService;
    public interface IChannelsMyListService {
        void addChannelToMyListResponse(JSONObject response);
        void deleteChannelFromMyListResponse(JSONObject response);
        void getMyListResponse(ArrayList<ChannelsMyListDTOForMyList> channelsMyListDTOForMyListArrayList, ArrayList<ChannelMyListDTO> channelMyListDTOArrayList);
        void myListError(String ERROR);
        void accessTokenExpired();
        void clientTokenExpired();
    }

    public boolean addingFlag = false;
    public boolean deletingFlag = false;
    public boolean gettingFlag = false;

    public ChannelsMyListService_V1(Context ctx) {
        if (ctx instanceof ChannelsMyListService_V1.IChannelsMyListService)
            iChannelsMyListService = (ChannelsMyListService_V1.IChannelsMyListService) ctx;
        else
            throw new RuntimeException(ctx.toString()+ " must implement IChannelsMyListService");
    }

    public String ADDING_SERVICE_FLAG = "adding";
    public String DELETING_SERVICE_FLAG = "deleting";
    public String GETTING_SERVICE_FLAG = "getting";

    public void setFlagFor(String serviceName) {
        addingFlag = false;
        deletingFlag = false;
        gettingFlag = false;
        if(serviceName.equals(ADDING_SERVICE_FLAG)) {
            addingFlag = true;
        } else if(serviceName.equals(DELETING_SERVICE_FLAG)) {
            deletingFlag = true;
        } else if(serviceName.equals(GETTING_SERVICE_FLAG)) {
            gettingFlag = true;
        }
    }

    public void addChannelToMyList(String channelID, String xAccessToken, String xClientToken, String API_URL) {
        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", xClientToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("channel_id", channelID));

        setFlagFor(ADDING_SERVICE_FLAG);

        CommonAsyncHttpClient_V1.getInstance(this).postAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    public void deleteChannelFromMyList(String channelID, String xAccessToken, String xClientToken, String API_URL) {
        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", xClientToken));

        ArrayList<ParameterItem> requestParamsArrayList = new ArrayList<>();
        requestParamsArrayList.add(new ParameterItem("channel_id", channelID));

        setFlagFor(DELETING_SERVICE_FLAG);
        CommonAsyncHttpClient_V1.getInstance(this).deleteAsyncHttpsClient(headerItemsArrayList, requestParamsArrayList,
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }

    public void getChannelFromMyList(String xAccessToken, String xClientToken, String API_URL) {
        ArrayList<ParameterItem> headerItemsArrayList = new ArrayList<>();
        headerItemsArrayList.add(new ParameterItem("x-access-token", xAccessToken));
        headerItemsArrayList.add(new ParameterItem("x-client-token", xClientToken));

        setFlagFor(GETTING_SERVICE_FLAG);

        System.out.println("getChannelFromMyList==>"+xAccessToken);
        System.out.println("getChannelFromMyList==>"+xClientToken);
        System.out.println("getChannelFromMyList==>"+API_URL);

        CommonAsyncHttpClient_V1.getInstance(this).getAsyncHttpsClient(headerItemsArrayList, null,
                API_URL, AccessTokenHandler.getInstance().fetchTokenCalledInCategoriesPageString);
    }
    @Override
    public void onResultHandler(JSONObject response) {
        try {
            System.out.println("onResultHandler==>"+response);
            if(addingFlag) {
                iChannelsMyListService.addChannelToMyListResponse(response);
            } else if(deletingFlag) {
                iChannelsMyListService.deleteChannelFromMyListResponse(response);
            } else if(gettingFlag) {
                if(response.has("success")) {
                    if(response.getBoolean("success")) {
                        if(response.has("channels")) {
                            ArrayList<ChannelsMyListDTOForMyList> channelsMyListDTOForMyListArrayList = new ArrayList<>();
                            JSONArray channelsArray = response.getJSONArray("channels");

                            //add two channels in a row
                            for(int i = 0; i < channelsArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) channelsArray.get(i);
                                ChannelsMyListDTOForMyList channelsMyListDTOForMyList = new ChannelsMyListDTOForMyList();
                                if(jsonObject.has("_id")) {
                                    channelsMyListDTOForMyList.getChannelMyListDTO1().setId(jsonObject.getString("_id"));
                                }
                                if(jsonObject.has("title")) {
                                    channelsMyListDTOForMyList.getChannelMyListDTO1().setTitle(jsonObject.getString("title"));
                                }
                                if(jsonObject.has("company_id")) {
                                    channelsMyListDTOForMyList.getChannelMyListDTO1().setCompanyId(jsonObject.getString("company_id"));
                                }
                                if(jsonObject.has("poster")) {
                                    channelsMyListDTOForMyList.getChannelMyListDTO1().setPoster(jsonObject.getString("poster"));
                                }
                                if(jsonObject.has("spotlight_poster")) {
                                    channelsMyListDTOForMyList.getChannelMyListDTO1().setSpotlightPoster(jsonObject.getString("spotlight_poster"));
                                }
                                if(jsonObject.has("slug")) {
                                    channelsMyListDTOForMyList.getChannelMyListDTO1().setSlug(jsonObject.getString("slug"));
                                }

                                i++;

                                if(i < channelsArray.length()) {
                                    JSONObject jsonObject2 = (JSONObject) channelsArray.get(i);
                                    if(jsonObject2.has("_id")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO2().setId(jsonObject2.getString("_id"));
                                    }
                                    if(jsonObject2.has("title")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO2().setTitle(jsonObject2.getString("title"));
                                    }
                                    if(jsonObject2.has("company_id")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO2().setCompanyId(jsonObject2.getString("company_id"));
                                    }
                                    if(jsonObject2.has("poster")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO2().setPoster(jsonObject2.getString("poster"));
                                    }
                                    if(jsonObject2.has("spotlight_poster")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO2().setSpotlightPoster(jsonObject2.getString("spotlight_poster"));
                                    }
                                    if(jsonObject2.has("slug")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO2().setSlug(jsonObject2.getString("slug"));
                                    }
                                }

                                i++;

                                if(i < channelsArray.length()) {
                                    JSONObject jsonObject3 = (JSONObject) channelsArray.get(i);
                                    if(jsonObject3.has("_id")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO3().setId(jsonObject3.getString("_id"));
                                    }
                                    if(jsonObject3.has("title")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO3().setTitle(jsonObject3.getString("title"));
                                    }
                                    if(jsonObject3.has("company_id")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO3().setCompanyId(jsonObject3.getString("company_id"));
                                    }
                                    if(jsonObject3.has("poster")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO3().setPoster(jsonObject3.getString("poster"));
                                    }
                                    if(jsonObject3.has("spotlight_poster")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO3().setSpotlightPoster(jsonObject3.getString("spotlight_poster"));
                                    }
                                    if(jsonObject3.has("slug")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO3().setSlug(jsonObject3.getString("slug"));
                                    }
                                }

                                i++;

                                if(i < channelsArray.length()) {
                                    JSONObject jsonObject4 = (JSONObject) channelsArray.get(i);
                                    if(jsonObject4.has("_id")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO4().setId(jsonObject4.getString("_id"));
                                    }
                                    if(jsonObject4.has("title")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO4().setTitle(jsonObject4.getString("title"));
                                    }
                                    if(jsonObject4.has("company_id")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO4().setCompanyId(jsonObject4.getString("company_id"));
                                    }
                                    if(jsonObject4.has("poster")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO4().setPoster(jsonObject4.getString("poster"));
                                    }
                                    if(jsonObject4.has("spotlight_poster")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO4().setSpotlightPoster(jsonObject4.getString("spotlight_poster"));
                                    }
                                    if(jsonObject4.has("slug")) {
                                        channelsMyListDTOForMyList.getChannelMyListDTO4().setSlug(jsonObject4.getString("slug"));
                                    }
                                }
                                channelsMyListDTOForMyListArrayList.add(channelsMyListDTOForMyList);
                            }

                            //add only one channel in a row
                            ArrayList<ChannelMyListDTO> channelMyListDTOArrayList = new ArrayList<>();
                            for(int i = 0; i < channelsArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) channelsArray.get(i);
                                ChannelMyListDTO channelMyListDTO = new ChannelMyListDTO();
                                if(jsonObject.has("_id")) {
                                    channelMyListDTO.setId(jsonObject.getString("_id"));
                                }
                                if(jsonObject.has("title")) {
                                    channelMyListDTO.setTitle(jsonObject.getString("title"));
                                }
                                if(jsonObject.has("company_id")) {
                                    channelMyListDTO.setCompanyId(jsonObject.getString("company_id"));
                                }
                                if(jsonObject.has("poster")) {
                                    channelMyListDTO.setPoster(jsonObject.getString("poster"));
                                }
                                if(jsonObject.has("spotlight_poster")) {
                                    channelMyListDTO.setSpotlightPoster(jsonObject.getString("spotlight_poster"));
                                }
                                if(jsonObject.has("slug")) {
                                    channelMyListDTO.setSlug(jsonObject.getString("slug"));
                                }
                                channelMyListDTOArrayList.add(channelMyListDTO);
                            }

                            iChannelsMyListService.getMyListResponse(channelsMyListDTOForMyListArrayList, channelMyListDTOArrayList);
                        }
                    }
                }
            }
        } catch(Exception e) {
            iChannelsMyListService.myListError("ERROR");
            e.printStackTrace();
        }
    }
    @Override
    public void onErrorHandler(String ERROR) {
        iChannelsMyListService.myListError(ERROR);
    }
    @Override
    public void accessTokenExpired() {
        iChannelsMyListService.accessTokenExpired();
    }
    @Override
    public void clientTokenExpired() {
        iChannelsMyListService.clientTokenExpired();
    }

    private void resultProcessingForCategories(JSONArray response) {

    }
}
