package com.pp.nasaapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pp.nasaapp.network.APODResponse
import com.pp.nasaapp.network.Repository
import com.pp.nasaapp.network.ResultResponse
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    val isCalendarDisplayed = MutableLiveData<Boolean>()

    init {
        isCalendarDisplayed.value = false
    }

    fun onDisplayDatePickerClick() {
        isCalendarDisplayed.value = true
    }
    fun getPictures(): LiveData<ResultResponse<ArrayList<APODResponse>?>> {
        val locaData= MutableLiveData<ResultResponse<ArrayList<APODResponse>?>>()
        viewModelScope.launch {
            repository.getPictures().apply {
                locaData.value=this
            }
        }
        return locaData
    }

    fun getPictureOftheDay(date: String): LiveData<ResultResponse<APODResponse?>> {
        var locaData= MutableLiveData<ResultResponse<APODResponse?>>()
        viewModelScope.launch {
            repository.getPictureOfTheDay(date).apply {
                locaData.value=this
            }
        }
        return locaData
    }
}