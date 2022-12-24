package com.pk.fishmarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pk.fishmarket.ResponseModel.CartUpdateResponseModel
import com.pk.fishmarket.ResponseModel.OrderDetailsResponseSucccessModel
import com.pk.fishmarket.Utils.Event
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.repository.AppRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class OrderCancelViewModel (val appRepository: AppRepository) : ViewModel(){
    private val _userResponse = MutableLiveData<Event<Resource<Response<CartUpdateResponseModel>>>>()

    val response : LiveData<Event<Resource<Response<CartUpdateResponseModel>>>> = _userResponse
    private val disposable = CompositeDisposable()

    fun cancelOrderHistory(userid: String,Orderid:String) = getOrderHistoryResponce(userid,Orderid)

    fun getOrderHistoryResponce(userid: String,Orderid:String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.cancelOrder(userid,Orderid).subscribeOn(Schedulers.io()).observeOn(
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