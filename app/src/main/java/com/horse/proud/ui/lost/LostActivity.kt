package com.horse.proud.ui.lost

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.horse.core.proud.Const
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.proud.R
import com.horse.proud.callback.LoadDataListener
import com.horse.core.proud.model.lost.LostItem
import com.horse.proud.databinding.ActivityLostBinding
import com.horse.proud.event.FinishActivityEvent
import com.horse.proud.event.LikeEvent
import com.horse.proud.event.MessageEvent
import com.horse.proud.ui.common.BaseActivity
import com.horse.proud.ui.common.MapActivity
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import kotlinx.android.synthetic.main.activity_lost.*
import kotlinx.android.synthetic.main.activity_lost.avatar
import kotlinx.android.synthetic.main.activity_lost.iv_local
import kotlinx.android.synthetic.main.activity_lost.iv_time
import kotlinx.android.synthetic.main.activity_lost.iv_type
import kotlinx.android.synthetic.main.activity_lost.ll_local
import kotlinx.android.synthetic.main.activity_lost.ll_time
import kotlinx.android.synthetic.main.activity_lost.ll_type
import kotlinx.android.synthetic.main.activity_lost.snpl_moment_add_photos
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.ArrayList

/**
 * @author liliyuan
 * @since 2020年5月2日12:27:31
 * */
class LostActivity : BaseActivity(), LoadDataListener, EasyPermissions.PermissionCallbacks,
    BGASortableNinePhotoLayout.Delegate {

    /**
     * 区分当前页面的状态
     * 0：新增
     * 1：编辑
     * */
    var flag = 0

    /**
     * 默认被选中的类型，编辑
     * */
    private var selectedTypes = ArrayList<Int>()

    /**
     * 默认被选中的时间，编辑
     * */
    private var selectedTime  = 0

    /**
     * 编辑状态，页面持有的信息对象
     * */
    lateinit var item: LostItem

    private val viewModelFactory by inject<LostActivityViewModelFactory>()

    val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(LostActivityViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN or
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )

        flag = intent.getIntExtra(Const.ACTIVITY_FLAG,0)
        if (flag!=0){
            item = intent.getParcelableExtra(Const.ACTIVITY_CONTENT)!!
        }

        val binding = DataBindingUtil.setContentView<ActivityLostBinding>(this,R.layout.activity_lost)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun setupViews() {
        setupToolbar()
        // 设置拖拽排序控件的代理
        snpl_moment_add_photos.setDelegate(this)
        setOnClickListener()
        Glide.with(this).load(R.drawable.avatar_default)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(avatar)
        if(flag != 0){
            initEditContent()
        }
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
                if(content.text.isNotEmpty()){
                    viewModel.publish()
                }else{
                    showToast("请添加描述信息")
                }
            }
            else->{
                showToast("任务未发布")
                finish()
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode ==Activity.RESULT_OK){
            when(requestCode){
                RC_CHOOSE_PHOTO ->{
                    snpl_moment_add_photos.addMoreData(BGAPhotoPickerActivity.getSelectedPhotos(data))
                    viewModel.imagePath = BGAPhotoPickerActivity.getSelectedPhotos(data)[0]
                }
                RC_PHOTO_PREVIEW ->{
                    snpl_moment_add_photos.data = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data)
                }
                LOCATION_FOT_RESULT ->{
                    iv_local.setImageResource(R.drawable.local_click)
                    var latitude = data?.getDoubleExtra(Const.Item.POSITION_LATITUDE,-1.0)
                    var longitude = data?.getDoubleExtra(Const.Item.POSITION_LONGITUTE,-1.0)
                    if(latitude!=-1.0&&longitude!=-1.0){
                        viewModel.local = "$latitude,$longitude"
                    }
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

    private fun setOnClickListener(){
        ll_local.setOnClickListener {
            getLocation()
        }

        ll_type.setOnClickListener {
            getType()
        }

        ll_time.setOnClickListener {
            getTime()
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
            val takePhotoDir = File(Proud.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Proud")
            var photoPickerIntent:Intent = BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(takePhotoDir)
                .maxChooseCount(1)
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
        var photoPickerPreviewIntent:Intent = BGAPhotoPickerPreviewActivity.IntentBuilder(this)
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

    private fun getTime() {
        val items = Const.TIME
        val checkedIndex = 1
        QMUIDialog.CheckableDialogBuilder(this)
            .setCheckedIndex(selectedTime)
            .addItems(items) { dialog, which ->
                dialog.dismiss()
                viewModel.time = items[which]
                iv_time.setImageResource(R.drawable.time_click)
            }
            .create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show()
    }

    private fun getType() {
        val items = Const.LOST_AND_FOUND_TYPE
        val builder = QMUIDialog.MultiCheckableDialogBuilder(this)
            .addItems(items) { dialog, which -> }
        builder.addAction("取消") { dialog, index -> dialog.dismiss() }
        builder.addAction("提交") { dialog, index ->
            var result = ""
            for (i in builder.checkedItemIndexes.indices) {
                result += "" + items[i] + ","
            }
            dialog.dismiss()
            viewModel.type = result
            if(result.isNotEmpty()){
                iv_type.setImageResource(R.drawable.type_click)
            }
        }
        builder.create(com.qmuiteam.qmui.R.style.QMUI_Dialog).show()
    }

    /**
     * 编辑任务，初始化已编辑的信息
     * */
    private fun initEditContent(){
        viewModel.content.value = item.content
        item.image.run {
            if(this.isNotEmpty()){
                val selected = ArrayList<String>()
                selected.add(item.image)
                snpl_moment_add_photos.data = selected
                logWarn(TAG,selected[0])
            }
        }
        item.location.run {
            if(this.isNotEmpty()){
                iv_local.setImageResource(R.drawable.local_click)
            }
        }
        item.label.run {
            if(this.isNotEmpty()){
                iv_type.setImageResource(R.drawable.type_click)
                val array = this.split(",").toMutableList()
                array -= ""
                for((index,value) in Const.TASK_TYPE.withIndex()){
                    array.forEach {
                        if(it == value){
                            selectedTypes.add(index)
                        }
                    }
                }
            }
        }
        item.time.run {
            if(this.isNotEmpty()){
                if(this.isNotEmpty()){
                    iv_time.setImageResource(R.drawable.time_click)
                    for((index,value) in Const.TIME.withIndex()){
                        if(this == value){
                            selectedTime = index
                        }
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(messageEvent: MessageEvent) {
        if (messageEvent is FinishActivityEvent && messageEvent.category == Const.Like.LOST) {
            finish()
        }
    }


    //-------------------------------------定位----------------------------------

    companion object{

        private const val TAG = "LostActivity"

        //申请相册权限回调标志
        private const val PRC_PHOTO_PICKER: Int = 0

        //点击选择图片，用的回调
        private const val RC_CHOOSE_PHOTO: Int = 1

        //点击预览图片，用的回调
        private const val RC_PHOTO_PREVIEW: Int = 2

        //定位权限
        private const val LOCATION: Int = 3

        //定位请求码
        private const val LOCATION_FOT_RESULT = 4

        /**
         * 新增
         * */
        fun actionStart(activity: Activity){
            val intent = Intent(activity, LostActivity::class.java)
            activity.startActivity(intent)
        }

        /**
         * 编辑
         * */
        fun actionStart(activity:Activity,item: LostItem){
            val intent = Intent(activity,LostActivity::class.java)
            intent.putExtra(Const.ACTIVITY_FLAG,1)
            intent.putExtra(Const.ACTIVITY_CONTENT,item)
            activity.startActivity(intent)
        }

    }

}
