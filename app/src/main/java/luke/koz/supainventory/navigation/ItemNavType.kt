package luke.koz.supainventory.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object ItemNavType {

    val ItemType = object : NavType<ItemNavType>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): ItemNavType? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): ItemNavType {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: ItemNavType): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: ItemNavType) {
            bundle.putString(key, Json.encodeToString(value))
        }

    }
}