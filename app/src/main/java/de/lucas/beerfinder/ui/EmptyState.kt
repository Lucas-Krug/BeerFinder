package de.lucas.beerfinder.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import de.lucas.beerfinder.R
import de.lucas.beerfinder.ui.theme.White

@Composable
fun EmptyState(onClickRetry: () -> Unit) {
    Column(
        verticalArrangement = Center,
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = White,
            elevation = 8.dp,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.error_message),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
        Button(
            onClick = onClickRetry,
            colors = ButtonDefaults.buttonColors(backgroundColor = White),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .height(48.dp)
                .width(112.dp)
        ) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}

@Composable
fun NoFavorites() {
    Column(
        verticalArrangement = Center,
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_no_drinks),
            contentDescription = "",
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(64.dp)
        )
        Text(
            text = stringResource(id = R.string.no_favorites),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}