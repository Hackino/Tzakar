package com.senior25.tzakar.ui.presentation.screen.registration.sign_up

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.button.OutlinedCustomButton
import com.senior25.tzakar.ui.presentation.components.checkbox.RoundedCheckbox
import com.senior25.tzakar.ui.presentation.components.fields.EmailField
import com.senior25.tzakar.ui.presentation.components.fields.PasswordField
import com.senior25.tzakar.ui.presentation.components.fields.userNameField
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH1
import com.senior25.tzakar.ui.theme.fontLink
import com.senior25.tzakar.ui.theme.fontParagraphL
import com.senior25.tzakar.ui.theme.fontParagraphM
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.already_have_an_account
import tzakar_reminder.composeapp.generated.resources.app_icon
import tzakar_reminder.composeapp.generated.resources.copyright
import tzakar_reminder.composeapp.generated.resources.dont_have_an_account
import tzakar_reminder.composeapp.generated.resources.email_address
import tzakar_reminder.composeapp.generated.resources.enter_email_address
import tzakar_reminder.composeapp.generated.resources.enter_password
import tzakar_reminder.composeapp.generated.resources.enter_username
import tzakar_reminder.composeapp.generated.resources.forgot_password
import tzakar_reminder.composeapp.generated.resources.ic_email
import tzakar_reminder.composeapp.generated.resources.ic_eye_off
import tzakar_reminder.composeapp.generated.resources.ic_google
import tzakar_reminder.composeapp.generated.resources.ic_lock
import tzakar_reminder.composeapp.generated.resources.ic_person
import tzakar_reminder.composeapp.generated.resources.ic_sign_in
import tzakar_reminder.composeapp.generated.resources.lets_create_your_account
import tzakar_reminder.composeapp.generated.resources.lets_sign_in_and_get_starter
import tzakar_reminder.composeapp.generated.resources.lets_sign_up_and_join_the_journey
import tzakar_reminder.composeapp.generated.resources.password
import tzakar_reminder.composeapp.generated.resources.remember_me
import tzakar_reminder.composeapp.generated.resources.sign_in
import tzakar_reminder.composeapp.generated.resources.sign_in_with_google
import tzakar_reminder.composeapp.generated.resources.sign_up
import tzakar_reminder.composeapp.generated.resources.sign_up_with_google
import tzakar_reminder.composeapp.generated.resources.username
import tzakar_reminder.composeapp.generated.resources.welcome_back

@Composable
fun SignUpScreen() {

    Column(
        modifier =  Modifier.fillMaxSize()
            .background(MyColors.colorOffWhite)
            .padding(bottom = 24.dp, top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                modifier = Modifier.size(48.dp),
                painter = painterResource(Res.drawable.app_icon),
                contentDescription =  ""
            )
            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MyColors.colorPurple)) {
                    append("T")
                }
                append("zakar")
            }
            Text(
                text = annotatedText,
                style = fontH1.copy(
                    color = MyColors.colorDarkBlue,
                    textAlign = TextAlign.Center,
                ),
            )
        }


        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape =  RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MyColors.colorLightGrey,),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.lets_create_your_account),
                    color = MyColors.colorDarkBlue,
                    textAlign = TextAlign.Center,
                    style = fontH1.copy(fontSize = 34.sp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.lets_sign_up_and_join_the_journey),
                    color = MyColors.colorLightDarkBlue,
                    textAlign = TextAlign.Center,
                    style = fontParagraphL
                )

                Spacer(modifier = Modifier.height(24.dp))


                userNameField(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    label = stringResource(Res.string.username),
                    placeHolder = stringResource(Res.string.enter_username),
                    value = "",
                    onValueChange = {},
                    isInputValid = {},
                    imeAction = null,
                    focusRequester = null,
                    onKeyPressed = {},
                    leadingIcon = painterResource(Res.drawable.ic_person)
                )

                Spacer(modifier = Modifier.height(16.dp))

                EmailField(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    label = stringResource(Res.string.email_address),
                    placeHolder = stringResource(Res.string.enter_email_address),
                    value = "",
                    onValueChange = {},
                    isInputValid = {},
                    imeAction = null,
                    focusRequester = null,
                    onKeyPressed = {},
                    leadingIcon = painterResource(Res.drawable.ic_email)
                )

                Spacer(modifier = Modifier.height(16.dp))

                PasswordField (
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    label  = stringResource(Res.string.password),
                    placeHolder = stringResource(Res.string.enter_password),
                    value = null,
                    onValueChange = {},
                    isInputValid = {},
                    imeAction = null,
                    focusRequester = null,
                    onKeyPressed = {},
                    leadingIcon = painterResource(Res.drawable.ic_lock),
                    trailingIcon = painterResource(Res.drawable.ic_eye_off)
                )


                Spacer(modifier = Modifier.height(16.dp))

                CustomButton(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    onClick = {  },
                    endIcon = painterResource(Res.drawable.ic_sign_in),
                    text = stringResource(Res.string.sign_up)
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedCustomButton(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    onClick = { },
                    keepIconColor = true,
                    startIcon = painterResource(Res.drawable.ic_google),
                    text = stringResource(Res.string.sign_up_with_google)
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val annotatedText = buildAnnotatedString {
                append(stringResource(Res.string.already_have_an_account))
                append(" ")
                withStyle(style = SpanStyle(color = MyColors.colorPurple)) {
                    pushStringAnnotation(tag = "SIGN_UP", annotation = "sign_up")
                    append(stringResource(Res.string.sign_in))
                    pop()
                }
            }

            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations(
                        tag = "SIGN_UP",
                        start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                            //navigate to sign up
                        }
                },
                style = fontLink.copy(
                    color = MyColors.colorDarkBlue,
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.copyright),
                color = MyColors.colorLightDarkBlue,
                textAlign = TextAlign.Center,
                style = fontParagraphM
            )
        }

    }
}