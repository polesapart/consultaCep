package com.aleques.cep

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path



@ExperimentalSerializationApi
class ConsultaCep constructor(debugHttp: Boolean = false) {

    companion object {
        private const val baseUrl = "https://viacep.com.br/ws/"
        private val contentType = "application/json; charset=utf-8".toMediaType()

    }

    private val interceptor = if (debugHttp) {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    } else {
        null
    }
    private val httpClient = if (debugHttp) {
        OkHttpClient.Builder().addInterceptor(interceptor!!).build()
    } else {
        null
    }


    private val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(
        Json { isLenient = true; ignoreUnknownKeys = true }.asConverterFactory(
            contentType
        )
    ).let {
        if (debugHttp) {
            it.client(httpClient!!)
        } else {
            it
        }
    }.build()
    private val service = retrofit.create(ConsultaCepService::class.java)

    fun getDataByAddress(estado: String, municipio: String, logradouro: String): List<Map<String, String?>> {
        val ret = service.getCepByAddress(estado, municipio, logradouro).execute()
        if (ret.isSuccessful) {
            return ret.body()!!
        }
        throw  HttpErrorException("Http transport failed: ${ret.code()} ${ret.body()} ${ret.errorBody()}")
    }

    fun getDataByCep(cep: String): Map<String, String?> {
        val ret = service.getCepAsJson(cep).execute()
        if (ret.isSuccessful) {
            val r = ret.body()!!
            if (r.containsKey("erro")) {
                throw CepApiErrorException("Unknown CEP $cep")
            }
            return r
        }
        throw HttpErrorException("Http transport failed: ${ret.code()} ${ret.errorBody()}")
    }
}

interface ConsultaCepService {
    @GET("{cep}/json")
    fun getCepAsJson(@Path("cep") cep: String): Call<Map<String, String?>>

    @GET("{estado}/{municipio}/{logradouro}/json")
    fun getCepByAddress(
        @Path("estado") estado: String, @Path("municipio") municipio: String,
        @Path("logradouro") logradouro: String
    ): Call<List<Map<String, String?>>>

}