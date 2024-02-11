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

class BookSearchViewModel(val bookRepository: BookRepository): ViewModel() {
    var booklist by mutableStateOf<List<Book>?>(null)
        private set


fun getListOfBook(){
    booklist = emptyList()

    viewModelScope.launch {
        try {
            val authorName: String = "Nesbø"
            // čistý seznam knížek vrácený od google api
            booklist = bookRepository.searchBooksByAuthor(authorName)






            // Zpracujte data
        } catch (e: Exception) {
            // Zpracujte výjimku
        }
    }



//    booklist =listOf(
//        Book("1", "knizka"),
//        Book("2", "knizecka"),
//        Book("3", "kniha"),
//        Book("4", "kniga"),
//    )
}

    fun addBookToList(){
        booklist = booklist?.plus(
            Book("5",
                VolumeInfo("Test", listOf("dsa","dada"),"adsasd","description", imageLinks = null))
        )
    }


}