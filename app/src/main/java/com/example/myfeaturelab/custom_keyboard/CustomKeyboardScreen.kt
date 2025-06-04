package com.example.myfeaturelab.custom_keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomKeyboardScreen() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    var showKeyboard by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Box(Modifier.fillMaxSize()) {
        /* ① 입력창 */
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged { state ->
                    if (state.isFocused) {
                        showKeyboard = true
                        keyboardController?.hide()    // 시스템 키보드 숨기기
                    }
                }
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .padding(12.dp),
            singleLine = true
        )

        /* ② 커스텀 키보드 */
        if (showKeyboard) {
            ArabicKeyboard(
                modifier =  Modifier.align(Alignment.BottomCenter),
                onKey = { ch -> text = text.copy(text = text.text + ch) },
                onBackspace = {
                    if (text.text.isNotEmpty())
                        text = text.copy(text = text.text.dropLast(1))
                },
                onClose = {
                    showKeyboard = false
                    focusManager.clearFocus()
                }
            )
        }
    }
}

@Composable
private fun KeyButton(label: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier
            .size(48.dp)
            .padding(2.dp),
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(label, fontSize = 18.sp)
    }
}

/* 아랍어 키보드 전체 */
@Composable
private fun ArabicKeyboard(
    modifier: Modifier = Modifier,
    onKey: (String) -> Unit,
    onBackspace: () -> Unit,
    onClose: () -> Unit
) {
    val rows = listOf(
        listOf("ا", "ب", "ت", "ث", "ج", "ح", "خ"),
        listOf("د", "ذ", "ر", "ز", "س", "ش", "ص"),
        listOf("ض", "ط", "ظ", "ع", "غ", "ف", "ق"),
        listOf("ك", "ل", "م", "ن", "ه", "و", "ي")
    )

    Column(
        modifier
            .fillMaxWidth()
            .background(Color(0xFF222222))
            .navigationBarsPadding()
            .padding(bottom = 8.dp)
    ) {
        rows.forEach { row ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { c -> KeyButton(c) { onKey(c) } }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            KeyButton("⌫") { onBackspace() }
            KeyButton("닫기") { onClose() }
        }
    }
}