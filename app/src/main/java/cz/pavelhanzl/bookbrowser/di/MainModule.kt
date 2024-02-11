package cz.pavelhanzl.bookbrowser.di

import cz.pavelhanzl.bookbrowser.data.BookRepository
import cz.pavelhanzl.bookbrowser.data.BookRepositoryImpl
import cz.pavelhanzl.bookbrowser.features.booksearch.presentation.BookSearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<BookRepository> { BookRepositoryImpl() }
    viewModel { BookSearchViewModel(get()) }
}