package desktop.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope

abstract class ViewModel {
    open fun onCleared() {}
}

abstract class CoroutineViewModel(protected val scope: CoroutineScope) : ViewModel()

interface ViewModelFactory {
    fun <VM : ViewModel> createViewModel(clazz: Class<VM>): VM
}

private class DefaultViewModelFactory(
    private val scope: CoroutineScope
) : ViewModelFactory {
    override fun <VM : ViewModel> createViewModel(clazz: Class<VM>): VM {
        return runCatching {
            if (CoroutineViewModel::class.java.isAssignableFrom(clazz)) {
                clazz.getConstructor(CoroutineScope::class.java).newInstance(scope)
            } else {
                clazz.getConstructor().newInstance()
            }
        }.getOrNull() ?: throw IllegalStateException("Create Instance Failure(${clazz})")
    }
}

@Composable
fun <VM : ViewModel> rememberViewModel(clazz: Class<VM>): VM {
    val scope = rememberCoroutineScope()
    val vm = remember {
        DefaultViewModelFactory(scope).createViewModel(clazz)
    }
    DisposableEffect(Unit) {
        onDispose {
            vm.onCleared()
        }
    }
    return vm
}

@Composable
inline fun <reified VM : ViewModel> rememberViewModel(): VM = rememberViewModel(VM::class.java)

