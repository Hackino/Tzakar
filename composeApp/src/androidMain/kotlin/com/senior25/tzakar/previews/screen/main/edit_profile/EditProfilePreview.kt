package com.senior25.tzakar.previews.screen.main.edit_profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.EditProfilePageScreen
import com.senior25.tzakar.ui.presentation.screen.main.profile.ProfileScreen

@Preview
@Composable
fun EditProfilePageScreenPreview() {
    EditProfilePageScreen(PaddingValues(8.dp),null)
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(null)
}