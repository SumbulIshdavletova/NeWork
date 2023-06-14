package ru.sumbul.nework.posts.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.sumbul.nework.events.domain.model.EventResponse
import ru.sumbul.nework.posts.domain.PostRepository
import ru.sumbul.nework.posts.domain.model.PostResponse
import ru.sumbul.nework.util.FeedModelState
import javax.inject.Inject


@HiltViewModel
@ExperimentalCoroutinesApi
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
) : ViewModel() {


    private val cached = repository
        .data
        .cachedIn(viewModelScope)

    val data: Flow<PagingData<PostResponse>> = cached

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val getPost = MutableLiveData<PostResponse>()

    fun postLiveDataTransformed(): LiveData<PostResponse>? {
        return getPost
    }

    fun getEventById(id: Long) = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            getPost.value = repository.getPostById(id)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }
}