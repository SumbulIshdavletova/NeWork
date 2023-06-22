package ru.sumbul.nework.posts.ui

import android.net.Uri
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
import ru.sumbul.nework.posts.domain.Mapper
import ru.sumbul.nework.posts.domain.PostRepository
import ru.sumbul.nework.posts.domain.model.PostCoordinates
import ru.sumbul.nework.posts.domain.model.PostCreate
import ru.sumbul.nework.posts.domain.model.PostResponse
import ru.sumbul.nework.util.FeedModelState
import ru.sumbul.nework.util.SingleLiveEvent
import java.io.File
import javax.inject.Inject

private val empty = PostCreate(
    id = 0,
    content = "",
    coords = PostCoordinates("127.024612", "127.047325"),
    link = null,
    attachment = null,
    mentionIds = emptyList()
)

data class PhotoModel(
    val uri: Uri?,
    val file: File?
)

private val noPhoto = PhotoModel(null, null)


@HiltViewModel
@ExperimentalCoroutinesApi
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    private val mapper: Mapper,
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

    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    fun save() {
        edited.value?.let {
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    when (_photo.value) {
                        noPhoto -> repository.save(it)
                        else -> _photo.value?.file?.let { file ->
                            repository.saveWithAttachment(it, file)
                        }
                    }

                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(saveError = true)
                }
            }
        }
        edited.value = empty
        _photo.value = noPhoto
    }

    fun edit(post: PostResponse) {
        edited.value = mapper.mapResponseToCreate(post)
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }

    fun deletePhoto() {
        _photo.value = noPhoto
    }

    fun removeById(id: Long) = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.removeById(id)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(removeError = true)
        }
    }



    fun likeById(id: Long) = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.likeById(id)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(likeError = true)
        }
    }

    fun unlikeById(id: Long) = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.unlikeById(id)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(likeError = true)
        }
    }

}