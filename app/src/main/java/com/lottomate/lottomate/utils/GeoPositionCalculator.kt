package com.lottomate.lottomate.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import com.naver.maps.map.Projection
import kotlin.math.cos

/**
 * 디바이스 화면 크기를 구한 후, 좌측 상단/우측 하단의 위경도를 구하는 클래스입니다.
 */
object GeoPositionCalculator {
    /**
     * 주어진 중심 좌표와 줌 레벨을 기반으로 **좌측 상단** 위경도를 계산하는 함수입니다.
     *
     * @param currentLat 현재 중심 위도
     * @param currentLon 현재 중심 경도
     * @param zoom 줌 레벨
     * @return 좌측 상단 위경도 (위도, 경도)
     */
    @Composable
    fun calculateLeftTopGeoPosition(currentLat: Double, currentLon: Double, zoom: Double): Pair<Double, Double> {
        // 화면 크기(픽셀 단위) 가져오기
        val (screenWidth, screenHeight) = getScreenSizeInPx()

        // 좌측 상단 좌표 계산에 필요한 위도와 경도 변화량 계산
        val (deltaLat, deltaLon) = calculateGeoPosition(currentLat, currentLon, zoom, screenWidth, screenHeight)

        // 좌측 상단 좌표 계산: 위로 이동하고 왼쪽으로 이동하므로
        // 위도는 증가, 경도는 감소해야 함
        val topLeftLat = currentLat + deltaLat  // 위로 이동 (위도 증가)
        val topLeftLon = currentLon - deltaLon // 왼쪽 이동 (경도 감소)

        // 로그로 좌측 상단 위경도 확인
        Log.d("MapCoordinateCalculator", "좌측 상단 위도 : $topLeftLat / 경도 : $topLeftLon")

        // 계산된 좌측 상단 위경도 반환
        return Pair(topLeftLat, topLeftLon)
    }

    /**
     * 주어진 중심 좌표와 줌 레벨을 기반으로 **우측 하단** 위경도를 계산하는 함수입니다.
     *
     * @param currentLat 현재 중심 위도
     * @param currentLon 현재 중심 경도
     * @param zoom 줌 레벨
     * @return 우측 하단 위경도 (위도, 경도)
     */
    @Composable
    fun calculateBottomRightGeoPosition(currentLat: Double, currentLon: Double, zoom: Double): Pair<Double, Double> {
        // 화면 크기(픽셀 단위) 가져오기
        val (screenWidth, screenHeight) = getScreenSizeInPx()

        // 우측 하단 좌표 계산에 필요한 위도와 경도 변화량 계산
        val (deltaLat, deltaLon) = calculateGeoPosition(currentLat, currentLon, zoom, screenWidth, screenHeight)

        // 우측 하단 좌표 계산: 아래로 이동하고 오른쪽으로 이동하므로
        // 위도는 감소, 경도는 증가해야 함
        val bottomRightLat = currentLat - deltaLat  // 아래로 이동 (위도 감소)
        val bottomRightLon = currentLon + deltaLon // 오른쪽 이동 (경도 증가)

        // 로그로 우측 하단 위경도 확인
        Log.d("MapCoordinateCalculator", "우측 하단 위도 : $bottomRightLat / 경도 : $bottomRightLon")

        // 계산된 우측 하단 위경도 반환
        return Pair(bottomRightLat, bottomRightLon)
    }

    /**
     * 화면에서 현재 중심 위도/경도와 줌 레벨을 바탕으로
     * 해당 위치를 기준으로 화면 내 좌표 변화량을 계산하는 함수입니다.
     *
     * @param currentLat 현재 중심 위도
     * @param currentLon 현재 중심 경도
     * @param zoom 줌 레벨
     * @param screenWidth 화면 가로 크기 (픽셀 단위)
     * @param screenHeight 화면 세로 크기 (픽셀 단위)
     * @return 변화된 위도와 경도 (위도 변화량, 경도 변화량)
     */
    @Composable
    private fun calculateGeoPosition(
        currentLat: Double,  // 현재 중심 위도
        currentLon: Double,  // 현재 중심 경도
        zoom: Double,        // 줌 레벨
        screenWidth: Int,    // 화면 가로 크기 (픽셀 단위)
        screenHeight: Int    // 화면 세로 크기 (픽셀 단위)
    ): Pair<Double, Double> {
        // 화면 밀도를 가져옴 (예: 1dp 당 픽셀 밀도)
        val density = LocalDensity.current.density

        // 네이버 지도에서 제공하는 1dp 당 미터 변환 값
        val mapPerDp = Projection.getMetersPerDp(currentLat, zoom)
        val mapPerPixel = mapPerDp * density  // 픽셀 당 미터 변환 값

        // 화면의 크기와 줌 레벨을 바탕으로 이동 거리 계산 (미터 단위)
        val dx = (screenWidth / zoom) * mapPerPixel // 가로 이동 거리 (m)
        val dy = (screenHeight / zoom) * mapPerPixel // 세로 이동 거리 (m)

        // 위도 변화량 계산 (위도는 1도당 111320m)
        val deltaLat = dy / 111_320  // 위도 변화량 (미터 → 위도)

        // 경도 변화량 계산 (경도는 위도에 따라 다르기 때문에 cos(위도) 값으로 변환)
        val deltaLon = dx / (88_850 * cos(Math.toRadians(currentLat)))  // 경도 변화량 (미터 → 경도)

        // 변화된 위도와 경도 값 반환
        return Pair(deltaLat, deltaLon)
    }

    /**
     * 사용자의 디바이스 화면 크기를 구합니다.
     */
    @Composable
    private fun getScreenSizeInPx(): Pair<Int, Int> {
        val configuration = LocalConfiguration.current
        val density = LocalDensity.current.density

        val screenWidthPx = (configuration.screenWidthDp * density).toInt()
        val screenHeightPx = (configuration.screenHeightDp * density).toInt()

        return Pair(screenWidthPx, screenHeightPx)
    }
}