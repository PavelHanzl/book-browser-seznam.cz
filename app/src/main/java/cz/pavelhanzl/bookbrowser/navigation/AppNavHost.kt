package cz.pavelhanzl.bookbrowser.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cz.pavelhanzl.bookbrowser.features.bookdetail.presentation.BookDetailScreen
import cz.pavelhanzl.bookbrowser.features.booksearch.presentation.BookSearchScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NavigationStrings.BOOKSEARCH.toString()
) {
    NavHost(navController = navController, startDestination = startDestination) {

        //BookSearch screen
        composable(NavigationStrings.BOOKSEARCH.toString()) { BookSearchScreen(navController) }

        //BookDetail screen with id as a string parameter
        composable(
            "${NavigationStrings.BOOKDETAIL}/{bookId}",
            arguments = listOf(navArgument("bookId"){
                type = NavType.StringType
            })
            ) {
            backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId")
            // here you have to fetch user data
            BookDetailScreen(
                navController,
                bookId
            )
        }

    }
}