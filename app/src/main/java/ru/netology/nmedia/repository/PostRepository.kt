package ru.netology.nmedia.repository


import ru.netology.nmedia.dto.Post

interface PostRepository {
    //fun getAll(): List<Post>
    fun likeByIdAsync(post: Post, callback: AsyncCallback<Post>)
    fun save(post: Post, callback: AsyncCallback<Post>)
    fun removeById(id: Long, callback: AsyncCallback<Unit> )

    fun getAllAsync(callback: AsyncCallback<List<Post>>)

    interface AsyncCallback<T> {
        fun onSuccess(posts: T) {}
        fun onError(e: Exception) {}
    }
}
