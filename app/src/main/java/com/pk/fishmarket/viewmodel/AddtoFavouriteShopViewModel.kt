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

class AddtoFavouriteShopViewModel  (val appRepository: AppRepository) : ViewModel(){
    private val _userResponse = MutableLiveData<Event<Resource<Response<CartUpdateResponseModel>>>>()

    val response : LiveData<Event<Resource<Response<CartUpdateResponseModel>>>> = _userResponse
    private val disposable = CompositeDisposable()

    fun addtoFavouriteShopItems(userid: String,shopid:String,favourite:String) = AddtoFavouriteShopData(userid,shopid,favourite)

    fun  AddtoFavouriteShopData(userid: String,shopid:String,favourite:String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.addtoFavour(userid,shopid,favourite).subscribeOn(
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