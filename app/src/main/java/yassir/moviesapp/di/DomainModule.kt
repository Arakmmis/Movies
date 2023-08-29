package yassir.moviesapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yassir.moviesapp.domain.MovieRepository
import yassir.moviesapp.domain.MovieRepositoryImpl
import yassir.moviesapp.domain.api.Api
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DomainModule {

    @Provides
    @Singleton
    fun provideMovieRepository(api: Api): MovieRepository = MovieRepositoryImpl(api)
}