package com.github.polesapart.cep

import retrofit2.http.GET
import retrofit2.http.Path

interface CepRetrofitService {
    @GET("{cep}/json")
    suspend fun getCepAsJson(@Path("cep") cep: String): Map<String, String?>

    @GET("{estado}/{municipio}/{logradouro}/json")
    suspend fun getCepByAddress(
        @Path("estado") estado: String, @Path("municipio") municipio: String,
        @Path("logradouro") logradouro: String
    ): List<Map<String, String?>>

}