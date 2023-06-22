package ru.sumbul.nework.user_page.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.sumbul.nework.R
import ru.sumbul.nework.databinding.PostCardBinding
import ru.sumbul.nework.posts.domain.model.PostResponse
import ru.sumbul.nework.user_page.domain.model.WallPosts
import ru.sumbul.nework.util.load


interface WallPostsOnInteractionListener {
    fun onClick(post: WallPosts) {}
    fun onEdit(post: WallPosts) {}
    fun onRemove(post: WallPosts) {}
    fun onLike(post: WallPosts) {}
    fun onDeleteLike(post: WallPosts) {}
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

            if (post.attachment?.url != null) {
                binding.attachment.isVisible = true
                val url = "https://netomedia.ru/api/media/${post.attachment.url}"
                Glide.with(binding.attachment)
                    .load(url)
                    .placeholder(R.drawable.ic_baseline_rotate_right_24)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .timeout(10_000)
                    .into(binding.attachment)
            }

            binding.menu.isVisible = post.ownedByMe
            binding.menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
            if (post.likedByMe) {
                binding.like.setOnClickListener {
                    onInteractionListener.onDeleteLike(post)
                }
            } else {
                binding.like.setOnClickListener {
                    onInteractionListener.onLike(post)
                }
            }


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
