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

    fun getLoginResponse(phonenumber : String,user_pass:String) = getLogin(phonenumber,user_pass)

    fun getLogin(phonenumber : String,user_pass:String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.userLogin(phonenumber,user_pass).subscribeOn(Schedulers.io()).observeOn(
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