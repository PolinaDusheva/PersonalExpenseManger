package com.example.personalexpensemanager.ui.goals

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.personalexpensemanager.R
import com.example.personalexpensemanager.domain.Goal
import com.example.personalexpensemanager.ui.components.AppSnackbarHost
import com.example.personalexpensemanager.ui.components.ErrorScreen
import com.example.personalexpensemanager.ui.components.Headline
import com.example.personalexpensemanager.ui.components.appButtons.GradientIconButton
import com.example.personalexpensemanager.ui.goals.components.AddGoalDialog
import com.example.personalexpensemanager.ui.goals.components.GoalActionButton
import com.example.personalexpensemanager.ui.goals.components.GoalProgressItem
import com.example.personalexpensemanager.ui.goals.components.SetBudgetCard
import com.example.personalexpensemanager.ui.goals.components.SetBudgetDialog
import com.example.personalexpensemanager.ui.goals.components.MonthlyExpenseArc
import com.example.personalexpensemanager.ui.goals.components.SetDailyLimitDialog
import com.example.personalexpensemanager.ui.theme.PersonalExpenseManagerTheme
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel,
    onGoalAdded: (() -> Unit)? = null
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val formErrors = viewModel.formErrors.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var showBudgetDialog by remember { mutableStateOf(false) }
    var editingGoal by remember { mutableStateOf<Goal?>(null) }
    var showDailyLimitDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect { resId ->
            snackbarHostState.showSnackbar(context.getString(resId))
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { AppSnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            GoalsContent(
                state = state.value,
                onRetry = viewModel::retry,
                onAddGoalClick = {
                    viewModel.resetForm()
                    showAddDialog = true
                },
                onSetBudgetClick = { showBudgetDialog = true },
                onDailyLimitClick = { showDailyLimitDialog = true },
                onGoalClick = { goal ->
                    viewModel.prepareEditForm(goal)
                    editingGoal = goal
                }
            )
        }
    }

    if (showAddDialog) {
        AddGoalDialog(
            formErrors = formErrors.value,
            onTitleChanged = viewModel::onGoalTitleChanged,
            onTitleTouched = viewModel::onGoalTitleTouched,
            onAmountChanged = viewModel::onGoalAmountChanged,
            onAmountTouched = viewModel::onGoalAmountTouched,
            onConfirm = { title, amount, deadline ->
                if (viewModel.addGoal(title, amount, deadline)) {
                    showAddDialog = false
                    onGoalAdded?.invoke()
                }
            },
            onDismiss = { showAddDialog = false }
        )
    }

    if (editingGoal != null) {
        AddGoalDialog(
            formErrors = formErrors.value,
            editingGoal = editingGoal,
            onTitleChanged = viewModel::onGoalTitleChanged,
            onTitleTouched = viewModel::onGoalTitleTouched,
            onAmountChanged = viewModel::onGoalAmountChanged,
            onAmountTouched = viewModel::onGoalAmountTouched,
            onConfirm = { title, amount, deadline ->
                if (viewModel.updateGoal(editingGoal!!.id, title, amount, deadline)) {
                    editingGoal = null
                }
            },
            onDelete = { goalId ->
                viewModel.deleteGoal(goalId)
                editingGoal = null
            },
            onDismiss = { editingGoal = null }
        )
    }

    if (showBudgetDialog) {
        val currentBudget = (state.value as? IGoalsUIState.Success)?.monthlyBudget
        SetBudgetDialog(
            currentBudget = currentBudget,
            onConfirm = { amount ->
                viewModel.setMonthlyBudget(amount)
                showBudgetDialog = false
            },
            onDismiss = { showBudgetDialog = false }
        )
    }

    if (showDailyLimitDialog) {
        val currentLimit = (state.value as? IGoalsUIState.Success)?.dailyLimit
        SetDailyLimitDialog(
            currentLimit = currentLimit,
            onConfirm = { amount ->
                viewModel.setDailyLimit(amount)
                showDailyLimitDialog = false
            },
            onDismiss = { showDailyLimitDialog = false }
        )
    }
}

@Composable
fun GoalsContent(
    state: IGoalsUIState,
    onRetry: () -> Unit = {},
    onAddGoalClick: () -> Unit = {},
    onSetBudgetClick: () -> Unit = {},
    onDailyLimitClick: () -> Unit = {},
    onSavingsClick: () -> Unit = {},
    onGoalClick: (Goal) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = dimensionResource(R.dimen.padding_small))
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.padding_horizontal)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Headline(text = stringResource(R.string.goals_title))
            }
            GradientIconButton(
                icon = Icons.Filled.Add,
                contentDescription = stringResource(R.string.goals_add_description),
                onClick = onAddGoalClick
            )
        }

        when (state) {
            is IGoalsUIState.Loading -> Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is IGoalsUIState.Error -> ErrorScreen(
                messageResId = state.messageResId,
                onRetry = onRetry
            )

            is IGoalsUIState.Success -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_horizontal))) {
                    MonthlyExpenseArc(
                        totalSpent = state.totalSpentThisMonth,
                        budget = state.monthlyBudget,
                        label = stringResource(R.string.goals_budget_spent)
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.goals_background),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-80).dp),
                    contentScale = ContentScale.FillWidth
                )

                Column(
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(R.dimen.padding_horizontal))
                        .offset(y = (-100).dp)
                ) {
                    SetBudgetCard(
                        currentBudget = state.monthlyBudget,
                        onClick = onSetBudgetClick
                    )

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard)))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_standard))
                    ) {
                        GoalActionButton(
                            icon = Icons.Filled.Speed,
                            label = stringResource(R.string.goal_daily_limit),
                            onClick = onDailyLimitClick,
                            modifier = Modifier.weight(1f)
                        )
                        GoalActionButton(
                            icon = Icons.Filled.Savings,
                            label = stringResource(R.string.goal_savings),
                            onClick = onSavingsClick,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dashboard_section_spacer)))

                    if (state.goals.isEmpty()) {
                        Text(
                            text = stringResource(R.string.goals_empty_state),
                            color = Color.Gray,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = dimensionResource(R.dimen.padding_standard))
                        )
                    } else {
                        for (goalProgress in state.goals) {
                            GoalProgressItem(
                                goalProgress = goalProgress,
                                onClick = { onGoalClick(goalProgress.goal) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_standard)))
                }
            }
        }
    }
    }
}

@Preview(showBackground = true)
@Composable
fun GoalsScreenPreview() {
    PersonalExpenseManagerTheme {
        GoalsContent(
            state = IGoalsUIState.Success(
                goals = listOf(
                    IGoalsUIState.GoalProgress(
                        goal = Goal("1", "Лятна ваканция", BigDecimal("3000.00"), LocalDate.of(2026, 8, 1)),
                        currentAmount = BigDecimal("1850.00")
                    ),
                    IGoalsUIState.GoalProgress(
                        goal = Goal("2", "Нов лаптоп", BigDecimal("2500.00"), null),
                        currentAmount = BigDecimal("600.00")
                    )
                ),
                monthlyBudget = BigDecimal("1500.00"),
                totalSpentThisMonth = BigDecimal("620.00"),
                dailyLimit = BigDecimal("50.00"),
                totalSpentToday = BigDecimal("30.00")
            )
        )
    }
}