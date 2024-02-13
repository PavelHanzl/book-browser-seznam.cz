package cz.pavelhanzl.bookbrowser.features.booksearch.presentation

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.ktx.ExperimentGlideFlows
import com.bumptech.glide.integration.compose.Placeholder
import com.bumptech.glide.integration.compose.placeholder
import cz.pavelhanzl.bookbrowser.R
import cz.pavelhanzl.bookbrowser.features.bookdetail.model.Book
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSearchScreen(
    navController: NavController,
    viewModel: BookSearchViewModel = koinViewModel()
) {
    val state = viewModel.state

    Scaffold(
        topBar = {
            SearchBar(viewModel)
        }
    ) { it ->

        when {
            state.isLoading && state.items.isEmpty() -> {
                Box(modifier = Modifier
                    .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }

            state.items.isNotEmpty() -> {
                BookList(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                        .imePadding(),
                    viewModel = viewModel
                )
            }

            else -> {

                Box(modifier = Modifier
                    .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Zadejte autora a ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

        }



    }

}

@Composable
fun SearchBar(viewModel: BookSearchViewModel) {

    Row(
        modifier = Modifier
            .background(Color(0xFFf3f3f3))
            .fillMaxWidth()
            .padding(10.dp)
            .padding(bottom = 20.dp)
            .height(IntrinsicSize.Max),
        verticalAlignment = Alignment.Bottom
    ) {
        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = viewModel.state.searchText,
            onValueChange = { newText ->
                viewModel.onSearchTextChanged(newText)
            },
            label = { Text("Zadejte jméno autora") }
        )

        Spacer(modifier = Modifier.size(8.dp))


        OutlinedIconButton(
            modifier = Modifier
                .size(55.dp),
            onClick = {
                focusManager.clearFocus()
                viewModel.onSearchButtonClick()
            }
        ) {
            Icon(Icons.Outlined.Search, contentDescription = "Hledat")
        }
    }
}

@Composable
fun BookList(
    modifier: Modifier = Modifier,
    viewModel: BookSearchViewModel
) {
    val state = viewModel.state

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        items(state.items.size) { i ->
            BookItem(state.items[i])

            if (i >= state.items.size - 1 && !state.endReached && !state.isLoading) {
                viewModel.loadNextBooks()
            }
        }
        item {
            if (state.isLoading) {
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }


}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentGlideFlows::class)
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
            modifier = Modifier
                .size(100.dp),
            model = book.volumeInfo.imageLinks?.smallThumbnailToHttps(),
            contentDescription = "Book image thumbnail",
            loading = placeholder(R.drawable.ic_launcher_background),
            failure = placeholder(R.drawable.ic_launcher_foreground),
            transition = CrossFade
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
                text = (book.volumeInfo?.authors?.take(2)
                    ?.joinToString(", ")) + if ((book.volumeInfo.authors.size ?: 0) > 2
                ) " a další..." else "",
                fontSize = 14.sp,
                color = Color.Gray
            )

            // Popis knihy
            Text(
                text = book.volumeInfo.description ?: "(Popis není dostupný)",
                maxLines = 3,
                fontSize = 14.sp,
                lineHeight = 16.sp,
                overflow = TextOverflow.Ellipsis,
                fontStyle = if (book.volumeInfo.description == null) FontStyle.Italic else FontStyle.Normal

            )

        }
    }
}