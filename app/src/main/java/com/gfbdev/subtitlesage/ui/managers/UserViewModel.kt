package com.gfbdev.subtitlesage.ui.managers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gfbdev.subtitlesage.AppApplication
import com.gfbdev.subtitlesage.data.ExpRepository
import com.gfbdev.subtitlesage.data.UserRepository
import com.gfbdev.subtitlesage.model.Experience
import com.gfbdev.subtitlesage.model.UserInfo
import com.gfbdev.subtitlesage.model.defaultExp
import com.gfbdev.subtitlesage.model.defaultUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserViewModel(
    private val userRepository: UserRepository,
    private val expRepository: ExpRepository,
) : ViewModel() {

    init {
        getInfo()
        getExp()
    }

    private val _mainUser = MutableStateFlow(defaultUser)
    val mainUser: StateFlow<UserInfo> = _mainUser.asStateFlow()

    private var blankExp = defaultExp.copy()

    private var _mainExp = MutableStateFlow(listOf(blankExp))
    var mainExp: StateFlow<List<Experience>> = _mainExp.asStateFlow()

    private var _personalInfoBool = MutableStateFlow(false)
    var personalInfoBool = _personalInfoBool.asStateFlow()

    fun updateInfoBool(infoBool: Boolean) {
        _personalInfoBool.update {
            infoBool
        }
    }

    private fun getInfo() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userRepository.getUser().collect { user ->
                    _mainUser.value = user ?: defaultUser
                }
            }
        }
    }

    private fun getExp() {
        viewModelScope.launch {
                val experiences by mutableStateOf(expRepository.getAllItemsStream().first())
                if (experiences.isEmpty()) {
                    expRepository.insertItem(blankExp)
                    getExp()
                } else {
                    _mainExp.update {
                        experiences
                    }
                }
        }
    }

    fun saveMainUser(user: UserInfo) {
        viewModelScope.launch {
            userRepository.insertItem(user)
            _mainUser.update { user }
        }
        getInfo()
        getExp()
    }

    fun addExp(experience: Experience) {
        viewModelScope.launch {
            expRepository.insertItem(experience)
            blankExp = defaultExp.copy()
            expRepository.insertItem(blankExp)
            getExp()
        }
    }

    fun deleteExp(experience: Experience) {
        viewModelScope.launch {
            expRepository.deleteItem(experience)
            getExp()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as AppApplication)
                val userRepository = application.container.userRepository
                val expRepository = application.container.expRepository
                UserViewModel(
                    userRepository = userRepository, expRepository = expRepository
                )
            }
        }
    }
}