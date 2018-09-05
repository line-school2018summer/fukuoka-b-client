package com.line.fukuokabclient.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.line.fukuokabclient.R
import com.line.fukuokabclient.dto.UserDTO

import kotlinx.android.synthetic.main.fragment_friends_select.view.*

/**
 * [RecyclerView.Adapter] that can display a [UserDTO] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class SelectFriendsRecyclerViewAdapter(
        private val mValues: List<UserDTO>,
        private val mListener: OnListItemSelectedInteractionListener?)
    : RecyclerView.Adapter<SelectFriendsRecyclerViewAdapter.SelectViewHolder>() {

    interface OnListItemSelectedInteractionListener {
        fun onListItemSelectedInteraction(item: UserDTO, selected: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.fragment_friends_select, parent, false)
                return SelectViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {
        val item = mValues[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = mValues.size

    inner class SelectViewHolder(val mView: View): RecyclerView.ViewHolder(mView), View.OnClickListener {
        val mIconView: ImageView = mView.user_icon_img as ImageView
        val selectImg: ImageView = mView.image_select
        val nameView: TextView = mView.txt_select_username
        var selected = false

        fun bind(user: UserDTO) {
            mIconView.setImageResource(R.drawable.default_user_icon)
            nameView.text = user.name
            with(mView) {
                tag = user
            }
            mView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val item = v?.tag as UserDTO
            selected = if (selected) {
                selectImg.animate()
                        .alpha(0f)
                        .setDuration(200)
                        .setListener(null)
                false
            } else {
                selectImg.animate()
                        .alpha(1f)
                        .setDuration(200)
                        .setListener(null)
                true
            }
            mListener?.onListItemSelectedInteraction(item, selected)
        }
    }
}
