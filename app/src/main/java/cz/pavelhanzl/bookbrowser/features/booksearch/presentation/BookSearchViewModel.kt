package cz.pavelhanzl.bookbrowser.features.booksearch.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cz.pavelhanzl.bookbrowser.data.BookRepository
import cz.pavelhanzl.bookbrowser.features.bookdetail.Model.Book
import cz.pavelhanzl.bookbrowser.features.bookdetail.Model.VolumeInfo
import cz.pavelhanzl.bookbrowser.features.booksearch.domain.GetBookListByAuthorUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class BookSearchViewModel(
    val bookRepository: BookRepository,
    private val getBooksByAuthorUseCase: GetBookListByAuthorUseCase
) : ViewModel() {


    private val _booksState: MutableStateFlow<PagingData<Book>> =
        MutableStateFlow(value = PagingData.empty())
    val booksState: MutableStateFlow<PagingData<Book>> get() = _booksState

    init {
        onEvent(HomeEvent.GetHome)
    }


    var booklist by mutableStateOf<List<Book>?>(null)
        private set

    var searchText by mutableStateOf("")

    fun getListOfBooks() {
        //Return empty list if Searchbar is empty
        if (searchText == "") {
            booklist = emptyList()
            return}

        viewModelScope.launch {
            try {
                // vrací očištěný seznam knížek vrácený od google api
                booklist = bookRepository.searchBooksByAuthor(searchText)

            } catch (e: Exception) {

            }

        }
    }

    fun onSearchTextChanged(newText: String) {
        searchText = newText
    }

    fun onEvent(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                is HomeEvent.GetHome -> {
                    getBooks()
                }
            }
        }
    }

    private suspend fun getBooks() {
        getBooksByAuthorUseCase("Nesbo")
            ?.distinctUntilChanged()
            ?.cachedIn(viewModelScope)
            ?.collect {
                _booksState.value = it
            }
    }

}
sealed class HomeEvent {
    object GetHome : HomeEvent()
}
