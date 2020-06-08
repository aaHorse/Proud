package com.horse.proud.ui.login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.ui.camera.CameraActivity
import com.google.gson.Gson
import com.horse.core.proud.Const
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logError
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.proud.R
import com.horse.core.proud.model.login.WordsResult
import com.horse.core.proud.model.regist.Register
import com.horse.proud.databinding.ActivityForgetPasswordBinding
import com.horse.proud.event.RegisterEvent
import com.horse.proud.ui.common.BaseActivity
import com.horse.proud.ui.home.MainActivity
import com.horse.proud.util.FileUtil
import com.horse.proud.util.RecognizeService
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_forget_password.bt_phone
import kotlinx.android.synthetic.main.activity_forget_password.btn_verify
import kotlinx.android.synthetic.main.activity_forget_password.comfir
import kotlinx.android.synthetic.main.activity_forget_password.et_name
import kotlinx.android.synthetic.main.activity_forget_password.et_number
import kotlinx.android.synthetic.main.activity_forget_password.et_password
import kotlinx.android.synthetic.main.activity_forget_password.et_phone
import kotlinx.android.synthetic.main.activity_forget_password.et_repassword
import org.greenrobot.eventbus.EventBus
import org.koin.android.ext.android.inject
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/**
 * 忘记密码界面
 *
 * @author liliyuan
 * @since 2020年6月1日19:34:14
 * */
class ForgetPasswordActivity : BaseActivity() , EasyPermissions.PermissionCallbacks {

    private val viewModelFactory by inject<ForgetPasswordActivityViewModelFactory>()

    val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(ForgetPasswordActivityViewModel::class.java) }

    private val register = Register()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityForgetPasswordBinding>(this,R.layout.activity_forget_password)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupViews() {
        bt_phone.setOnClickListener {
            showToast("经费不足，验证码功能只有UI >o<")
            comfir.visibility = View.VISIBLE
        }
        btn_verify.setOnClickListener {
            if(et_name.text.toString().isEmpty()){
                showToast("姓名不能为空")
                return@setOnClickListener
            }
            if(et_number.text.toString().isEmpty()){
                showToast("学号不能为空")
                return@setOnClickListener
            }
            if(et_password.text.toString().isEmpty()||et_repassword.text.toString().isEmpty()){
                showToast("密码不能为空")
                return@setOnClickListener
            }
            if(et_password.text.toString() != et_repassword.text.toString()){
                showToast("两次输入的密码不一致")
                return@setOnClickListener
            }
            if(et_phone.text.toString().isEmpty()){
                showToast("手机号码不能为空")
                return@setOnClickListener
            }
            register.name = et_name.text.toString()
            register.number = et_number.text.toString()
            register.password = et_password.text.toString()
            register.phoneNumber = et_phone.text.toString()

            checkPermission()
        }
        observe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_HANDWRITING -> if(resultCode == Activity.RESULT_OK){
                RecognizeService.recReceipt(this, FileUtil.getSaveFile(applicationContext)!!.absolutePath,
                    object : RecognizeService.ServiceListener {
                        override fun onResult(result: String) {
                            regexCheck(result)
                        }

                    })
            }
            else -> {
            }
        }
    }

    /**
     * 以license文件方式初始化
     */
    private fun initAccessToken() {
        OCR.getInstance(this).initAccessToken(object : OnResultListener<AccessToken> {
            override fun onResult(accessToken: AccessToken) {
                val token = accessToken.accessToken
                logWarn(TAG,token)
                val intent = Intent(activity, CameraActivity::class.java)
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, FileUtil.getSaveFile(application)?.absolutePath)
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_GENERAL)
                startActivityForResult(intent, REQUEST_CODE_HANDWRITING)
            }

            override fun onError(error: OCRError) {
                logError(TAG,error)
            }
        }, Proud.context)
    }

    private fun observe(){
        viewModel.dataChanged.observe(this, Observer {
            val event = RegisterEvent()
            event.loginState = Const.Auth.COMFIR
            event.register = register
            EventBus.getDefault().post(event)
            finish()
        })
    }

    /**
     * 正则表达式验证身份
     * */
    private fun regexCheck(result:String){
        //福州大学，匹配2次
        var i1 = 2
        //姓名，匹配1次
        var i2 = 1
        //学号，匹配1次
        var i3 = 1
        //福，匹配1次,事实有2次，为了提高匹配度，只验证一个
        var i4 = 1
        val bean = Gson().fromJson(result, WordsResult::class.java)
        for(item in bean.resultList){
            with(item){
                if(words.matches(Regex(".*福州大学.*"))){
                    i1 --
                }
                if(words.matches(Regex(register.name))){
                    i2 --
                }
                if(words.matches(Regex(".*${register.number}"))){
                    i3 --
                }
                if(words.matches(Regex("福"))){
                    i4 --
                }
                if(words.matches(Regex("福.*"))){
                    i4 --
                }
            }
        }
        if(i1+i2+i3+i4 <= 0){
            showToast("修改密码成功")
            viewModel.forgetPassword(register.number,register.phoneNumber,register.password)
        }else{
            showToast("认证失败")
        }
    }

    /**
     * 检查权限
     */
    @AfterPermissionGranted(PRC_PHOTO_PICKER)
    private fun checkPermission() {
        val perms: Array<String> = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA)
        if(!EasyPermissions.hasPermissions(this, *perms)){
            EasyPermissions.requestPermissions(this,
                "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照",
                PRC_PHOTO_PICKER, *perms)
        }else{
            initAccessToken()
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

        private const val TAG = "ForgetPasswordActivity"

        /**
         * 图片识别回调
         * */
        const val REQUEST_CODE_HANDWRITING = 0

        /**
         * 申请相册权限回调标志
         * */
        private const val PRC_PHOTO_PICKER: Int = 1


        fun actionStart(activity: Activity){
            val intent = Intent(activity, ForgetPasswordActivity::class.java)
            activity.startActivity(intent)
        }

    }

}
