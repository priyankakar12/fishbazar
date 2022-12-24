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

class AddRatingViewModel (val appRepository: AppRepository) : ViewModel(){
    private val _userResponse = MutableLiveData<Event<Resource<Response<RatingModel>>>>()

    val response : LiveData<Event<Resource<Response<RatingModel>>>> = _userResponse
    private val disposable = CompositeDisposable()

    fun getReviewResponse(rating_count: String,feedback: String,userid: String,shopid: String) = getRatingData(rating_count,feedback,userid,shopid)

    fun getRatingData(rating_count: String,feedback: String,userid: String,shopid: String){
        _userResponse.postValue(Event(Resource.Loading()))
        try{
            disposable.add(appRepository.addReview(rating_count,feedback,userid,shopid).subscribeOn(Schedulers.io()).observeOn(
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