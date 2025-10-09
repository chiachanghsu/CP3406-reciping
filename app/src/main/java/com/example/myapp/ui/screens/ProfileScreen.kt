package com.example.myapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.R
import com.example.myapp.ui.theme.MyAndroidAppTheme

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.profile),
            contentDescription = "Profile photo",
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Hsu Chia Chang",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Profile")
@Composable
fun ProfileScreenPreview() {
    MyAndroidAppTheme { ProfileScreen() }
}
