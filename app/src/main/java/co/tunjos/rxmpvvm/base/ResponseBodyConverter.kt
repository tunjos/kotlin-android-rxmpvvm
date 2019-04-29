package co.tunjos.rxmpvvm.base

import co.tunjos.rxmpvvm.model.error.APIError
import dagger.Lazy
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResponseBodyConverter @Inject constructor() {

    @Inject
    lateinit var converter: Lazy<Converter<ResponseBody, APIError>>

    fun convertToApiError(errorBody: ResponseBody): APIError? {
        val error: APIError?

        try {
            error = converter.get().convert(errorBody)
        } catch (e: IOException) {
            return APIError()
        }

        return error
    }
}
