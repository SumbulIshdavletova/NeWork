package ru.sumbul.nework.user_page.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.nework.databinding.PostCardBinding
import ru.sumbul.nework.user_page.domain.model.WallPosts
import ru.sumbul.nework.util.load


interface WallPostsOnInteractionListener {
    fun onClick(post: WallPosts) {}
}

class WallPostsAdapter(
    private val onInteractionListener: WallPostsOnInteractionListener,
) : ListAdapter<WallPosts,
        WallPostsViewHolder>(WallPostsDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallPostsViewHolder {
        val binding =
            PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WallPostsViewHolder(onInteractionListener, binding)
    }

    override fun onBindViewHolder(holder: WallPostsViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }


}

class WallPostsViewHolder(
    private val onInteractionListener: WallPostsOnInteractionListener,
    val binding: PostCardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: WallPosts) {
        binding.apply {

            post.authorAvatar?.let { binding.avatar.load(it) }
            post.attachment?.url?.let { binding.attachment.load(it) }
            //TODO FIX ATTACHMENT CUZ IT IS ALSO CAN BE VIDEO OR AUDIO LOL

            binding.author.text = post.author
            binding.published.text = post.published
            binding.content.text = post.content


            itemView.setOnClickListener {
                onInteractionListener.onClick(post)
            }
            //TODO MENU
        }
    }
}


class WallPostsDiffCallback : DiffUtil.ItemCallback<WallPosts>() {
    override fun areItemsTheSame(oldItem: WallPosts, newItem: WallPosts): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WallPosts, newItem: WallPosts): Boolean {
        return oldItem == newItem
    }
}
