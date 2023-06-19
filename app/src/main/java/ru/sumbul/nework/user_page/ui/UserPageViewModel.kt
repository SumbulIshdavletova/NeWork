package ru.sumbul.nework.user_page.ui

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.sumbul.nework.user_page.domain.UserPageRepository
import ru.sumbul.nework.user_page.domain.model.Job
import ru.sumbul.nework.user_page.domain.model.UserResponse
import ru.sumbul.nework.user_page.domain.model.WallPosts
import javax.inject.Inject


@HiltViewModel
@ExperimentalCoroutinesApi
class UserPageViewModel @Inject constructor(
    private val repository: UserPageRepository
) : ViewModel() {

    private val jobs: MutableLiveData<List<Job>?>? = repository.getJobs()

    fun getJobs(): MutableLiveData<List<Job>?>? {
        return jobs
    }

    //    private val _dataState = MutableLiveData<ListModelState>()
//    val dataState: LiveData<ListModelState>
//        get() = _dataState
//
    fun getJobs(id: Long) {
        viewModelScope.launch {
            repository.getJobs(id)
        }
    }



    private val wall: MutableLiveData<List<WallPosts>?>? = repository.getAllUsersWall()

    fun getWall(): MutableLiveData<List<WallPosts>?>? {
        return wall
    }

    //    private val _dataState = MutableLiveData<ListModelState>()
//    val dataState: LiveData<ListModelState>
//        get() = _dataState
//
    fun getWall(id: Long) {
        viewModelScope.launch {
            repository.getAllWall(id)
        }
    }



    private val getUser = MutableLiveData<UserResponse>()

    fun userLiveDataTransformed(): LiveData<UserResponse>? {
        return getUser
    }

    fun getUserById(id: Long) {
        //_dataState.value = ListModelState(loading = true)
        viewModelScope.launch {
            getUser.value = repository.getUser(id)
        }
    }

}