package setixx.software.kaimono.presentation.product

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import setixx.software.kaimono.R
import setixx.software.kaimono.presentation.components.ListWithTwoIcons
import setixx.software.kaimono.presentation.components.ProductImageCarousel
import setixx.software.kaimono.presentation.components.ReviewCardSquare
import setixx.software.kaimono.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProductScreen(
    navController: NavController
){
    val options = listOf("S", "M", "L", "XL")
    var selectedIndex by remember { mutableIntStateOf(0) }
    var isDescriptionExpanded by remember { mutableStateOf(false) }
    var isAdded by remember { mutableStateOf(false) }
    var quantity by remember { mutableIntStateOf(1) }
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState(0)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProductImageCarousel(
                images = listOf(
                    ImageBitmap.imageResource(R.drawable.placeholder),
                    ImageBitmap.imageResource(R.drawable.placeholder),
                    ImageBitmap.imageResource(R.drawable.placeholder),
                    ImageBitmap.imageResource(R.drawable.placeholder),
                    ImageBitmap.imageResource(R.drawable.placeholder),
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = "Product name • 4.5",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(
                    onClick = {}
                ) {
                    Icon(Icons.Outlined.Favorite, contentDescription = "Favorite")
                }
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "100" + stringResource(R.string.label_currency),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Normal
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp)
                    .clip(MaterialTheme.shapes.large),
            ) {
                ListWithTwoIcons(
                    contentDescription = stringResource(R.string.label_information),
                    header = stringResource(R.string.label_information),
                    onClick = { isDescriptionExpanded = !isDescriptionExpanded },
                    trailingIcon = if (isDescriptionExpanded) {
                        Icons.Outlined.ExpandLess
                    } else {
                        Icons.Outlined.ExpandMore
                    }
                )

                AnimatedVisibility(
                    visible = isDescriptionExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .clip(MaterialTheme.shapes.large)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            text = stringResource(R.string.label_article_number),
                            color = MaterialTheme.colorScheme.onSecondaryFixed,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .padding(bottom = 4.dp)
                                .clickable(true, onClick = {
                                    clipboardManager.setText(AnnotatedString("Текст для копирования"))
                                }),
                            text = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            text = stringResource(R.string.label_description),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .padding(bottom = 4.dp),
                            text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            text = stringResource(R.string.label_categories),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .padding(bottom = 8.dp),
                            text = "Category 1, Category 2, Category 3",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                        text = stringResource(R.string.label_reviews),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    FilledIconButton(
                        onClick = {
                            navController.navigate(Routes.ProductReviews.route)
                        },
                        Modifier
                            .size(
                                IconButtonDefaults.extraSmallContainerSize(
                                    IconButtonDefaults.IconButtonWidthOption.Narrow
                                )
                            ),
                        shape = MaterialTheme.shapes.large,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                LazyRow(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(5) {
                        ReviewCardSquare(
                            withImageAndDate = false,
                            isExpanded = false,
                            isEditable = false,
                        )
                    }
                }
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                text = stringResource(R.string.label_select_size),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
            ) {
                options.forEachIndexed { index, label ->
                    ToggleButton(
                        checked = selectedIndex == index,
                        onCheckedChange = { selectedIndex = index },
                        modifier = Modifier.weight(1f),
                        shapes =
                            when (index) {
                                0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                            },
                        enabled = true
                    ) {
                        if (selectedIndex == index) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                        }
                        Text(label)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                        .padding(4.dp)
                ) {
                    FilledIconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier.size(40.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(20.dp))
                    }

                    Text(
                        text = quantity.toString(),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    FilledIconButton(
                        onClick = { quantity++ },
                        modifier = Modifier.size(40.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(20.dp))
                    }
                }

                Button(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    onClick = { isAdded = true },
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Text(
                        text = if (isAdded) stringResource(R.string.label_delivered) else stringResource(R.string.action_add_to_card),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview(
    heightDp = 1200
)
@Composable
fun ProductSheetPreview() {
    ProductScreen(
        navController = rememberNavController()
    )
}
