package com.horse.proud.ui.rental

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.showToast
import com.horse.proud.R
import com.horse.proud.callback.LoadDataListener
import com.horse.proud.ui.common.BaseActivity
import com.horse.proud.ui.common.MapActivity
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import kotlinx.android.synthetic.main.activity_rental.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.ArrayList

/**
 * @author liliyuan
 * @since 2020年5月2日12:26:53
 * */
class RentalActivity : BaseActivity(), LoadDataListener, EasyPermissions.PermissionCallbacks,
    BGASortableNinePhotoLayout.Delegate {

    //声明AMapLocationClient类对象
    lateinit var mLocationClient: AMapLocationClient

    //声明定位回调监听器
    lateinit var mLocationListener: AMapLocationListener

    lateinit var mLocationOption: AMapLocationClientOption

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
        setContentView(R.layout.activity_rental)
    }

    override fun setupViews() {
        setupToolbar()
        // 设置拖拽排序控件的代理
        snpl_moment_add_photos.setDelegate(this)
        setUpRecyclerView()
        setOnClickListener()
        /*       bt.setOnClickListener {

                   //choicePhotoWrapper()
                   //logWarn(TAG,Test.sHA1(this))
               }*/
    }

    override fun onLoad() {
        //
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.publish->{
                showToast("测试")
            }
            else->{
                showToast("测试2")
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                RC_CHOOSE_PHOTO ->{
                    snpl_moment_add_photos.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data))
                }
                RC_PHOTO_PREVIEW ->{
                    snpl_moment_add_photos.data = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data)
                }
                LOCATION_FOT_RESULT ->{
                    iv_local.setImageResource(R.drawable.local_click)
                    showToast("定位回调")
                }
            }
        }else{
            when(requestCode){
                LOCATION_FOT_RESULT ->{
                    showToast("没有标记定位")
                }
            }
        }
    }

    private fun setUpRecyclerView(){

    }

    private fun setOnClickListener(){
        ll_local.setOnClickListener {
            getLocation()
        }

        ll_type.setOnClickListener {
            showMultiChoiceDialog()
            iv_type.setImageResource(R.drawable.type_click)
        }

        ll_time.setOnClickListener {
            showSingleChoiceDialog()
            iv_time.setImageResource(R.drawable.time_click)
        }
    }


    //-------------------------------------EasyPermissions----------------------

    /**
     * EasyPermissions失败回调
     * */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        if(requestCode == PRC_PHOTO_PICKER){
            showToast("您拒绝了选择图片相关权限的申请！")
        }else if(requestCode == LOCATION){
            showToast("您拒绝了标记地点相关权限的申请！")
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) { }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    //-------------------------------------EasyPermissions----------------------

    //-------------------------------------选择图片------------------------------

    /**
     * 选择图片
     * */
    @AfterPermissionGranted(PRC_PHOTO_PICKER)
    private fun choicePhotoWrapper(){
        val perms: Array<String> = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        if(EasyPermissions.hasPermissions(this, *perms)){
            val takePhotoDir = File(Proud.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Proud")
            var photoPickerIntent: Intent = BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(takePhotoDir)
                .maxChooseCount(12)
                .selectedPhotos(null)//当前已选择图片的集合
                .pauseOnScroll(false)//滚动列表时，是否暂停加载图片
                .build()
            startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO)
        }else{
            EasyPermissions.requestPermissions(
                this,
                "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照",
                PRC_PHOTO_PICKER,
                *perms
            )
        }
    }

    override fun onClickNinePhotoItem(
        sortableNinePhotoLayout: BGASortableNinePhotoLayout?,
        view: View?,
        position: Int,
        model: String?,
        models: ArrayList<String>?
    ) {
        //预览图片
        var photoPickerPreviewIntent: Intent = BGAPhotoPickerPreviewActivity.IntentBuilder(this)
            .previewPhotos(models)
            .selectedPhotos(models)
            .maxChooseCount(snpl_moment_add_photos.maxItemCount)
            .currentPosition(position)
            .isFromTakePhoto(false)
            .build()
        startActivityForResult(photoPickerPreviewIntent, RC_PHOTO_PREVIEW)
    }

    override fun onClickAddNinePhotoItem(
        sortableNinePhotoLayout: BGASortableNinePhotoLayout?,
        view: View?,
        position: Int,
        models: ArrayList<String>?
    ) {
        choicePhotoWrapper()
    }

    override fun onNinePhotoItemExchanged(
        sortableNinePhotoLayout: BGASortableNinePhotoLayout?,
        fromPosition: Int,
        toPosition: Int,
        models: ArrayList<String>?
    ) {
        showToast("你调整了图片的顺序！")
    }

    override fun onClickDeleteNinePhotoItem(
        sortableNinePhotoLayout: BGASortableNinePhotoLayout?,
        view: View?,
        position: Int,
        model: String?,
        models: ArrayList<String>?
    ) {
        snpl_moment_add_photos.removeItem(position)
    }

    //-------------------------------------选择图片------------------------------

    //-------------------------------------定位----------------------------------

    @AfterPermissionGranted(LOCATION)
    private fun getLocation(){
        val perms: Array<String> = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
        )
        if(EasyPermissions.hasPermissions(this, *perms)){
            //权限申请成功
            MapActivity.actionStartForResult(this, LOCATION_FOT_RESULT)
        }else{
            EasyPermissions.requestPermissions(
                this,
                "标记地点需要以下权限:\n\n1.定位",
                LOCATION,
                *perms
            )
        }
    }

    private fun showSingleChoiceDialog() {
        val items = arrayOf("1天内过期", "2天内过期", "3天内过期")
        val checkedIndex = 1
        QMUIDialog.CheckableDialogBuilder(this)
            .setCheckedIndex(checkedIndex)
            .addItems(items) { dialog, which ->
                dialog.dismiss()
            }
            .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show()
    }

    private fun showMultiChoiceDialog() {
        val items =
            arrayOf("选项1", "选项2", "选项3", "选项4", "选项5", "选项6")
        val builder = QMUIDialog.MultiCheckableDialogBuilder(this)
            .setCheckedItems(intArrayOf(1, 3))
            .addItems(items) { dialog, which -> }
        builder.addAction(
            "取消"
        ) { dialog, index -> dialog.dismiss() }
        builder.addAction(
            "提交"
        ) { dialog, index ->
            var result = "你选择了 "
            for (i in builder.checkedItemIndexes.indices) {
                result += "" + builder.checkedItemIndexes[i] + "; "
            }
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show()
    }


    //-------------------------------------定位----------------------------------

    companion object{

        private const val TAG = "RentalActivity"

        //申请相册权限回调标志
        private const val PRC_PHOTO_PICKER: Int = 1

        //点击选择图片，用的回调
        private const val RC_CHOOSE_PHOTO: Int = 1

        //点击预览图片，用的回调
        private const val RC_PHOTO_PREVIEW: Int = 2

        //定位权限
        private const val LOCATION: Int = 3

        //定位请求码
        private const val LOCATION_FOT_RESULT = 4

        fun actionStart(activity: Activity){
            val intent = Intent(activity, RentalActivity::class.java)
            activity.startActivity(intent)
        }

    }

}
