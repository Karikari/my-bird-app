import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import repositories.BirdRepository

/**
 * Created by Kwaku Karikari on 02/09/2023.
 */


data class BirdsUiState (
    val images: List<BirdImage> = emptyList(),
    val selectedCategory: String? = null
) {
    val categories = images.map { it.category }.toSet()
    val selectedImages = images.filter { it.category == selectedCategory }
}

class BirdViewModel : ViewModel(){

    private var birdRepository: BirdRepository = BirdRepository()


    private val _uiState = MutableStateFlow(BirdsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        updateImages()
    }

    fun selectCategory(category: String){
        _uiState.update {
            it.copy(
                selectedCategory = category
            )
        }
    }

    fun updateImages() = viewModelScope.launch {
        _uiState.update {
            it.copy(
                images = birdRepository.getBirds()
            )
        }
    }


    override fun onCleared() {
        birdRepository.closehttp()
    }

}