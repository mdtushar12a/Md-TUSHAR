package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.CleanHands
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

data class ShopProduct(
    val id: String,
    val title: String,
    val currentPrice: Int,
    val originalPrice: Int,
    val discountText: String,
    val tagText: String? = null,
    val icon: ImageVector,
    val iconTint: Color,
    val cardGradient: List<Color>
)

@Composable
fun ShopScreen(
    userBalance: Double,
    onBack: () -> Unit,
    onOpenDeposit: () -> Unit,
    onDeductBalance: (Double) -> Unit = {}
) {
    val context = LocalContext.current

    val products = remember {
        listOf(
            ShopProduct(
                id = "1",
                title = "M76Z ULTRA Earphone",
                currentPrice = 2400,
                originalPrice = 2600,
                discountText = "7% OFF",
                icon = Icons.Default.Headphones,
                iconTint = Color(0xFF00E5FF),
                cardGradient = listOf(Color(0xFF0C1D2D), Color(0xFF070F19))
            ),
            ShopProduct(
                id = "2",
                title = "RX3 PLUS Earphone",
                currentPrice = 1100,
                originalPrice = 1400,
                discountText = "23% OFF",
                icon = Icons.Default.Headphones,
                iconTint = Color(0xFF00E676),
                cardGradient = listOf(Color(0xFF0B251D), Color(0xFF070F19))
            ),
            ShopProduct(
                id = "3",
                title = "GAMING TRIGGER",
                currentPrice = 550,
                originalPrice = 590,
                discountText = "7% OFF",
                tagText = "Physics pair",
                icon = Icons.Default.SportsEsports,
                iconTint = Color(0xFFFFB300),
                cardGradient = listOf(Color(0xFF2B1F0D), Color(0xFF070F19))
            ),
            ShopProduct(
                id = "4",
                title = "Memo CX07 Magnetic Phone Cooler- 15W",
                currentPrice = 1130,
                originalPrice = 1400,
                discountText = "20% OFF",
                icon = Icons.Default.AcUnit,
                iconTint = Color(0xFF29B6F6),
                cardGradient = listOf(Color(0xFF0D2230), Color(0xFF070F19))
            ),
            ShopProduct(
                id = "5",
                title = "MEMO FINGER SPEED POWDER",
                currentPrice = 250,
                originalPrice = 300,
                discountText = "16% OFF",
                icon = Icons.Default.CleanHands,
                iconTint = Color(0xFFE0E0E0),
                cardGradient = listOf(Color(0xFF1F2430), Color(0xFF070F19))
            ),
            ShopProduct(
                id = "6",
                title = "MEMO FINGER SLEEVE",
                currentPrice = 200,
                originalPrice = 350,
                discountText = "37% OFF",
                icon = Icons.Default.TouchApp,
                iconTint = Color(0xFFFF2A6D),
                cardGradient = listOf(Color(0xFF2E121E), Color(0xFF070F19))
            )
        )
    }

    var selectedProduct by remember { mutableStateOf<ShopProduct?>(null) }
    var showBuyDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1017))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF162032))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Shop",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Products Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 14.dp, end = 14.dp, top = 4.dp, bottom = 30.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onClick = {
                            selectedProduct = product
                            showBuyDialog = true
                        }
                    )
                }
            }
        }
    }

    // Buy Product Dialog
    if (showBuyDialog && selectedProduct != null) {
        val product = selectedProduct!!
        var deliveryAddress by remember { mutableStateOf("") }
        var phoneNo by remember { mutableStateOf("") }
        val hasEnoughBalance = userBalance >= product.currentPrice

        Dialog(onDismissRequest = { showBuyDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2E)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF26354D), RoundedCornerShape(20.dp))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Buy ${product.title}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0A0E17))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Price:", color = Color.Gray, fontSize = 13.sp)
                            Text(
                                text = "৳${product.currentPrice}",
                                color = Color(0xFF00E5FF),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0A0E17))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Your Balance:", color = Color.Gray, fontSize = 13.sp)
                            Text(
                                text = "৳${String.format("%.2f", userBalance)}",
                                color = if (hasEnoughBalance) Color(0xFF00E676) else Color(0xFFFF5252),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Contact Phone Number:", fontSize = 12.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = phoneNo,
                        onValueChange = { phoneNo = it },
                        placeholder = { Text("017...", color = Color.Gray) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0A0E17),
                            unfocusedContainerColor = Color(0xFF0A0E17),
                            focusedBorderColor = Color(0xFF00E5FF),
                            unfocusedBorderColor = Color(0xFF26354D),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Full Delivery Address:", fontSize = 12.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = deliveryAddress,
                        onValueChange = { deliveryAddress = it },
                        placeholder = { Text("House, Road, Area, District", color = Color.Gray) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF0A0E17),
                            unfocusedContainerColor = Color(0xFF0A0E17),
                            focusedBorderColor = Color(0xFF00E5FF),
                            unfocusedBorderColor = Color(0xFF26354D),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (!hasEnoughBalance) {
                        Button(
                            onClick = {
                                showBuyDialog = false
                                onOpenDeposit()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF2A6D)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Insufficient Balance — Deposit Now",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { showBuyDialog = false },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF26354D)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                            ) {
                                Text("Cancel", color = Color.White)
                            }

                            Button(
                                onClick = {
                                    if (phoneNo.trim().isEmpty() || deliveryAddress.trim().isEmpty()) {
                                        Toast.makeText(context, "Please enter phone and delivery address", Toast.LENGTH_SHORT).show()
                                        return@Button
                                    }
                                    onDeductBalance(product.currentPrice.toDouble())
                                    showBuyDialog = false
                                    selectedProduct = null
                                    Toast.makeText(context, "Order placed successfully! Delivery within 2-3 days.", Toast.LENGTH_LONG).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1.2f)
                                    .height(48.dp)
                            ) {
                                Text("Confirm Order", color = Color(0xFF0A0E17), fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: ShopProduct,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF131A27))
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(18.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Product Image Container with Badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        Brush.verticalGradient(colors = product.cardGradient)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Product Graphic Representation
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(product.iconTint.copy(alpha = 0.15f))
                        .border(1.dp, product.iconTint.copy(alpha = 0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = product.icon,
                        contentDescription = product.title,
                        tint = product.iconTint,
                        modifier = Modifier.size(38.dp)
                    )
                }

                // Left Badge (if any)
                if (product.tagText != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFFFB300))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = product.tagText,
                            color = Color.Black,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Top Right Discount Badge (Pink Pill)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFF2A6D))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = product.discountText,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // Title and Price Section
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 2,
                    lineHeight = 16.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "৳${product.currentPrice}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF38BDF8)
                    )
                    Text(
                        text = "৳${product.originalPrice}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF64748B),
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }
        }
    }
}
