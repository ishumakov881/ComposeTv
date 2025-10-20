package co.joebirch.composetv

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.tv.material3.Carousel
import androidx.tv.material3.CarouselState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import co.joebirch.composetv.DataFactory.makeCarouselItem
import coil.compose.AsyncImage

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalTvMaterial3Api
@Composable
fun HomeCarousel(
    modifier: Modifier = Modifier
) {
    val items = makeCarouselItem()
    val state = remember {
        CarouselState()
    }
    Carousel(
        modifier = modifier,
        carouselState = state,
        autoScrollDurationMillis = 7500,
        itemCount = items.count(),
        content = { index ->
            val transform = ContentTransform(
                targetContentEnter = fadeIn(tween(durationMillis = 1000)),
                initialContentExit = fadeOut(tween(durationMillis = 1000))
            )
            items[index].also { item ->
                CarouselItem(
                    background = { modifier ->
                        AsyncImage(
                            item.image, null, modifier = modifier, contentScale = ContentScale.Crop
                        )
                    },
                    content = {
                        HomeItemBody(item)
                    },
                    contentTransformForward = transform,
                    //contentTransformBackward = TODO(),
                )
            }
        })
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CarouselItem(
    modifier: Modifier = Modifier,
    background: @Composable (Modifier) -> Unit = {},
    contentTransformForward: ContentTransform,
    //contentTransformBackward: ContentTransform,
    content: @Composable () -> Unit,
){
    Box(modifier = modifier.fillMaxSize()) {
        background(Modifier.fillMaxSize())
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.7f),
                            Color.Transparent
                        )
                    )
                )
        )
        AnimatedContent(
            targetState = content,
            transitionSpec = { contentTransformForward }
        ) {
            it()
        }
    }
}