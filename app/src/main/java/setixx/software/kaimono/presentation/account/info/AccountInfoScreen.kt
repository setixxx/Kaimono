package setixx.software.kaimono.presentation.account.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import setixx.software.kaimono.presentation.navigation.Routes
import setixx.software.kaimono.R
import setixx.software.kaimono.presentation.components.ListWithTwoIcons
import setixx.software.kaimono.presentation.components.PasswordChangeSheetContent

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
    val passwordChangeSheetState = rememberModalBottomSheetState()

    val genders = listOf(stringResource(R.string.label_male), stringResource(R.string.label_female))
    var selectedGenderIndex by remember { mutableIntStateOf(0) }


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
                onClick = {  },
                expanded = false,
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
                        label = { Text(stringResource(R.string.hint_name)) }
                    )
                }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp, top = 8.dp, bottom = 8.dp),
                    value = state.surname,
                    onValueChange = viewModel::onSurnameChange,
                    label = { Text(stringResource(R.string.hint_surname)) }
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
                        label = { Text(stringResource(R.string.hint_phone)) }
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
                        label = { Text(stringResource(R.string.hint_email)) }
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
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = state.dateOfBirth,
                        onValueChange = viewModel::onDateOfBirthChange,
                        label = { Text(stringResource(R.string.hint_date_of_birth)) }
                    )
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
                            onCheckedChange = { selectedGenderIndex = index },
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
                PasswordChangeSheetContent(
                    onClose = { showPasswordChangeSheet = false }
                )
            }
        }
    }
}
