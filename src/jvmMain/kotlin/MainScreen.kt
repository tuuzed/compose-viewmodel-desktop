import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import desktop.viewmodel.rememberViewModel

@Composable
fun MainScreen() {
    val viewModel = rememberViewModel<MainViewModel>()

    Column {
        Text(viewModel.count.value.toString())
        Button(onClick = { viewModel.increment() }) {
            Text("Increment")
        }
    }

}
