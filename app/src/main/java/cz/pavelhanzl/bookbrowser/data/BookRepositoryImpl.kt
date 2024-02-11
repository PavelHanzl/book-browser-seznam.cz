package cz.pavelhanzl.bookbrowser.data

import cz.pavelhanzl.bookbrowser.features.bookdetail.Model.Book

class BookRepositoryImpl(
    private val dataSource : BookApiService
):BookRepository {
    override suspend fun searchBooks(authorName: String): List<Book>? {


                val response = dataSource.searchBooks(authorName)
                if (response.isSuccessful && response.body() != null) {
                    val books = response.body()!!.items
                    return books
                    // Zpracujte data
                } else {
                    return null
                    // Zpracujte chybu
                }



//        return listOf(
//            Book("1", "knizka"),
//            Book("2", "knizecka"),
//            Book("3", "knihaaa"))
    }
}