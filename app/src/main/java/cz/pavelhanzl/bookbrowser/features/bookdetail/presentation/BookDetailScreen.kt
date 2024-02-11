package cz.pavelhanzl.bookbrowser.features.bookdetail.presentation

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    navController: NavController,
//    sharedViewModel: BookSearchSharedViewModel = koinViewModel(
//        viewModelStoreOwner = LocalContext.current as ComponentActivity)
    )
{
Text(text = "tohle je bookdetail")
}
