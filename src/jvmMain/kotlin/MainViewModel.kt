import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import desktop.viewmodel.CoroutineViewModel
import kotlinx.coroutines.CoroutineScope

class MainViewModel(scope: CoroutineScope) : CoroutineViewModel(scope) {
    private val _count = mutableStateOf(0)
    val count: State<Int> get() = _count

    fun increment() {
        _count.value++
        println("increment: count=${count.value}")
    }

    override fun onCleared() {
        super.onCleared()
        println("onCleared: ")
    }
}
