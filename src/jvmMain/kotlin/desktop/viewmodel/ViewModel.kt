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

@Composable
inline fun <reified VM : ViewModel> rememberViewModel(): VM {
    val scope = rememberCoroutineScope()
    val vm = remember {
        runCatching {
            if (CoroutineViewModel::class.java.isAssignableFrom(VM::class.java)) {
                VM::class.java.getConstructor(CoroutineScope::class.java).newInstance(scope)
            } else {
                VM::class.java.getConstructor().newInstance()
            }
        }.getOrNull() ?: throw IllegalStateException("Create Instance Failure(${VM::class.java})")

    }
    DisposableEffect(Unit) {
        onDispose {
            vm.onCleared()
        }
    }
    return vm
}

