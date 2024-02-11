package cz.pavelhanzl.bookbrowser.features.booksearch.presentation

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import cz.pavelhanzl.bookbrowser.features.bookdetail.Model.Book
import org.koin.androidx.compose.defaultExtras
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.compose.viewModel
import org.koin.compose.rememberCurrentKoinScope
import org.koin.java.KoinJavaComponent.inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSearchScreen(
    navController: NavController,
    viewModel: BookSearchViewModel = koinViewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
) {
    viewModel.getListOfBooks()

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                SearchBar(
                    text=viewModel.searchText,
                    onTextChange = { viewModel.onSearchTextChanged(it) },
                )

                BookList(
                    viewModel.booklist
                )
//                Button(
//                    onClick = { viewModel.addBookToList() }
//
//                ) {
//                    Text(text = "add book")
//                }
//                Button(
//                    onClick = { navController.navigate("bookdetail") }
//
//                ) {
//                    Text(text = "Navigate to book detail")
//                }

            }


        }
    }

}

@Composable
fun SearchBar(text: String, onTextChange: (String) -> Unit) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text("Zadejte jméno autora") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun BookList(books: List<Book>?) {
    books?.let {
        LazyColumn {
            items(books) { book ->
                BookItem(book)
            }
        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BookItem(book: Book) {
    Row(
        modifier = Modifier
            .clickable(onClick = { /* akce při kliknutí */ })
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Obrázek knihy
        GlideImage(
            model = book.volumeInfo.imageLinks?.smallThumbnailToHttps(),
            contentDescription = "Book image thumbnail",
            modifier = Modifier
                .size(100.dp)

        )
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ) {
            // Název knihy
            Text(
                text = book.volumeInfo.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            // Autor knihy
            Text(
                text = book.volumeInfo.authors?.joinToString(", ") ?: "Neznámý autor",
                fontSize = 14.sp
            )
        }
    }
}