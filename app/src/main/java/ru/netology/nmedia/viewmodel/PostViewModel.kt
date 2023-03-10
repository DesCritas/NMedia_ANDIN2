package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImplRetrofit
import ru.netology.nmedia.util.SingleLiveEvent

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likedByMe = false,
    likes = 0,
    published = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImplRetrofit(
        AppDb.getInstance(context = application).postDao()
    )

    val data: LiveData<FeedModel> = repository.data.map { FeedModel(it) }
    private val _state = MutableLiveData<FeedModelState>(FeedModelState.Idle)
    val state: LiveData<FeedModelState>
        get() = _state
    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated
    private val _errorOnCreation = SingleLiveEvent<Unit>()
    val errorOnCreation: LiveData<Unit>
        get() = _errorOnCreation


    init {
        loadPosts()
    }

    fun loadPosts() {
        viewModelScope
            .launch {

                try {
                    _state.value = FeedModelState.Loading
                    repository.getAllAsync()
                    _state.value = FeedModelState.Idle
                } catch (e: Exception) {
                    _state.value = FeedModelState.Error
                }
            }
    }

    fun refresh() {
        viewModelScope
            .launch {

                try {
                    _state.value = FeedModelState.Refresh
                    repository.getAllAsync()
                    _state.value = FeedModelState.Idle
                } catch (e: Exception) {
                    _state.value = FeedModelState.Error
                }
            }

    }


    fun save() {
        edited.value?.let {
            _postCreated.value = Unit
            viewModelScope
                .launch {
                    try {
                        repository.save(it)
                        _state.value = FeedModelState.Idle
                    } catch (e: Exception) {
                        _state.value = FeedModelState.Error
                    }
                }
        }
        edited.value = empty

    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun likeById(post: Post) {
        viewModelScope
            .launch {
                try {
                    repository.likeByIdAsync(post)
                    _state.value = FeedModelState.Idle

                } catch (e: Exception) {
                    _state.value = FeedModelState.Error
                }
            }


        /*override fun onSuccess(posts: Post) {
            _data.postValue(_data.value?.posts?.map {
                if (it.id == posts.id) posts else it
            }?.let {
                _data.value?.copy(posts = it)
            })
        }

        override fun onError(e: Exception) {
            _data.postValue(FeedModel(error = true))
        }*/
    }


    fun removeById(id: Long) {
        val old = data.value?.posts.orEmpty()
        viewModelScope
            .launch {
                try {

                    data.value?.posts.let {
                        data.value?.copy(posts = data.value?.posts.orEmpty()
                            .filter { it.id != id }
                        )
                    }
                    repository.removeById(id)
                    _state.value = FeedModelState.Idle
                } catch (e: Exception) {

                    data.value?.posts.let { (data.value?.copy(posts = old)) }
                    _state.value = FeedModelState.Error
                }

            }

        // Оптимистичная модель
        /*val old = _data.value?.posts.orEmpty()

        try {
            {
                override fun onSuccess(posts: Unit) {
                    _data.postValue(
                        _data.value?.copy(posts = _data.value?.posts.orEmpty()
                            .filter { it.id != id }
                        )
                    )
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        } catch (e: IOException) {
            _data.postValue(_data.value?.copy(posts = old))
        }*/

    }


}
