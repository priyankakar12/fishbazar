package com.pk.fishmarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.pk.fishmarket.ResponseModel.ShopListResponseModel
import com.pk.fishmarket.Utils.Event
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.repository.AppRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class ShopListViewModel (val appRepository: AppRepository) : ViewModel(){
    private val _userResponse = MutableLiveData<Event<Resource<Response<ShopListResponseModel>>>>()

    val response : LiveData<Event<Resource<Response<ShopListResponseModel>>>> = _userResponse
    private val disposable = CompositeDisposable()

    fun getShopListResponse(shop_id:String) = getShopListData(shop_id)

    fun getShopListData(shop_id:String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.getShopsData(shop_id).subscribeOn(Schedulers.io()).observeOn(
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