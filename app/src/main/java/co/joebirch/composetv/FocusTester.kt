package co.joebirch.composetv

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component2
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.Text
import kotlinx.coroutines.delay


@Composable
fun FocusTester() {
    Row(modifier = Modifier.fillMaxSize()) {
        Test1(modifier = Modifier.weight(1.0f))
        Test1(modifier = Modifier.weight(1.0f))
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Test1(modifier: Modifier) {
    val (lazyRow, firstChild) = remember { FocusRequester.createRefs() }
    val state = rememberLazyListState()
    var scrollReq by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(scrollReq) {
        if (scrollReq) {

            delay(1200)
            //state.scrollToItem(0)//0
            //state.scrollToItem(1)//1                  scrollToItem - ломает focusRestorer
            //state.scrollToItem(10)//1
            //scrollReq = false
            //lazyRow.requestFocus()
        }
    }

    var isFocused1 by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            //.focusGroup()
            .focusProperties {
//                onExit = {   .. - сломает focusRestorer в child
////                    Toast.makeText(context, "@@@@", Toast.LENGTH_SHORT).show()
////                    scrollReq = true
////                }
//                enter = { lazyRow }
            }
            .onFocusChanged { isFocused1 = it.isFocused }
            .then(
                if (isFocused1) Modifier.border(3.dp, Color.Yellow) else Modifier
            )) {
        Text("@@@ $scrollReq")

        LazyColumn(
            state = state, modifier = modifier
//            .focusProperties {
//                onExit = { scrollReq = true } - Обьявленный тут блок ломает работу восстановления
//            }
                .focusRequester(lazyRow)
//            .focusProperties {
//                onExit = { scrollReq = true }//- Обьявленный тут блок ломает работу восстановления
//            }
                .focusRestorer(firstChild)

//            .focusProperties {
//                onExit = { scrollReq = true } //А тут коректно работает focusRestorer не не срабатывает onExit = { scrollReq = true }
//            }
//                .focusProperties {
//                    onEnter = {
//                        Toast.makeText(context, "@@@@", Toast.LENGTH_SHORT).show()
//                    }
//                }
        ) {
            items(100) { index ->
                var isFocused by remember { mutableStateOf(false) }
                val modifier = if (index == 0) Modifier.focusRequester(firstChild) else Modifier
                Button(
                    onClick = {}, modifier = modifier
                        .then(
                            if (isFocused) Modifier.border(3.dp, Color.Red) else Modifier
                        )
                        .onFocusChanged { isFocused = it.isFocused }) {
                    Text("$index")
                }
            }
        }
    }

    //+ ставлю фокус в item 99, проскроливаю руками лист вверх до 0
}