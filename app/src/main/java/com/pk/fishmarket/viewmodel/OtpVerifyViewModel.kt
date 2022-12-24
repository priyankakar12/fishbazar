package com.pk.fishmarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pk.fishmarket.ResponseModel.OtpResponseModel
import com.pk.fishmarket.Utils.Event
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.repository.AppRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class OtpVerifyViewModel(val appRepository: AppRepository) : ViewModel(){
    private val _userResponse = MutableLiveData<Event<Resource<Response<OtpResponseModel>>>>()

    val response : LiveData<Event<Resource<Response<OtpResponseModel>>>> = _userResponse
    private val disposable = CompositeDisposable()

    fun getOtpresponse(phone_number : String,otp:String) = getOtp(phone_number,otp)

    fun getOtp(phone_number : String,otp:String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.userOtpVerify(phone_number,otp).subscribeOn(Schedulers.io()).observeOn(
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