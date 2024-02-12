package cz.pavelhanzl.bookbrowser.features.booksearch.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pavelhanzl.bookbrowser.data.BookRepository
import cz.pavelhanzl.bookbrowser.data.PaginatorImpl
import cz.pavelhanzl.bookbrowser.features.bookdetail.Model.Book
import kotlinx.coroutines.launch

class BookSearchViewModel(val bookRepository: BookRepository) : ViewModel() {

    var state by mutableStateOf(ScreenState())

    var booklist by mutableStateOf<List<Book>?>(null)
        private set

    var searchText by mutableStateOf("")


    private val paginator = PaginatorImpl(
        initialKey = state.startIndex,
        onLoadUpdated = {
            state = state.copy(isLoading = it)
        },
        onRequest = { nextPage ->
            bookRepository.searchBooksByAuthor("Nesbo",nextPage,state.maxResults)
        },
        getNextKey = {
             state.startIndex + state.maxResults

        },
        onError = {
            state = state.copy(error=it?.localizedMessage)
        },
        onSuccess = { items, newKey ->
            //TODO refactor
            if (items!=null){
                val rawList = state.items + items
                val uniqueBookList = uniqueBookList(rawList,"Nesbo")

            state = state.copy(
                items = uniqueBookList,
                startIndex = newKey,
                endReached = items.isEmpty()
            )
            }

        }

    )



    init {
        loadNextBooks()
    }

    private fun uniqueBookList(
        rawList: List<Book>,
        authorName: String
    ): List<Book> {

        // občas google api vrátí knihu i když by ji němělo vrátit
        // např pro autora Novák https://www.googleapis.com/books/v1/volumes?q=inauthor:Nov%C3%A1k
        // vrátí mimo jiné i https://www.googleapis.com/books/v1/volumes/N0wWmueHMokC
        // proto list ještě filtrujeme na úrovni aplikace
        val filteredList = rawList?.filter { book ->
            book.volumeInfo.authors?.any { author ->
                authorName in author
            } ?: false
        }

        //Filtruje jen unikátní názvy knížek
        //Např pro autora Nesbø se vrací dvakrát kniho Netopýr https://www.googleapis.com/books/v1/volumes?q=inauthor:Nesb%C3%B8
        val uniqueBooklist = filteredList?.distinctBy { it.volumeInfo.title }

        return uniqueBooklist ?: emptyList()
    }


    fun loadNextBooks() {
    viewModelScope.launch {
        paginator.loadNextItems()
    }
    }



    fun getListOfBooks() {
        //Return empty list if Searchbar is empty
        if (searchText == "") {
            booklist = emptyList()
            return}

        viewModelScope.launch {
            try {
                // čistý seznam knížek vrácený od google api
                //booklist = bookRepository.searchBooksByAuthor(searchText)

            } catch (e: Exception) {

            }
        }
    }

    fun onSearchTextChanged(newText: String) {
        searchText = newText
    }


}

data class ScreenState(
    val isLoading: Boolean = false,
    val items: List<Book> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val startIndex: Int = 0,
    val maxResults: Int = 10
)