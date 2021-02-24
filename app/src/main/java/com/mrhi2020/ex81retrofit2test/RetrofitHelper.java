package com.mrhi2020.ex81retrofit2test;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    static String baseUrl="http://mrhi2021.dothome.co.kr/";

    //Retrofit 객체를 만들어서 리턴해주는 static 메소드[객체 생성없이 사용가능 함]
    static Retrofit getRetrofitInstance(){
        Retrofit.Builder builder= new Retrofit.Builder();
        builder.baseUrl(baseUrl);
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit= builder.build();

        return retrofit;
    }

}
