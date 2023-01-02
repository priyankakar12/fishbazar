package com.pk.fishmarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pk.fishmarket.ResponseModel.RatingModel

import com.pk.fishmarket.Utils.Event
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.repository.AppRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class AddToCartViewModel  (val appRepository: AppRepository) : ViewModel(){
    private val _userResponse = MutableLiveData<Event<Resource<Response<RatingModel>>>>()

    val response : LiveData<Event<Resource<Response<RatingModel>>>> = _userResponse
    private val disposable = CompositeDisposable()

    fun AddToCartItems(productid: String,shopid: String,product_quantity: String,
                       userid: String,price:String,status:String,quantity_amount:String,base_amount:String,base_price:String)
    =  AddToCartItemsData(productid,shopid,product_quantity,userid,price,status,quantity_amount,base_amount,base_price)

    fun  AddToCartItemsData(productid: String,shopid: String,
                            product_quantity: String,userid: String,price:String,status:String,quantity_amount:String,base_amount:String,base_price:String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.addToCart(productid,shopid,product_quantity,
                userid,price,status,quantity_amount,base_amount,base_price).subscribeOn(Schedulers.io()).observeOn(
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