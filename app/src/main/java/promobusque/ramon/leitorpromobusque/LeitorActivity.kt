package promobusque.ramon.leitorpromobusque

import android.Manifest
import android.hardware.Camera
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_leitor.*
import me.dm7.barcodescanner.core.CameraUtils
import me.dm7.barcodescanner.zxing.ZXingScannerView
import promobusque.ramon.leitorpromobusque.retrofit.RetrofitInitializer
import promobusque.ramon.leitorpromobusque.servicos.HelperJson
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

@Suppress("DEPRECATION")
class LeitorActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks, ZXingScannerView.ResultHandler {

    val REQUEST_CODE_CAMERA = 182

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leitor)

        askCameraPermission()
    }

    override fun onResume() {
        super.onResume()
        z_xing_scanner.setResultHandler( this )
        startCamera()
    }

    override fun onPause() {
        super.onPause()
        z_xing_scanner.stopCamera()

        val camera = CameraUtils.getCameraInstance()
        if( camera != null ){
            (camera as Camera).release()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            this )
    }

    override fun onPermissionsDenied(
        requestCode: Int,
        perms: MutableList<String>) {

        askCameraPermission()
    }

    private fun askCameraPermission(){
        EasyPermissions.requestPermissions(
            PermissionRequest.Builder(this, REQUEST_CODE_CAMERA, Manifest.permission.CAMERA)
                .setRationale("A permissão de uso de câmera é necessária para que o aplicativo funcione.")
                .setPositiveButtonText("Ok")
                .setNegativeButtonText("Cancelar")
                .build() )
    }

    override fun onPermissionsGranted(
        requestCode: Int,
        perms: MutableList<String>) {

        startCamera()
    }

    private fun startCamera(){
        if( EasyPermissions.hasPermissions( this, Manifest.permission.CAMERA ) ){
            z_xing_scanner.startCamera()
        }
    }

    override fun handleResult(result: Result?) {

        var mensagem: String = ""
        val conteudo = result!!.text
        Log.i("LOG", "Conteúdo do código lido: ${conteudo}")
        Log.i("LOG", "Formato do código lido: ${result.barcodeFormat.name}")

        val json = HelperJson().deserializaJsonParticipacao(conteudo)
        RetrofitInitializer()
            .participacaoService()
            .validarParticipacao(json)
            .enqueue(object: Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    mensagem = "participação ${json.CodigoGerado} vinculada com sucesso"
                    Log.i("Leitor", mensagem)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    mensagem = "ocorreu um erro ao validar participação: ${t.message}"
                    Log.e("Leitor", mensagem)
                }
            })

        //Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show()

        z_xing_scanner.resumeCameraPreview( this )
    }
}
