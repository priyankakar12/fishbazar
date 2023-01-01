package com.pk.fishmarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pk.fishmarket.ResponseModel.CartUpdateResponseModel
import com.pk.fishmarket.Utils.Event
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.repository.AppRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class PlaceOrderViewModel (val appRepository: AppRepository) : ViewModel(){
    private val _userResponse = MutableLiveData<Event<Resource<Response<CartUpdateResponseModel>>>>()

    val response : LiveData<Event<Resource<Response<CartUpdateResponseModel>>>> = _userResponse
    private val disposable = CompositeDisposable()

    fun addOrder(userid: String,product: String,total_price: String,mode_of_payment:String
                   ,orderdate:String,
                   transaction_id:String,
                   deliverydate:String,
                   addressid:String,delivary_status:String) =  addOrderData(userid,product,total_price,mode_of_payment,transaction_id,orderdate,deliverydate,
        addressid,delivary_status)

    fun  addOrderData(userid: String,product: String,total_price: String,mode_of_payment:String
                           ,orderdate:String,
                           transaction_id:String,
                           deliverydate:String,
                           addressid:String,delivary_status:String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.placeOrder(userid,product,total_price,mode_of_payment,transaction_id,orderdate,deliverydate,
                addressid,delivary_status).subscribeOn(Schedulers.io()).observeOn(
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