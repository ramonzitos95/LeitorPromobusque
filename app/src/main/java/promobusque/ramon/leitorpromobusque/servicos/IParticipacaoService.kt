package promobusque.ramon.leitorpromobusque.servicos

import promobusque.ramon.leitorpromobusque.modelos.Participacao
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IParticipacaoService {

    @POST("/Api/Participacao/ValidarParticipacao")
    fun validarParticipacao(@Body participacao: Participacao): Call<Void>
}