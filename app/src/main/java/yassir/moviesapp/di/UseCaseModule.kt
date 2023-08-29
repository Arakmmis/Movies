package yassir.moviesapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import yassir.moviesapp.domain.MovieRepository
import yassir.moviesapp.domain.usecases.GetMoviesListUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideGetMoviesListUseCase(repo: MovieRepository): GetMoviesListUseCase =
        GetMoviesListUseCase(repo)
}