package com.pk.fishmarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pk.fishmarket.ResponseModel.AddressDetailsResponseModel
import com.pk.fishmarket.ResponseModel.CartUpdateResponseModel
import com.pk.fishmarket.Utils.Event
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.repository.AppRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class DeleteFavouritesViewModel(val appRepository: AppRepository) : ViewModel(){
    private val _userResponse = MutableLiveData<Event<Resource<Response<CartUpdateResponseModel>>>>()

    val response : LiveData<Event<Resource<Response<CartUpdateResponseModel>>>> = _userResponse
    private val disposable = CompositeDisposable()

    fun deleteFavourites(userid: String,shopid:String) =
        getAddressData(userid,shopid)

    fun getAddressData(userid: String,shopid:String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.deleteFavourites(userid,shopid).subscribeOn(
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