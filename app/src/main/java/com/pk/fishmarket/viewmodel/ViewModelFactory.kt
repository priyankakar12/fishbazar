package com.pk.fishmarket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pk.fishmarket.repository.AppRepository

class ViewModelFactory(private val appRepository: AppRepository) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java))
        {
            return LoginViewModel(appRepository) as T
        }
         if (modelClass.isAssignableFrom(OtpVerifyViewModel::class.java))
        {
            return OtpVerifyViewModel(appRepository) as T
        }
         if (modelClass.isAssignableFrom(DashboardViewModel::class.java))
        {
            return DashboardViewModel(appRepository) as T
        }
        if (modelClass.isAssignableFrom(ShopListViewModel::class.java))
        {
            return ShopListViewModel(appRepository) as T
        }
        if (modelClass.isAssignableFrom(AddRatingViewModel::class.java))
        {
            return AddRatingViewModel(appRepository) as T
        }
        if (modelClass.isAssignableFrom(SearchItemViewModel::class.java))
        {
            return SearchItemViewModel(appRepository) as T
        }
        if (modelClass.isAssignableFrom(AddToCartViewModel::class.java))
        {
            return AddToCartViewModel(appRepository) as T
        }
        if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java))
        {
            return ProductDetailsViewModel(appRepository) as T
        }
        if (modelClass.isAssignableFrom(GetCartDetailsViewModel::class.java))
        {
            return GetCartDetailsViewModel(appRepository) as T
        }
        if (modelClass.isAssignableFrom(CartUpdateViewModel::class.java))
        {
            return CartUpdateViewModel(appRepository) as T
        }
    if (modelClass.isAssignableFrom(DeleteCartIViewModel::class.java))
        {
            return DeleteCartIViewModel(appRepository) as T
        }
        if (modelClass.isAssignableFrom(AddAddressViewModl::class.java))
        {
            return AddAddressViewModl(appRepository) as T
        }

        if (modelClass.isAssignableFrom(GetAllAddressViewModel::class.java))
        {
            return GetAllAddressViewModel(appRepository) as T
        }
        if (modelClass.isAssignableFrom(AddtoFavouriteShopViewModel::class.java))
        {
            return AddtoFavouriteShopViewModel(appRepository) as T
        }
        if (modelClass.isAssignableFrom(PlaceOrderViewModel::class.java))
        {
            return PlaceOrderViewModel(appRepository) as T
        }
        if (modelClass.isAssignableFrom(EditAddressViewModel::class.java))
        {
            return EditAddressViewModel(appRepository) as T
        }
        if (modelClass.isAssignableFrom(SearchShopViewModel::class.java))
        {
            return SearchShopViewModel(appRepository) as T
        }
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java))
        {
            return RegistrationViewModel(appRepository) as T
        }

        if (modelClass.isAssignableFrom(OrderHistoryViewModel::class.java))
        {
            return OrderHistoryViewModel(appRepository) as T
        }
        if (modelClass.isAssignableFrom(OrderDetailsViewModel::class.java))
        {
            return OrderDetailsViewModel(appRepository) as T
        }

        if (modelClass.isAssignableFrom(OrderCancelViewModel::class.java))
        {
            return OrderCancelViewModel(appRepository) as T
        }
        if (modelClass.isAssignableFrom(UserDetailsViewModel::class.java))
        {
            return UserDetailsViewModel(appRepository) as T
        }


        throw IllegalArgumentException("Unknown class name")
    }
}