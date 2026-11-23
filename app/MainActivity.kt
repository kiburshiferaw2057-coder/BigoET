package et.bigo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Blue = androidx.compose.ui.graphics.Color(0xFF1557A5)
private val PaleBlue = androidx.compose.ui.graphics.Color(0xFFEAF3FF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BigoTheme { BigoApp() } }
    }
}

@Composable
private fun BigoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Blue,
            secondary = androidx.compose.ui.graphics.Color(0xFF4C8DCE),
            background = androidx.compose.ui.graphics.Color.White,
            surface = androidx.compose.ui.graphics.Color.White
        ),
        content = content
    )
}

private enum class Page { HOME, LIVE, WALLET, CHECK, PROFILE, REGISTER }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BigoApp() {
    var page by remember { mutableStateOf(Page.HOME) }
    var language by remember { mutableStateOf("EN") }
    var registered by remember { mutableStateOf(false) }
    var ageConfirmed by remember { mutableStateOf(false) }

    val am = language == "አማ"
    val title = if (am) "ቢጎ ኢቲ" else "BIGO ET"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold, color = Blue) },
                actions = {
                    TextButton(onClick = { language = if (am) "EN" else "አማ" }) {
                        Text(if (am) "English" else "አማርኛ")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = page == Page.HOME, onClick = { page = Page.HOME },
                    icon = { Icon(Icons.Default.Home, null) }, label = { Text(if (am) "ቤት" else "Home") })
                NavigationBarItem(selected = page == Page.LIVE, onClick = { page = Page.LIVE },
                    icon = { Icon(Icons.Default.Videocam, null) }, label = { Text(if (am) "ቀጥታ" else "Live") })
                NavigationBarItem(selected = page == Page.WALLET, onClick = { page = Page.WALLET },
                    icon = { Icon(Icons.Default.AccountBalanceWallet, null) }, label = { Text(if (am) "ቦርሳ" else "Wallet") })
                NavigationBarItem(selected = page == Page.PROFILE, onClick = { page = Page.PROFILE },
                    icon = { Icon(Icons.Default.Person, null) }, label = { Text(if (am) "መገለጫ" else "Profile") })
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (page) {
                Page.HOME -> HomeScreen(am, onOpenLive = { page = Page.LIVE }, onRegister = { page = Page.REGISTER })
                Page.LIVE -> LiveScreen(am, ageConfirmed, onConfirmAge = { ageConfirmed = true })
                Page.WALLET -> WalletScreen(am, onCheck = { page = Page.CHECK })
                Page.CHECK -> TransactionCheckScreen(am)
                Page.PROFILE -> ProfileScreen(am, registered, onRegister = { page = Page.REGISTER })
                Page.REGISTER -> RegisterScreen(am, onComplete = { registered = true; page = Page.PROFILE })
            }
        }
    }
}

@Composable
private fun HeaderCard(title: String, subtitle: String) {
    Card(colors = CardDefaults.cardColors(containerColor = PaleBlue),
        shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp)) {
            Text(title, color = Blue, fontSize = 23.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(subtitle, color = androidx.compose.ui.graphics.Color.DarkGray)
        }
    }
}

@Composable
private fun HomeScreen(am: Boolean, onOpenLive: () -> Unit, onRegister: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            HeaderCard(
                if (am) "ወደ ቢጎ ኢቲ እንኳን በደህና መጡ" else "Welcome to BIGO ET",
                if (am) "ቀጥታ ስርጭት፣ ውይይት እና ማህበራዊ ግንኙነት" else "Live video, chat and community"
            )
        }
        item { Text(if (am) "ታዋቂ የቀጥታ ክፍሎች" else "Featured live rooms", fontWeight = FontWeight.Bold, fontSize = 18.sp) }
        items(3) { index ->
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = PaleBlue, shape = RoundedCornerShape(12.dp)) {
                        Icon(Icons.Default.PlayCircle, null, tint = Blue, modifier = Modifier.padding(14.dp).size(30.dp))
                    }
                    Column(Modifier.weight(1f).padding(start = 12.dp)) {
                        Text(listOf("Music & Culture", "Community Chat", "Creator Spotlight")[index], fontWeight = FontWeight.SemiBold)
                        Text(if (am) "የሙከራ ክፍል • ቀጥታ ያልሆነ" else "Demo room • Not broadcasting", fontSize = 12.sp)
                    }
                    TextButton(onClick = onOpenLive) { Text(if (am) "ግባ" else "Open") }
                }
            }
        }
        item {
            Button(onClick = onRegister, modifier = Modifier.fillMaxWidth()) {
                Text(if (am) "መለያ ይፍጠሩ" else "Create an account")
            }
        }
        item { SafetyNote(am) }
    }
}

@Composable
private fun LiveScreen(am: Boolean, ageConfirmed: Boolean, onConfirmAge: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        HeaderCard(if (am) "የቀጥታ ክፍሎች" else "Live rooms",
            if (am) "ይህ የማሳያ ስሪት ነው፤ ቪዲዮ አያስተላልፍም።" else "This is a demo screen; no video is being broadcast.")
        if (!ageConfirmed) {
            Text(if (am) "የቀጥታ ማስተናገድ ለ18+ ተጠቃሚዎች ብቻ ነው።" else "Live hosting is restricted to users aged 18 and over.")
            Button(onClick = onConfirmAge) { Text(if (am) "18+ መሆኔን አረጋግጣለሁ" else "I confirm I am 18+") }
        } else {
            Text(if (am) "የዕድሜ ማረጋገጫ ተመዝግቧል (ማሳያ ብቻ)" else "Age confirmation recorded (demo only).")
            Text(if (am) "እውነተኛ የዕድሜ ማረጋገጫ በአገልጋይ በኩል መተግበር አለበት።" else "Production requires robust server-side age assurance.")
        }
        SafetyNote(am)
    }
}

@Composable
private fun WalletScreen(am: Boolean, onCheck: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        HeaderCard(if (am) "የማሳያ ቦርሳ" else "Demo wallet",
            if (am) "ትክክለኛ ገንዘብ አይይዝም።" else "No real money is stored or transferred.")
        Text(if (am) "ቀሪ ሂሳብ: 0.00 ETB (ማሳያ)" else "Balance: 0.00 ETB (demo)", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(if (am) "የክፍያ አገልግሎቶች" else "Payment providers")
        OutlinedCard(Modifier.fillMaxWidth()) { Text("Telebirr — provider integration required", Modifier.padding(14.dp)) }
        OutlinedCard(Modifier.fillMaxWidth()) { Text("CBE — provider integration required", Modifier.padding(14.dp)) }
        Button(onClick = onCheck, modifier = Modifier.fillMaxWidth()) {
            Text(if (am) "የግብይት ቁጥር ይፈትሹ" else "Check transaction reference")
        }
        SafetyNote(am)
    }
}

@Composable
private fun TransactionCheckScreen(am: Boolean) {
    var ref by remember { mutableStateOf("") }
    var status by remember { mutableStateOf<String?>(null) }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HeaderCard(if (am) "የግብይት ማረጋገጫ" else "Transaction check",
            if (am) "የማሳያ ማጣቀሻ ቁጥር ብቻ ይሞክሩ።" else "Enter a demo reference number only.")
        OutlinedTextField(value = ref, onValueChange = { ref = it; status = null },
            label = { Text(if (am) "የማጣቀሻ ቁጥር" else "Reference number") },
            modifier = Modifier.fillMaxWidth(), singleLine = true)
        Button(onClick = {
            status = when (ref.trim()) {
                "BIGO-DEMO-1001" -> if (am) "ተሳክቷል — የማሳያ ግብይት" else "SUCCESS — simulated transaction"
                "BIGO-DEMO-1002" -> if (am) "በሂደት ላይ — የማሳያ ግብይት" else "PENDING — simulated transaction"
                else -> if (am) "አልተገኘም — የማሳያ ቁጥሮችን ይጠቀሙ" else "NOT FOUND — use a demo reference"
            }
        }, modifier = Modifier.fillMaxWidth()) { Text(if (am) "አረጋግጥ" else "Verify") }
        status?.let { Text(it, color = Blue, fontWeight = FontWeight.Bold) }
        Text(if (am) "የሙከራ ቁጥሮች: BIGO-DEMO-1001, BIGO-DEMO-1002" else "Demo refs: BIGO-DEMO-1001, BIGO-DEMO-1002")
        SafetyNote(am)
    }
}

@Composable
private fun RegisterScreen(am: Boolean, onComplete: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var accepted by remember { mutableStateOf(false) }
    var adult by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        HeaderCard(if (am) "መለያ ይፍጠሩ" else "Create your account",
            if (am) "ለማሳያ ብቻ ነው።" else "Prototype registration only.")
        OutlinedTextField(username, { username = it }, label = { Text(if (am) "የተጠቃሚ ስም" else "Username") }, modifier = Modifier.fillMaxWidth())
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = accepted, onCheckedChange = { accepted = it })
            Text(if (am) "የአጠቃቀም ደንቦችን እቀበላለሁ" else "I agree to the community rules")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = adult, onCheckedChange = { adult = it })
            Text(if (am) "18 ዓመት ወይም ከዚያ በላይ ነኝ" else "I am 18 or older")
        }
        Text(if (am) "ይህ የማሳያ ምልክት ነው፤ በእውነተኛ ስሪት የዕድሜ ማረጋገጫ ያስፈልጋል።" else "This checkbox is not real age verification; production needs stronger age assurance.")
        Button(onClick = onComplete, enabled = username.isNotBlank() && accepted,
            modifier = Modifier.fillMaxWidth()) { Text(if (am) "ቀጥል" else "Continue") }
    }
}

@Composable
private fun ProfileScreen(am: Boolean, registered: Boolean, onRegister: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        HeaderCard(if (am) "መገለጫ" else "Profile",
            if (registered) (if (am) "የማሳያ መለያ ተፈጥሯል" else "Demo profile created") else (if (am) "ገና አልተመዘገቡም" else "You are not registered yet"))
        Button(onClick = onRegister, modifier = Modifier.fillMaxWidth()) { Text(if (am) "መለያ ይፍጠሩ" else "Register") }
        SafetyNote(am)
    }
}

@Composable
private fun SafetyNote(am: Boolean) {
    Card(colors = CardDefaults.cardColors(containerColor = PaleBlue), shape = RoundedCornerShape(14.dp)) {
        Text(
            if (am) "ደህንነት: ለማንም ሰው PIN፣ OTP ወይም የባንክ ይለፍ ቃል አያጋሩ። ተጠቃሚዎችን ማገድና ሪፖርት ማድረግ በሙሉ ስሪት መኖር አለበት።"
            else "Safety: Never share PINs, OTPs or banking passwords. Production must include blocking, reporting, moderation and privacy controls.",
            Modifier.padding(14.dp), fontSize = 12.sp
        )
    }
}
