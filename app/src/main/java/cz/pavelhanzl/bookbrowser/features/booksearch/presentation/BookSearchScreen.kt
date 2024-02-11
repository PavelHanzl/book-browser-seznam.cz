package cz.pavelhanzl.bookbrowser.features.booksearch.presentation

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
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
        viewModelStoreOwner = LocalContext.current as ComponentActivity)
) {

    viewModel.getListOfBook()
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
                SearchBar()

                BookList(
                    /*TODO: nahradit za repozitory ve viewmodelu*/
                    viewModel.booklist
                )
                Button(
                    onClick = { viewModel.addBookToList() }

                ) {
                    Text(text = "add book")
                }
                Button(
                    onClick = { navController.navigate("bookdetail") }

                ) {
                    Text(text = "Navigate to book detail")
                }
                // Zde bude další
            }


        }
    }

}

@Composable
fun SearchBar() {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Zadejte jméno autora") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun BookList(books: List<Book>) {
    LazyColumn {
        items(books) { book ->
            BookItem(book)
        }
    }
}

@Composable
fun BookItem(book: Book) {
    Row(modifier = Modifier
        .padding(10.dp)
        .clickable(enabled = true, onClick = { Log.d("myTag", "click ${book.name}") })) {
        Text(text = book.id)
        Text(text = book.name)
    }

}