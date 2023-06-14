package ru.sumbul.nework.events.ui

import android.net.Uri
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.sumbul.nework.events.data.entity.Type
import ru.sumbul.nework.events.domain.EventsRepository
import ru.sumbul.nework.events.domain.model.*
import ru.sumbul.nework.util.FeedModelState
import ru.sumbul.nework.util.SingleLiveEvent
import java.io.File
import javax.inject.Inject

private val empty = EventCreate(
    id = 0,
    content = "",
    "",
    Coordinates("", ""),
    type = null,
    attachment = null,
    "", ArrayList<Int>()
)

data class PhotoModel(
    val uri: Uri?,
    val file: File?
)

private val noPhoto = PhotoModel(null, null)

@HiltViewModel
@ExperimentalCoroutinesApi
class EventsViewModel @Inject constructor(
    private val repository: EventsRepository,
) : ViewModel() {

    private val cached = repository
        .data
        .cachedIn(viewModelScope)


    val data: Flow<PagingData<EventResponse>> = cached

    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }


    private val getEvent = MutableLiveData<EventResponse>()

    fun eventLiveDataTransformed(): LiveData<EventResponse>? {
        return getEvent
    }

    fun getEventById(id: Long) = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            getEvent.value = repository.getEventById(id)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            //     repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun update() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            repository.update()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

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

    fun edit(eventCreate: EventCreate) {
        edited.value = eventCreate
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
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

    fun removeById(id: Long) = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.removeById(id)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(removeError = true)
        }
    }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }

    fun deletePhoto() {
        _photo.value = noPhoto
    }

}