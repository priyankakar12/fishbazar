package com.pk.fishmarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pk.fishmarket.ResponseModel.OrderDetailsResponseSucccessModel
import com.pk.fishmarket.Utils.Event
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.repository.AppRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class OrderDetailsViewModel(val appRepository: AppRepository) : ViewModel(){
    private val _userResponse = MutableLiveData<Event<Resource<Response<OrderDetailsResponseSucccessModel>>>>()

    val response : LiveData<Event<Resource<Response<OrderDetailsResponseSucccessModel>>>> = _userResponse
    private val disposable = CompositeDisposable()

    fun getOrderDetailsHistory(userid: String,Orderid:String) = getOrderHistoryResponce(userid,Orderid)

    fun getOrderHistoryResponce(userid: String,Orderid:String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.orderDetails(userid,Orderid).subscribeOn(Schedulers.io()).observeOn(
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