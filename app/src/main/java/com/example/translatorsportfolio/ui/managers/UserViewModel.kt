package com.example.translatorsportfolio.ui.managers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.translatorsportfolio.AppApplication
import com.example.translatorsportfolio.data.ExpRepository
import com.example.translatorsportfolio.data.UserRepository
import com.example.translatorsportfolio.model.Experience
import com.example.translatorsportfolio.model.UserInfo
import com.example.translatorsportfolio.model.defaultExp
import com.example.translatorsportfolio.model.defaultUser
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

    private var _mainExp = MutableStateFlow(listOf(defaultExp))
    var mainExp: StateFlow<List<Experience>> = _mainExp.asStateFlow()


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
                val experiences = expRepository.getAllItemsStream().first()
                if (experiences.isEmpty()) {
                    expRepository.insertItem(defaultExp)
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
    }

    fun addExp(experience: Experience) {
        viewModelScope.launch {
            expRepository.insertItem(experience)
            expRepository.insertItem(defaultExp)
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