package ru.netology.nmedia.model

import ru.netology.nmedia.dto.Post
import java.util.Objects

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val empty: Boolean = false,

)

sealed interface FeedModelState {
    object Idle: FeedModelState
    object Loading : FeedModelState
    object Refresh : FeedModelState
    object Error : FeedModelState
}
