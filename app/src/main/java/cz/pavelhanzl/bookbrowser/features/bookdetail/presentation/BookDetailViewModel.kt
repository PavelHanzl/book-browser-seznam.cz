package cz.pavelhanzl.bookbrowser.features.bookdetail.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pavelhanzl.bookbrowser.data.BookApiService
import cz.pavelhanzl.bookbrowser.data.BookRepository
import cz.pavelhanzl.bookbrowser.features.bookdetail.model.Book
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class BookDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val bookRepository: BookRepository,
    private val dataSource: BookApiService
) : ViewModel() {

    var state by mutableStateOf(BookDetailScreenState())
    //var selectedBook by mutableStateOf<Book?>(null)

    init {
        //on init loads book details using book id in saved state
        val bookId: String? = savedStateHandle["bookId"]
        loadBookDetail(bookId!!) //cannot be null, because id is from search screen where the book was loaded from
    }

    fun loadBookDetail(bookId: String) {
        viewModelScope.launch {
            state = state.copy(
                selectedBook = bookRepository.searchBookById(bookId)
            )


        }
    }

    //wrapper function for checkPageContent()
    fun checkPageContentWrapper(url: String) {
        viewModelScope.launch {
            state = state.copy(
                checkPageResult = checkPageContent(url)
            )
        }
    }


    //Checks if the html page of the book on google play returns success or 404
    suspend fun checkPageContent(url: String): Boolean {
        try {
            val response = dataSource.getPage(url)
            return response.isSuccessful

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


}

data class BookDetailScreenState(
    val checkPageResult: Boolean? = null,
    val selectedBook: Book? = null
)