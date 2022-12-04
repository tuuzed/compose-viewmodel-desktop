package ui.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import desktop.viewmodel.ViewModel

class MainViewModel : ViewModel() {
    private val _count = mutableStateOf(0)
    val count: State<Int> get() = _count

    fun increment() {
        _count.value++
    }
}
