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

class SearchShopViewModel(val appRepository: AppRepository) : ViewModel(){
    private val _userResponse = MutableLiveData<Event<Resource<Response<ShopListDashboardModel>>>>()

    val response : LiveData<Event<Resource<Response<ShopListDashboardModel>>>> = _userResponse
    private val disposable = CompositeDisposable()

    fun getSearchedItem(shop_name: String,userid:String,lat:String,long:String) =  getSearchedItemData(shop_name,userid,lat,long)

    fun  getSearchedItemData(shop_name: String,userid:String,lat:String,long:String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.searchShop(shop_name,userid,lat,long).subscribeOn(Schedulers.io()).observeOn(
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