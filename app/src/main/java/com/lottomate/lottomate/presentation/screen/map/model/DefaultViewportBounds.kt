package com.lottomate.lottomate.presentation.screen.map.model

import com.naver.maps.geometry.LatLng
import kotlin.math.roundToInt

enum class DefaultViewportBounds(
    val zoom: Int,
    val topLeft: LatLng,
    val bottomRight: LatLng
) {
    LEVEL13(
        zoom = 13,
        topLeft = LatLng(37.59356912686478, 126.95180410368839),
        bottomRight = LatLng(37.53942887313522, 126.98530589631154)
    ),
    LEVEL14(
        zoom = 14,
        topLeft = LatLng(37.57906727318722, 126.96077779814102),
        bottomRight = LatLng(37.55393072681278, 126.97633220185891),
    ),
    LEVEL15(
        zoom = 15,
        topLeft = LatLng(37.572364194154034, 126.96492563913246),
        bottomRight = LatLng(37.56063380584597, 126.97218436086747),
    ),
    LEVEL16(
        zoom = 16,
        topLeft = LatLng(37.56924830975971, 126.96685373709332),
        bottomRight = LatLng(37.56374969024029, 126.97025626290662),
    ),
    LEVEL17(
        zoom = 17,
        topLeft = LatLng(37.567792792828094, 126.96775440569095),
        bottomRight = LatLng(37.56520520717191, 126.96935559430898),
    ),
    LEVEL18(
        zoom = 18,
        topLeft = LatLng(37.56710995772438, 126.96817694157627),
        bottomRight = LatLng(37.565888042275624, 126.96893305842366),
    ),
    LEVEL19(
        zoom = 19,
        topLeft = LatLng(37.56678840102734, 126.968375919694),
        bottomRight = LatLng(37.56620959897266, 126.96873408030594),
    ),
    LEVEL20(
        zoom = 20,
        topLeft = LatLng(37.56663646548799, 126.96846993685463),
        bottomRight = LatLng(37.56636153451201, 126.9686400631453),
    );

    companion object {
        fun fromZoomLevel(zoom: Double): DefaultViewportBounds {
            return entries.find { it.zoom == zoom.roundToInt() } ?: LEVEL15
        }
    }
}