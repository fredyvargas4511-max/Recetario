package com.example.recetario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                GastronomiaDashboardScreen()
            }
        }
    }
}

// --- PALETA DE COLOR ---
val PrimaryTeal = Color(0xFF00897B)
val LightTealBg = Color(0xFFE0F2F1)
val DarkNavyCard = Color(0xFF1E293B)
val AccentLime = Color(0xFF84CC16)
val SoftGrayBorder = Color(0xFFE2E8F0)
val TextMuted = Color(0xFF64748B)

// --- PANTALLA PRINCIPAL CONTENEDORA ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastronomiaDashboardScreen() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Perfil", "Recetas", "Descubrir")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        // Barra Superior
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "MI RECETARIO",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = PrimaryTeal,
                    letterSpacing = 1.2.sp
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White
            )
        )

        // Pestañas (Tabs)
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White,
            contentColor = PrimaryTeal,
            indicator = {
                TabRowDefaults.PrimaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(selectedTabIndex),
                    color = AccentLime,
                    height = 3.dp
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title.uppercase(),
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp,
                            color = if (selectedTabIndex == index) Color.Black else TextMuted
                        )
                    }
                )
            }
        }

        HorizontalDivider(color = SoftGrayBorder, thickness = 1.dp)

        // CAMBIO DINÁMICO DE PANTALLAS SEGÚN EL TAB
        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedTabIndex) {
                0 -> DashboardHomeContent()
                1 -> RecetasTabScreen(onAddRecipeClick = { /* Acción para agregar receta */ })
                2 -> DescubrirTabScreen()
            }
        }
    }
}

// --- CONTENIDO TAB 0: PERFIL / RESUMEN ---
@Composable
fun DashboardHomeContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        HeroBannerCard()

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "ESTADÍSTICAS & CONTROL",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = TextMuted,
            letterSpacing = 0.8.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardMetricCard(
                modifier = Modifier.weight(1f),
                icon = "🥗",
                title = "SALUDABLES",
                subtitle = "14 Recetas fit",
                badgeColor = LightTealBg
            )
            DashboardMetricCard(
                modifier = Modifier.weight(1f),
                icon = "🔥",
                title = "CALORÍAS HOY",
                subtitle = "1,850 kcal prom.",
                badgeColor = Color(0xFFFFEDD5)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DashboardMetricCard(
                modifier = Modifier.weight(1f),
                icon = "📍",
                title = "RESTAURANTES",
                subtitle = "8 Favoritos",
                badgeColor = Color(0xFFFEF3C7)
            )
            DashboardMetricCard(
                modifier = Modifier.weight(1f),
                icon = "⭐",
                title = "TOP GUARDADOS",
                subtitle = "24 Platillos",
                badgeColor = Color(0xFFF3E8FF)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        FeaturedMealCard()
    }
}

// --- CONTENIDO TAB 1: LISTADO DE RECETAS ---
data class RecipeItem(
    val id: Int,
    val title: String,
    val timeMinutes: Int,
    val calories: Int,
    val category: String,
    val icon: String,
    val isFavorite: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecetasTabScreen(onAddRecipeClick: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todas") }

    val categories = listOf("Todas", "Saludables", "Rápidas", "Almuerzos", "Cenas")
    val sampleRecipes = listOf(
        RecipeItem(1, "Ensalada César con Pollo", 15, 380, "Saludables", "🥗", true),
        RecipeItem(2, "Salmón al Horno con Espárragos", 25, 520, "Saludables", "🐟", false),
        RecipeItem(3, "Tacos de Pollo y Aguacate", 20, 450, "Rápidas", "🌮", true),
        RecipeItem(4, "Bowl de Avena con Frutas", 10, 290, "Rápidas", "🥣", false),
        RecipeItem(5, "Pechuga a la Plancha con Quinoa", 30, 410, "Almuerzos", "🍗", false)
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRecipeClick,
                containerColor = PrimaryTeal,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar receta")
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por receta o ingrediente...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted) },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = PrimaryTeal,
                    unfocusedBorderColor = SoftGrayBorder
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = { Text(category, fontSize = 12.sp) },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryTeal,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = TextMuted
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) PrimaryTeal else SoftGrayBorder
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "MIS RECETAS DISPONIBLES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextMuted,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(sampleRecipes) { recipe ->
                    RecipeCardItem(recipe = recipe)
                }
                item {
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        }
    }
}

@Composable
fun RecipeCardItem(recipe: RecipeItem) {
    var isFav by remember { mutableStateOf(recipe.isFavorite) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LightTealBg),
                contentAlignment = Alignment.Center
            ) {
                Text(recipe.icon, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recipe.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⏱ ${recipe.timeMinutes} min", fontSize = 12.sp, color = TextMuted)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "🔥 ${recipe.calories} kcal", fontSize = 12.sp, color = TextMuted)
                }
            }

            IconButton(onClick = { isFav = !isFav }) {
                Icon(
                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFav) Color(0xFFEF4444) else TextMuted
                )
            }
        }
    }
}

// --- CONTENIDO TAB 2: DESCUBRIR (TIPS Y RESTAURANTES) ---
data class CookingTip(
    val id: Int,
    val icon: String,
    val title: String,
    val description: String
)

data class RestaurantItem(
    val id: Int,
    val name: String,
    val cuisine: String,
    val rating: Double,
    val priceLevel: String,
    val location: String,
    val icon: String
)

@Composable
fun DescubrirTabScreen() {
    val tips = listOf(
        CookingTip(1, "🧂", "El toque de sal", "Agrega la sal al agua de la pasta cuando empiece a hervir, nunca antes."),
        CookingTip(2, "🥩", "Carne jugosa", "Deja reposar los cortes cocinados 5 minutos antes de cortar para retener jugos."),
        CookingTip(3, "🥑", "Conserva el aguacate", "Guarda la mitad restante junto a un trozo de cebolla en un recipiente cerrado.")
    )

    val restaurants = listOf(
        RestaurantItem(1, "Bistró La Huerta", "Orgánica & Saludable", 4.8, "$$", "Calle 85 # 12-40", "🌿"),
        RestaurantItem(2, "Trattoria Della Nonna", "Italiana Tradicional", 4.7, "$$$", "Cra 5 # 69-22", "🍝"),
        RestaurantItem(3, "Komorebi Ramen Bar", "Asiática Urbana", 4.9, "$$", "Av. 19 # 104-18", "🍜"),
        RestaurantItem(4, "Fuego & Leña Grill", "Parrilla & Asados", 4.6, "$$$", "Vía Principal Km 2", "🥩")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 1. SECCIÓN TIPS CULINARIOS
        Text(
            text = "TIPS DEL CHEF",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = TextMuted,
            letterSpacing = 0.8.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(tips) { tip ->
                CookingTipCard(tip)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. SECCIÓN RESTAURANTES RECOMENDADOS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "RESTAURANTES RECOMENDADOS",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = TextMuted,
                letterSpacing = 0.8.sp
            )
            Text(
                text = "Explorar",
                color = PrimaryTeal,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            restaurants.forEach { restaurant ->
                RestaurantCardItem(restaurant)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CookingTipCard(tip: CookingTip) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightTealBg),
        modifier = Modifier.width(220.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(tip.icon, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = tip.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tip.description,
                fontSize = 12.sp,
                color = Color(0xFF334155),
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun RestaurantCardItem(restaurant: RestaurantItem) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Text(restaurant.icon, fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = restaurant.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "${restaurant.cuisine} • ${restaurant.priceLevel}",
                    fontSize = 12.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "📍 ${restaurant.location}",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFEF3C7)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⭐", fontSize = 11.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = restaurant.rating.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF92400E)
                    )
                }
            }
        }
    }
}

// --- TARJETAS AUXILIARES ---
@Composable
fun HeroBannerCard() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkNavyCard),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "RESUMEN GASTRONÓMICO",
                color = Color(0xFF94A3B8),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(PrimaryTeal),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👨‍🍳", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text("Chef Culinario", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text("Plan Nutricional Activo", color = AccentLime, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun DashboardMetricCard(modifier: Modifier, icon: String, title: String, subtitle: String, badgeColor: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF0F172A))
            Text(text = subtitle, color = TextMuted, fontSize = 11.sp)
        }
    }
}

@Composable
fun FeaturedMealCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(LightTealBg),
                contentAlignment = Alignment.Center
            ) {
                Text("🥘", fontSize = 28.sp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text("PLATO DEL DÍA", color = PrimaryTeal, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                Text("Bowl de Salmón y Quinoa", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("480 kcal • Dificultad Baja", color = TextMuted, fontSize = 12.sp)
            }
        }
    }
}