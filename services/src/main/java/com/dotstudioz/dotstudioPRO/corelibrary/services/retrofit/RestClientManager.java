package com.dotstudioz.dotstudioPRO.corelibrary.services.retrofit;

import com.dotstudioz.dotstudioPRO.corelibrary.dto.ParameterItem;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClientManager {

    private static Retrofit retrofit = null;
    private static String BASE_URL = null;

    public static Retrofit getClient(String baseURL, String accessToken, String clientToken,String contentType) {
        BASE_URL = baseURL;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        //OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).addInterceptor(interceptor).addNetworkInterceptor(new AddHeaderInterceptor(accessToken, clientToken,contentType)).build();
        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient(accessToken, clientToken, contentType);//.Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).addInterceptor(interceptor).addNetworkInterceptor(new AddHeaderInterceptor(accessToken, clientToken,contentType)).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }
    public static Retrofit getClient(String baseURL, ArrayList<ParameterItem> headersArrayList) {
        BASE_URL = baseURL;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        //OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).addInterceptor(interceptor).addNetworkInterceptor(new AddHeaderInterceptor(headersArrayList)).build();
        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient(headersArrayList);//.Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).addInterceptor(interceptor).addNetworkInterceptor(new AddHeaderInterceptor(headersArrayList)).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                //.addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    /*class NullOnEmptyConverterFactory implements Converter.Factory {
        @Override
        public Converter<ResponseBody, ?> responseBody(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return new Converter<>() {
                @Override
                public void convert(ResponseBody body) {
                    if (body.contentLength() == 0)
                        return null;
                    return delegate.convert(body);
                }
            };
        }
    }*/

}
