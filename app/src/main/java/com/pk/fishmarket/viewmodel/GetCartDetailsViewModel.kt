package com.pk.fishmarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pk.fishmarket.ResponseModel.Cart_Response_Model
import com.pk.fishmarket.Utils.Event
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.repository.AppRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class GetCartDetailsViewModel(val appRepository: AppRepository) : ViewModel(){
    private val _userResponse = MutableLiveData<Event<Resource<Response<Cart_Response_Model>>>>()

    val response : LiveData<Event<Resource<Response<Cart_Response_Model>>>> = _userResponse
    private val disposable = CompositeDisposable()

    fun getCartResponse(userid: String,lat:String,long:String) = getCartData(userid,lat,long)

    fun getCartData(userid: String, lat: String, long: String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.getCartDetails(userid,lat,long).subscribeOn(
                Schedulers.io()).observeOn(
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