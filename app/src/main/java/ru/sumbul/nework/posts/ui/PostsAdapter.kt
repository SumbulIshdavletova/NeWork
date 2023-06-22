package ru.sumbul.nework.posts.ui

import android.system.Os.remove
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.sumbul.nework.R
import ru.sumbul.nework.databinding.PostCardBinding
import ru.sumbul.nework.posts.domain.model.PostResponse
import ru.sumbul.nework.util.load

interface PostOnInteractionListener {
    fun onClick(post: PostResponse) {}
    fun onAuthor(post: PostResponse) {}
    fun onLike(post: PostResponse) {}
    fun onEdit(post: PostResponse) {}
    fun onRemove(post: PostResponse) {}
    fun onDeleteLike(post: PostResponse)

}

class PostsAdapter(
    private val onInteractionListener: PostOnInteractionListener
) : PagingDataAdapter<PostResponse, RecyclerView.ViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(onInteractionListener, binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            (holder as PostViewHolder).bind(item)
        }
    }
}

class PostViewHolder(
    private val onInteractionListener: PostOnInteractionListener,
    private val binding: PostCardBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: PostResponse) {

        post.authorAvatar?.let { binding.avatar.load(it) }
        post.attachment?.url?.let { binding.attachment.load(it) }
        //TODO FIX ATTACHMENT CUZ IT IS ALSO CAN BE VIDEO OR AUDIO LOL

        binding.author.text = post.author
        binding.published.text = post.published
        binding.content.text = post.content


        itemView.setOnClickListener {
            onInteractionListener.onClick(post)
        }

        binding.author.setOnClickListener {
            onInteractionListener.onAuthor(post)
        }

        binding.avatar.setOnClickListener {
            onInteractionListener.onAuthor(post)
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

        post.attachment?.url?.let { binding.attachment.load(it) }

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

class PostDiffCallback : DiffUtil.ItemCallback<PostResponse>() {
    override fun areItemsTheSame(
        oldItem: PostResponse,
        newItem: PostResponse
    ): Boolean {
        if (oldItem::class != newItem::class) {
            return false
        }
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PostResponse,
        newItem: PostResponse
    ): Boolean {
        return oldItem == newItem
    }
}