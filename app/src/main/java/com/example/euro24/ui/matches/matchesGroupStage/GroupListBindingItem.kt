package com.example.euro24.ui.matches.matchesGroupStage

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.euro24.R
import com.example.euro24.data.groups.Group
import com.mikepenz.fastadapter.items.AbstractItem

class GroupListBindingItem(val group: Group) : AbstractItem<GroupListBindingItem.ViewHolder>() {

    override val type: Int
        get() = R.id.fastadapter_id

    override val layoutRes: Int
        get() = R.layout.rv_group_list_item

    override fun bindView(holder: ViewHolder, payloads: List<Any>) {
        super.bindView(holder, payloads)

        holder.groupName.text = group.groupName
    }

    override fun unbindView(holder: ViewHolder) {
        super.unbindView(holder)

        holder.groupName.text = null
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var groupName: TextView = view.findViewById(R.id.text_rv_group_name)
    }
}