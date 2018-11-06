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
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

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
        Log.i("LOG", "Conteúdo do código lido: ${result!!.text}")
        Log.i("LOG", "Formato do código lido: ${result.barcodeFormat.name}")
        z_xing_scanner.resumeCameraPreview( this )
    }
}
