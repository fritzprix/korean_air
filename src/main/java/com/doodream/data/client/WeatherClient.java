package com.doodream.data.client;

import com.doodream.data.client.svc.WeatherService;
import com.doodream.data.dagger.DaggerClientComponent;
import com.doodream.data.model.weather.WeatherInfo;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class WeatherClient extends ReactiveClient {

    private WeatherService weatherService;


    public WeatherClient() {
        DaggerClientComponent.create().inject(this);
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("http://www.weather.go.kr")
                .build();

        weatherService = retrofit.create(WeatherService.class);


    }


    public Observable<WeatherInfo> getWeatherForecast() {
        return weatherService.getWeatherForecast("108")
                .filter(Response::isSuccessful)
                .flatMapObservable(WeatherInfo::fromRSS)
                .subscribeOn(getScheduler());
    }




}
