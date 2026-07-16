package com.example.personalexpensemanager.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.data.IExpenseDataService
import com.example.personalexpensemanager.domain.Category
import com.example.personalexpensemanager.domain.validation.notEmpty
import com.example.personalexpensemanager.domain.validation.maxLength
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.coroutines.cancellation.CancellationException

class CategoriesViewModel(
    private val dataService: IExpenseDataService
) : ViewModel() {

    private val retrySignal = MutableStateFlow(0)

    private val _formErrors = MutableStateFlow(CategoryFormErrors())
    val formErrors: StateFlow<CategoryFormErrors> = _formErrors.asStateFlow()

    private val _snackbarEvent = MutableSharedFlow<Int>()
    val snackbarEvent: SharedFlow<Int> = _snackbarEvent.asSharedFlow()

    private val _nameInput = MutableStateFlow("")
    private var editingCategoryId: String? = null

    val uiState: StateFlow<ICategoriesUIState> = retrySignal.flatMapLatest {
        dataService.categories
            .map { categories -> ICategoriesUIState.Success(categories) as ICategoriesUIState }
            .catch { e -> emit(ICategoriesUIState.Error(R.string.error_load_categories)) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ICategoriesUIState.Loading
    )

    init {
        viewModelScope.launch {
            _nameInput
                .debounce(300)
                .collect { name -> validateName(name) }
        }
    }

    fun retry() { retrySignal.value++ }

    fun startEditing(category: Category?) {
        editingCategoryId = category?.id
        val initialName = category?.name ?: ""
        _formErrors.value = CategoryFormErrors()
        _nameInput.value = initialName
        validateName(initialName)
        if (category == null) {
            _formErrors.update { it.copy(iconErrorResId = R.string.validation_category_icon_required) }
        }
    }

    fun onNameChanged(name: String) {
        _nameInput.value = name
    }

    fun onIconSelected(iconName: String) {
        _formErrors.update { it.copy(iconErrorResId = null) }
    }

    fun onSubmitAttempted() {
        _formErrors.update { it.copy(submitted = true) }
    }

    fun addCategory(name: String, iconName: String) {
        validationErrors(name, iconName)?.let { _formErrors.value = it; return }

        if (isDuplicateName(name)) {
            viewModelScope.launch { _snackbarEvent.emit(R.string.validation_category_name_duplicate) }
            return
        }

        viewModelScope.launch {
            val category = Category(
                id = UUID.randomUUID().toString(),
                name = name,
                iconName = iconName
            )
            try {
                dataService.addCategory(category)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _snackbarEvent.emit(R.string.error_add_category)
            }
        }
    }

    fun updateCategory(category: Category) {
        validationErrors(category.name, category.iconName)?.let { _formErrors.value = it; return }

        if (isDuplicateName(category.name)) {
            viewModelScope.launch { _snackbarEvent.emit(R.string.validation_category_name_duplicate) }
            return
        }

        viewModelScope.launch {
            try {
                dataService.updateCategory(category)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _snackbarEvent.emit(R.string.error_update_category)
            }
        }
    }

    fun deleteCategory(categoryId: String) {
        viewModelScope.launch {
            try {
                dataService.deleteCategory(categoryId)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _snackbarEvent.emit(R.string.error_delete_category)
            }
        }
    }

    private fun validateName(name: String) {
        _formErrors.update { it.copy(nameErrorResId = nameError(name)) }
    }

    private fun nameError(name: String): Int? =
        notEmpty(name, R.string.validation_category_name_empty)
            ?: maxLength(name, 20, R.string.validation_category_name_too_long)

    private fun validationErrors(name: String, iconName: String): CategoryFormErrors? {
        val nameErr = nameError(name)
        val iconErr = if (iconName.isBlank()) R.string.validation_category_icon_required
        else null
        return if (nameErr != null || iconErr != null)
            CategoryFormErrors(nameErrorResId = nameErr, iconErrorResId = iconErr, submitted = true)
        else null
    }

    private fun isDuplicateName(name: String): Boolean =
        dataService.categories.value.any {
            it.id != editingCategoryId && it.name.equals(name, ignoreCase = true)
        }
}