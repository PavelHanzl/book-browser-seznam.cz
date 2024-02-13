package cz.pavelhanzl.bookbrowser.navigation

enum class NavigationStrings(val route:String) {
    BOOKSEARCH("booksearch"),
    BOOKDETAIL("bookdetail/{bookId}");

    override fun toString(): String {
        return route
    }

}
