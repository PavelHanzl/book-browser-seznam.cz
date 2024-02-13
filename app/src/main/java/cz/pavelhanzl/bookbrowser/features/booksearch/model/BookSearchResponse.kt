package cz.pavelhanzl.bookbrowser.features.booksearch.model

import cz.pavelhanzl.bookbrowser.features.bookdetail.model.Book

data class BookSearchResponse (
    val kind: String,
    val totalItems:Int,
    val items: List<Book>?
)