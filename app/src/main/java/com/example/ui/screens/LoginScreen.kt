package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

@Composable
fun LoginScreen(
    onLoginSuccess: (phoneNumber: String) -> Unit,
    onOpenTerms: () -> Unit = {},
    onOpenSupport: () -> Unit = {}
) {
    val context = LocalContext.current
    var phoneNumber by remember { mutableStateOf("") }

    val darkBackground = Color(0xFF0C1017)
    val inputBackground = Color(0xFF151B26)
    val inputBorder = Color(0xFF283244)
    val primaryAmber = Color(0xFFFFBA00)
    val cyanAccent = Color(0xFF00E5FF)
    val mutedText = Color(0xFF8C9BAE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Top Section: Logo & Titles
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Games Club Shield Logo
            Image(
                painter = painterResource(id = R.drawable.games_club_logo_1786525357371),
                contentDescription = "Games Club Logo",
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Get Started",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Login/Signup",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Phone Input Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(inputBackground, RoundedCornerShape(12.dp))
                    .border(1.dp, inputBorder, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "🇧🇩",
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "+88",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        if (phoneNumber.isEmpty()) {
                            Text(
                                text = "01XXXXXXXXX",
                                fontSize = 15.sp,
                                color = mutedText
                            )
                        }
                        BasicTextField(
                            value = phoneNumber,
                            onValueChange = { input ->
                                if (input.length <= 11 && input.all { it.isDigit() }) {
                                    phoneNumber = input
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            cursorBrush = SolidColor(primaryAmber),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_phone_input")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CONTINUE Button
            Button(
                onClick = {
                    val clean = phoneNumber.trim()
                    val isValidBdNumber = clean.length == 11 && 
                                          clean.startsWith("01") && 
                                          clean[2] in '3'..'9' && 
                                          clean.all { it.isDigit() }

                    if (isValidBdNumber) {
                        onLoginSuccess("+88$clean")
                    } else if (clean.length != 11) {
                        Toast.makeText(context, "অবশ্যই ১১ ডিজিটের মোবাইল নম্বর দিতে হবে", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "সঠিক বাংলাদেশী মোবাইল নম্বর প্রদান করুন (যেমন: 017XXXXXXXX)", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("login_continue_btn"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryAmber,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "CONTINUE",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Terms and Privacy Disclaimer Text
            val agreementText = buildAnnotatedString {
                append("By continuing, I hereby confirm that I am 18 years of age or above and I agree to the ")
                pushStringAnnotation(tag = "TERMS", annotation = "terms")
                withStyle(
                    style = SpanStyle(
                        color = cyanAccent,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Terms and Conditions")
                }
                pop()
                append(" and ")
                pushStringAnnotation(tag = "PRIVACY", annotation = "privacy")
                withStyle(
                    style = SpanStyle(
                        color = cyanAccent,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Privacy Policy")
                }
                pop()
                append(".")
            }

            Text(
                text = agreementText,
                fontSize = 12.sp,
                color = mutedText,
                textAlign = TextAlign.Center,
                lineHeight = 17.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clickable { onOpenTerms() }
            )
        }

        // Bottom Support Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            // Messenger Icon Bubble
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0084FF))
                    .clickable { onOpenSupport() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Contact Support",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Need help? Contact Us
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Need help? ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Text(
                    text = "Contact Us",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = cyanAccent,
                    modifier = Modifier.clickable { onOpenSupport() }
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // প্রবাসী? Click here
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "প্রবাসী? ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Text(
                    text = "Click here",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = cyanAccent,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "প্রবাসী গেমারদের সহায়তার জন্য সাপোর্ট টিমে মেসেজ দিন", Toast.LENGTH_LONG).show()
                        onOpenSupport()
                    }
                )
            }
        }
    }
}
