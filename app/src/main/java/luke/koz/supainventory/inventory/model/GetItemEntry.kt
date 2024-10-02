package luke.koz.supainventory.inventory.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetItemEntry(
    @SerialName(value = "id") val id : Int,
    @SerialName(value = "item_name") val itemName : String,
    @SerialName(value = "item_price") val itemPrice : Float,
    @SerialName(value = "item_quantity") val itemQuantity : Int,
)
