package cz.pavelhanzl.bookbrowser.features.booksearch.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pavelhanzl.bookbrowser.data.BookRepository
import cz.pavelhanzl.bookbrowser.features.bookdetail.Model.Book
import cz.pavelhanzl.bookbrowser.features.bookdetail.Model.VolumeInfo
import kotlinx.coroutines.launch

class BookSearchViewModel(val bookRepository: BookRepository) : ViewModel() {
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
                // čistý seznam knížek vrácený od google api
                booklist = bookRepository.searchBooksByAuthor(searchText)

            } catch (e: Exception) {

            }
        }
    }

    fun onSearchTextChanged(newText: String) {
        searchText = newText
    }


}