package com.on99.elmcomposeui.component

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.on99.elmcomposeui.ShopDetailsApplication
import com.on99.elmcomposeui.component.cameraComponent.CameraSettingState
import com.on99.elmcomposeui.component.componentdetail.ComponentDetailUiState
import com.on99.elmcomposeui.data.ShopDetailsRepository
import com.on99.elmcomposeui.model.ShopDetails
import com.on99.elmcomposeui.model.TextBoard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException


sealed interface ShopUiState {
    data class Success(val shopdetails: List<ShopDetails>) : ShopUiState
    object Error : ShopUiState
    object Loading : ShopUiState
}

sealed interface TextBoardUiState {
    data class Success(val textBoards: List<TextBoard>) : TextBoardUiState
    object Error : TextBoardUiState
    object Loading : TextBoardUiState
}


class ShopsViewModel(private val shopDetailsRepository: ShopDetailsRepository) : ViewModel() {
    var shopUiState: ShopUiState by mutableStateOf(ShopUiState.Loading)
        private set

    var textBoardUiState: TextBoardUiState by mutableStateOf(TextBoardUiState.Loading)
        private set

    /**
     * 所有具体的控制细节
     */
    private val _detailUiState = MutableStateFlow(ComponentDetailUiState())

    /**
     * 所有具体的控制细节
     */
    val detailUiState: StateFlow<ComponentDetailUiState> = _detailUiState


    /**
     * 摄像头功能相关
     */
    private val _cameraSettingState = MutableStateFlow(CameraSettingState())

    /**
     * 摄像头功能相关
     */
    val cameraSettingState: StateFlow<CameraSettingState> = _cameraSettingState

    fun switchFlashLight() {
        //Log.e("viewmodel", "exec! before ${_cameraUiStateBool.value.flashController}")
        _cameraSettingState.update {
            it.copy(
                flashLightController = !_cameraSettingState.value.flashLightController
            )
        }
        // Log.e("viewmodel", "exec! after ${_cameraUiStateBool.value.flashController}")
    }

    fun switchFocusMod() {
        _cameraSettingState.update {
            it.copy(
                focusController = !_cameraSettingState.value.focusController
            )
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        private var LocCount = 0
        override fun onLocationChanged(location: Location) {
            LocCount += 1
            _detailUiState.update {
                it.copy(
                    locationDetail = "Changed Count:: ${LocCount} ||  Longitude:: ${location.longitude} ||  Latitude:: ${location.latitude}"
                )
            }
        }

        override fun onLocationChanged(locations: MutableList<Location>) {
            Log.e("locationListener", "MutableList<Location>")
            super.onLocationChanged(locations)
        }

    }


    init {
        getShopDetailVM()
        getTextBoardVM()
    }

    fun getShopDetailVM() {
        viewModelScope.launch {
            shopUiState = ShopUiState.Loading
            delay(2000)
            shopUiState = try {
                ShopUiState.Success(shopDetailsRepository.getShopDetails())
            } catch (e: IOException) {
                Log.e("IOException", e.toString())
                ShopUiState.Error
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString())
                ShopUiState.Error
            } catch (e: SocketTimeoutException) {
                Log.e("SocketTimeoutException", e.toString())
                ShopUiState.Error
            } catch (e: Exception) {
                Log.e("Exception", e.toString())
                ShopUiState.Error
            }
            //delay(2000)
        }
    }

    fun getTextBoardVM() {
        viewModelScope.launch {
            textBoardUiState = TextBoardUiState.Loading
            delay(2000)
            textBoardUiState = try {
                TextBoardUiState.Success(shopDetailsRepository.getTextBoards())
            } catch (e: IOException) {
                Log.e("IOException", e.toString())
                TextBoardUiState.Error
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString())
                TextBoardUiState.Error
            } catch (e: SocketTimeoutException) {
                Log.e("SocketTimeoutException", e.toString())
                TextBoardUiState.Error
            } catch (e: Exception) {
                Log.e("Exception", e.toString())
                TextBoardUiState.Error
            }
        }
    }


    fun updateShopCheck(shopDetails: ShopDetails) {
        _detailUiState.update {
            it.copy(
                isDetailScreen = true,
                detail = shopDetails
            )
        }
    }

    fun outShopCheck() {
//        _detailUiState.update {
//            it.copy(
//                isDetailScreen = false
//            )
//        }
        closeAllExtraScreen()
    }

    fun switchNavigationButtonScreenStateFalse() {
//        _detailUiState.update {
//            it.copy(
//                isHomeTopBarNavigationButtonScreen = false
//            )
//        }
        closeAllExtraScreen()
    }

    fun switchNavigationButtonScreenStateTrue() {
        _detailUiState.update {
            it.copy(
                isHomeTopBarNavigationButtonScreen = true
            )
        }
    }

    fun switchMessageButtonScreenStateFalse() {
//        _detailUiState.update {
//            it.copy(
//                isHomeTopBarMessageButtonScreen = false
//            )
//        }
        closeAllExtraScreen()
    }

    fun switchMessageButtonScreenStateTrue() {
        _detailUiState.update {
            it.copy(
                isHomeTopBarMessageButtonScreen = true
            )
        }
    }

    fun switchCameraScreenTrue() {
        _detailUiState.update {
            it.copy(
                isCameraScreen = true
            )
        }
    }

    fun clearTheBadgeNumber(index: Int) {
        _detailUiState.value.messageNumber[index] = ""
    }

    fun closeAllExtraScreen() {
        _detailUiState.update {
            it.copy(
                isDetailScreen = false,
                isHomeTopBarMessageButtonScreen = false,
                isHomeTopBarNavigationButtonScreen = false,
                isCameraScreen = false
            )
        }
    }

    @Deprecated("wait compose update get LocationManager smoothly")
    fun updateLocationDetail(locationManager: LocationManager?, context: Context) {
        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("updateLocationDetail", "No Permission")
                return
            }
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                locationListener
            )
        } catch (e: SecurityException) {
            Log.e("SecurityException", e.toString())
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ShopDetailsApplication)
                val shopDetailsRepository = application.container.shopDetailsRepository
                ShopsViewModel(shopDetailsRepository = shopDetailsRepository)
            }
        }
    }
}