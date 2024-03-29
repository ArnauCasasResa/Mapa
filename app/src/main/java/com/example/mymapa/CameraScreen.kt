import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.mymapa.MyViewModel
import com.example.mymapa.Routes
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(navController: NavController,myViewModel: MyViewModel) {
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val context= LocalContext.current
    val controller=remember{LifecycleCameraController(context).apply { CameraController.IMAGE_CAPTURE }}
    LaunchedEffect(Unit){
        permissionState.launchPermissionRequest()
    }
    if (permissionState.status.isGranted){
        Box(Modifier.fillMaxSize()) {
            CameraPreview(controller = controller,modifier = Modifier.fillMaxSize())
            IconButton(onClick = {
                controller.cameraSelector=
                if (controller.cameraSelector== CameraSelector.DEFAULT_BACK_CAMERA){
                     CameraSelector.DEFAULT_FRONT_CAMERA
                } else CameraSelector.DEFAULT_BACK_CAMERA},Modifier.offset(16.dp,16.dp)) {
                Icon(imageVector = Icons.Default.FlipCameraAndroid, contentDescription = "Switch camera")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally,modifier=Modifier.fillMaxWidth()) {
                IconButton(onClick = {
                    takePhoto(context,controller){photo->
                        myViewModel.addImage(photo);myViewModel.editMarker()
                        navController.navigate(Routes.DetallMarcador.route)}

                })
                {
                    Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = "Take photo")
                }
            }
        }
    }else{
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = "No hay permisos para la camara.",
                fontStyle = FontStyle.Italic,
                color = Color.LightGray)
        }
    }
}




@Composable
fun CameraPreview(controller: LifecycleCameraController,modifier:Modifier=Modifier){
    val lifecycleOwner= LocalLifecycleOwner.current
    AndroidView(factory = { PreviewView(it).apply {
        this.controller=controller
        controller.bindToLifecycle(lifecycleOwner)
    }},modifier = modifier)
}

private fun takePhoto(context: Context, controller: LifecycleCameraController, onPhotoTaken: (Bitmap) -> Unit) {
    controller.takePicture(ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val matri = Matrix().apply{
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matri,
                    true
                )
                onPhotoTaken(rotatedBitmap)
            }
            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Error capturing image", exception)
            }
        }
    )
}