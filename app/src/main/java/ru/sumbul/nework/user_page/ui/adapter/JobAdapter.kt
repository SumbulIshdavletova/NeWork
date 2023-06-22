package ru.sumbul.nework.user_page.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.sumbul.nework.R
import ru.sumbul.nework.databinding.JobCardBinding
import ru.sumbul.nework.user_page.domain.model.Job
import ru.sumbul.nework.user_page.domain.model.WallPosts


interface JobOnInteractionListener {
    fun onClick(job: Job) {}
    fun onEdit(job: Job) {}
    fun onRemove(job: Job) {}
}

class JobAdapter(
    private val onInteractionListener: JobOnInteractionListener,
) : ListAdapter<Job,
        JobViewHolder>(JobDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val binding =
            JobCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(onInteractionListener, binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = getItem(position)
        holder.bind(job)
    }


}

class JobViewHolder(
    private val onInteractionListener: JobOnInteractionListener,
    val binding: JobCardBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(job: Job) {
        binding.apply {
            binding.position.text = job.position
            binding.name.text = job.name
            binding.start.text = job.start
            binding.finish.text = job.finish
            binding.link.text = job.link

            itemView.setOnClickListener {
                onInteractionListener.onClick(job)
            }

            binding.menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(job)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(job)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}


class JobDiffCallback : DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem == newItem
    }
}
