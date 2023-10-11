package com.github.polesapart.cep

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit


@ExperimentalSerializationApi
class ConsultaCep(private val httpClient: OkHttpClient? = null) {

    companion object {
        private const val BASEURL = "https://viacep.com.br/ws/"
        private val contentType = "application/json; charset=utf-8".toMediaType()

    }


    private val json = Json { isLenient = true; ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder().baseUrl(BASEURL).addConverterFactory(
        json.asConverterFactory(
            contentType
        )
    ).apply {
        if (httpClient != null) {
            this.client(httpClient)
        }
    }.build()
    private val service = retrofit.create(CepRetrofitService::class.java)

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
