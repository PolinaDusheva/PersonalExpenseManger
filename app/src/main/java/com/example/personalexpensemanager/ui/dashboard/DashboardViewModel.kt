package com.example.personalexpensemanager.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.data.MockDataService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val dataService: MockDataService
): ViewModel() {
    private val _uiState = MutableStateFlow<DashboardUIState>(DashboardUIState.Loading)
    val uiState : StateFlow<DashboardUIState> = _uiState

    init{load()}

    private fun load(){
        viewModelScope.launch {
            _uiState.value = DashboardUIState.Loading
            try {
                _uiState.value = DashboardUIState.Success(dataService.getTransactions())
            }catch (e: Exception) {
                _uiState.value = DashboardUIState.Error(e.message ?: "Error")
            }

        }
    }
}