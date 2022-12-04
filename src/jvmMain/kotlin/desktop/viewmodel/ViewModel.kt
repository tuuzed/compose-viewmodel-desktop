package desktop.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope

abstract class ViewModel {
    open fun onCleared() {}

    interface Factory {
        fun <VM : ViewModel> get(clazz: Class<VM>): VM
    }
}

abstract class CoroutineViewModel(protected val scope: CoroutineScope) : ViewModel()

private class DefaultViewModelFactory(private val scope: CoroutineScope) : ViewModel.Factory {
    override fun <VM : ViewModel> get(clazz: Class<VM>): VM {
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
fun <VM : ViewModel> rememberViewModel(
    clazz: Class<VM>,
    factory: ViewModel.Factory? = null
): VM {
    val scope = rememberCoroutineScope()
    val vm = remember {
        (factory ?: DefaultViewModelFactory(scope)).get(clazz)
    }
    DisposableEffect(Unit) {
        onDispose {
            vm.onCleared()
        }
    }
    return vm
}

@Composable
inline fun <reified VM : ViewModel> rememberViewModel(
    factory: ViewModel.Factory? = null
): VM = rememberViewModel(VM::class.java, factory)
