package co.tunjos.rxmpvvm.base.di

import android.content.Context
import co.tunjos.rxmpvvm.BuildConfig
import co.tunjos.rxmpvvm.api.AddHeadersInterceptor
import co.tunjos.rxmpvvm.api.GithubApi
import co.tunjos.rxmpvvm.model.error.APIError
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    private val CACHE_SIZE = 20 * 1024 * 1024 //20MB

    @Provides
    @Singleton
    fun provideGithubApi(retrofit: Retrofit): GithubApi {
        return retrofit.create(GithubApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GithubApi.GITHUB_API_BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        addHeadersInterceptor: AddHeadersInterceptor,
        cache: Cache
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(addHeadersInterceptor)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    @Provides
    @Singleton
    fun provideAddHeadersInterceptor(): AddHeadersInterceptor {
        return AddHeadersInterceptor()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideCache(context: Context): Cache {
        return Cache(context.cacheDir, CACHE_SIZE.toLong())
    }

    @Provides
    @Singleton
    fun provideResponseBodyConverter(retrofit: Retrofit): Converter<ResponseBody, APIError> {
        return retrofit.responseBodyConverter(APIError::class.java, arrayOfNulls(0))
    }
}
