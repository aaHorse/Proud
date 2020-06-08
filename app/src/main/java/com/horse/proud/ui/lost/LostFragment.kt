package com.horse.proud.ui.lost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.horse.core.proud.Const
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.model.lost.LostItem
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.callback.LoadDataListener
import com.horse.proud.databinding.FragmentLostBindingImpl
import com.horse.proud.event.*
import com.horse.proud.ui.common.BaseItemsFragment
import com.horse.proud.ui.home.MainActivity
import com.horse.proud.ui.lost.adapter.LostAdapter
import com.horse.proud.ui.setting.OverViewPublishedActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject

/**
 * 失物招领列表界面
 *
 * @author liliyuan
 * @since 2020年4月26日15:26:22
 * */
class LostFragment : BaseItemsFragment(),LoadDataListener, BGANinePhotoLayout.Delegate {

    lateinit var lostFragment: LostFragment

    val viewModelFactory by inject<LostFragmentViewModelFactory>()

    val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(LostFragmentViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_lost, container, false)
        super.onCreateView(view)
        val binding = DataBindingUtil.bind<FragmentLostBindingImpl>(view)
        binding?.viewModel = viewModel
        initViews(view)
        lostFragment = activity.lostFragment!!
        observe()
        EventBus.getDefault().register(this)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    override fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(activity)
        adapter = LostAdapter(lostFragment , recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun dataSetSize(): Int {
        return viewModel.lostItems.size
    }

    /**
     * 正常加载用
     * */
    override fun onLoad() {
        if (!isLoadingMore && !isNoMoreData) {
            startLoading()
            viewModel.getLost(activity.flag,activity.userID)
        }
    }

    /**
     * 刷新用
     * */
    override fun refresh() {
        if(!isLoadingMore){
            startLoading()
            viewModel.page = 1
            viewModel.lostItems.clear()
            viewModel.getLost(activity.flag,activity.userID)
        }
    }

    override fun loadItems() {

    }

    override fun loadFinished() {
        super.loadFinished()
        adapter.data = viewModel.lostItems
        adapter.notifyDataSetChanged()
    }

    /**
     * 观察 ViewModel 中的数据变化
     * */
    private fun observe(){
        viewModel.lostItemsChanged.observe(activity, Observer {
            loadFinished()
        })

        viewModel.badNetWork.observe(activity, Observer {
            loadFailed(null)
        })

        viewModel.noContent.observe(activity, Observer {
            loadNull()
        })

        viewModel.isLoadingMore.observe(activity, Observer {
            isLoadingMore = viewModel.isLoadingMore.value!!
        })

        viewModel.isNoMoreData.observe(activity, Observer {
            isNoMoreData = viewModel.isNoMoreData.value!!
        })
    }

    override fun onClickNinePhotoItem(
        ninePhotoLayout: BGANinePhotoLayout?,
        view: View?,
        position: Int,
        model: String?,
        models: MutableList<String>?
    ) {
        var mCurrentClickNpl: BGANinePhotoLayout? = ninePhotoLayout
        var photoPreviewIntentBuilder = BGAPhotoPreviewActivity.IntentBuilder(Proud.context)
        photoPreviewIntentBuilder.previewPhotos(mCurrentClickNpl!!.data)
            .currentPosition(mCurrentClickNpl!!.currentClickItemPosition)
        startActivity(photoPreviewIntentBuilder.build())

    }

    override fun onClickExpand(
        ninePhotoLayout: BGANinePhotoLayout?,
        view: View?,
        position: Int,
        model: String?,
        models: MutableList<String>?
    ) {
        ninePhotoLayout!!.setIsExpand(true)
        ninePhotoLayout.flushItems()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(messageEvent: MessageEvent) {
        when(messageEvent){
            is LikeEvent -> {
                if(messageEvent.category == Const.Like.LOST||messageEvent.category == Const.Like.FOUND){
                    viewModel.like(messageEvent.id)
                }
            }
            is RefreshEvent -> {
                refresh()
            }
            is CommentEvent -> {
                if(messageEvent.category == Const.Like.LOST){
                    viewModel.publishComment(messageEvent.comment)
                }
            }
            is DeleteEvent -> {
                if(activity.flag != 0 && (messageEvent.category == Const.Like.LOST||messageEvent.category == Const.Like.FOUND)){
                    /*
                    * 这里是一个设计不规范的地方，传了一个json对象给后端，正确的方法应该是id参数
                    * */
                    val item = LostItem()
                    item.id = messageEvent.id
                    viewModel.delete(item)
                    /*
                    * 先更新界面
                    * */
                    adapter.data = adapter.data.filter {
                        it as LostItem
                        it.id != messageEvent.id
                    }
                    adapter.notifyDataSetChanged()
                }
            }
            is CommentToOverViewEvent -> {
                if(messageEvent.category == Const.Like.LOST||messageEvent.category == Const.Like.FOUND){
                    OverViewPublishedActivity.actionStart(activity,messageEvent.userId)
                }
            }
        }
    }

    companion object{

        private const val TAG = "LostFragment"

    }

}