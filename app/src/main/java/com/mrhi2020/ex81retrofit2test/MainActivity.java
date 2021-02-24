package com.mrhi2020.ex81retrofit2test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    // Http 통신 작업을 위한 Library
    // 1.OkHttp - 처음 개발된 라이브러리 oracle 이 보유
    // 2.Retrofit - OkHttp 를 기반으로 개량한 라이브러리 - sqareup 이 보유 [가장 많이 사용됨] : 현재는 version 2
    // 3.Volley - Google에서 제작..하여서 많이 기대를 받았고 사용되어 왔으나 더이상 업데이트를 안해서 앞으로 사장될 것 같아요...

    //그래서 Retrofit2 라이브러리를 사용하여 네트워크 작업 수행 연습
    // Retrofit2 라이브러리의 특징 : 기본적으로 json 데이터를 주고받는데 특화되어 있음.
    // 어노테이션 @ 많이 사용!!!

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv= findViewById(R.id.tv);
    }

    public void clickBtn(View view) {
        //retrofit library 를 이용하여 서버에서 json 데이터를 읽어와서 Item객체로 곧바로 파싱

        //1. Retrofit 객체 생성
        Retrofit.Builder builder= new Retrofit.Builder();
        builder.baseUrl("http://mrhi2021.dothome.co.kr/"); //서버 기본주소 설정
        builder.addConverterFactory(GsonConverterFactory.create());//Gson으로 json을 자동 파싱해주는 녀석 설정 - 이 설정을 하면 통신결과를 객체로 바로 얻을 수 있음
        Retrofit retrofit= builder.build();

        //2. Service 인터페이스 설계 [원하는 GET, POST 동작을 하는 추상메소드 설계]
        //RetrofitService.java interface 설계 및 추상메소드 : getBoardJson()

        //3. 2단계에서 설계한 RetrofitService 인터페이스 객체를 생성 - 자동으로 추상메소드의 내용을 Retrofit이 다 설계하여 그 능력을 가진 Call객체를 리턴할 준비를 함
        RetrofitService retrofitService= retrofit.create(RetrofitService.class);

        //4. 위에서 만든 서비스객체의 추상메소드를 호출하여 실제 서버작업을 수행하는 기능을 가진 Call 이라는 객체를 리턴받기
        Call<Item> call= retrofitService.getBoardJson();

        //5.4단계로 리턴받은 Call 객체에게 네트워크 작업을 수행하도록 요청
        call.enqueue(new Callback<Item>() {
            //서버로 부터 응답을 받았을때 자동으로 실행되는 메소드
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {

                //파라미터로 전달된 응답객체로 부터 GSON 라이브러리에 의해 자동으로
                //Item 객체로 파싱되어 있는 데이터 값 Body얻어오기

                Item item= response.body();
                tv.setText(item.name +" : " + item.msg);

            }

            //서버와 통신이 실패할때 자동으로 실행되는 메소드
            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                tv.setText("failure");
            }
        });
    }

    public void clickBtn2(View view) {

        //1) Retrofit 객체 생성
        Retrofit.Builder builder= new Retrofit.Builder();
        builder.baseUrl("http://mrhi2021.dothome.co.kr/");
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit= builder.build();

        //2) Service 인터페이스 설계 [ 원하는 GET, POST 동작을 하는 추상메소드 설계 ]
        //getBoardJsonByParams() 메소드 설계

        //3) Service 인터페이스 객체 생성
        RetrofitService retrofitService= retrofit.create(RetrofitService.class);

        //4) 2단계에서 설계한 추상메소드 호출
        Call<Item> call= retrofitService.getBoardJsonByParams("Retrofit", "board.json");

        //5) 4단계의 리턴 객체인 Call에게 네트워킹 작업 시작 밍 응답 받기
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                //응답된 json을 Gson을 이용하여 Item객체로 자동 파싱한 결과 Body값 얻기
                Item item= response.body();
                tv.setText(item.name+"\n"+item.msg);
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                tv.setText("실패");
            }
        });
    }

    public void clickBtn3(View view) {
        //서버에 보낼 데이터들
        String name="홍길동";
        String msg="안녕하세요.";

        //1) Retrofit객체생성 - 매번 4줄 쓰기 귀찮음.
        Retrofit retrofit= RetrofitHelper.getRetrofitInstance();

        //2) Service 인터페이스 설계 [ 원하는 GET 작업 추상메소드 설계 ]
        // getMethodTest(name, msg);

        //3) RetrofitService객체 생성
        RetrofitService retrofitService= retrofit.create(RetrofitService.class);

        //4) 서비스객체의 추상메소드 호출하면 서버에 보낼 값 전달
        Call<Item> call= retrofitService.getMethodTest(name, msg);

        //5) 네트워크 작업 시작 및 응답 받기
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Item item= response.body();
                tv.setText(item.name+"\n"+item.msg);
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                tv.setText("실패");
            }
        });
    }

    public void clickBtn4(View view) {
        String name="kim";
        String msg="Good afternoon";

        //1)
        Retrofit retrofit= RetrofitHelper.getRetrofitInstance();
        //2,3)추상메소드 설계 및 인터페이스 객체 생성
        RetrofitService retrofitService= retrofit.create(RetrofitService.class);
        //4) 추상메소드 호출
        Call<Item> call= retrofitService.getMethodTest2("getTest.php", name, msg);
        //5) 작업 시작
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Item item= response.body();
                tv.setText(item.name+" : "+ item.msg);
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                tv.setText("실패 : " + t.getMessage());//에러 메세지
            }
        });
    }

    public void clickBtn5(View view) {

        Retrofit retrofit= RetrofitHelper.getRetrofitInstance();
        RetrofitService retrofitService= retrofit.create(RetrofitService.class);

        //전달할 데이터들의 식별자(키)와 값(벨류)들을 한방에 Map 컬렉션에 넣고 전달
        Map<String, String> datas= new HashMap<>();
        datas.put("name", "Hong");
        datas.put("msg","Nice Retrofit");
        Call<Item> call= retrofitService.getMethodTest3(datas);

        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Item item= response.body();
                tv.setText(item.name +" : "+ item.msg);
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                tv.setText("실패 : " + t.getMessage()); //에러메세지 출력
            }
        });
    }

    public void clickBtn6(View view) {
        //POST 방식으로 전달할 값 객체
        Item item= new Item("lee","Good evening");

        Retrofit retrofit= RetrofitHelper.getRetrofitInstance();
        RetrofitService retrofitService= retrofit.create(RetrofitService.class);

        //서버로 보낼 값을 가진 객체를 그냥 보내기
        Call<Item> call= retrofitService.postMethodTest(item);

        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Item i= response.body();
                tv.setText(i.name +" : "+i.msg);
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                tv.setText("실패 : " + t.getMessage());
            }
        });

    }

    public void clickBtn7(View view) {
        //POST방식으로 서버에 보낼 데이터들
        String name= "김말똥";
        String msg= "반갑습니다.";

        RetrofitService retrofitService= RetrofitHelper.getRetrofitInstance().create(RetrofitService.class);
        Call<Item> call= retrofitService.postMethodTest2(name, msg);
        call.enqueue(new Callback<Item>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                Item item= response.body();
                tv.setText(item.name+" : "+item.msg);
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                tv.setText("실패 : "+ t.getMessage());
            }
        });
    }

    public void clickBtn8(View view) {
        RetrofitService retrofitService= RetrofitHelper.getRetrofitInstance().create(RetrofitService.class);
        Call<ArrayList<Item>> call= retrofitService.getBoardArray();
        call.enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                ArrayList<Item> items= response.body();

                //실제로는 items를 보여주는 RecyclerView를 이용
                StringBuffer buffer= new StringBuffer();
                for(Item item : items){
                    buffer.append(item.name+" : "+ item.msg+"\n");
                }

                tv.setText(buffer.toString());
            }

            @Override
            public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                tv.setText("error : " + t.getMessage());
            }
        });
    }

    public void clickBtn9(View view) {
        //결과를 단순 String으로 받으려면 GsonConverter가 아닌 다른 녀석이 필요함
        //ScalarsConverter 사용
        Retrofit.Builder builder= new Retrofit.Builder();
        builder.baseUrl("http://mrhi2021.dothome.co.kr/");
        builder.addConverterFactory(ScalarsConverterFactory.create());
        Retrofit retrofit= builder.build();

        RetrofitService retrofitService= retrofit.create(RetrofitService.class);
        Call<String> call= retrofitService.getJsonString();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s= response.body();
                tv.setText(s);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                tv.setText("error : " +t.getMessage());
            }
        });

    }
}