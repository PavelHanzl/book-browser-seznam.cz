package cz.pavelhanzl.bookbrowser.features.booksearch.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.pavelhanzl.bookbrowser.data.BookRepository
import cz.pavelhanzl.bookbrowser.data.PaginatorImpl
import cz.pavelhanzl.bookbrowser.features.bookdetail.model.Book
import kotlinx.coroutines.launch

class BookSearchViewModel(val bookRepository: BookRepository) : ViewModel() {

    var state by mutableStateOf(ScreenState())

    private val paginator = PaginatorImpl(
        initialIndex = state.startIndex,
        maxResultsPerPage = state.maxResults,
        onLoadUpdated = {
            state = state.copy(isLoading = it)
        },
        onRequest = { startIndex ->
            bookRepository.searchBooksByAuthor(state.searchText, startIndex, state.maxResults)
        },
        getNextIndex = { currentIndex, maxResults ->
            currentIndex+maxResults
        },
        onError = {
            state = state.copy(error = it?.localizedMessage)
        },
        onSuccess = { items, newStartIndex ->
                val rawList = state.items + items
                val uniqueBookList = uniqueBookList(rawList, state.searchText)

                state = state.copy(
                    items = uniqueBookList,
                    startIndex = newStartIndex,
                    endReached = items.isEmpty()
                )

        }

    )


//    init {
//        loadNextBooks()
//    }

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
                //case insensitive porovnávání
                authorName.lowercase() in author.lowercase()
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

    fun onSearchTextChanged(newText: String) {
        state = state.copy(
            searchText = newText
        )
    }

    fun onSearchButtonClick() {
        //Při kliku na search button nejdříve smaže recycleview
        state=state.copy(
            items = emptyList(),
        )

        //vyresetuje paginátor
        paginator.reset()

        //Return empty list if Searchbar is empty
        if (state.searchText == "") {
            return
        }

        //zavolá načtení položek
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }
}

data class ScreenState(
    val isLoading: Boolean = false,
    val items: List<Book> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val startIndex: Int = 0,
    val maxResults: Int = 10,
    val searchText: String = ""
)