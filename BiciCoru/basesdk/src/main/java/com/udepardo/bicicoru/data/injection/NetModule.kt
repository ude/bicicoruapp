package com.udepardo.bicicoru.data.injection

import com.udepardo.bicicoru.data.source.remote.BiciCoruInterfaceApi
import com.udepardo.bicicoru.data.source.remote.CityBikesInterfaceApi
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class NetModule {

//single<Service>("default") { ServiceImpl() }

    val networkModule = module {
        single<Retrofit>("BicicoruRetrofit") { provideBicicoruRetrofit() }
        single<Retrofit>("CitybikesRetrofit") { provideCBRetrofit()}
        single { provideOkHttpClient()}
        single("CBInterfaz") { provideCBInterfaceApi(get("CitybikesRetrofit"))}
        single ("BCInterfaz"){ provideBCInterfaceApi(get("BicicoruRetrofit"))}
    }



    private fun provideBicicoruRetrofit() = Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl("https://bicicoru.herokuapp.com/")
                .client(
                        OkHttpClient.Builder()
                                .addInterceptor(
                                        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
                                ).build()
                )
                .build()

    private fun provideCBRetrofit() = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://api.citybik.es/")
            .build()

    private fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(60, TimeUnit.SECONDS)
        builder.followRedirects(false)
        builder.followSslRedirects(false)
        builder.addInterceptor (
                    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        )
        builder.cookieJar(
                object: CookieJar{
                    val cookieList = mutableListOf<Cookie>()
                    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
                        cookieList.addAll(cookies)
                    }

                    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
                        return cookieList
                    }

                }
        )
        return builder.build()

    }


    private fun provideBCInterfaceApi(retrofit: Retrofit): BiciCoruInterfaceApi {
        return retrofit.create(BiciCoruInterfaceApi::class.java)
    }


    private fun provideCBInterfaceApi(retrofit: Retrofit): CityBikesInterfaceApi {
        return retrofit.create(CityBikesInterfaceApi::class.java)
    }



}

























