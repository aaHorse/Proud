package com.horse.proud.ui.login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.ui.camera.CameraActivity
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.AndroidVersion
import com.horse.proud.R
import com.horse.proud.data.model.regist.Register
import com.horse.proud.databinding.ActivityRegisterBinding
import com.horse.proud.event.RegisterSucceedEvent
import com.horse.proud.ui.common.BaseActivity
import com.horse.proud.ui.home.MainActivity
import com.horse.proud.util.FileUtil
import com.horse.proud.util.RecognizeService
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.IOException

/**
 * 注册界面
 *
 * @author liliyuan
 * @since 2020年5月2日14:21:59
 * */
class RegisterActivity : BaseActivity() , EasyPermissions.PermissionCallbacks {

    private val viewModelFactory by inject<RegisterActivityViewModelFactory>()

    val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(RegisterActivityViewModel::class.java) }

    private lateinit var register:Register

    /**
     * 校园卡拍照验证的图片的临时地址
     * */
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityRegisterBinding>(this,R.layout.activity_register)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupViews() {
        btn_verify.setOnClickListener {
            //takePhoto()
            initAccessToken()


//            register = Register()
//            register.name = et_name.text.toString()
//            register.number = et_number.text.toString()
//            register.password = et_password.text.toString()
//            viewModel.register(register)
        }
        btn_visit.setOnClickListener {
            MainActivity.actionStart(this)
        }
        observe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAKE_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                showToast("获取图片成功")
                logWarn(TAG,photoUri.toString())
                viewModel.getAccessToken(TYPE,AK,SK)
            }
            REQUEST_CODE_HANDWRITING -> if(resultCode == Activity.RESULT_OK){
                RecognizeService.recHandwriting(this, FileUtil.getSaveFile(applicationContext)!!.absolutePath,
                    object : RecognizeService.ServiceListener {
                        override fun onResult(result: String) {
                            logWarn(TAG,result)
                        }

                    })
            }
            else -> {
            }
        }
    }

    private fun observe(){
        viewModel.dataChanged.observe(this, Observer {
            var event = RegisterSucceedEvent()
            event.register = register
            EventBus.getDefault().post(event)
            finish()
        })
    }

    /**
     * 以license文件方式初始化
     */
    private fun initAccessToken() {
        OCR.getInstance(this).initAccessToken(object : OnResultListener<AccessToken> {
            override fun onResult(accessToken: AccessToken) {
                val token = accessToken.accessToken
                //hasGotToken = true
                logWarn(TAG,token)
                val intent = Intent(activity, CameraActivity::class.java)
                intent.putExtra(
                    CameraActivity.KEY_OUTPUT_FILE_PATH,
                    FileUtil.getSaveFile(application)?.getAbsolutePath()
                )
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL)
                startActivityForResult(intent, REQUEST_CODE_HANDWRITING)
            }

            override fun onError(error: OCRError) {
                error.printStackTrace()
                //alertText("licence方式获取token失败", error.message)
            }
        },Proud.getContext())
    }

    /**
     * 打开摄像头拍照。
     */
    @AfterPermissionGranted(PRC_PHOTO_PICKER)
    private fun takePhoto() {
        val perms: Array<String> = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA)
        if(EasyPermissions.hasPermissions(this, *perms)){
            // 创建 File 对象，用于存储拍照后的图片
            val outputImage = File(externalCacheDir, TEMP_PHOTO)
            try {
                if (outputImage.exists()) {
                    outputImage.delete()
                }
                outputImage.createNewFile()
            } catch (e: IOException) {
                logWarn(TAG, e.message, e)
            }

            photoUri = if (AndroidVersion.hasNougat()) {
                FileProvider.getUriForFile(this, "com.horse.fileprovider", outputImage)
            } else {
                Uri.fromFile(outputImage)
            }
            // 启动相机程序
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(intent, TAKE_PHOTO)
        }else{
            EasyPermissions.requestPermissions(
                this,
                "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照",
                PRC_PHOTO_PICKER,
                *perms
            )
        }
    }

    //-------------------------------------EasyPermissions----------------------

    /**
     * EasyPermissions失败回调
     * */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        if(requestCode == PRC_PHOTO_PICKER){
            showToast("您拒绝了选择图片相关权限的申请！")
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) { }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    //-------------------------------------EasyPermissions----------------------

    companion object{

        private const val TAG = "RegisterActivity"

        const val TEMP_PHOTO = "taken_photo.jpg"

        const val TAKE_PHOTO = 1000

        const val REQUEST_CODE_HANDWRITING = 0

        //申请相册权限回调标志
        private const val PRC_PHOTO_PICKER: Int = 1

        //识别校园卡用
        const val SK = "hSimfEPbPMxvWMZ8RtXIGMqo7sBCrzoB"
        const val AK = "auiGfbTHzngFsK1VrWI3c2ai"
        const val TYPE = "client_credentials"
        //const val token_url = "https://aip.baidubce.com/oauth/2.0/token?"

        /**
         * 获取识别校园卡用的token
         * */
//        private fun getAuth():String?{
//            val getAccessTokenUrl = "${token_url}" +
//                    "grant_type=client_credentials" +
//                    "&client_id=${AK}" +
//                    "&client_secret=${SK}"
//
//            try {
//                val realUrl = URL(getAccessTokenUrl)
//                val connection = realUrl.openConnection() as HttpURLConnection
//                connection.requestMethod = "GET"
//                connection.connect()
//                val inReader = BufferedReader(InputStreamReader(connection.inputStream))
//                var result = StringBuilder()
//                inReader.forEachLine {
//                    result.append(it)
//                }
//                val jsonObj = JSONObject(result.toString())
//                return jsonObj.getString("access_token")
//            }catch (e:Exception){
//                logWarn(TAG,"token获取失败:${e.printStackTrace()}")
//            }
//            return null
//        }

        fun actionStart(activity: Activity){
            val intent = Intent(activity,
                RegisterActivity::class.java)
            activity.startActivity(intent)
        }

    }

}
