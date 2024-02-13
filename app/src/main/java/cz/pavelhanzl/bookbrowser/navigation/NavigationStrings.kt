package cz.pavelhanzl.bookbrowser.navigation

enum class NavigationStrings(val route:String) {
    BOOKSEARCH("booksearch"),
    BOOKDETAIL("bookdetail");

    override fun toString(): String {
        return route
    }

}
