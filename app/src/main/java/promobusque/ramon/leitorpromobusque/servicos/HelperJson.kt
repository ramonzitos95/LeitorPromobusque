package promobusque.ramon.leitorpromobusque.servicos

import com.squareup.moshi.Moshi
import promobusque.ramon.leitorpromobusque.modelos.Participacao

class HelperJson {

    //Gera objeto participacao a partir do json
    public fun deserializaJsonParticipacao(json: String) : Participacao {

        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter<Participacao>(Participacao::class.java)
        val participacao = adapter.fromJson(json)

        return participacao!!
    }
}