package com.example.recetario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainSidebarScreen()
            }
        }
    }
}

// --- PALETA DE COLOR ---
val PrimaryTeal = Color(0xFF00897B)
val LightTealBg = Color(0xFFE0F2F1)
val SidebarBg = Color(0xFFF8FAFC)
val SoftGrayBorder = Color(0xFFE2E8F0)
val TextDark = Color(0xFF0F172A)
val TextMuted = Color(0xFF64748B)

// --- ESTRUCTURA PRINCIPAL (HEADER + SIDEBAR + PANEL DERECHO) ---
@Composable
fun MainSidebarScreen() {
    var selectedOption by remember { mutableStateOf("Perfil") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryTeal)
    ) {
        // 1. Barra superior con título
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🍳 Recetario Culinario",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // 2. Contenedor blanco dividido: Barra izquierda + Pantalla activa
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 22.dp))
                .background(Color.White)
        ) {
            // MENÚ LATERAL IZQUIERDO (NAVEGACIÓN)
            SidebarMenu(
                selectedItem = selectedOption,
                onItemSelected = { selectedOption = it },
                modifier = Modifier
                    .weight(0.26f)
                    .fillMaxHeight()
                    .background(SidebarBg)
            )

            // CONTENIDO PRINCIPAL DERECHO (CAMBIA SEGÚN EL BOTÓN)
            Box(
                modifier = Modifier
                    .weight(0.74f)
                    .fillMaxHeight()
                    .padding(14.dp)
            ) {
                when (selectedOption) {
                    "Perfil" -> PerfilGastronomicoView()
                    "Fotos" -> RecetasFotosView()
                    "Video" -> TipsYVideosView()
                    "Web" -> RestaurantesWebView()
                    "Botones" -> AccionesRapidasView()
                }
            }
        }
    }
}

// --- COMPONENTE: BARRA LATERAL IZQUIERDA ---
@Composable
fun SidebarMenu(
    selectedItem: String,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val menuItems = listOf(
        Pair("Perfil", Icons.Default.Person),
        Pair("Fotos", Icons.Default.AccountBox),
        Pair("Video", Icons.Default.PlayArrow),
        Pair("Web", Icons.Default.Share),
        Pair("Botones", Icons.Default.Settings)
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        menuItems.forEach { (label, icon) ->
            val isSelected = selectedItem == label

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isSelected) PrimaryTeal else Color.Transparent)
                    .clickable { onItemSelected(label) }
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isSelected) Color.White else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = if (isSelected) Color.White else Color.DarkGray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
            HorizontalDivider(color = SoftGrayBorder, thickness = 0.5.dp)
        }
    }
}

// ==================== VISTAS DEL PANEL DERECHO ====================

// 1. PANTALLA: PERFIL
@Composable
fun PerfilGastronomicoView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryTeal)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Perfil", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Contenedor con borde exterior que agrupa las tarjetas
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = CardDefaults.outlinedCardBorder(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                // Tarjeta Principal de Presentación
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = LightTealBg),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(PrimaryTeal),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👨‍🍳", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Chef Ejecutivo", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                            Text("Nutrición & Cocina", color = PrimaryTeal, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Resumen
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("📌 Resumen Gastronómico", color = PrimaryTeal, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("Platos saludables y seguimiento calórico", color = TextMuted, fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Fila de Estadísticas Rápidas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricBox(modifier = Modifier.weight(1f), icon = "🥗", title = "Saludables", value = "14 Platos")
                    MetricBox(modifier = Modifier.weight(1f), icon = "🔥", title = "Calorías", value = "1,850 kcal")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricBox(modifier = Modifier.weight(1f), icon = "📍", title = "Restaurantes", value = "8 Guardados")
                    MetricBox(modifier = Modifier.weight(1f), icon = "⭐", title = "Top Favoritos", value = "24 Recetas")
                }
            }
        }
    }
}

@Composable
fun MetricBox(modifier: Modifier, icon: String, title: String, value: String) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder(),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 10.sp, color = TextDark)
                Text(value, color = TextMuted, fontSize = 9.sp)
            }
        }
    }
}

// 2. PANTALLA: FOTOS (Catálogo de recetas visuales)
@Composable
fun RecetasFotosView() {
    val items = listOf(
        Pair("Ensalada César", "🥗 380 kcal"),
        Pair("Salmón Horneado", "🐟 520 kcal"),
        Pair("Tacos de Pollo", "🌮 450 kcal"),
        Pair("Bowl de Quinoa", "🥣 290 kcal")
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Fotos de Recetas", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items) { item ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(item.second.take(2), fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(item.first, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text(item.second.drop(2), color = TextMuted, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

// 3. PANTALLA: VIDEO (Tips del Chef)
@Composable
fun TipsYVideosView() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Videos & Tips Culinarios", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = LightTealBg),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("▶️ Cómo sellar carne correctamente", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Tiempo de reposo y temperatura de la sartén.", color = TextMuted, fontSize = 10.sp)
            }
        }
    }
}

// 4. PANTALLA: WEB
@Composable
fun RestaurantesWebView() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Restaurantes Web", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = CardDefaults.outlinedCardBorder(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text("🌿 Bistró La Huerta", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text("Comida Orgánica • 4.8 ⭐", color = PrimaryTeal, fontSize = 10.sp)
                Text("Calle 85 # 12-40", color = TextMuted, fontSize = 9.sp)
            }
        }
    }
}

// 5. PANTALLA: BOTONES
@Composable
fun AccionesRapidasView() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Panel de Botones", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar Nueva Receta", fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(
            onClick = { },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Exportar Reporte Nutricional", fontSize = 12.sp, color = PrimaryTeal)
        }
    }
}