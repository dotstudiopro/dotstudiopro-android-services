package com.dotstudioz.dotstudioPRO.services;

import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dotstudioz.dotstudioPRO.models.dto.SpotLightCategoriesDTO;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightCategoriesDTOForCategories;
import com.dotstudioz.dotstudioPRO.models.dto.SpotLightCategoriesDTOForCollection;
import com.dotstudioz.dotstudioPRO.models.dto.SubscriptionDTO;
import com.dotstudioz.dotstudioPRO.models.dto.TokenResponseDTO;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstantURL;
import com.dotstudioz.dotstudioPRO.services.constants.ApplicationConstants;
import com.dotstudioz.dotstudioPRO.services.services.CompanyTokenService;
import com.dotstudioz.dotstudioPRO.services.services.GetAllCategoriesServiceForHomepage_V1;
import com.dotstudioz.dotstudioPRO.services.services.GetAllCategoriesService_V1;
import com.dotstudioz.dotstudioPRO.services.services.GetAllSubscriptionsService_V1;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initConfiguration();

        getToken();
    }
    public void initConfiguration() {
        //PRODUCTION_URL
        ApplicationConstantURL.API_DOMAIN = "http://api.myspotlight.tv"; //PRODUCTION SERVER
        //public  String API_DOMAIN = "http://f960b3e8.ngrok.io"; //PRODUCTION SERVER
        ApplicationConstantURL.API_DOMAIN_S = "https://api.myspotlight.tv"; //PRODUCTION SERVER
        // ApplicationConstantURL.getInstance().API_DOMAIN = "http://c69ffc89.ngrok.io/"; // This was Erics machine
        // ApplicationConstantURL.getInstance().API_DOMAIN = "http://dev.api.myspotlight.tv"; // DEVELOPMENT SERVER
        ApplicationConstantURL.BLOG_LIST_ABS = "http://api.americanbeautystar.com:80";
        ApplicationConstantURL.SPOTLIGHT_DOMAIN = "http://spotlight.dotstudiopro.com";
        ApplicationConstantURL.API_DSPRO_DOMAIN_S = "https://api.dotstudiopro.com";
        ApplicationConstantURL.API_SPOTLIGHT_DOMAIN = "http://api.spotnetwork.tv";
        ApplicationConstantURL.IMAGES_DSPRO_DOMAIN_S = "https://images.dotstudiopro.com/";
        ApplicationConstantURL.TEASER_DOMAIN = "http://cdn.dotstudiopro.com/";
        ApplicationConstantURL.ADSERVER_DOMAIN = "http://adserver.dotstudiopro.com";
        ApplicationConstantURL.MYSPOTLIGHT_DOMAIN = "http://myspotlight.tv";
        ApplicationConstantURL.COLLECTOR_DEV_DOMAIN_S = "https://collector.dotstudiodev.com";
        ApplicationConstantURL.COLLECTOR_DSPRO_DOMAIN_S = "https://collector.dotstudiopro.com";


        //Reset all URL's
        ApplicationConstantURL.getInstance().setAPIDomain();

        // Assign platform
        ApplicationConstants.PLATFORM = "andriod";
    }
    public CompanyTokenService companyTokenService;
    public void getToken() {
        if(companyTokenService == null)
            companyTokenService = new CompanyTokenService(this);
        companyTokenService.setCompanyTokenServiceListener(new CompanyTokenService.ICompanyTokenService() {
            @Override
            public void companyTokenServiceResponse(JSONObject responseBody) {
                try {
                    Log.d(TAG, "companyTokenServiceResponse: Inside response!==>" + responseBody.getString("token"));
                    ApplicationConstants.xAccessToken = responseBody.getString("token");
                } catch(Exception e) {
                    e.printStackTrace();
                }

                callServicesParallely();

            }

            @Override
            public void companyTokenServiceError(String responseBody) {
                Log.d(TAG, "companyTokenServiceError: Inside Error==>"+responseBody);
            }
        });
        companyTokenService.requestForToken("a12878949f4ea52703ab6a07c662b31895886cea", ApplicationConstantURL.getInstance().TOKEN_URL);
    }

    public void callServicesParallely() {
        callServiceOne();
        callServiceTwo();
        callServiceThree();
    }

    public void callServiceOne() {
        GetAllCategoriesServiceForHomepage_V1 getAllCategoriesServiceForHomepageV1 = new GetAllCategoriesServiceForHomepage_V1(this);
        getAllCategoriesServiceForHomepageV1.PLATFORM = "andriod";
        getAllCategoriesServiceForHomepageV1.setGetAllCategoriesServiceForHomepageListener(new GetAllCategoriesServiceForHomepage_V1.IGetAllCategoriesServiceForHomepage_V1() {
            @Override
            public void getAllCategoriesServiceForHomepageResponse(
                    ArrayList<SpotLightCategoriesDTO> CategoriesListAll,
                    ArrayList<SpotLightCategoriesDTO> CategoriesList,
                    ArrayList<SpotLightCategoriesDTO> SliderShowcaseList,
                    ArrayList<SpotLightCategoriesDTO> RosterList,
                    ArrayList<SpotLightCategoriesDTO> GenreList) {

                Log.d(TAG, "getAllCategoriesServiceForHomepageResponse: CALL SERVICE ONE");
                Log.d(TAG, "getAllCategoriesServiceForHomepageResponse: CategoriesList.size()==>"+CategoriesList.size());

            }

            @Override
            public void getAllCategoriesForHomepageError(String ERROR) {
                if(ERROR != null)
                    Toast.makeText(MainActivity.this, "" + ERROR, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "getAllCategoriesForHomepageError: CALL SERVICE ONE");
            }

            @Override
            public void accessTokenExpired1() {
            }

            @Override
            public void clientTokenExpired1() {
            }
        });
        getAllCategoriesServiceForHomepageV1.getAllCategoriesServiceForHomePage(ApplicationConstants.xAccessToken, ApplicationConstantURL.getInstance().HOMEPAGE_API_ANDROID);

    }

    public void callServiceTwo() {
        GetAllCategoriesService_V1 getAllCategoriesService_V1 = new GetAllCategoriesService_V1(this);
        getAllCategoriesService_V1.PLATFORM = "andriod";
        getAllCategoriesService_V1.setGetAllCategoriesService_V1Listener(new GetAllCategoriesService_V1.IGetAllCategoriesService_V1() {
            @Override
            public void getAllCategoriesServiceResponse(ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListALL, ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOList, ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListForSliderShowcase, ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListForRoster, ArrayList<SpotLightCategoriesDTO> spotLightCategoriesDTOListForGenre) {
                Log.d(TAG, "getAllCategoriesServiceResponse: CALL SERVICE TWO");
                Log.d(TAG, "getAllCategoriesServiceResponse: spotLightCategoriesDTOListALL.size()==>"+spotLightCategoriesDTOListALL.size());
            }

            @Override
            public void getAllCategoriesError(String ERROR) {
                Log.d(TAG, "getAllCategoriesError: CALL SERVICE TWO");
            }

            @Override
            public void accessTokenExpired1() {

            }

            @Override
            public void clientTokenExpired1() {

            }
        });
        getAllCategoriesService_V1.getAllCategoriesService(ApplicationConstants.xAccessToken, ApplicationConstantURL.getInstance().CATEGORIES_LIST);
    }
    public void callServiceThree() {
        GetAllSubscriptionsService_V1 getAllSubscriptionServiceV1 = new GetAllSubscriptionsService_V1(this);
        getAllSubscriptionServiceV1.setGetAllSubscriptionsServiceListener(new GetAllSubscriptionsService_V1.IGetAllSubscriptionsService() {
            @Override
            public void getAllSubscriptionsServiceResponse(ArrayList<SubscriptionDTO> subscriptionDTOArrayList) {
                Log.d(TAG, "getAllSubscriptionsServiceResponse: CALL SERVICE THREE");
                Log.d(TAG, "getAllSubscriptionsServiceResponse: subscriptionDTOArrayList.size()==>"+subscriptionDTOArrayList.size());
            }

            @Override
            public void getAllSubscriptionsError(String ERROR) {
                Log.d(TAG, "getAllSubscriptionsError:ERROR==>"+ERROR);
            }

            @Override
            public void accessTokenExpired1() {

            }

            @Override
            public void clientTokenExpired1() {

            }
        });
        getAllSubscriptionServiceV1.getAllSubscriptionsService(ApplicationConstants.xAccessToken, ApplicationConstantURL.getInstance().SUBSCRIPTION_LIST);
    }
}
