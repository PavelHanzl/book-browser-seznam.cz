package cz.pavelhanzl.bookbrowser.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cz.pavelhanzl.bookbrowser.features.bookdetail.presentation.BookDetailScreen
import cz.pavelhanzl.bookbrowser.features.booksearch.presentation.BookSearchScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationStrings.BOOKSEARCH.toString()
){
    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavigationStrings.BOOKSEARCH.toString()) { BookSearchScreen(navController) }
        composable(NavigationStrings.BOOKDETAIL.toString()) { BookDetailScreen(navController) }
        /*...*/
    }
}