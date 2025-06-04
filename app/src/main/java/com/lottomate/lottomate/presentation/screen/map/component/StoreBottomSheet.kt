package com.lottomate.lottomate.presentation.screen.map.component

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.telephony.PhoneNumberUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.component.LottoMateButtonProperty
import com.lottomate.lottomate.presentation.component.LottoMateDialog
import com.lottomate.lottomate.presentation.component.LottoMateSolidButton
import com.lottomate.lottomate.presentation.component.LottoMateText
import com.lottomate.lottomate.presentation.component.LottoMateTextButton
import com.lottomate.lottomate.presentation.res.Dimens
import com.lottomate.lottomate.presentation.screen.map.StoreBottomSheetViewModel
import com.lottomate.lottomate.presentation.screen.map.model.LottoTypeFilter
import com.lottomate.lottomate.presentation.screen.map.model.StoreBottomSheetExpendedType
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfoMock
import com.lottomate.lottomate.presentation.screen.map.model.StoreListFilter
import com.lottomate.lottomate.presentation.screen.map.model.StoreWinCount
import com.lottomate.lottomate.presentation.screen.map.model.WinningDetail
import com.lottomate.lottomate.presentation.ui.LottoMateBlack
import com.lottomate.lottomate.presentation.ui.LottoMateBlue5
import com.lottomate.lottomate.presentation.ui.LottoMateBlue50
import com.lottomate.lottomate.presentation.ui.LottoMateGray10
import com.lottomate.lottomate.presentation.ui.LottoMateGray100
import com.lottomate.lottomate.presentation.ui.LottoMateGray20
import com.lottomate.lottomate.presentation.ui.LottoMateGray30
import com.lottomate.lottomate.presentation.ui.LottoMateGray60
import com.lottomate.lottomate.presentation.ui.LottoMateGray80
import com.lottomate.lottomate.presentation.ui.LottoMateGreen5
import com.lottomate.lottomate.presentation.ui.LottoMateGreen70
import com.lottomate.lottomate.presentation.ui.LottoMatePeach5
import com.lottomate.lottomate.presentation.ui.LottoMatePeach50
import com.lottomate.lottomate.presentation.ui.LottoMateRed50
import com.lottomate.lottomate.presentation.ui.LottoMateTheme
import com.lottomate.lottomate.presentation.ui.LottoMateWhite
import com.lottomate.lottomate.utils.PermissionManager
import com.lottomate.lottomate.utils.noInteractionClickable
import com.naver.maps.geometry.LatLng

@Composable
fun StoreBottomSheet(
    vm: StoreBottomSheetViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    bottomSheetExpendedType: StoreBottomSheetExpendedType,
    selectedStore: StoreInfo? = null,
    isInSeoul: Boolean,
    selectedStoreListFilter: StoreListFilter,
    onExploreMapClicked: () -> Unit,
    onShowSnackBar: (String) -> Unit,
    onSizeChanged: (Int) -> Unit,
    onSelectStore: (StoreInfo) -> Unit,
    onLoadNextPage: () -> Unit,
    onFilterSelected: (StoreListFilter) -> Unit,
) {
    val context = LocalContext.current
    val uiState by vm.state.collectAsStateWithLifecycle()

    StoreInfoBottomSheetContent(
        modifier = modifier,
        stores = uiState.stores,
        selectedStore = selectedStore,
        selectedStoreListFilter = selectedStoreListFilter,
        isInSeoul = isInSeoul,
        bottomSheetExpendedType = bottomSheetExpendedType,
        onClickStore = { onSelectStore(it) },
        onClickStoreLike = { vm.setFavoriteStore(it) },
        onClickFilter = onFilterSelected,
        onClickStoreInfoCopy = { store ->
            vm.copyStoreInfo(
                context,
                store,
                onSuccess = { onShowSnackBar(it)}
            )
        },
        onExploreMapClicked = onExploreMapClicked,
        onLoadNextPage = onLoadNextPage,
        onSizeChanged = onSizeChanged,
    )
}

@Composable
private fun StoreInfoBottomSheetContent(
    modifier: Modifier = Modifier,
    stores: List<StoreInfo>,
    selectedStore: StoreInfo?,
    selectedStoreListFilter: StoreListFilter,
    isInSeoul: Boolean,
    bottomSheetExpendedType: StoreBottomSheetExpendedType,
    onClickStore: (StoreInfo) -> Unit,
    onClickStoreLike: (Int) -> Unit,
    onClickFilter: (StoreListFilter) -> Unit,
    onClickStoreInfoCopy: (StoreInfo) -> Unit,
    onExploreMapClicked: () -> Unit,
    onSizeChanged: (Int) -> Unit,
    onLoadNextPage: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        TopGrabber(Modifier.align(Alignment.CenterHorizontally))

        if (stores.isEmpty()) {
            val isNotCollapsed = bottomSheetExpendedType != StoreBottomSheetExpendedType.COLLAPSED

            if (isNotCollapsed) {
                StoreEmptyContents(
                    isInSeoul = isInSeoul,
                    onExploreMapClicked = onExploreMapClicked,
                )
            }
        } else {
            selectedStore?.let { store ->
                SelectStoreInfoContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { onSizeChanged(it.height) },
                    store = store,
                    onClickStoreLike = onClickStoreLike,
                    onClickStoreInfoCopy = onClickStoreInfoCopy,
                )
            } ?: run {
                StoreInfoListContent(
                    modifier = Modifier.fillMaxWidth(),
                    stores = stores,
                    selectedStoreListFilter = selectedStoreListFilter,
                    onClickStore = onClickStore,
                    onClickStoreLike = onClickStoreLike,
                    onClickFilter = onClickFilter,
                    onClickStoreInfoCopy = onClickStoreInfoCopy,
                    onLoadNextPage = onLoadNextPage,
                )
            }
        }
    }
}

@Composable
private fun TopGrabber(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(top = 12.dp, bottom = 20.dp)
            .background(LottoMateGray30, RoundedCornerShape(Dimens.RadiusSmall))
            .size(width = 40.dp, height = 4.dp)
    )
}

@Composable
private fun SelectStoreInfoContent(
    modifier: Modifier = Modifier,
    store: StoreInfo,
    onClickStoreLike: (Int) -> Unit,
    onClickStoreInfoCopy: (StoreInfo) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        StoreInfoListItem(
            store = store,
            isSelect = true,
            onClickStoreLike = { onClickStoreLike(store.key) },
            onClickStoreInfoCopy = onClickStoreInfoCopy,
        )
    }
}

@Composable
private fun StoreEmptyContents(
    modifier: Modifier = Modifier,
    isInSeoul: Boolean,
    onExploreMapClicked: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.img_pochi_black),
            contentDescription = null,
        )

        LottoMateText(
            text = "해당 지역엔 로또 판매점이 없어요.\n" +
                    "위치를 이동해서 다른 지점을 찾아볼까요?",
            style = LottoMateTheme.typography.body2
                .copy(color = LottoMateGray100),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        )

        // 서울이 아니면 BottomSheet에 둘러보기 버튼 표시 (농협 본점으로 이동)
        if (!isInSeoul) {
            LottoMateSolidButton(
                text = "로또 지도 둘러보기",
                buttonSize = LottoMateButtonProperty.Size.SMALL,
                buttonShape = LottoMateButtonProperty.Shape.ROUND,
                onClick = onExploreMapClicked,
                modifier = Modifier.padding(top = 20.dp),
            )
        }
    }
}

@Composable
private fun StoreInfoListContent(
    modifier: Modifier = Modifier,
    stores: List<StoreInfo>,
    selectedStoreListFilter: StoreListFilter,
    onClickStore: (StoreInfo) -> Unit,
    onClickStoreLike: (Int) -> Unit,
    onClickFilter: (StoreListFilter) -> Unit,
    onClickStoreInfoCopy: (StoreInfo) -> Unit,
    onLoadNextPage: () -> Unit,
) {
    val listState = rememberLazyListState()
    var expendHistoryStore by remember { mutableStateOf<Int?>(null) }
    val shouldLoadNext = remember {
        derivedStateOf {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val total = listState.layoutInfo.totalItemsCount
            lastVisible?.index != 0 && lastVisible?.index == total - 5
        }
    }

    LaunchedEffect(shouldLoadNext.value) {
        if (shouldLoadNext.value) { onLoadNextPage() }
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
    ) {
        item {
            FilterRow(
                modifier = Modifier.fillMaxWidth(),
                filters = stringArrayResource(id = R.array.map_store_info_top_filters),
                selectedStoreListFilter = selectedStoreListFilter,
                onClickFilter = onClickFilter,
            )
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        items(stores) {store ->
            Column(
                modifier = Modifier
                    .clickable { onClickStore(store) }
                    .padding(top = 20.dp),
            ) {
                StoreInfoListItem(
                    store = store,
                    isExpendHistory = if (expendHistoryStore == null) false else expendHistoryStore == store.key,
                    onClickStoreLike = { onClickStoreLike(store.key) },
                    onClickStoreInfoCopy = onClickStoreInfoCopy,
                    onClickExpend = {
                        expendHistoryStore = when {
                            expendHistoryStore == null || expendHistoryStore != store.key -> store.key
                            else -> null
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun StoreInfoListItem(
    modifier: Modifier = Modifier,
    store: StoreInfo,
    isSelect: Boolean = false,
    isExpendHistory: Boolean = false,
    onClickStoreLike: () -> Unit,
    onClickStoreInfoCopy: (StoreInfo) -> Unit,
    onClickExpend: () -> Unit = {},
) {
    val context = LocalContext.current
    var storeNameLineCount by remember { mutableIntStateOf(1) }
    var showPhonePermissionDialog by remember { mutableStateOf(false) }

    if (showPhonePermissionDialog) {
        LottoMateDialog(
            title = """
                로또 지점에 전화하려면
                앱 설정에서 전화 권한을 허용해야
                전화를 걸 수 있어요
            """.trimIndent(),
            cancelText = "사용 안 함",
            confirmText = "설정으로 이동",
            onConfirm = {
                showPhonePermissionDialog = false

                val openSettings = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }

                context.startActivity(openSettings)
            },
            onDismiss = { showPhonePermissionDialog = false }
        )
    }

    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Row {
                store.hasLottoType.forEach { type ->
                    val containerColor = when (type) {
                        LottoTypeFilter.Lotto645.kr -> LottoMateGreen5
                        LottoTypeFilter.Lotto720.kr -> LottoMateBlue5
                        LottoTypeFilter.Speetto.kr -> LottoMatePeach5
                        else -> Color.Unspecified
                    }

                    val textColor = when (type) {
                        LottoTypeFilter.Lotto645.kr -> LottoMateGreen70
                        LottoTypeFilter.Lotto720.kr -> LottoMateBlue50
                        LottoTypeFilter.Speetto.kr -> LottoMatePeach50
                        else -> Color.Unspecified
                    }

                    Box(
                        modifier = modifier
                            .background(containerColor, RoundedCornerShape(Dimens.RadiusExtraSmall))
                    ) {
                        LottoMateText(
                            text = type,
                            style = LottoMateTheme.typography.caption
                                .copy(color = textColor),
                            modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp),
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .weight(1f)
                ) {
                    LottoMateText(
                        text = store.storeName,
                        style = LottoMateTheme.typography.headline1
                            .copy(color = LottoMateBlack),
                        onTextLayout = {
                            storeNameLineCount = it.lineCount
                        },
                        modifier = Modifier
                            .alignByBaseline()
                            .weight(1f),
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    LottoMateText(
                        text = if (store.distance >= 1000) {
                            if (store.distance % 1000 == 0) { "${store.distance / 1000}km" }
                            else { "${store.distance / 1000}.${(store.distance % 1000) / 100}km" }
                        } else { "${store.distance}m" },
                        style = LottoMateTheme.typography.caption
                            .copy(color = LottoMateGray80),
                        modifier = Modifier.alignByBaseline(),
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        painter = if (store.isLike) painterResource(id = R.drawable.icon_like)
                        else painterResource(id = R.drawable.icon_unlike),
                        contentDescription = "The Favorite Store Icon",
                        tint = if (store.isLike) LottoMateRed50 else LottoMateGray80,
                        modifier = Modifier.noInteractionClickable { onClickStoreLike() },
                    )

                    LottoMateText(
                        text = store.getCountLike(),
                        style = LottoMateTheme.typography.caption2
                            .copy(color = if (store.isLike) LottoMateRed50 else LottoMateGray80),
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = if (storeNameLineCount > 1) 8.dp else 0.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_place),
                    contentDescription = "",
                    tint = LottoMateGray100,
                    modifier = Modifier.size(12.dp),
                )

                LottoMateText(
                    text = store.address,
                    style = LottoMateTheme.typography.label2
                        .copy(color = LottoMateGray100),
                    modifier = Modifier.padding(start = 4.dp),
                )

                Icon(
                    painter = painterResource(id = R.drawable.icon_copy),
                    contentDescription = "",
                    tint = LottoMateGray100,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(12.dp)
                        .noInteractionClickable { onClickStoreInfoCopy(store) },
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
                    .noInteractionClickable {
                        if (store.phone.isNotEmpty()) {
                            when (
                                PermissionManager.hasPermissions(
                                    context,
                                    listOf(android.Manifest.permission.CALL_PHONE)
                                )
                            ) {
                                true -> {
                                    val formattedPhoneNumber =
                                        PhoneNumberUtils.formatNumber(store.phone, "KR")
                                    val dialIntent = Intent(
                                        Intent.ACTION_CALL,
                                        Uri.parse("tel:$formattedPhoneNumber")
                                    )
                                    context.startActivity(dialIntent)
                                }

                                false -> {
                                    val activity =
                                        context as? Activity ?: return@noInteractionClickable

                                    val permission = android.Manifest.permission.CALL_PHONE

                                    // 1. 권한을 다시 요청할 수 있는 상태인지 확인
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                                            activity,
                                            permission
                                        )
                                    ) {
                                        // 정상적인 권한 요청
                                        PermissionManager.requestPermissions(
                                            context = activity,
                                            permissions = listOf(permission),
                                        )
                                    } else {
                                        showPhonePermissionDialog = true
                                    }
                                }
                            }
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_call),
                    contentDescription = "",
                    tint = LottoMateGray100,
                    modifier = Modifier.size(12.dp),
                )

                Spacer(modifier = Modifier.width(4.dp))

                LottoMateText(
                    text = store.phone.ifEmpty { "-" },
                    style = LottoMateTheme.typography.label2
                        .copy(color = LottoMateGray100)
                )
            }

            if (store.winCountOfLottoType.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    WinLottoTypeChipGroup(store = store)

                    if (!isSelect) {
                        Icon(
                            painter = painterResource(id = if (isExpendHistory) R.drawable.icon_arrow_up else R.drawable.icon_arrow_down),
                            contentDescription = "Win Lotto History More Button Icon",
                            tint = LottoMateGray80,
                            modifier = Modifier
                                .size(20.dp)
                                .noInteractionClickable { onClickExpend() },
                        )
                    }
                }
            }
        }

        if (isExpendHistory || isSelect) {
            val winHistories = store.winCountOfLottoType.flatMap { it.winningDetails }

            StoreWinHistory(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = when {
                            isSelect && store.winCountOfLottoType.isEmpty() -> 0.dp
                            else -> 0.dp
                        }
                    ),
                histories = winHistories,
            )
        }

        if (!isSelect) {
            HorizontalDivider(
                color = LottoMateGray20,
                thickness = 1.dp,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp),
            )
        }
    }
}

@Composable
private fun StoreWinHistory(
    modifier: Modifier = Modifier,
    histories: List<WinningDetail>,
) {
    if (histories.isEmpty()) {
        Box(
            modifier = modifier
                .padding(top = 24.dp)
                .fillMaxSize()
                .background(LottoMateGray10),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pochi_6),
                    contentDescription = "",
                    colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
                    modifier = Modifier.size(100.dp),
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                LottoMateText(
                    text = "아직 당첨 이력이 없어요.",
                    style = LottoMateTheme.typography.body2
                        .copy(color = LottoMateGray100),
                )
            }
        }
    } else {
        val groupHistories = histories.chunked(4)

        Column(modifier = modifier) {
            HorizontalDivider(
                color = LottoMateGray20, 
                modifier = Modifier.padding(20.dp),
            )

            LazyRow(contentPadding = PaddingValues(horizontal = 20.dp)) {
                itemsIndexed(groupHistories) { index, history ->
                    StoreWinHistoryChipGroup(histories = history)

                    if (index != groupHistories.lastIndex) {
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterRow(
    modifier: Modifier = Modifier,
    filters: Array<String>,
    selectedStoreListFilter: StoreListFilter,
    onClickFilter: (StoreListFilter) -> Unit,
) {
    Row(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        filters.forEachIndexed { index, filter ->
            FilterItem(
                filter = filter,
                isSelected = selectedStoreListFilter.ordinal == index,
                onClickFilter = {
                    val selectFilter = StoreListFilter.findFromOrdinal(index)

                    onClickFilter(selectFilter)
                }
            )

            if (index != filters.lastIndex) {
                Divider(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(width = 1.dp, height = 16.dp)
                        .align(Alignment.CenterVertically),
                    color = LottoMateGray20,
                )
            }
        }
    }
}

@Composable
private fun FilterItem(
    modifier: Modifier = Modifier,
    filter: String,
    isSelected: Boolean = false,
    onClickFilter: () -> Unit,
) {
    LottoMateTextButton(
        buttonText = filter,
        buttonSize = LottoMateButtonProperty.Size.SMALL,
        textColor = if (isSelected) LottoMateRed50 else LottoMateGray60,
        modifier = modifier,
        onClick = onClickFilter,
    )
}

@Preview(showBackground = true)
@Composable
private fun StoreInfoBottomSheetPreview() {
    LottoMateTheme {
        Box(modifier = Modifier.background(LottoMateWhite)) {
            SelectStoreInfoContent(
                store = StoreInfoMock.copy(
                    key = 8,
                    storeName = "포이로또방",
                    address = "서울 강남구 개포로22길 19 1층",
                    latLng = LatLng(37.477752673752, 127.048542099489),
                    isLike = true,
                    winCountOfLottoType = listOf(
                        StoreWinCount(
                            lottoType = LottoType.Group.LOTTO645,
                            count = 1,
                            winningDetails = List(1) {
                                WinningDetail(
                                    lottoType = LottoType.L645,
                                    prize = "25억원",
                                    round = "6102회"
                                )
                            },
                        ),
                        StoreWinCount(
                            lottoType = LottoType.Group.LOTTO720,
                            count = 0,
                            winningDetails = List(0) {
                                WinningDetail(
                                    lottoType = LottoType.L720,
                                    prize = "25억원",
                                    round = "6102회"
                                )
                            },
                        ),
                        StoreWinCount(
                            lottoType = LottoType.Group.SPEETTO,
                            count = 2,
                            winningDetails = List(2) {
                                WinningDetail(
                                    lottoType = LottoType.S2000,
                                    prize = "25억원",
                                    round = "6102회"
                                )
                            },
                        ),
                        StoreWinCount(
                            lottoType = LottoType.Group.SPEETTO,
                            count = 2,
                            winningDetails = List(2) {
                                WinningDetail(
                                    lottoType = LottoType.S2000,
                                    prize = "25억원",
                                    round = "6102회"
                                )
                            },
                        ),
                    ).shuffled(),
                    countLike = 99999,
                    distance = 10,
                ),
                onClickStoreLike = {},
                onClickStoreInfoCopy = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StoresBottomSheetPreview() {
    LottoMateTheme {
        Box(modifier = Modifier.background(LottoMateWhite)) {
            StoreInfoListContent(
                stores = List(10) { StoreInfoMock.copy(
                    key = (1..20).random(),
                    winCountOfLottoType =
                listOf(
                    StoreWinCount(
                        lottoType = LottoType.Group.LOTTO645,
                        count = 1,
                        winningDetails = List(1) {
                            WinningDetail(
                                lottoType = LottoType.L645,
                                prize = "25억원",
                                round = "6102회"
                            )
                        },
                    ),
                    StoreWinCount(
                        lottoType = LottoType.Group.LOTTO720,
                        count = 0,
                        winningDetails = List(0) {
                            WinningDetail(
                                lottoType = LottoType.L720,
                                prize = "25억원",
                                round = "6102회"
                            )
                        },
                    ),
                    StoreWinCount(
                        lottoType = LottoType.Group.SPEETTO,
                        count = 2,
                        winningDetails = List(2) {
                            WinningDetail(
                                lottoType = LottoType.S2000,
                                prize = "25억원",
                                round = "6102회"
                            )
                        },
                    ),
                    StoreWinCount(
                        lottoType = LottoType.Group.SPEETTO,
                        count = 2,
                        winningDetails = List(2) {
                            WinningDetail(
                                lottoType = LottoType.S2000,
                                prize = "25억원",
                                round = "6102회"
                            )
                        },
                    ),
                ).shuffled()
                ) },
                selectedStoreListFilter = StoreListFilter.DISTANCE,
                onClickStore = {},
                onClickStoreLike = {},
                onClickFilter = {},
                onClickStoreInfoCopy = {},
                onLoadNextPage = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StoreInfoLongBottomSheetPreview() {
    LottoMateTheme {
        Box(modifier = Modifier.background(LottoMateWhite)) {
            SelectStoreInfoContent(
                store = StoreInfoMock.copy(
                    key = (1..10).random(),
                    storeName = "포이로또방포이로또방포이로또방포이로또방포이로또방포이로또방포이로또방",
                    address = "서울 강남구 개포로22길 19 1층",
                    latLng = LatLng(37.477752673752, 127.048542099489),
                    isLike = true,
                    winCountOfLottoType = listOf(
                        StoreWinCount(
                            lottoType = LottoType.Group.LOTTO645,
                            count = 1,
                            winningDetails = List(1) {
                                WinningDetail(
                                    lottoType = LottoType.L645,
                                    prize = "25억원",
                                    round = "6102회"
                                )
                            },
                        ),
                        StoreWinCount(
                            lottoType = LottoType.Group.LOTTO720,
                            count = 0,
                            winningDetails = List(0) {
                                WinningDetail(
                                    lottoType = LottoType.L720,
                                    prize = "25억원",
                                    round = "6102회"
                                )
                            },
                        ),
                        StoreWinCount(
                            lottoType = LottoType.Group.SPEETTO,
                            count = 2,
                            winningDetails = List(2) {
                                WinningDetail(
                                    lottoType = LottoType.S2000,
                                    prize = "25억원",
                                    round = "6102회"
                                )
                            },
                        ),
                        StoreWinCount(
                            lottoType = LottoType.Group.SPEETTO,
                            count = 2,
                            winningDetails = List(2) {
                                WinningDetail(
                                    lottoType = LottoType.S2000,
                                    prize = "25억원",
                                    round = "6102회"
                                )
                            },
                        ),
                    ).shuffled(),
                    countLike = 99999,
                    distance = 10,
                ),
                onClickStoreLike = {},
                onClickStoreInfoCopy = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StoreInfoEmptyBottomSheetPreview() {
    LottoMateTheme {
        Box(modifier = Modifier.background(LottoMateWhite)) {
            SelectStoreInfoContent(
                store = StoreInfoMock.copy(
                    key = 8,
                    storeName = "포이로또방",
                    address = "서울 강남구 개포로22길 19 1층",
                    latLng = LatLng(37.477752673752, 127.048542099489),
                    isLike = true,
                    winCountOfLottoType = emptyList(),
                    countLike = 99999,
                    distance = 10,
                ),
                onClickStoreLike = {},
                onClickStoreInfoCopy = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
private fun StoreEmptyIsInSeoulBottomSheetPreview() {
    LottoMateTheme {
        Box(modifier = Modifier.background(LottoMateWhite)) {
            val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
            )

            StoreInfoBottomSheetContent(
                stores = emptyList(),
                selectedStore = null,
                selectedStoreListFilter = StoreListFilter.DISTANCE,
                isInSeoul = true,
                bottomSheetExpendedType = StoreBottomSheetExpendedType.HALF,
                onClickStore = {},
                onClickStoreLike = {},
                onClickFilter = {},
                onClickStoreInfoCopy = {},
                onExploreMapClicked = {},
                onSizeChanged = {},
                onLoadNextPage = {},
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
private fun StoreEmptyIsInNotSeoulBottomSheetPreview() {
    LottoMateTheme {
        Box(modifier = Modifier.background(LottoMateWhite)) {
            val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
            )

            StoreInfoBottomSheetContent(
                stores = emptyList(),
                selectedStore = null,
                selectedStoreListFilter = StoreListFilter.DISTANCE,
                isInSeoul = false,
                bottomSheetExpendedType = StoreBottomSheetExpendedType.HALF,
                onClickStore = {},
                onClickStoreLike = {},
                onClickFilter = {},
                onClickStoreInfoCopy = {},
                onExploreMapClicked = {},
                onSizeChanged = {},
                onLoadNextPage = {},
            )
        }
    }
}