package com.github.polesapart.cep

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path


@ExperimentalSerializationApi
class ConsultaCep(debugHttp: Boolean = false) {

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


    private val json = Json { isLenient = true; ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(
        json.asConverterFactory(
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

    suspend fun getDataByAddress(estado: String, municipio: String, logradouro: String): List<Map<String, String?>> {
        return runCatching {
            service.getCepByAddress(
                estado,
                municipio,
                logradouro
            )
        }.getOrElse { throw HttpErrorException("Error: ${it.message} ${it.stackTraceToString()}") }
    }

    suspend fun getDataByCep(cep: String): Map<String, String?> {
        val ret =
            runCatching { service.getCepAsJson(cep) }.getOrElse { throw HttpErrorException("Error: ${it.message} ${it.stackTraceToString()}") }
        if (ret.containsKey("erro")) {
            throw CepApiErrorException("Cep Inexistente")
        }
        return ret
    }
}

interface ConsultaCepService {
    @GET("{cep}/json")
    suspend fun getCepAsJson(@Path("cep") cep: String): Map<String, String?>

    @GET("{estado}/{municipio}/{logradouro}/json")
    suspend fun getCepByAddress(
        @Path("estado") estado: String, @Path("municipio") municipio: String,
        @Path("logradouro") logradouro: String
    ): List<Map<String, String?>>

}