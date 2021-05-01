package com.dvm.auth.restore

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dvm.auth.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Code(
    code: String,
    onCodeChanged: (String) -> Unit,
    onComplete: () -> Unit
) {
    val focus = FocusRequester()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(null) {
        focus.requestFocus()
    }

    Text(
        stringResource(R.string.password_restoration_code_description),
        Modifier
            .fillMaxWidth()
            .wrapContentWidth()
    )
    Spacer(modifier = Modifier.padding(bottom = 15.dp))
    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CodeItem(code.getOrNull(0))
            CodeItem(code.getOrNull(1))
            CodeItem(code.getOrNull(2))
            CodeItem(code.getOrNull(3))
        }
        TextField(
            value = code,
            onValueChange = { codeValue ->
                if (codeValue.count() >= 4) {
                    keyboardController?.hideSoftwareKeyboard()
                    onComplete()
                }
                if (codeValue.count() <= 4) {
                    onCodeChanged(codeValue)
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                cursorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .size(280.dp, 70.dp)
                .wrapContentWidth()
                .focusRequester(focus),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

    }
    Spacer(modifier = Modifier.height(150.dp))
}

@Composable
private fun CodeItem(number: Char?) {
    Text(
        text = (number ?: "").toString(),
        modifier = Modifier
            .padding(10.dp)
            .size(50.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.secondary,
                shape = RoundedCornerShape(4.dp)
            )
            .wrapContentSize()
    )
}