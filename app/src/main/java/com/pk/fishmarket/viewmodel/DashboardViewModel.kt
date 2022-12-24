package com.pk.fishmarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pk.fishmarket.ResponseModel.ShopListDashboardModel
import com.pk.fishmarket.Utils.Event
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.repository.AppRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class DashboardViewModel(val appRepository: AppRepository) : ViewModel(){
    private val _userResponse = MutableLiveData<Event<Resource<Response<ShopListDashboardModel>>>>()

    val response : LiveData<Event<Resource<Response<ShopListDashboardModel>>>> = _userResponse
    private val disposable = CompositeDisposable()

    fun getDasboardResponse(userid:String,lat:String,long:String) = getDasboardData(userid,lat,long)

    fun getDasboardData(userid:String,lat:String,long:String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.getDashboardData(userid,lat,long).subscribeOn(Schedulers.io()).observeOn(
                Schedulers.io()
            ).subscribe(
                {
                    if(it.isSuccessful){
                        _userResponse.postValue(Event(Resource.Success(it)))
                    }
                    else
                    {

                    }

                },
                {

                }
            ))
        }
        catch(t : Throwable)
        {

        }

    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}