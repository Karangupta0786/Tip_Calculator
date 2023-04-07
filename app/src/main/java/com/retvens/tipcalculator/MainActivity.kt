package com.retvens.tipcalculator

import android.icu.text.DecimalFormat
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View.OnClickListener
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.retvens.tipcalculator.ui.theme.TipcalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipcalculatorTheme {
                Tipcalculator()
            }
        }
    }
}

@Composable
fun Tipcalculator() {
    Column {

        val amount = remember {
            mutableStateOf("")
        }
        val personcounter = remember {
            mutableStateOf(1)
        }
        val tipPercentage = remember {
            mutableStateOf(0f)
        }

        Totalcast(
            amount = GetTotalHeaderAmount(
                amount.value,
                personcounter = personcounter.value,
                tipPercentage = tipPercentage.value
            )
        )
        //  Spacer (modifier = Modifier.height(5.dp))
        Userinputarea(
            amount = amount.value,
            amountchange = { amount.value = it },
            personcounter = personcounter.value,
            onAddorReducePerson = {
                if (it < 0) {
                    if (personcounter.value != 0) {
                        personcounter.value--
                    }
                } else {
                    personcounter.value++
                }
            },
            tipPercentage = tipPercentage.value,
            tipPercentageChange = { tipPercentage.value = it }
        )
    }
}

@Composable
fun Totalcast(amount: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(12.dp),
        color = colorResource(id = R.color.teal_200)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.body1,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = "$ ${formateTwoDecimalPoints(amount)}",
                style = MaterialTheme.typography.body1,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
    Spacer(modifier = Modifier.height(7.dp))
    //  Userinputarea(amount = "1", amountchange ={ })
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Userinputarea(
    amount: String,
    amountchange: (String) -> Unit,
    personcounter: Int,
    onAddorReducePerson: (Int) -> Unit,
    tipPercentage: Float,
    tipPercentageChange: (Float) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = amount, onValueChange = { amountchange.invoke(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "enter the amount ") },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    keyboardController?.hide()
                })
            )
            if (amount.isNotBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Split ", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.fillMaxWidth(.50f))
            Custombutton(imageVector = Icons.Default.KeyboardArrowUp) {
                onAddorReducePerson.invoke(1)
            }
            // Spacer(modifier = Modifier.width(3.dp))
            Text(text = "$personcounter", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.width(3.dp))
            Custombutton(imageVector = Icons.Default.KeyboardArrowDown) {
                onAddorReducePerson.invoke(-1)
            }
        }
        Spacer(modifier = Modifier.height(7.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Tip", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.fillMaxWidth(.68f))
            Text(
                text = "$${formateTwoDecimalPoints(GettipAmount(amount, tipPercentage))}",
                style = MaterialTheme.typography.body1
            )
        }
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            text = "${formateTwoDecimalPoints(tipPercentage.toString())}%",
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Slider(
            value = tipPercentage, onValueChange = {
                tipPercentageChange.invoke(it)
            }, valueRange = 0f..100f, steps = 0, modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
        )
    }
    }
}
    Spacer(modifier = Modifier.height(9.dp))
    if (personcounter==0){
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "Please select atleast one person to pay the bill")
        }

    }
}
@Composable
fun Custombutton(imageVector: ImageVector, onclick: () -> Unit) {
    Card(modifier = Modifier
        .wrapContentSize()
        .padding(12.dp)
        .clickable { onclick.invoke() }) {
        Icon(
            imageVector = imageVector, contentDescription = null,
            modifier = Modifier
                .size(30.dp)
                .padding(4.dp)
        )
    }
}

fun GettipAmount(useramount: String, tipPercentage: Float): String {
    return when {
        useramount.isEmpty() -> {
            "0"
        }
        else -> {
            val amount = useramount.toFloat()
            (amount * tipPercentage.div(100)).toString()
        }
    }
}


fun GetTotalHeaderAmount(amount: String, personcounter: Int, tipPercentage: Float): String {
    return when {
        amount.isEmpty() -> {
            "0"
        }
        else -> {
            val useramount = amount.toFloat()
            val tipamount = useramount * tipPercentage.div(100)
            val perheadamount = (useramount + tipamount).div(personcounter)
            perheadamount.toString()
        }
    }
}


fun formateTwoDecimalPoints(str: String): String {
    return if (str.isEmpty()) {
        ""
    } else {
        val format = DecimalFormat("##############.##")
        format.format(str.toFloat())
    }
}


//@Composable
//fun Heading() {
//    Surface(modifier = Modifier
//        .fillMaxWidth()
//        .padding(horizontal = 25.dp, vertical = 12.dp),
//        shape = RoundedCornerShape(12.dp),
//        elevation = 12.dp
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.Black),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Image(painter = painterResource(id = R.drawable.nano) ,
//                contentDescription = null,
//                Modifier
//                    .size(55.dp)
//                    .clip(CircleShape)
//                    .padding(horizontal = 10.dp)
//                    //shape = RoundedCornerShape(50.dp)
//            )
//            Text(text = "let's plan a trip", style = MaterialTheme.typography.h6, color = Color.White)
//        }
//
//    }
//}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TipcalculatorTheme {

    }
}