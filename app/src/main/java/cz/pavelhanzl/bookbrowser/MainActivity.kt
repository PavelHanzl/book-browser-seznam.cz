package cz.pavelhanzl.bookbrowser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.pavelhanzl.bookbrowser.Navigation.AppNavHost
import cz.pavelhanzl.bookbrowser.Navigation.NavigationStrings
import cz.pavelhanzl.bookbrowser.features.bookdetail.presentation.BookDetailScreen
import cz.pavelhanzl.bookbrowser.features.booksearch.presentation.BookSearchScreen
import cz.pavelhanzl.bookbrowser.ui.theme.BookBrowserTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookBrowserTheme{
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(navController = rememberNavController())
                }
            }
        }
    }
}