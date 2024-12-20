package com.yveskalume.eventcademy.core.data.firebase.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yveskalume.eventcademy.core.data.firebase.repository.AdvertisementRepositoryImpl
import com.yveskalume.eventcademy.core.data.firebase.repository.EventBookingRepositoryImpl
import com.yveskalume.eventcademy.core.data.firebase.repository.EventRepositoryImpl
import com.yveskalume.eventcademy.core.data.firebase.repository.PostLikesRepositoryImpl
import com.yveskalume.eventcademy.core.data.firebase.repository.UserRepositoryImpl
import com.yveskalume.eventcademy.core.data.firebase.repository.PostRepositoryImpl
import com.yveskalume.eventcademy.core.domain.repository.AdvertisementRepository
import com.yveskalume.eventcademy.core.domain.repository.EventBookingRepository
import com.yveskalume.eventcademy.core.domain.repository.EventRepository
import com.yveskalume.eventcademy.core.domain.repository.PostLikesRepository
import com.yveskalume.eventcademy.core.domain.repository.PostRepository
import com.yveskalume.eventcademy.core.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideUserRepository(
        firebaseAuth: FirebaseAuth,
        fireStore: FirebaseFirestore
    ): UserRepository {
        return UserRepositoryImpl(firebaseAuth, fireStore)
    }

    @Provides
    fun provideEventRepository(
        fireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
        firebaseStorage: FirebaseStorage
    ): EventRepository {
        return EventRepositoryImpl(fireStore, firebaseAuth, firebaseStorage)
    }

    @Provides
    fun provideAdvertisementRepository(
        fireStore: FirebaseFirestore
    ): AdvertisementRepository {
        return AdvertisementRepositoryImpl(fireStore)
    }

    @Provides
    fun provideEventBookingRepository(
        fireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): EventBookingRepository {
        return EventBookingRepositoryImpl(fireStore, firebaseAuth)
    }

    @Provides
    fun providePostRepository(
        fireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
        firebaseStorage: FirebaseStorage
    ): PostRepository {
        return PostRepositoryImpl(fireStore, firebaseAuth, firebaseStorage)
    }

    @Provides
    fun providePostLikesRepository(
        fireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth,
    ): PostLikesRepository{
        return PostLikesRepositoryImpl(fireStore, firebaseAuth)
    }
}