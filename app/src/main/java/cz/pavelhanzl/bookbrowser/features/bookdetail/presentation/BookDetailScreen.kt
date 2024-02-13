package cz.pavelhanzl.bookbrowser.features.bookdetail.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import cz.pavelhanzl.bookbrowser.features.booksearch.presentation.BookSearchViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    navController: NavController,
    bookId: String?,
    viewModel: BookDetailViewModel = koinViewModel()
    )
{
    Row {
        Text(text = viewModel.text)

        if (bookId != null) {
            Text(text = bookId)
        }
    }

}
