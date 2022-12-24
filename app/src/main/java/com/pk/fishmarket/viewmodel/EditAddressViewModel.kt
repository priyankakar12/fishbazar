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

class EditAddressViewModel (val appRepository: AppRepository) : ViewModel(){
    private val _userResponse = MutableLiveData<Event<Resource<Response<CartUpdateResponseModel>>>>()

    val response : LiveData<Event<Resource<Response<CartUpdateResponseModel>>>> = _userResponse
    private val disposable = CompositeDisposable()

    fun updateAddressResponse(userid: String,name: String,address_one: String,address_two:String,pincode:String,phone_number:String) =
        updateAddressData(userid,name,address_one,address_two,pincode,phone_number)

    fun updateAddressData(userid: String,name: String,address_one: String,address_two:String,pincode:String,phone_number:String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.updateAddress(userid,name,address_one,address_two,pincode,phone_number).subscribeOn(
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