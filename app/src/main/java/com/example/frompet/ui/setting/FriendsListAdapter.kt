// FriendsListAdapter.kt

package com.example.frompet.ui.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frompet.R
import com.example.frompet.data.model.User
import com.example.frompet.databinding.ItemChatlistBinding
import com.example.frompet.databinding.ItemFriendsBinding
import com.example.frompet.ui.chat.adapter.ChatListAdapter

class FriendsListAdapter(private val context: Context) :
    ListAdapter<User, FriendsListAdapter.ViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val binding = ItemFriendsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        val user = getItem(position)
        holder.bindItems(user)
    }

    class ViewHolder(private val binding: ItemFriendsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItems(user: User) {
            binding.apply {
                tvUserName3.text = user.petName
                tvUserType3.text = user.petType
                Glide.with(root.context).load(user.petProfile).into(binding.profileArea3)

            }
        }
    }
    class DiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

}
