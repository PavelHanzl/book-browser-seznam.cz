package cz.pavelhanzl.bookbrowser.features.bookdetail.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import cz.pavelhanzl.bookbrowser.features.bookdetail.model.Book
import cz.pavelhanzl.bookbrowser.features.bookdetail.model.VolumeInfo
import cz.pavelhanzl.bookbrowser.features.bookdetail.model.sampleBook
import cz.pavelhanzl.bookbrowser.features.booksearch.presentation.BookSearchViewModel
import cz.pavelhanzl.bookbrowser.features.booksearch.presentation.SearchBar
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    navController: NavController,
    bookId: String?,
    viewModel: BookDetailViewModel = koinViewModel()
) {
    val book = Book.sampleBook()
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        modifier = Modifier
                            .size(55.dp),
                        onClick = {
                           navController.navigateUp()
                        }
                    ) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Zpět")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Small Top App Bar")
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            bookId?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            BookDetailHeader(book.volumeInfo)
            Spacer(modifier = Modifier.height(16.dp))
            BookDetailBody(book.volumeInfo)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookDetailHeader(volumeInfo: VolumeInfo) {
    volumeInfo.imageLinks?.let { imageLinks ->
        GlideImage(
            model = imageLinks.thumbnail,
            contentDescription = "Book Thumbnail",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(text = volumeInfo.title, style = MaterialTheme.typography.headlineMedium)
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = volumeInfo.authors.joinToString(", "),
        style = MaterialTheme.typography.bodyLarge
    )
    Text(
        text = volumeInfo.publishedDate,
        style = MaterialTheme.typography.bodySmall
    )

}

@Composable
fun BookDetailBody(volumeInfo: VolumeInfo) {
    volumeInfo.description?.let { description ->
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
//
//@Composable
//fun BasicTextFieldSample() {
//    var text by remember { mutableStateOf("Hello") }
//
//    BasicTextField(
//        value = text,
//        onValueChange = { /*text = it*/ },
//        textStyle = TextStyle(color = Color.Black)
//    )
//}