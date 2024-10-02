package luke.koz.supainventory.inventory.data

import kotlinx.serialization.Serializable

@Serializable
data class InventoryItemEntry(
    val id: Int = 0,
    val name: String,
    val price: Double,
    val quantity: Int
)
