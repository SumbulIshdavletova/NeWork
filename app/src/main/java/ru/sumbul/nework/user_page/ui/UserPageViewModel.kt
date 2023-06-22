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

    fun getJobs(id: Long) {
        viewModelScope.launch {
            repository.getJobs(id)
        }
    }

    private val myJobs: MutableLiveData<List<Job>?>? = repository.getMyJobsLiveData()

    fun getMyJobsLiveData(): MutableLiveData<List<Job>?>? {
        return myJobs
    }

    fun getMyJobs() {
        viewModelScope.launch {
            repository.getMyJobs()
        }
    }

    fun saveJob(job: Job) {
        viewModelScope.launch {
            repository.save(job)
        }
    }

    fun removeJobById(id: Long) {
        viewModelScope.launch {
            repository.deleteJobById(id)
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

    private val myWall: MutableLiveData<List<WallPosts>?>? = repository.getMyWallLiveData()

    fun getMyWallLiveData(): MutableLiveData<List<WallPosts>?>? {
        return myWall
    }
    fun getMyWall() {
        viewModelScope.launch {
            repository.getMyWall()
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