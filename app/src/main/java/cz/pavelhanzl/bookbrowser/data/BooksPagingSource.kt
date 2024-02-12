import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import cz.pavelhanzl.bookbrowser.data.BookApiService
import cz.pavelhanzl.bookbrowser.features.bookdetail.Model.Book
import cz.pavelhanzl.bookbrowser.features.booksearch.model.BookSearchResponse
import retrofit2.Response

class BooksPagingSource(
    private val apiService: BookApiService
) : PagingSource<Int, Book>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Book> {
        try {
            var books:List<Book>
            // Získání aktuální stránky. Pokud je null, začneme od první stránky
            val currentPage = params.key ?: 1
            val startIndex = if (currentPage == 1) 0 else (((currentPage - 1) * params.loadSize))
            val response = apiService.searchBooks(query = "inauthor:Nesbo", pageSize = params.loadSize, startIndex = startIndex)

            // Získání dat a příprava výsledku pro Paging


            if (response.isSuccessful && response.body() != null) {
            //books = response.body()!!.items
            books = purgedBookList(response,"Nesbo")
            } else {
                books= emptyList()
            }


            val nextPageNumber = if (books.isEmpty()) null else currentPage + 1

            Log.d("Page load:", books.count().toString())

            return LoadResult.Page(
                data = books,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            // V případě chyby vrátíme LoadResult.Error
            return LoadResult.Error(e)
        }
    }

    private fun purgedBookList(
        response: Response<BookSearchResponse>,
        authorName: String
    ): List<Book> {
        val rawBooklist = response.body()!!.items

        // občas google api vrátí knihu i když by ji němělo vrátit
        // např pro autora Novák https://www.googleapis.com/books/v1/volumes?q=inauthor:Nov%C3%A1k
        // vrátí mimo jiné i https://www.googleapis.com/books/v1/volumes/N0wWmueHMokC
        // proto list ještě filtrujeme na úrovni aplikace
        val filteredList = rawBooklist?.filter { book ->
            book.volumeInfo.authors?.any { author ->
                authorName in author
            } ?: false
        }

        //Filtruje jen unikátní názvy knížek
        //Např pro autora Nesbø se vrací dvakrát kniho Netopýr https://www.googleapis.com/books/v1/volumes?q=inauthor:Nesb%C3%B8
        val uniqueBooklist = filteredList?.distinctBy { it.volumeInfo.title }

        return uniqueBooklist ?: emptyList()
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        return state.anchorPosition
    }
}