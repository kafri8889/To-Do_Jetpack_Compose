package com.anafthdev.todo.ui

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.anafthdev.todo.R
import com.anafthdev.todo.data.CategoryColor
import com.anafthdev.todo.model.Category
import com.anafthdev.todo.model.DrawerMenu
import com.anafthdev.todo.ui.theme.*
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import timber.log.Timber

@OptIn(ExperimentalUnitApi::class)
@Composable
fun DrawerItem(
	drawerMenu: DrawerMenu,
	todoCount: Int,
	isSelected: Boolean,
	todoCountVisible: Boolean = true,
	onClick: () -> Unit
) {
	if ((drawerMenu.iconRes == null) and (drawerMenu.iconVector == null)) throw RuntimeException("no icon in drawerItem")
	
	val background = Brush.linearGradient(
		colors = listOf(
			black.copy(alpha = 0.1f),
			Color.White
		)
	)
	
	Card(
		elevation = 0.dp,
		shape = RoundedCornerShape(8.dp),
		backgroundColor = Color.Transparent,
		modifier = Modifier
			.fillMaxWidth()
			.height(48.dp)
			.clip(RoundedCornerShape(8.dp))
			.clickable(
				indication = rememberRipple(color = black.copy(alpha = 0.8f)),
				interactionSource = remember { MutableInteractionSource() }
			) {
				onClick()
			}
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.then(run {
					if (isSelected) Modifier.background(background)
					else Modifier.background(Color.Transparent)
				})
		) {

			if (drawerMenu.iconVector == null) {
				Icon(
					painter = drawerMenu.iconRes!!,
					contentDescription = null,
					tint = black.copy(alpha = 0.8f),
					modifier = Modifier
						.size(18.dp)
						.padding(start = 8.dp)
						.weight(0.3f)
				)
			} else {
				Icon(
					imageVector = drawerMenu.iconVector,
					contentDescription = null,
					tint = black.copy(alpha = 0.8f),
					modifier = Modifier
						.size(18.dp)
						.padding(start = 8.dp)
						.weight(0.3f)
				)
			}

			Text(
				text = drawerMenu.name,
				color = black.copy(alpha = 0.8f),
				fontSize = TextUnit(14f, TextUnitType.Sp),
				fontWeight = FontWeight.Bold,
				modifier = Modifier
					.padding(8.dp)
					.weight(2f),
			)

			
			
			// Show To-Do size or not
			if (todoCountVisible) {
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Center,
					modifier = Modifier
						.size(28.dp)
						.padding(end = 8.dp)
						.weight(0.32f)
						.background(
							gray.copy(alpha = 0.12f),
							shape = RoundedCornerShape(5.dp)
						)
				) {
					Text(
						text = todoCount.toString(),
						textAlign = TextAlign.Center,
						fontSize = TextUnit(12f, TextUnitType.Sp),
						fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
						color = gray.copy(
							alpha = if (isSelected) 1f else 0.8f
						)
					)
				}
			} else {
				Spacer(
					modifier = Modifier
						.size(28.dp)
						.padding(end = 8.dp)
						.weight(0.28f)
				)
			}

		}
	}
}

@Preview(showBackground = true)
@Composable
fun DrawerMenuPreview() {
	val drawerMenu = DrawerMenu(
		"Dashboard",
		iconRes = painterResource(id = R.drawable.ic_space_dashboard),
		iconVector = null
	)
	
	Column {
		DrawerItem(
			drawerMenu = drawerMenu,
			todoCount = 4,
			isSelected = true
		) {}
		
		DrawerItem(
			drawerMenu = drawerMenu,
			todoCount = 60,
			isSelected = false
		) {}
	}
}





@OptIn(ExperimentalUnitApi::class)
@Composable
fun CategoryItem(
	category: Category,
	todoCount: Int,
	showMenu: Boolean = false,
	isSelected: Boolean = false,
	asDrawerItem: Boolean = true,
	onEdit: () -> Unit = {},
	onEditAsDrawerItem: () -> Unit = {},
	onDelete: () -> Unit = {},
	onClick: () -> Unit
) {
	val menuLabels = listOf(
		"Edit",
		"Delete"
	)
	
	val menuIcons = listOf(
		Icons.Default.Edit,
		Icons.Default.Delete
	)
	
	var expandMenu by remember { mutableStateOf(false) }
	
	val background = Brush.linearGradient(
		colors = listOf(
			Color(category.color).copy(alpha = 0.1f),
			Color.White
		)
	)
	
	Card(
		elevation = 0.dp,
		shape = RoundedCornerShape(8.dp),
		backgroundColor = Color.Transparent,
		modifier = Modifier
			.fillMaxWidth()
			.height(48.dp)
			.clip(RoundedCornerShape(8.dp))
			.clickable(
				indication = rememberRipple(color = black.copy(alpha = 0.8f)),
				interactionSource = remember { MutableInteractionSource() }
			) { onClick() }
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.fillMaxWidth()
				.then(run {
					if (isSelected) Modifier.background(background)
					else Modifier.background(Color.Transparent)
				})
		) {
			
			Icon(
				painter = painterResource(id = R.drawable.ic_rect),
				contentDescription = null,
				tint = Color(category.color),
				modifier = Modifier
					.size(18.dp)
					.padding(start = 8.dp)
					.weight(0.3f)
			)
			
			Text(
				text = category.name,
				color = black.copy(alpha = 0.8f),
				fontSize = TextUnit(14f, TextUnitType.Sp),
				fontWeight = FontWeight.Bold,
				modifier = Modifier
					.padding(8.dp)
					.weight(2f),
			)
			
			if (isSelected or showMenu) {
				IconButton(
					onClick = {
						expandMenu = true
					},
					modifier = Modifier
						.size(32.dp)
						.padding(end = 8.dp, start = 8.dp)
						.background(
							gray.copy(alpha = 0.12f),
							shape = RoundedCornerShape(5.dp)
						)
						.weight(0.45f)
				) {
					Icon(
						imageVector = Icons.Default.MoreVert,
						tint = black.copy(alpha = 0.8f),
						contentDescription = null
					)
					
					DropdownMenu(
						expanded = expandMenu,
						onDismissRequest = {
							expandMenu = false
						},
						modifier = Modifier
							.clip(RoundedCornerShape(24.dp))
					) {
						for ((i, s) in menuLabels.withIndex()) {
							DropdownMenuItem(
								onClick = {
									when (i) {
										0 -> if (asDrawerItem) onEditAsDrawerItem() else onEdit()
										1 -> onDelete()
									}
									expandMenu = false
								},
							) {
								Icon(
									imageVector = menuIcons[i],
									tint = black.copy(alpha = 0.8f),
									contentDescription = null,
									modifier = Modifier
										.padding(end = 8.dp)
								)
								
								Text(
									text = s,
									color = black.copy(alpha = 0.8f)
								)
							}
						}
					}
				}
			} else {
				Spacer(
					modifier = Modifier
						.size(32.dp)
						.padding(end = 8.dp, start = 8.dp)
						.weight(0.5f)
				)
			}
			
			
			
			// TO-DO Size
			Column(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center,
				modifier = Modifier
					.size(28.dp)
					.padding(end = 8.dp)
					.weight(0.32f)
					.background(
						gray.copy(alpha = 0.12f),
						shape = RoundedCornerShape(5.dp)
					)
			) {
				Text(
					text = todoCount.toString(),
					textAlign = TextAlign.Center,
					fontSize = TextUnit(12f, TextUnitType.Sp),
					fontWeight = if (asDrawerItem) {
						if (isSelected) FontWeight.SemiBold else FontWeight.Normal
					} else FontWeight.SemiBold,
					color = gray.copy(
						alpha = if (asDrawerItem) {
							if (isSelected) 1f else 0.8f
						} else 1f
					)
				)
			}
			
		}
	}
}

@Preview(showBackground = true)
@Composable
fun CategoryDrawerItemPreview() {
	val categoryHealth = Category(
		"Health",
		0xFF904D00
	)
	
	val categoryPersonal = Category(
		"Personal",
		0xFF745943
	)
	
	Column {
		CategoryItem(
			category = categoryHealth,
			todoCount = 6,
			isSelected = true
		) {}
		
		CategoryItem(
			category = categoryPersonal,
			todoCount = 150,
			isSelected = false
		) {}
	}
}





@OptIn(
	ExperimentalComposeUiApi::class,
	ExperimentalFoundationApi::class,
	ExperimentalAnimationApi::class
)
@Composable
fun CategoryItemInput(
	name: String,
	color: Long,
	textFieldFocusRequester: FocusRequester,
	inputModeEdit: Boolean = false,
	onValueChange: (String, Long) -> Unit,
	onDone: (String, Long) -> Unit
) {
	val keyboardController = LocalSoftwareKeyboardController.current
	
	Column(
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Row {
			OutlinedTextField(
				value = name,
				maxLines = 1,
				keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
				textStyle = TextStyle(
					color = black.copy(alpha = 0.8f)
				),
				onValueChange = { s ->
					onValueChange(s, color)
				},
				label = {
					Text("Category Name")
				},
				keyboardActions = KeyboardActions(
					onDone = {
						keyboardController?.hide()
					}
				),
				trailingIcon = {
					AnimatedVisibility(
						visible = name.isNotBlank(),
						enter = scaleIn(),
						exit = scaleOut()
					) {
						Row(
							verticalAlignment = Alignment.CenterVertically
						) {
							Divider(
								color = black.copy(alpha = 0.6f),
								modifier = Modifier.size(1.dp, 40.dp)
							)
							
							IconButton(
								onClick = {
									keyboardController?.hide()
									onDone(name, color)
								}
							) {
								if (inputModeEdit) Icon(
									painter = painterResource(id = R.drawable.ic_save),
									tint = black.copy(alpha = 0.6f),
									contentDescription = null
								) else Icon(
									imageVector = Icons.Rounded.Add,
									contentDescription = null
								)
							}
						}
					}
				},
				modifier = Modifier
					.weight(1f)
					.padding(end = 8.dp, start = 8.dp)
					.focusRequester(textFieldFocusRequester)
			)
		}
		
		
		
		// Color Selector
		AnimatedVisibility(
			visible = name.isNotBlank(),
			enter = expandVertically(
					animationSpec = spring(
					dampingRatio = 0.4f,  // Bounce damping ratio
					stiffness = Spring.StiffnessLow
				)
			),
			modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
		) {
			FlowRow(
				mainAxisAlignment = FlowMainAxisAlignment.Center
			) {
				CategoryColor.values.forEach { hex ->
					Column(
						horizontalAlignment = Alignment.CenterHorizontally,
						verticalArrangement = Arrangement.Center,
						modifier = Modifier
							.size(34.dp)
							.clip(RoundedCornerShape(25))
							.border(
								width = 1.dp,
								color = if (color == hex) primary_light else Color.Transparent,
								shape = RoundedCornerShape(25)
							)
							.clickable(
								interactionSource = MutableInteractionSource(),
								indication = rememberRipple(
									color = Color(hex)
								)
							) { onValueChange(name, hex) }
							.animateEnterExit(
								enter = scaleIn(
									animationSpec = tween(
										durationMillis = 800,
										delayMillis = 200
									)
								),
								exit = scaleOut()
							)
					) {
						Box(
							modifier = Modifier
								.size(24.dp)
								.clip(RoundedCornerShape(100))
								.background(Color(hex))
						)
					}
				}
			}
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun CategoryItemInputPreview() {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Row {
			OutlinedTextField(
				value = "",
				onValueChange = {},
				label = {
					Text("Category Name")
				},
				keyboardActions = KeyboardActions(
					onDone = {
					
					}
				),
				trailingIcon = {
					Row(
						verticalAlignment = Alignment.CenterVertically
					) {
						Divider(
							color = black.copy(alpha = 0.6f),
							modifier = Modifier.size(1.dp, 40.dp)
						)
						
						IconButton(onClick = {}) {
							Icon(
								imageVector = Icons.Rounded.Add,
								contentDescription = null
							)
						}
					}
				},
				modifier = Modifier
					.weight(1f)
					.padding(end = 8.dp, start = 8.dp)
			)
			
//			OutlinedButton(
//				onClick = {},
//				border = BorderStroke(
//					1.dp,
//					primary_light
//				),
//				modifier = Modifier
//					.align(Alignment.CenterVertically)
//					.padding(end = 8.dp)
//			) {
//				Text("Add")
//			}
		}
		
		FlowRow(
			modifier = Modifier
				.padding(top = 8.dp, bottom = 8.dp)
		) {
			CategoryColor.values.forEach { hex ->
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Center,
					modifier = Modifier
						.size(36.dp)
						.border(
							width = 1.dp,
							color = on_surface_light.copy(alpha = 0.6f),
							shape = RoundedCornerShape(25)
						)
				) {
					Box(
						modifier = Modifier
							.size(24.dp)
							.clip(RoundedCornerShape(100))
							.background(Color(hex))
					)
				}
			}
		}
	}
}
