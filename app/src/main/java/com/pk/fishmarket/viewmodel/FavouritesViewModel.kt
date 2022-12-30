package com.pk.fishmarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pk.fishmarket.ResponseModel.AddressDetailsResponseModel
import com.pk.fishmarket.ResponseModel.FavouriteItems.FavouriteItemsMainModel
import com.pk.fishmarket.Utils.Event
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.repository.AppRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class FavouritesViewModel(val appRepository: AppRepository) : ViewModel(){
    private val _userResponse = MutableLiveData<Event<Resource<Response<FavouriteItemsMainModel>>>>()

    val response : LiveData<Event<Resource<Response<FavouriteItemsMainModel>>>> = _userResponse
    private val disposable = CompositeDisposable()

    fun getFavouritesResponse(userid: String) =
        getAddressData(userid)

    fun getAddressData(userid: String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.getFavourites(userid).subscribeOn(
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