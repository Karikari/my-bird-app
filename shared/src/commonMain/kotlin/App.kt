import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource


@Composable
fun BirdAppTheme(
    content: @Composable () -> Unit
){
    MaterialTheme(
        colors = MaterialTheme.colors.copy(primary = Color.Black),
        shapes = MaterialTheme.shapes.copy(
            small = AbsoluteCutCornerShape(0.dp),
            medium = AbsoluteCutCornerShape(0.dp),
            large = AbsoluteCutCornerShape(0.dp)
        )
    ){
        content()
    }

}


@Composable
fun App() {
    BirdAppTheme {
        val viewModel = getViewModel(Unit, viewModelFactory { BirdViewModel() })
        BirdPage(viewModel = viewModel)
    }
}


@Composable
fun BirdPage(viewModel: BirdViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    BirdPageScreen(
        birdImages = uiState.selectedImages,
        categories = uiState.categories,
        onCategoryChanged = { category ->
            viewModel.selectCategory(category)
        }
    )

    LaunchedEffect(uiState.categories.isNotEmpty()){
        if (uiState.categories.isNotEmpty()){
            viewModel.selectCategory(uiState.categories.first())
        }
    }
}

@Composable
fun BirdPageScreen(
    birdImages: List<BirdImage> = emptyList(),
    categories: Set<String>,
    onCategoryChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            categories.forEach { category ->
                Button(onClick = {
                    onCategoryChanged(category)
                }, modifier = Modifier.fillMaxWidth().weight(1f)){
                    Text(category)
                }
            }
        }

        AnimatedVisibility(birdImages.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
                content = {
                    items(birdImages) {
                        BirdImageView(it)
                    }
                }
            )
        }
    }
}


@Composable
fun BirdImageView(
    birdImage: BirdImage
) {
    KamelImage(
        asyncPainterResource(data = "https://sebi.io/demo-image-api/${birdImage.path}"),
        contentDescription = "${birdImage.category} by ${birdImage.author}",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)
    )

}


expect fun getPlatformName(): String