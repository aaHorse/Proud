package com.horse.proud.ui.task.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter
import cn.bingoogolapple.baseadapter.BGARecyclerViewHolder
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.amap.api.mapcore.util.it
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.horse.core.proud.Const
import com.horse.core.proud.Proud
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.core.proud.util.GlobalUtil
import com.horse.proud.R
import com.horse.core.proud.model.other.CommentItem
import com.horse.core.proud.model.task.TaskItem
import com.horse.proud.event.*
import com.horse.proud.ui.common.ViewLocationActivity
import com.horse.proud.ui.setting.OverViewPublishedActivity
import com.horse.proud.ui.task.TaskActivity
import com.horse.proud.ui.task.TaskFragment
import com.horse.proud.util.DateUtil
import com.horse.proud.widget.SeeMoreView
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import kotlinx.android.synthetic.main.item_task.view.*
import org.greenrobot.eventbus.EventBus

/**
 * @author liliyuan
 * @since 2020年4月30日19:00:15
 * */
class TaskAdapter(private val fragment:TaskFragment, private var recyclerView: RecyclerView?) : BGARecyclerViewAdapter<TaskItem>(recyclerView, R.layout.item_task) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BGARecyclerViewHolder {
        mInflater = LayoutInflater.from(fragment.context)
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun fillData(helper: BGAViewHolderHelper, position: Int, item: TaskItem) {

        var adapter:CommentAdapter ?= null

        helper.getView<LinearLayout>(R.id.ll).setOnClickListener {
            val event = ClickEvent()
            EventBus.getDefault().post(event)
        }

        Glide.with(fragment.requireContext()).load(R.mipmap.icon)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(helper.getImageView(R.id.avatar))

        helper.getImageView(R.id.avatar).setOnClickListener {
            if(fragment.activity.flag == 0){
                OverViewPublishedActivity.actionStart(fragment.activity,item.userId)
            }
        }

        if(item.title.isNotEmpty()){
            helper.setText(R.id.text, item.title)
        }else{
            helper.setText(R.id.text, " ")
        }

        val done = helper.getTextView(R.id.done)
        if(item.done == 0){
            done.text = "待领取"
            done.setTextColor(Color.parseColor("#F4606C"))
            if(item.endTime.isNotEmpty()){
                helper.getTextView(R.id.end).text = item.endTime
            }
        }else{
            done.text = "已解决"
            done.setTextColor(Color.parseColor("#19CAAD"))
        }

        if(fragment.activity.flag!=0&&fragment.activity.userID == Proud.register.id){
            with(helper.getImageView(R.id.more)){
                visibility = View.VISIBLE
                setOnClickListener {
                    val items = arrayOf(
                        GlobalUtil.getString(R.string.edit_personal),
                        GlobalUtil.getString(R.string.delete_personal))
                    QMUIDialog.MenuDialogBuilder(fragment.context)
                        .addItems(items) { dialog, which ->
                            dialog.dismiss()
                            when(which){
                                0 -> {
                                    showToast("编辑")
                                    TaskActivity.actionStart(fragment.activity,item)
                                }
                                1 -> {
                                    showToast("删除")
                                    val event = DeleteEvent()
                                    event.id = item.id
                                    event.category = Const.Like.TASK
                                    EventBus.getDefault().post(event)
                                }
                            }
                        }.create(R.style.MenuDialog).show()
                }
            }
        }

        if(item.startTime.isNotEmpty()){
            helper.getTextView(R.id.publish_time).text = item.startTime
        }else{
            helper.getTextView(R.id.publish_time).text = "  "
        }

        if(item.content.isNotEmpty()){
            helper.getView<SeeMoreView>(R.id.seemore).setText(item.content)
        }else{
            helper.getView<SeeMoreView>(R.id.seemore).setText("  ")
        }

        val ninePhotoLayout = helper.getView<BGANinePhotoLayout>(R.id.npl_item_moment_photos)
        if(item.image != null && item.image.isNotEmpty()){
            ninePhotoLayout.setDelegate(fragment)
            ninePhotoLayout.data = item.images
            ninePhotoLayout.visibility = View.VISIBLE
        }else{
            ninePhotoLayout.visibility = View.GONE
        }


        helper.getView<LinearLayout>(R.id.ll_local).setOnClickListener {
            if(item.location.isEmpty()){
                showToast("该任务未标记地点")
            }else{
                logWarn(TAG,item.location)
                var locations = item.location.split(",")
                locations -= ""
                if(locations.size == 2){
                    val latitude:Double = locations[0].toDouble()
                    val longitude:Double = locations[1].toDouble()
                    ViewLocationActivity.actionStartForResult(latitude,longitude,fragment.activity,1)
                }else{
                    showToast("该地点暂时无法查看")
                }
            }
        }

        helper.getTextView(R.id.tv_comment).text = "${item.comment}"

        helper.getTextView(R.id.tv_like).text = "${item.thumbUp}"

        helper.getView<CheckBox>(R.id.iv_like).setOnClickListener {
            if(it.iv_like.isChecked){
                showToast("！ ！ ~ 赞 ~ ！ ！")
                helper.getTextView(R.id.tv_like).text = "${++item.thumbUp}"
                val event = LikeEvent()
                event.category = Const.Like.TASK
                event.id = item.id
                EventBus.getDefault().post(event)
            }else{
                helper.getTextView(R.id.tv_like).text = "${--item.thumbUp}"
                showToast("取消点赞")
            }
        }

        /*
        * 嵌套类型对应的 RecyclerView
        * */
        val rvType:RecyclerView = helper.getView(R.id.rv_type)
        if(item.label.isNotEmpty()){
            logWarn(TAG,item.label)
            val types: MutableList<String> = item.label.split(",").toMutableList()
            types -= ""
            rvType.setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(fragment.context)
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            rvType.layoutManager = linearLayoutManager
            rvType.adapter = TypeAdapter(types)
            rvType.visibility = View.VISIBLE
        }else{
            rvType.visibility = View.GONE
        }

        /*
        * 嵌套评论对应的 RecyclerView
        * */
        val rvComment:RecyclerView = helper.getView(R.id.rv_comment)
        if(item.comments != null){
            rvComment.setHasFixedSize(true)
            rvComment.layoutManager = LinearLayoutManager(fragment.context)
            if(item.comments!!.commentList == null){
                item.comments!!.commentList = ArrayList()
            }
            adapter = CommentAdapter(item.comments!!.commentList!!)
            rvComment.adapter = adapter
            helper.getTextView(R.id.tv_comment).text = "${item.comments!!.commentList!!.size}"
            rvComment.visibility = View.VISIBLE
        }else{
            helper.getTextView(R.id.tv_comment).text = "0"
            rvComment.visibility = View.GONE
        }

        helper.getView<Button>(R.id.send).setOnClickListener {
            if(Proud.loginState != Const.Auth.COMFIR){
                showToast(GlobalUtil.getString(R.string.visitor_reminder))
            }else{
                val content:String = helper.getView<EditText>(R.id.et_comment).text.toString()
                if (!content.isBlank()){
                    val comment = CommentItem()
                    comment.userId = Proud.register.id
                    comment.content = content
                    comment.time = DateUtil.nowDateTime
                    comment.itemId = item.id
                    comment.name = Proud.register.name

                    val event = CommentEvent()
                    event.category = Const.Like.TASK
                    event.comment = comment
                    EventBus.getDefault().post(event)

                    item.comments!!.commentList!!.add(comment)
                    if(adapter!=null){
                        helper.getView<EditText>(R.id.et_comment).setText("")
                        notifyItemChanged(position)
                    }
                }else{
                    showToast("评论不能为空")
                }
            }
        }
    }

    private class TypeAdapter(items:List<String>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        var items:List<String> = items

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = mInflater.inflate(R.layout.item_type,parent,false)
            return TypeItemViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder as TypeItemViewHolder
            holder.textView.text = items[position]
        }

        internal class TypeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var textView: TextView = itemView.findViewById(R.id.tv_type)
        }

    }

    private class CommentAdapter(items: ArrayList<CommentItem>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        var items: ArrayList<CommentItem> = items

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = mInflater.inflate(R.layout.item_comment,parent,false)
            return CommentItemViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder as CommentItemViewHolder
            holder.comment_name.text = "${items[position].name}"
            //holder.comment_name.text = "${items[position].userId}"
            holder.comment_content.text = items[position].content
            holder.comment_name.setOnClickListener {
                val event = CommentToOverViewEvent()
                event.category = Const.Like.TASK
                event.userId = items[position].userId
                EventBus.getDefault().post(event)
            }
            holder.linearLayout.setOnClickListener {
                val event = ClickEvent()
                EventBus.getDefault().post(event)
            }
        }

        internal class CommentItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            var comment_name: TextView = itemView.findViewById(R.id.comment_name)
            var comment_content: TextView = itemView.findViewById(R.id.comment_content)
            val linearLayout:LinearLayout = itemView.findViewById(R.id.comment_ll)
        }

    }

    companion object{

        lateinit var mInflater:LayoutInflater

        private const val TAG = "TaskAdapter"

    }

}