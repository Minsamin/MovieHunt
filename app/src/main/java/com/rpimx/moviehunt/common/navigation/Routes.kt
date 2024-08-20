package com.rpimx.moviehunt.common.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.rpimx.moviehunt.common.domain.model.Film
import com.rpimx.moviehunt.features.cast.domain.model.Credits
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

@Serializable
sealed class DashboardScreenRoute(val routeName: String) {
    companion object {
        const val ROUTE = "dashboard"
    }

    @Serializable
    data object MainRoute : DashboardScreenRoute(routeName = ROUTE)

    @Serializable
    data object Home : DashboardScreenRoute(routeName = "Home")

    @Serializable
    data object Details : DashboardScreenRoute(routeName = "Details")

    @Serializable
    data object Search : DashboardScreenRoute(routeName = "Search")

    @Serializable
    data object Bookmark : DashboardScreenRoute(routeName = "Bookmark")

    @Serializable
    data object Settings : DashboardScreenRoute(routeName = "Settings")

    @Serializable
    data class DetailsScreenArguments(
        val film: Film
    )

    @Serializable
    data class CastsOverviewScreenArguments(
        val credits: Credits
    )

}

class SealedClassNavType<T : Any>(
    private val serializer: KSerializer<T>
) : NavType<T>(isNullableAllowed = false) {

    override fun get(bundle: Bundle, key: String): T? {
        return bundle.getString(key)?.let { Json.decodeFromString(serializer, it) }
    }

    override fun parseValue(value: String): T {
        return try {
            Json.decodeFromString(serializer, value)
        } catch (e: SerializationException) {
            throw IllegalArgumentException("Could not parse $value")
        }
    }

    override fun serializeAsValue(value: T): String {
        return Uri.encode(Json.encodeToString(serializer, value))
    }

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, Json.encodeToString(serializer, value))
    }
}
