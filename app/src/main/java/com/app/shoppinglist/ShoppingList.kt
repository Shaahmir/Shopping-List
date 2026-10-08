package com.app.shoppinglist

import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val bgLight = 0xFFF7F5F0
val fgLight = 0xFF1C1B19

val bgDark = 0xFF161513
val fgDark = 0xFFEDEAE3

@Composable
fun ShoppingList() {

    var listItems by remember { mutableStateOf(listOf<ItemDataClass>()) }
    var showInput by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("1") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
        contentColor = if (isSystemInDarkTheme()) Color(fgDark) else Color(fgLight)
    ) {
        Column (modifier = Modifier.padding(16.dp, 64.dp, 16.dp, 16.dp)) {

            Text(
                "List",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 6.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listItems){ item ->
                    ListColumn(
                        item,
                        {
                            listItems = listItems.map{
                                if (it.id == item.id) it.copy(isEditing = true) else it
                            }
                        },
                        {
                            listItems = listItems.filter {it.id != item.id}
                        }
                    )
                    ItemEdit(
                        item,
                        {
                        listItems = listItems.map{
                            if (it.id == item.id) it.copy(isEditing = false) else it
                        }
                        },
                        { newName, newQuantity ->
                            listItems = listItems.map{
                                if (it.id == item.id) it.copy(name = newName, quantity = newQuantity, isEditing = false) else it
                            }
                        }
                    )
                }
            }

            if(showInput){
                AlertDialog(
                    onDismissRequest = {showInput = false},
                    confirmButton = {
                        Row {

                            Button(
                                onClick = {showInput = false},
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSystemInDarkTheme()) Color(fgDark) else Color(fgLight),
                                    contentColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight)
                                )
                            ){
                                Text("Cancel")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    if (itemName.isNotBlank()){
                                        val newItem = ItemDataClass(
                                            id = listItems.size + 1,
                                            name = itemName,
                                            quantity = itemQuantity.toInt()
                                        )

                                        listItems += newItem
                                        showInput = false
                                        itemName = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSystemInDarkTheme()) Color(fgDark) else Color(fgLight),
                                    contentColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight)
                                )
                            ) {
                                Text("Add")
                            }
                        }
                    },
                    title = {
                        Text(
                            "Add Item",
                            modifier = Modifier.padding(bottom = 6.dp),
                            color = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                        )
                    },
                    text = {
                        Column {

                            OutlinedTextField(
                                value = itemName,
                                onValueChange = {itemName = it},
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = if (isSystemInDarkTheme()) Color(fgDark) else Color(fgLight),
                                    unfocusedContainerColor = if (isSystemInDarkTheme()) Color(fgDark) else Color(fgLight),
                                    focusedTextColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                                    unfocusedTextColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                                    focusedBorderColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                                    unfocusedBorderColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                                    cursorColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                                    focusedPlaceholderColor = Color.Gray,
                                    unfocusedPlaceholderColor = Color.Gray
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = itemQuantity,
                                onValueChange = {itemQuantity = it},
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = if (isSystemInDarkTheme()) Color(fgDark) else Color(fgLight),
                                    unfocusedContainerColor = if (isSystemInDarkTheme()) Color(fgDark) else Color(fgLight),
                                    focusedTextColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                                    unfocusedTextColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                                    focusedBorderColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                                    unfocusedBorderColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                                    cursorColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight),
                                    focusedPlaceholderColor = Color.Gray,
                                    unfocusedPlaceholderColor = Color.Gray
                                )
                            )
                        }
                    },
                    containerColor = if (isSystemInDarkTheme()) Color(fgDark) else Color(fgLight),
                )
            }

            Box (modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { showInput = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) Color(fgDark) else Color(fgLight),
                        contentColor = if (isSystemInDarkTheme()) Color(bgDark) else Color(bgLight)
                    )
                ){
                    Text("Add")
                }
            }
        }
    }
}

@Preview
@Composable
fun ShoppingListPreview() {
    ShoppingList()
}

data class ItemDataClass(
    val id: Int,
    val name: String,
    val quantity: Int,
    val isEditing: Boolean = false
)

@Composable
fun ListColumn(
    item: ItemDataClass,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(80.dp)
            .fillMaxWidth()
            .border(
                width = 0.8.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
        , horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)){
            Text(
                text = item.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(item.quantity.toString())
        }

        Row(){
            IconButton(onClick = onEditClick) {
                Icon(
                    painter = painterResource(R.drawable.cm_edit),
                    contentDescription = "Edit",
                    tint =  if (isSystemInDarkTheme()) Color(fgDark) else Color(fgLight),
                    modifier = Modifier.size(50.dp)
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    painter = painterResource(R.drawable.cm_delete),
                    contentDescription = "Edit",
                    tint =  if (isSystemInDarkTheme()) Color(fgDark) else Color(fgLight),
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}

@Composable
fun ItemEdit(item: ItemDataClass, onDismiss: () -> Unit, onEditComplete: (String, Int) -> Unit){

    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }

    if (item.isEditing){
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Row {

                    Button(onClick = onDismiss){
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (editedName.isNotBlank()){
                                onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
                            }
                        },
                    ) {
                        Text("Edit")
                    }
                }
            },
            title = { Text("Edit Item", modifier = Modifier.padding(bottom = 6.dp)) },
            text = {
                Column {

                    OutlinedTextField(
                        value = editedName,
                        onValueChange = {editedName = it},
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = editedQuantity,
                        onValueChange = {editedQuantity = it},
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
            }
        )
    }
}