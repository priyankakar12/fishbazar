package com.pk.fishmarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pk.fishmarket.ResponseModel.LoginResponseModel
import com.pk.fishmarket.Utils.Event
import com.pk.fishmarket.Utils.Resource
import com.pk.fishmarket.repository.AppRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class LoginViewModel(val appRepository: AppRepository) : ViewModel(){
    private val _userResponse = MutableLiveData<Event<Resource<Response<LoginResponseModel>>>>()

    val response : LiveData<Event<Resource<Response<LoginResponseModel>>>> = _userResponse
    private val disposable = CompositeDisposable()

    fun getLoginResponse(phone_number : String) = getLogin(phone_number)

    fun getLogin(phone_number : String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.userLogin(phone_number).subscribeOn(Schedulers.io()).observeOn(
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