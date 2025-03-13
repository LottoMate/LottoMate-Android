package com.lottomate.lottomate.presentation.screen.scan

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.journeyapps.barcodescanner.CaptureManager
import com.lottomate.lottomate.databinding.ActivityLottoScanBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 로또 용지에 있는 QR을 스캔하는 QR 스캐너를 관리하는 Activity 입니다.
 */
@AndroidEntryPoint
class LottoScanActivity : ComponentActivity() {
    private var _binding: ActivityLottoScanBinding? = null
    private val binding: ActivityLottoScanBinding get() = _binding!!

    private var captureManager: CaptureManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLottoScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        setHideSoftKey()

        initCaptureManager()
        removeScannerLaser()

        binding.ivClose.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        captureManager?.onResume()
    }

    override fun onPause() {
        super.onPause()
        captureManager?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        captureManager?.onDestroy()
    }

    /**
     * 스캐너에 보여지는 중앙 레이저를 제거합니다.
     */
    private fun removeScannerLaser() {
        binding.viewScan.viewFinder?.apply {
            try {
                this::class.java.getDeclaredField("SCANNER_ALPHA")?.apply {
                    isAccessible = true
                    set(this@apply, intArrayOf(1))
                }
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Barcode Scanner를 관리하는 CaptureManager를 초기화합니다.
     */
    private fun initCaptureManager() {
        captureManager ?: run {
            CaptureManager(this@LottoScanActivity, binding.viewScan).apply {
                initializeFromIntent(intent, null)
                decode()
            }.also { captureManager = it }
        }
    }

    /**
     * 하단 소프트키 보이지 않도록 설정합니다.
     */
    private fun setHideSoftKey() {
        window.decorView.apply {
            systemUiVisibility = systemUiVisibility xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            systemUiVisibility = systemUiVisibility xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }
}