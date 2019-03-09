package co.tunjos.rxmpvvm.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AddHeadersInterceptor : Interceptor {

    /**
     * @throws IOException
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Accept", GithubApi.ACCEPT_HEADER_JSON)
            .build()

        return chain.proceed(request)
    }
}
