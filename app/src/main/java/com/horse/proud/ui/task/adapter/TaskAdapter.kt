package com.horse.proud.ui.task.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bingoogolapple.baseadapter.BGARecyclerViewAdapter
import cn.bingoogolapple.baseadapter.BGARecyclerViewHolder
import cn.bingoogolapple.baseadapter.BGAViewHolderHelper
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.horse.core.proud.extension.logWarn
import com.horse.core.proud.extension.showToast
import com.horse.proud.R
import com.horse.proud.data.model.other.CommentItem
import com.horse.proud.data.model.task.TaskItem
import com.horse.proud.ui.common.MapActivity
import com.horse.proud.ui.task.TaskFragment
import com.horse.proud.widget.SeeMoreView
import kotlinx.android.synthetic.main.item_task.view.*

/**
 * @author liliyuan
 * @since 2020年4月30日19:00:15
 * */
class TaskAdapter(private val taskFragment:TaskFragment, private var recyclerView: RecyclerView?) : BGARecyclerViewAdapter<TaskItem>(recyclerView, R.layout.item_task) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BGARecyclerViewHolder {
        mInflater = LayoutInflater.from(taskFragment.context)
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun fillData(helper: BGAViewHolderHelper, position: Int, item: TaskItem) {

        Glide.with(taskFragment.requireContext()).load(R.drawable.avatar_default)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(helper.getImageView(R.id.avatar));

        if(!item.title.isNullOrEmpty()){
            helper.setText(R.id.text, item.title)
        }

        val done = helper.getTextView(R.id.done)
        if(item.done == 0){
            done.text = "待领取"
            done.setTextColor(Color.parseColor("#F4606C"))
            if(!item.endTime.isNullOrEmpty()){
                helper.getTextView(R.id.end).text = item.endTime
            }
        }else{
            done.text = "已领取"
            done.setTextColor(Color.parseColor("#19CAAD"))
        }

        if(!item.startTime.isNullOrEmpty()){
            helper.getTextView(R.id.publish_time).text = item.startTime
        }

        if(!item.content.isNullOrEmpty()){
            helper.getView<SeeMoreView>(R.id.seemore).setText(item.content)
        }

        if(!item.image.isNullOrEmpty()){
            val ninePhotoLayout = helper.getView<BGANinePhotoLayout>(R.id.npl_item_moment_photos)
            ninePhotoLayout.setDelegate(taskFragment)
            val photos = ArrayList<String>()
            photos.add(item.image)
            ninePhotoLayout.data = photos
        }

        helper.getImageView(R.id.iv_local).setOnClickListener {
            if(item.location.isNullOrEmpty()){
                showToast("该任务未标记地点")
            }else{
                MapActivity.actionStartForResult(taskFragment.activity,1)
            }
        }

        helper.getTextView(R.id.tv_comment).text = "${item.comment}"

        helper.getTextView(R.id.tv_like).text = "${item.thumbUp}"

        helper.getView<CheckBox>(R.id.iv_like).setOnClickListener {
            if(it.iv_like.isChecked){
                showToast("点赞成功")
            }else{
                showToast("取消点赞")
            }
        }

        /*
        * 嵌套类型对应的 RecyclerView
        * */
        if(!item.label.isNullOrEmpty()){
            var types:List<String> = item.label.split(",")
            types -= ""
            if(types.isNotEmpty()){
                var rvType:RecyclerView = helper.getView(R.id.rv_type)
                rvType.setHasFixedSize(true)
                var linearLayoutManager = LinearLayoutManager(taskFragment.context)
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                rvType.layoutManager = linearLayoutManager
                rvType.adapter = TypeAdapter(types)
            }
        }

        /*
        * 嵌套评论对应的 RecyclerView
        * */
        var rv_comment:RecyclerView = helper.getView(R.id.rv_comment)
        rv_comment.setHasFixedSize(true)
        rv_comment.layoutManager = LinearLayoutManager(taskFragment.context)
        var comments = ArrayList<CommentItem>()
        var comment = CommentItem()
        comment.name = "会飞的鱼"
        comment.content = "评论111111111111111111111"
        comments.add(comment)
        var comment2 = CommentItem()
        comment2.name = "会飞的鱼"
        comment2.content = "评论222222222222222222222222222"
        comments.add(comment2)
        rv_comment.adapter = CommentAdapter(comments)
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
            holder.comment_name.text = items[position].name
            holder.comment_content.text = items[position].content
        }

        internal class CommentItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            var comment_name: TextView = itemView.findViewById(R.id.comment_name)
            var comment_content: TextView = itemView.findViewById(R.id.comment_content)
        }

    }

    companion object{

        lateinit var mInflater:LayoutInflater

        private const val TAG = "TaskAdapter"

    }

}