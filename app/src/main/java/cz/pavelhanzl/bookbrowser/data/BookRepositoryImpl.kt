package cz.pavelhanzl.bookbrowser.data

import cz.pavelhanzl.bookbrowser.features.bookdetail.Model.Book
import cz.pavelhanzl.bookbrowser.features.booksearch.model.BookSearchResponse
import retrofit2.Response

class BookRepositoryImpl(
    private val dataSource: BookApiService
) : BookRepository {
    override suspend fun searchBooksByAuthor(authorName: String): List<Book>? {

        //vyhledává všechny knihy s daným autorem pomocí Google Api
        val response = dataSource.searchBooks("inauthor:${authorName}")

        if (response.isSuccessful && response.body() != null) {
            return purgedBookList(response, authorName)
        } else {
            return null
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
}