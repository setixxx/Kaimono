package setixx.software.kaimono.presentation.account.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import setixx.software.kaimono.R
import setixx.software.kaimono.presentation.components.ListWithTwoIcons
import setixx.software.kaimono.presentation.account.password.PasswordChangeSheetContent
import setixx.software.kaimono.presentation.account.password.PasswordChangeViewModel
import setixx.software.kaimono.presentation.navigation.Routes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AccountInfoScreen(
    navController: NavController,
    viewModel: AccountInfoViewModel
){
    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            snackBarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    var showPasswordChangeSheet by remember { mutableStateOf(false) }
    val passwordChangeSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val genders = listOf(stringResource(R.string.label_male), stringResource(R.string.label_female))
    val selectedGenderIndex = if (state.gender == "Female") 1 else 0


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.label_personal_info),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.Account.route){
                                popUpTo(Routes.Account.route){ inclusive = true }
                            }
                        }
                    ) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (state.isFieldsChanged) {
                        viewModel.updateUserInfo()
                    }
                },
                containerColor = if (state.isFieldsChanged)
                    MaterialTheme.colorScheme.primaryContainer
                else Color.Gray,
                contentColor = if (state.isFieldsChanged)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else Color.DarkGray,
                expanded = state.isFieldsChanged,

                icon = { Icon(Icons.Filled.Save,
                    stringResource(R.string.action_save)) },
                text = { Text(text = stringResource(R.string.action_save)) },
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(end = 24.dp),
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Person",
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = state.name,
                        onValueChange = viewModel::onNameChange,
                        label = { Text(stringResource(R.string.hint_name)) },
                        isError = state.nameError != null,
                        supportingText = { state.nameError?.let { Text(text = it) } },
                        singleLine = true,
                        enabled = !state.isLoading,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp, top = 8.dp, bottom = 8.dp),
                    value = state.surname,
                    onValueChange = viewModel::onSurnameChange,
                    label = { Text(stringResource(R.string.hint_surname)) },
                    isError = state.surnameError != null,
                    supportingText = { state.surnameError?.let { Text(text = it) } },
                    singleLine = true,
                    enabled = !state.isLoading,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                Row(

                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Icon(
                        modifier = Modifier
                            .padding(end = 24.dp),
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = "Person",
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = state.phone,
                        onValueChange = viewModel::onPhoneChange,
                        label = { Text(stringResource(R.string.hint_phone)) },
                        isError = state.phoneError != null,
                        supportingText = { state.phoneError?.let { Text(text = it) } },
                        singleLine = true,
                        enabled = !state.isLoading,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Icon(
                        modifier = Modifier
                            .padding(end = 24.dp),
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Person",
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = state.email,
                        onValueChange = viewModel::onEmailChange,
                        label = { Text(stringResource(R.string.hint_email)) },
                        isError = state.emailError != null,
                        supportingText = { state.emailError?.let { Text(text = it) } },
                        singleLine = true,
                        enabled = !state.isLoading,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Icon(
                        modifier = Modifier
                            .padding(end = 24.dp),
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = "Person",
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = state.dateOfBirth,
                            onValueChange = viewModel::onDateOfBirthChange,
                            readOnly = true,
                            label = { Text(stringResource(R.string.hint_date_of_birth)) },
                            isError = state.dateOfBirthError != null,
                            supportingText = { state.dateOfBirthError?.let { Text(text = it) } },
                            singleLine = true,
                            enabled = !state.isLoading,
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { showDatePicker = true }
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        ButtonGroupDefaults.ConnectedSpaceBetween
                    ),
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                ) {
                    genders.forEachIndexed { index, label ->
                        ToggleButton(
                            checked = selectedGenderIndex == index,
                            onCheckedChange = { 
                                val gender = if (index == 0) "Male" else "Female"
                                viewModel.onGenderChange(gender)
                            },
                            modifier = Modifier.weight(1f),
                            shapes =
                                when (index) {
                                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                    genders.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                                },
                        ) {
                            if (selectedGenderIndex == index) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null
                                )
                            }
                            Text(label)
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.large)
                ){
                    ListWithTwoIcons(
                        header = stringResource(R.string.label_password_change),
                        contentDescription = stringResource(R.string.label_password_change),
                        trailingIcon = Icons.Filled.ChevronRight,
                        onClick = { showPasswordChangeSheet = true }
                    )
                }
            }
        }
        if (showPasswordChangeSheet) {
            ModalBottomSheet(
                onDismissRequest = { showPasswordChangeSheet = false },
                sheetState = passwordChangeSheetState
            ) {
                val passwordViewModel: PasswordChangeViewModel = hiltViewModel()
                PasswordChangeSheetContent(
                    onClose = { showPasswordChangeSheet = false },
                    viewModel = passwordViewModel
                )
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                formatter.timeZone = TimeZone.getTimeZone("UTC")
                                viewModel.onDateOfBirthChange(formatter.format(Date(millis)))
                            }
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}
