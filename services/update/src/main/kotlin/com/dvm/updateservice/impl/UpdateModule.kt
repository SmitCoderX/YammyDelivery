package com.dvm.updateservice.impl

import com.dvm.updateservice.api.UpdateService
import org.koin.dsl.module

val updateModule = module {

    factory<UpdateService> {
        DefaultUpdateService(
            categoryRepository = get(),
            dishRepository = get(),
            reviewRepository = get(),
            profileRepository = get(),
            orderRepository = get(),
            favoriteRepository = get(),
            menuApi = get(),
            profileApi = get(),
            orderApi = get(),
            datastore = get()
        )
    }
}