package com.example.recetario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.net.Uri
import android.widget.MediaController
import android.widget.VideoView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

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

// 1. PANTALLA: PERFIL (FOTO, ANTROPOMETRÍA, IMC Y ACTIVIDAD)
@Composable
fun PerfilGastronomicoView() {
    // Estados de entrada para el cálculo de IMC y datos personales
    var nombre by remember { mutableStateOf("Carlos Mendoza") }
    var edad by remember { mutableStateOf("28") }
    var pesoInput by remember { mutableStateOf("72.5") }
    var alturaInput by remember { mutableStateOf("1.75") }

    // Cálculo reactivo del IMC
    val peso = pesoInput.toDoubleOrNull() ?: 0.0
    val altura = alturaInput.toDoubleOrNull() ?: 0.0
    val imc = if (altura > 0.0) peso / (altura * altura) else 0.0

    // Clasificación y color según estándar OMS
    val (clasificacionImc, colorImc) = when {
        imc <= 0.0 -> Pair("Sin datos", Color.Gray)
        imc < 18.5 -> Pair("Bajo peso", Color(0xFF3B82F6))
        imc in 18.5..24.9 -> Pair("Peso normal", Color(0xFF10B981))
        imc in 25.0..29.9 -> Pair("Sobrepeso", Color(0xFFF59E0B))
        else -> Pair("Obesidad", Color(0xFFEF4444))
    }

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

        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = CardDefaults.outlinedCardBorder(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp)) {

                // A. Foto y Datos de Usuario
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = LightTealBg),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Foto de perfil circular desde res/drawable/foto.jpg
                        Image(
                            painter = painterResource(id = R.drawable.foto),
                            contentDescription = "Foto de perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(nombre, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextDark)
                            Text("$edad años • Plan Activo", color = PrimaryTeal, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // B. Tarjeta de Resultado de IMC
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = colorImc.copy(alpha = 0.12f)),
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("ÍNDICE DE MASA CORPORAL", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                            Text(clasificacionImc, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = colorImc)
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White
                        ) {
                            Text(
                                text = if (imc > 0.0) String.format("%.1f", imc) else "--",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = colorImc,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // C. Inputs Antropométricos (Peso y Altura)
                Text(
                    text = "PARÁMETROS FÍSICOS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedTextField(
                        value = pesoInput,
                        onValueChange = { pesoInput = it },
                        label = { Text("Peso (kg)", fontSize = 10.sp) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryTeal,
                            unfocusedBorderColor = SoftGrayBorder
                        )
                    )

                    OutlinedTextField(
                        value = alturaInput,
                        onValueChange = { alturaInput = it },
                        label = { Text("Altura (m)", fontSize = 10.sp) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryTeal,
                            unfocusedBorderColor = SoftGrayBorder
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // D. Resumen de Actividad y Estadísticas
                Text(
                    text = "ESTADÍSTICAS & CONTROL",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricBox(modifier = Modifier.weight(1f), icon = "🥗", title = "Saludables", value = "14 Platos")
                    MetricBox(modifier = Modifier.weight(1f), icon = "🔥", title = "Calorías", value = "1,900 kcal")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MetricBox(modifier = Modifier.weight(1f), icon = "📍", title = "Restaurantes", value = "8 Guardados")
                    MetricBox(modifier = Modifier.weight(1f), icon = "⭐", title = "Favoritos", value = "24 Recetas")
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

// MODELO DE DATOS PARA LA VISTA DE FOTOS CON DETALLE
data class RecetaFotoDetalle(
    val titulo: String,
    val calorias: String,
    val descripcion: String,
    val imagenRes: Int
)

// 2. PANTALLA: FOTOS (Catálogo de recetas visuales con selección y modal de foto ampliada)
@Composable
fun RecetasFotosView() {
    var recetaSeleccionada by remember { mutableStateOf<RecetaFotoDetalle?>(null) }

    val recetas = listOf(
        RecetaFotoDetalle(
            titulo = "Ensalada César",
            calorias = "380 kcal",
            descripcion = "Pechuga a la plancha, lechuga romana fresca, crutones integrales y aderezo ligero de yogur.",
            imagenRes = R.drawable.ensalada_cesar
        ),
        RecetaFotoDetalle(
            titulo = "Salmón Horneado",
            calorias = "520 kcal",
            descripcion = "Filete de salmón al horno con finas hierbas, pimentones asados y aceite de oliva virgen.",
            imagenRes = R.drawable.salmon
        ),
        RecetaFotoDetalle(
            titulo = "Tacos de Pollo",
            calorias = "450 kcal",
            descripcion = "Tortillas de maíz con pollo desmechado, guacamole casero, pico de gallo.",
            imagenRes = R.drawable.tacos
        ),
        RecetaFotoDetalle(
            titulo = "Bowl de Quinoa",
            calorias = "290 kcal",
            descripcion = "Base de quinoa con pollo horneado, aguacate y semillas de calabaza tostadas.",
            imagenRes = R.drawable.quinua
        )
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Fotos de Recetas",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Text(
            text = "Toca un plato para ver su foto y detalle",
            fontSize = 10.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(recetas) { item ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { recetaSeleccionada = item }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Miniatura de la receta
                        Image(
                            painter = painterResource(id = item.imagenRes),
                            contentDescription = item.titulo,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.titulo,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.calorias,
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Ver detalle",
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    // DIÁLOGO MODAL CON FOTO GRANDE AL TOCAR UN PLATO
    recetaSeleccionada?.let { receta ->
        AlertDialog(
            onDismissRequest = { recetaSeleccionada = null },
            confirmButton = {
                TextButton(onClick = { recetaSeleccionada = null }) {
                    Text("Cerrar", color = PrimaryTeal, fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text(
                    text = receta.titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextDark
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = receta.imagenRes),
                        contentDescription = receta.titulo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "🔥 ${receta.calorias}",
                        fontWeight = FontWeight.Bold,
                        color = PrimaryTeal,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = receta.descripcion,
                        fontSize = 11.sp,
                        color = TextDark,
                        lineHeight = 15.sp
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(14.dp)
        )
    }
}

// 3. PANTALLA: VIDEO (Tips del Chef con reproductor nativo)
@Composable
fun TipsYVideosView() {
    val context = LocalContext.current
    // Ruta al archivo MP4 guardado en res/raw/video_sellar_carne.mp4
    val videoUri = remember {
        Uri.parse("android.resource://${context.packageName}/${R.raw.video_sellar_carne}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Videos & Tips Culinarios",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Text(
            text = "Técnicas esenciales de cocina explicadas en video",
            fontSize = 10.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = CardDefaults.outlinedCardBorder(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "🥩 Cómo sellar carne correctamente",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                // REPRODUCTOR DE VIDEO NATIVO
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        factory = { ctx ->
                            VideoView(ctx).apply {
                                setVideoURI(videoUri)
                                val controller = MediaController(ctx)
                                controller.setAnchorView(this)
                                setMediaController(controller)
                                // Inicia pausado o dale start() si prefieres autoplay
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Puntos clave de la técnica:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = PrimaryTeal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "• Secar la superficie con papel absorbente.\n• Sartén a fuego alto con poco aceite.\n• No mover la pieza durante 3 a 4 minutos para formar costra.",
                    fontSize = 10.sp,
                    color = TextMuted,
                    lineHeight = 14.sp
                )
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