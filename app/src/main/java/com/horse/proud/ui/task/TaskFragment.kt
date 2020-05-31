package com.horse.proud.ui.task

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
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.proud.callback.LoadDataListener
import com.horse.proud.databinding.FragmentTaskBindingImpl
import com.horse.proud.event.*
import com.horse.proud.ui.common.BaseItemsFragment
import com.horse.proud.ui.home.MainActivity
import com.horse.proud.ui.task.adapter.TaskAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_task.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject

/**
 * 任务列表界面
 *
 * @author liliyuan
 * @since 2020年4月25日05:21:30
 * */
class TaskFragment : BaseItemsFragment(),LoadDataListener, BGANinePhotoLayout.Delegate {

    lateinit var taskFragment: TaskFragment

    private val viewModelFactory by inject<TaskFragmentViewModelFactory>()

    val viewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(TaskFragmentViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)
        super.onCreateView(view)
        val binding = DataBindingUtil.bind<FragmentTaskBindingImpl>(view)
        binding?.viewModel = viewModel
        initViews(view)
        taskFragment = activity.taskFragment!!
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
        adapter = TaskAdapter(taskFragment, recyclerView)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    override fun loadItems() {
        viewModel.getTask()
    }

    override fun refresh() {
        viewModel.getTask()
    }

    override fun dataSetSize(): Int {
        return viewModel.taskItems.size
    }

    override fun onLoad() {
        if (!isLoadingMore) {
            if (viewModel.taskItems.isNotEmpty()) {
                isLoadingMore = true
                isLoadFailed = false
                isNoMoreData = false
                loadItems()
            }
        }
    }

    override fun loadFinished() {
        super.loadFinished()
        if (viewModel.taskItems.isEmpty()) {
            recyclerView.visibility = View.GONE
            showNoContentViewWithButton(GlobalUtil.getString(R.string.app_name),
                GlobalUtil.getString(R.string.app_name),
                View.OnClickListener { MainActivity.actionStart(activity) })
        } else {
            hideNoContentView()
        }
    }

    /**
     * 观察 ViewModel 中的数据变化
     * */
    private fun observe(){
        viewModel.taskItemsChanged.observe(activity, Observer {
            if(viewModel.taskItems.isEmpty()){
                recyclerView.visibility = View.GONE
                showNoContentViewWithButton(
                    GlobalUtil.getString(R.string.items_null),
                    GlobalUtil.getString(R.string.items_null_click),
                    View.OnClickListener { refresh() })
            }else{
                loadFinished()
                hideNoContentView()
                adapter.data = viewModel.taskItems
                adapter.notifyDataSetChanged()
                scrollToTop()
            }
        })

        viewModel.isLoadingMore.observe(activity, Observer {
            isLoadingMore = viewModel.isLoadingMore.value!!
        })

        viewModel.loadFailed.observe(activity, Observer {
            loadFailed(null)
        })

        viewModel.isNoMoreData.observe(activity, Observer {
            logWarn(TAG,"被修改了")
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
        var photoPreviewIntentBuilder = BGAPhotoPreviewActivity.IntentBuilder(Proud.getContext())
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
                if(messageEvent.category == Const.Like.TASK){
                    viewModel.like(messageEvent.id)
                }
            }
            is RefreshEvent -> {
                refresh()
            }
            is CommentEvent -> {
                if(messageEvent.category == Const.Like.TASK){
                    viewModel.publishComment(messageEvent.comment)
                }
            }
        }
    }

    companion object{

        private const val TAG = "TaskFragment"

    }

}