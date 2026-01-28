package com.rewe.digital.msco.tooltipcaret

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DefaultTooltipCaretShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipScope
import androidx.compose.material3.TooltipState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.rewe.digital.msco.tooltipcaret.ui.theme.TooltipCaretTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TooltipCaretTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun MainContent(modifier: Modifier = Modifier) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(20) {
            Text("Filler content $it")
        }

        item {
            MyTooltip()
        }

        items(20) {
            Text("Filler content $it + 21")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTooltip() {
    val tooltipState = rememberTooltipState(initialIsVisible = false, isPersistent = true)

    LaunchedEffect(Unit) {
        tooltipState.show()
    }

    MyTooltipBox(tooltipState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTooltipBox(tooltipState: TooltipState) {
    var tooltipPosition by remember {
        mutableStateOf(TooltipAnchorPosition.Above)
    }

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            tooltipPosition,
            spacingBetweenTooltipAndAnchor = 8.dp
        ),
        tooltip = {
            TooltipContent(tooltipState)
        },
        state = tooltipState,
        onDismissRequest = {},
        content = {
            TooltipAnchorContent(
                onPositionChange = {
                    tooltipPosition = if (tooltipPosition == TooltipAnchorPosition.Above) {
                        TooltipAnchorPosition.Below
                    } else {
                        TooltipAnchorPosition.Above
                    }
                }
            )
        },
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TooltipScope.TooltipContent(
    tooltipState: TooltipState,
) {
    val scope = rememberCoroutineScope()

    RichTooltip(
        colors = TooltipDefaults.richTooltipColors(
            containerColor = Color.Black.copy(alpha = 0.9f),
            titleContentColor = Color.Green,
            contentColor = Color.White,
        ),
        shape = RectangleShape,
        title = {
            Row {
                Spacer(modifier = Modifier.width(4.dp))
                Text("Awesome!")
            }
        },
        action = {
            TooltipAction(scope, tooltipState)
        },
        caretShape = DefaultTooltipCaretShape(),
    ) {
        Text(
            "You've successfully opened a rich tooltip! 🎉\n"
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TooltipAction(scope: CoroutineScope, tooltipState: TooltipState) {
    TextButton(
        onClick = {
            scope.launch {
                tooltipState.dismiss()
            }
        }
    ) {
        Text("Dismiss")
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TooltipAnchorContent(
    onPositionChange: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onPositionChange,
        ) {
            Text("Above / Below")
        }
    }
}