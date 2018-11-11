package promobusque.ramon.leitorpromobusque.retrofit

import promobusque.ramon.leitorpromobusque.servicos.IParticipacaoService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitInitializer {

    private val url = "http://192.168.1.6:8000"
    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    fun participacaoService(): IParticipacaoService = retrofit.create(IParticipacaoService::class.java)
}