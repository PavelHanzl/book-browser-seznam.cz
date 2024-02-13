package cz.pavelhanzl.bookbrowser.features.bookdetail.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pavelhanzl.bookbrowser.data.BookRepository
import cz.pavelhanzl.bookbrowser.features.bookdetail.model.Book
import cz.pavelhanzl.bookbrowser.features.bookdetail.model.sampleBook
import cz.pavelhanzl.bookbrowser.features.booksearch.presentation.ScreenState
import kotlinx.coroutines.launch

class BookDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository
) : ViewModel() {

    var selectedBook by mutableStateOf<Book?>(null)

    init {
        val bookId: String? = savedStateHandle["bookId"]
        loadBookDetail(bookId!!)
    }

    fun loadBookDetail(bookId: String) {
        viewModelScope.launch {
            selectedBook = bookRepository.searchBookById(bookId)
        }
    }


}