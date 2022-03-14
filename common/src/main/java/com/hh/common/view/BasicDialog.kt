package com.hh.common.view

/**
 * @ProjectName: playandroid
 * @Package: com.hh.common.view
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/12/31  15:12
 */
import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.ImeAction
import com.hh.common.R
import com.vanpra.composematerialdialogs.iconTitle
import com.vanpra.composematerialdialogs.input
import com.vanpra.composematerialdialogs.message
import com.vanpra.composematerialdialogs.title

/**
 * @brief Basic Dialog Demos
 */
@Composable
fun BasicDialogDemo() {
    DialogAndShowButton(buttonText = "Basic Dialog") {
        title(res = R.string.tips)
        message(res = R.string.no_data)
    }

    DialogAndShowButton(
        buttonText = "Basic Dialog With Buttons",
        buttons = {
            negativeButton("Disagree")
            positiveButton("Agree")
        }
    ) {
        title(res = R.string.tips)
        message(res = R.string.no_data)
    }

    DialogAndShowButton(
        buttonText = "Basic Dialog With Buttons and Icon Title",
        buttons = {
            negativeButton("Disagree")
            positiveButton("Agree")
        }
    ) {
        iconTitle(
            icon = {
                Image(
                    Icons.Default.LocationOn,
                    contentDescription = "Location Icon",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                )
            },
            textRes = R.string.tips
        )
        message(res = R.string.no_data)
    }

    DialogAndShowButton(
        buttonText = "Basic Dialog With Stacked Buttons",
        buttons = {
            negativeButton("No Thanks")
            positiveButton("Turn On Speed Boost")
        }
    ) {
        title(res = R.string.tips)
        message(res = R.string.no_data)
    }

    DialogAndShowButton(
        buttonText = "Basic Input Dialog",
        buttons = {
            negativeButton("Cancel")
            positiveButton("Ok")
        }
    ) {
        title(res = R.string.tips)
        input(label = "Name", hint = "Jon Smith") {
            Log.d("SELECTION:", it)
        }
    }

    DialogAndShowButton(
        buttonText = "Basic Input Dialog With Immediate Focus",
        buttons = {
            negativeButton("Cancel")
            positiveButton("Ok")
        }
    ) {
        val focusRequester = remember { FocusRequester() }
        title(res = R.string.tips)
        input(
            label = "Name",
            hint = "Jon Smith",
            focusRequester = focusRequester,
            focusOnShow = true
        ) {
            Log.d("SELECTION:", it)
        }
    }

    DialogAndShowButton(
        buttonText = "Input Dialog with submit on IME Action",
        buttons = {
            negativeButton("Cancel")
            positiveButton("Ok")
        }
    ) {
        title(res = R.string.tips)
        input(
            label = "Name", hint = "Jon Smith",
            keyboardActions = KeyboardActions(
                onDone = { submit() }
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        ) {
            Log.d("SELECTION:", it)
        }
    }

    DialogAndShowButton(
        buttonText = "Input Dialog with input validation",
        buttons = {
            negativeButton("Cancel")
            positiveButton("Ok")
        }
    ) {
        title("Please enter your email")
        input(
            label = "Email",
            hint = "hello@example.com",
            errorMessage = "Invalid email",
            isTextValid = {
                Patterns.EMAIL_ADDRESS.matcher(it).matches() && it.isNotEmpty()
            }
        ) {
            Log.d("SELECTION:", it)
        }
    }
}