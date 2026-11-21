package com.tyranor.next.ui.main

import androidx.activity.compose.BackHandler
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.tyranor.next.R
import com.tyranor.next.core.game.launch.EngineLauncher
import com.tyranor.next.core.engine.runtime.RuntimeModuleBindings
import com.tyranor.next.core.engine.runtime.RuntimeModuleRegistry
import com.tyranor.next.core.i18n.AppLocaleController
import com.tyranor.next.core.settings.AppSettingsStore
import com.tyranor.next.theme.AppThemeColors
import com.tyranor.next.theme.MiuixSettingsTheme
import com.tyranor.next.theme.DefaultPageBackgroundLayer
import com.tyranor.next.theme.GlassNavSurface
import com.tyranor.next.theme.NavWhite
import com.tyranor.next.theme.UnselectedGrey
import com.tyranor.next.theme.glassBorder
import com.tyranor.next.theme.glassPageBackground
import com.tyranor.next.ui.common.AppIcons
import com.tyranor.next.ui.common.LiquidGlassNavItem
import com.tyranor.next.ui.common.AppAlertDialog
import com.tyranor.next.ui.common.AppNavigationRail
import com.tyranor.next.ui.common.DialogTextButton
import com.tyranor.next.ui.common.motion.LocalPageDepth
import com.tyranor.next.ui.common.motion.depthFoldStack
import com.tyranor.next.ui.common.motion.LocalSheetBackdrop
import com.tyranor.next.ui.common.motion.SheetBackdrop
import com.tyranor.next.ui.common.motion.rememberSheetBackdrop
import com.tyranor.next.ui.common.FloatingGlassNavButton
import com.tyranor.next.ui.common.FloatingNavOutsideTapBridge
import com.tyranor.next.ui.common.observeFloatingNavOutsideTaps
import com.tyranor.next.ui.common.LaunchErrorDialog
import com.tyranor.next.ui.common.LaunchErrorState
import com.tyranor.next.core.game.launch.LaunchFailureBus
import com.tyranor.next.ui.common.LiquidGlassNavigationBar
import com.tyranor.next.ui.common.NavigationTabIcon
import com.tyranor.next.ui.common.effectiveNavStyle
import com.tyranor.next.ui.common.isSideRailLayout
import com.tyranor.next.ui.common.toErrorState
import com.tyranor.next.ui.common.syncErrorResource
import com.tyranor.next.ui.common.glass.EnhancedLiquidGlassNavigationBar
import com.tyranor.next.ui.common.glass.GlassShaderSupport
import com.tyranor.next.ui.common.glass.GlassBottomBarSpec
import com.tyranor.next.ui.common.glass.rememberGlassBottomBarColors
import com.tyranor.next.theme.WithoutPressIndication
import com.tyranor.next.theme.AppComponentShape
import com.tyranor.next.ui.engine.EngineScreen
import com.tyranor.next.ui.game.GameScreen
import com.tyranor.next.ui.home.HomeScreen
import com.tyranor.next.ui.tools.ToolsScreen
import com.tyranor.next.ui.settings.SettingsScreen
import com.tyranor.next.ui.common.CoachAnchor
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.tyranor.next.ui.common.motion.rememberEntranceState
import com.tyranor.next.ui.common.motion.ProvideEntrance
import android.os.SystemClock
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberUpdatedState
import com.tyranor.next.ui.common.motion.EntranceState
import com.tyranor.next.ui.common.motion.MotionMode
import com.tyranor.next.ui.common.motion.HiddenPageAlpha
import com.tyranor.next.ui.common.motion.skipDrawWhenHidden
import com.tyranor.next.ui.common.motion.tabPageTransform
import com.tyranor.next.ui.common.motion.skipEntranceOnDrag
import com.tyranor.next.ui.common.collectSettingAsState
import com.tyranor.next.ui.common.notice.AppNotice

// 底部导航栏 Tab 定义（Material Icons：选中 Filled / 未选中 Outlined）
@Immutable
private data class Tab(
  @StringRes val labelRes: Int,
  val icon: ImageVector,
  val unselectedIcon: ImageVector,
)

private val tabItems = listOf(
  Tab(R.string.nav_home, AppIcons.home, AppIcons.homeOutlined),
  Tab(R.string.nav_games, AppIcons.game, AppIcons.gameOutlined),
  Tab(R.string.nav_engine, AppIcons.module, AppIcons.moduleOutlined),
  Tab(R.string.nav_tools, AppIcons.tools, AppIcons.toolsOutlined),
  Tab(R.string.nav_settings, AppIcons.settings, AppIcons.settingsOutlined),
)

/** 托管五个主标签页及根 Miuix 浮层；透明且零 inset 的宿主让菜单遮罩覆盖底栏。 */
@Composable
fun MainScreen(
  modifier: Modifier = Modifier,
  coachPage: Int? = null,
  onCoachAnchorChanged: (Int, Rect) -> Unit = { _, _ -> },
) {
  val context = LocalContext.current
  var globalLaunchError by remember { mutableStateOf<LaunchErrorState?>(null) }
  val busFailure by LaunchFailureBus.failure.collectAsStateWithLifecycle()
  LaunchedEffect(busFailure) {
    busFailure?.let {
      globalLaunchError = it.toErrorState(context)
      LaunchFailureBus.clear()
    }
  }
  // 「显示首页」开关（应用设置，默认开）：关闭后首页不参与导航，游戏页作为第一页
  // 随生命周期订阅：设置页盖在上面、主界面不可见时不在后台增删首页（组合 / 销毁首页会占满主线程，让设置页开关卡顿）
  // 先从 prefs 初始化要订阅的流；首帧页数和默认选中项使用同一个值，关闭首页时不会先闪出全部导航图标。
  val homePageEnabled by remember(context) {
    AppSettingsStore.initHomePageEnabled(context)
    AppSettingsStore.homePageEnabledState
  }.collectSettingAsState()
  var selectedIndex by rememberSaveable {
    mutableStateOf(if (homePageEnabled) 0 else 1)
  }
  // 关闭首页时，若当前停在其上则收敛到游戏页（引导页索引同样收敛，避免空白页）
  LaunchedEffect(homePageEnabled) {
    if (!homePageEnabled && selectedIndex == 0) selectedIndex = 1
  }
  // 引导期间由 coachPage 临时接管当前页；其余时间使用用户选择的 selectedIndex。
  // 所有转场、导航和页面交互都使用同一个 activeIndex，避免首帧出现双索引错位。
  val requestedIndex = coachPage?.coerceIn(tabItems.indices) ?: selectedIndex
  val activeIndex = if (!homePageEnabled && requestedIndex == 0) 1 else requestedIndex
  val libraryViewModel: MainLibraryViewModel = viewModel()
  val libraryState by libraryViewModel.uiState.collectAsStateWithLifecycle()
  val interactScope = rememberCoroutineScope()
  val unselectedColor = UnselectedGrey
  LaunchedEffect(Unit) {
    withContext(Dispatchers.IO) { AppSettingsStore.initEngineTabs(context) }
    withContext(Dispatchers.IO) { AppSettingsStore.initEngineCardIcon(context) }
    withContext(Dispatchers.IO) { AppSettingsStore.initGameCardHideTitleTag(context) }
    withContext(Dispatchers.IO) { AppSettingsStore.initGameCardBadge(context) }
    withContext(Dispatchers.IO) { AppSettingsStore.initGameCardStyle(context) }
    withContext(Dispatchers.IO) { AppSettingsStore.initGameCardCorners(context) }
    withContext(Dispatchers.IO) { AppSettingsStore.initHomeStyle(context) }
    withContext(Dispatchers.IO) { AppSettingsStore.initFloatingNavPosition(context) }
  }
  LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
    // 库刷新（含 xsys35 标题回写的前台补导入，见 MainLibraryViewModel.onForeground）
    libraryViewModel.onForeground()
    // 存档互通前台兜底：对已退出会话的待回写游戏补一次 Tyranor→标准同步
    // （引擎退出后 500ms 强杀、无回调，故在应用回到前台时补齐）。
    interactScope.launch {
      // runCatching 会把协程取消也当作失败吞掉，故显式区分：取消原样传播，其余仅记日志
      try {
        EngineLauncher.flushPendingSaveSync(context)
      } catch (ce: CancellationException) {
        throw ce
      } catch (t: Throwable) {
        android.util.Log.w("MainScreen", "pending RPG save sync failed", t)
      }
    }
    // 第三方运行时模块：卸载/禁用后把绑定游戏降级为 UNKNOWN；同时刷新模块 label 缓存。
    interactScope.launch {
      try {
        // 给安装器/包替换留出稳定窗口，避免模块更新瞬间把全部绑定误降级。
        delay(1500)
        val degraded = RuntimeModuleBindings.reconcile(context)
        RuntimeModuleRegistry.refresh(context)
        libraryViewModel.refreshFromStorage()
        if (degraded > 0) {
          AppNotice.makeText(
            context,
            R.string.engine_module_management_degraded_toast,
            android.widget.Toast.LENGTH_SHORT,
          ).show()
        }
      } catch (ce: CancellationException) {
        throw ce
      } catch (t: Throwable) {
        android.util.Log.w("MainScreen", "runtime module reconcile failed", t)
      }
    }
  }
  // Material 3 统一导航：底部 NavigationBar / 平板 NavigationRail，不再有液态玻璃与悬浮导航形态。
  // 页面级返回处理后注册，仍优先处理网页历史和浮层关闭。
  val hostActivity = AppLocaleController.findActivity(context)
  BackHandler { hostActivity?.leaveMainScreen() }
  // 页面区里的抽屉与弹窗（AppAlertDialog）升起时，页面区只褪色、不再上下翻折
  val overlayBackdrop = rememberSheetBackdrop()
  val cleanupError = libraryState.cleanupError
  if (cleanupError != null || libraryState.copyCleanupPending) {
    val cleanupMessage = if (cleanupError != null) {
      stringResource(R.string.settings_systemnnn_copy_cleanup_schedule_failed, libraryState.cleanupGameTitle.orEmpty(),
        stringResource(cleanupError.syncErrorResource()))
    } else {
      stringResource(R.string.settings_systemnnn_copy_cleanup_pending)
    }
    CompositionLocalProvider(LocalSheetBackdrop provides overlayBackdrop) {
      AppAlertDialog(
        onDismissRequest = libraryViewModel::dismissCleanupError,
        title = { Text(stringResource(R.string.save_operation_failed), style = MaterialTheme.typography.titleMedium) },
        text = { Text(cleanupMessage, style = MaterialTheme.typography.bodyMedium) },
        textScrollEnabled = true,
        confirmButton = {
          DialogTextButton(stringResource(R.string.common_confirm), onClick = libraryViewModel::dismissCleanupError)
        },
      )
    }
  }
  // 首页样式：Web 首页与原生首页由 HomeScreen 自行切换
  val homeStyle by AppSettingsStore.homeStyleState.collectSettingAsState()
  // 平板/大窗口：主导航移到侧边（M3 NavigationRail），否则使用 M3 NavigationBar。
  val railLayout = isSideRailLayout()
  val liquidBottomActive = false
  val floatingButtonActive = false
  val floatingDefaultNav = false
  val bottomBackdropActive = false
  val enhancedBarActive = false
  // 可见 Tab：关闭首页后首页不参与导航（游戏页成为第一页）
  val visibleTabEntries = remember(homePageEnabled) {
    tabItems.mapIndexedNotNull { index, tab ->
      if (homePageEnabled || index != 0) index to tab else null
    }
  }
  val tabLabels = visibleTabEntries.map { stringResource(it.second.labelRes) }
  // remember(tabLabels)：labels 内容不变时复用同一份 items，避免每次重组都给增强栏传新 List
  // （增强栏据此跳过重组，进而避免 drawBackdrop 元素被判不等而重建 RenderEffect 管线）
  val liquidGlassTabItems = remember(tabLabels) {
    visibleTabEntries.mapIndexed { index, entry ->
      LiquidGlassNavItem(
        label = tabLabels[index],
        icon = entry.second.icon,
        unselectedIcon = entry.second.unselectedIcon,
      )
    }
  }
  // 导航组件内部使用「可见条目下标」，选中值需从页面下标换算
  val selectedNavIndex = visibleTabEntries.indexOfFirst { it.first == activeIndex }.coerceAtLeast(0)

  val pageTransition = updateTransition(targetState = activeIndex, label = "mainTabTransition")
  val tabMotion = rememberMainTabMotion(pageTransition, tabItems.size, onboarding = coachPage != null)
  // 每个标签页一份入场节拍：切到该页时卡片 / 列表项依次弹入（页内 staggeredEntrance 使用）。
  // 在切页的同一帧同步重播，页内元素首帧即按隐藏绘制，不会先闪一下
  val tabEntrances = tabItems.indices.map { page -> rememberEntranceState(playOnStart = page == activeIndex) }
  val tabLeftAt = remember { LongArray(tabItems.size) }

  /** Both coach-driven and user-driven page changes share the existing hidden-page entrance gate. */
  fun changeSelectedPage(previous: Int, index: Int) {
    val now = SystemClock.uptimeMillis()
    tabLeftAt[previous] = now
    tabMotion[index].replayEntranceIfHidden(tabEntrances[index], now - tabLeftAt[index])
    selectedIndex = index
  }

  LaunchedEffect(coachPage) {
    val index = coachPage?.coerceIn(tabItems.indices) ?: return@LaunchedEffect
    if (index != selectedIndex) changeSelectedPage(selectedIndex, index)
  }

  /** Validates tab requests, preserves ordinary-mode guards, and replays entrance only for hidden pages. */
  fun selectPage(index: Int) {
    // 索引保护：宿主可能收到越界请求（例如 items 变化后的晚到回调）；
    // 关闭首页时首页下标同样不可达
    if (visibleTabEntries.none { it.first == index }) return
    if (index == activeIndex) return
    // 透镜档需要「转场期间接受新目标」：透镜已经跟手移动，页面却不动会明显脱节。
    // 这里判的是**实际生效的档**（enhancedBarActive）而不是设置值：AGSL 不可用时界面已回退成
    // 经典档，转场语义必须跟着回退，否则会出现「看着是经典档、行为是透镜档」的错配。
    // 默认路径保持原有守卫，避免把这一行为变更带给未开启该选项的用户（报告 §8.7）。
    // 「普通」动效档：原版守卫（转场进行中忽略）；「增强」档不设守卫：切页是弹簧转场，
    // 转场途中点新页签会从当前位置与速度直接转向新目标（可打断）
    val guardActive = !MotionMode.enhanced
    if (!enhancedBarActive && !floatingButtonActive && pageTransition.isRunning && guardActive) return
    // 切回刚离开不久的标签页（A→B→A）时不重播：那一页还在屏幕上，重播会把正在显示的内容清空再弹入
    changeSelectedPage(activeIndex, index)
  }
  // 导航组件回调使用可见条目下标；这里换算回页面下标（关闭首页后二者不同）
  val onSelectNavPage: (Int) -> Unit = { navIndex ->
    visibleTabEntries.getOrNull(navIndex)?.let { selectPage(it.first) }
  }
  // 外层只负责布局：内容区 + 底部导航栏（M3 NavigationBar 占布局高度）
  Box(modifier.fillMaxSize()) {
    val contentLayerModifier = Modifier
    if (railLayout) {
      // 平板/大窗口：主导航移到侧边，按外观风格取该主题「默认导航栏」形态
      Row(Modifier.fillMaxSize()) {
        AppNavigationRail(
          selectedIndex = selectedNavIndex,
          items = liquidGlassTabItems,
          onItemClick = onSelectNavPage,
        )
        Column(Modifier.weight(1f).fillMaxHeight().then(contentLayerModifier)) {
          MainTabPages(
            selectedIndex = activeIndex,
            pageTransition = pageTransition,
            tabMotion = tabMotion,
            tabEntrances = tabEntrances,
            homePageEnabled = homePageEnabled,
            libraryState = libraryState,
            libraryViewModel = libraryViewModel,
            onCoachAnchorChanged = onCoachAnchorChanged,
            overlay = overlayBackdrop,
            foldDesaturate = homeStyle != AppSettingsStore.HOME_STYLE_WEB,
            onboarding = coachPage != null,
          )
        }
      }
    } else {
      Column(Modifier.fillMaxSize().then(contentLayerModifier)) {
        MainTabPages(
          selectedIndex = activeIndex,
          pageTransition = pageTransition,
          tabMotion = tabMotion,
          tabEntrances = tabEntrances,
          homePageEnabled = homePageEnabled,
          libraryState = libraryState,
          libraryViewModel = libraryViewModel,
          onCoachAnchorChanged = onCoachAnchorChanged,
          overlay = overlayBackdrop,
          foldDesaturate = homeStyle != AppSettingsStore.HOME_STYLE_WEB,
          onboarding = coachPage != null,
        )
        DefaultBottomNavigationBar(
          selectedIndex = selectedNavIndex,
          items = liquidGlassTabItems,
          unselectedColor = unselectedColor,
          onSelectPage = onSelectNavPage,
        )
      }
    }
  }

  globalLaunchError?.let { state ->
    LaunchErrorDialog(state = state, onDismiss = { globalLaunchError = null })
  }
}

/**
 * 主界面可见页常驻组合（默认五页；应用设置关闭首页后为四页）：隐藏页只保留已测量节点且 alpha=0，
 * 切换时不再重建游戏网格/Miuix 设置页；
 * 中间 Tab 的 alpha 在任意状态均为 0，跨页时只显示起点和终点的直接水平动画。
 * 底栏形态与平板侧栏形态共用本组件，保证两种布局下的页面与转场完全一致。
 */
/** 页面内容的入场节拍 + 景深句柄：页面里的抽屉与弹窗（AppAlertDialog）经 LocalSheetBackdrop 登记，页面区随之翻折。 */
@Composable
private fun ProvideEntranceAndBackdrop(
  entrance: EntranceState,
  overlay: SheetBackdrop,
  content: @Composable () -> Unit,
) {
  ProvideEntrance(entrance) {
    CompositionLocalProvider(LocalSheetBackdrop provides overlay, content = content)
  }
}

@Composable
private fun ColumnScope.MainTabPages(
  selectedIndex: Int,
  pageTransition: Transition<Int>,
  tabMotion: List<MainTabMotion>,
  tabEntrances: List<EntranceState>,
  homePageEnabled: Boolean,
  libraryState: MainLibraryUiState,
  libraryViewModel: MainLibraryViewModel,
  onCoachAnchorChanged: (Int, Rect) -> Unit,
  overlay: SheetBackdrop,
  foldDesaturate: Boolean,
  onboarding: Boolean,
) {
    // 抽屉 / 弹窗与页面推入共用景深层，按最大进度合成，不叠加。
    Box(
      Modifier.weight(1f).fillMaxWidth().clipToBounds()
        .depthFoldStack(overlay = overlay, panel = null, push = LocalPageDepth.current, desaturate = foldDesaturate),
    ) {
      WithoutPressIndication {
        // 可见页面常驻组合：隐藏页只保留已测量节点且 alpha=0，切换时不再重建游戏网格/Miuix 设置页。
        // 中间 Tab 的 alpha 在任意状态均为 0，因此跨页时只显示起点和终点的直接水平动画。
        // 关闭首页时不再组合首页（下标 0），游戏页成为第一个可见页。
        val visiblePages = if (homePageEnabled) tabItems.indices.toList() else tabItems.indices.drop(1)
        // key(page)：页面身份按 Tab 下标固定。可见集合变化（如关闭/恢复首页）时，
        // 未变化页面的组合槽位与 pageOffset/pageAlpha 动画状态不会被错位复用而闪现。
        visiblePages.forEach { page ->
          key(page) {
            MainTabPageHost(
              page = page,
              selected = page == selectedIndex,
              pageTransition = pageTransition,
              motion = tabMotion[page],
              entrance = tabEntrances[page],
              overlay = overlay,
              allowHiddenDraw = page != 0 || foldDesaturate,
              reportActive = onboarding,
            ) { pageActive ->
              when (page) {
                0 -> HomeScreen(
                  modifier = Modifier.fillMaxSize(),
                  libraryState = libraryState,
                  onGameUpdated = libraryViewModel::replaceGame,
                  onGameCoverUpdated = libraryViewModel::applyCoverUpdate,
                  onGameRenamed = libraryViewModel::renameGame,
                  onGameLaunchFileUpdated = libraryViewModel::updateLaunchFile,
                  onGameBindingUpdated = libraryViewModel::applyBindingUpdate,
                  onGameDeleted = libraryViewModel::deleteGame,
                  onRecentRemoved = libraryViewModel::removeRecentGame,
                  onQuickLaunchToggle = libraryViewModel::toggleQuickLaunch,
                  onCoachAnchorChanged = { onCoachAnchorChanged(CoachAnchor.HOME, it) },
                  isActive = pageActive,
                )
                1 -> GameScreen(
                  modifier = Modifier.fillMaxSize(),
                  libraryState = libraryState,
                  onGameUpdated = libraryViewModel::replaceGame,
                  onGameCoverUpdated = libraryViewModel::applyCoverUpdate,
                  onGameRenamed = libraryViewModel::renameGame,
                  onGameLaunchFileUpdated = libraryViewModel::updateLaunchFile,
                  onGameBindingUpdated = libraryViewModel::applyBindingUpdate,
                  onGameDeleted = libraryViewModel::deleteGame,
                  onQuickLaunchToggle = libraryViewModel::toggleQuickLaunch,
                  onScanLibrary = libraryViewModel::scanLibrary,
                  onScrapeEventShown = libraryViewModel::acknowledgeScrapeEvent,
                  onSearchQueryChanged = libraryViewModel::onSearchQueryChanged,
                  onAddManualGame = libraryViewModel::addManualGame,
                  onCoachAnchorChanged = onCoachAnchorChanged,
                  isActive = pageActive,
                )
                2 -> EngineScreen(
                  Modifier.fillMaxSize(),
                  onCoachAnchorChanged = { onCoachAnchorChanged(CoachAnchor.ENGINE, it) },
                  isActive = pageActive,
                  gameCount = libraryState.games.size,
                )
                3 -> ToolsScreen(Modifier.fillMaxSize())
                4 -> SettingsScreen(
                  Modifier.fillMaxSize(),
                  onCoachAnchorChanged = { onCoachAnchorChanged(CoachAnchor.SETTINGS, it) },
                  isActive = pageActive,
                )
              }
            }
          }
        }
      }
    }
}

/** 保留桌面根任务正常返回时移到后台的系统语义。 */
private fun Activity.leaveMainScreen() {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isTaskRoot &&
    intent?.action == Intent.ACTION_MAIN && intent?.hasCategory(Intent.CATEGORY_LAUNCHER) == true &&
    moveTaskToBack(false)
  ) return
  finish()
}

/** 各页独立订阅交互门控。某一页到位时，无需重新执行其他常驻页的外壳。 */
@Composable
internal fun MainTabPageHost(
  page: Int,
  selected: Boolean,
  pageTransition: Transition<Int>,
  motion: MainTabMotion,
  entrance: EntranceState,
  overlay: SheetBackdrop,
  allowHiddenDraw: Boolean,
  reportActive: Boolean,
  content: @Composable (Boolean) -> Unit,
) {
  // 可交互：选中且已基本到位（弹簧尾巴不再阻挡点击）
  val pageSettled by remember(motion) {
    derivedStateOf { kotlin.math.abs(motion.offset.value) < 0.02f && motion.alpha.value > 0.98f }
  }
  val pageInteractive = selected &&
    (if (MotionMode.enhanced) pageSettled else !pageTransition.isRunning)
  // 隐藏页（alpha=0）不把内容画进显示列表（见 skipDrawWhenHidden）。Web 首页（WebView）的原生视图靠 Compose 的绘制回调
  // 才画出来，不参与；「普通」档保持原样
  val gateHiddenDraw = MotionMode.enhanced && allowHiddenDraw
  val pageShown by remember(motion) { derivedStateOf { motion.alpha.value > HiddenPageAlpha } }
  val pageSelected by rememberUpdatedState(selected)
  // 首页在停稳后恢复 Web 生命周期或导入游戏时长；其余页面只在引导需要锚点时订阅。
  val pageActive = (page == 0 || reportActive) && selected && !pageTransition.isRunning
  Box(
    Modifier
      .fillMaxSize()
      .skipEntranceOnDrag(entrance)
      .zIndex(if (selected) 1f else 0f)
      // 整宽水平位移 + 淡入淡出 + 景深缩放（滑动途中两页略微缩小，停稳时为 1，外观不变）；增强档的位移 / 缩放在绘制阶段图层里（见 tabPageTransform）
      .tabPageTransform(
        key = motion,
        offset = { motion.offset.value },
        alpha = { motion.alpha.value },
      )
      .then(
        if (gateHiddenDraw) {
          Modifier.skipDrawWhenHidden { pageShown || pageSelected }
        } else {
          Modifier
        },
      )
      .then(
        if (pageInteractive) {
          Modifier
        } else {
          Modifier
            .clearAndSetSemantics { }
            .pointerInput(page) {
              awaitPointerEventScope {
                while (true) {
                  awaitPointerEvent(PointerEventPass.Initial).changes.forEach { it.consume() }
                }
              }
            }
        },
      ),
  ) {
    ProvideEntranceAndBackdrop(entrance, overlay) { content(pageActive) }
  }
}

/**
 * 默认底部导航栏（Material3 NavigationBar，含选中态图标填充动画）。
 * 玻璃外观风格下由调用方包一层圆角玻璃容器悬浮显示，并传 `windowInsets = WindowInsets(0.dp)`
 * 由外层统一处理系统栏避让。
 */
@Composable
private fun DefaultBottomNavigationBar(
  selectedIndex: Int,
  items: List<LiquidGlassNavItem>,
  unselectedColor: Color,
  onSelectPage: (Int) -> Unit,
  modifier: Modifier = Modifier,
  showLabels: Boolean = true,
) {
  NavigationBar(
    modifier = modifier,
    containerColor = MaterialTheme.colorScheme.surfaceContainer,
    contentColor = MaterialTheme.colorScheme.onSurface,
    windowInsets = NavigationBarDefaults.windowInsets,
  ) {
    items.forEachIndexed { index, item ->
      val label = item.label
      val selected = selectedIndex == index
      NavigationBarItem(
        selected = selected,
        onClick = { onSelectPage(index) },
        icon = {
          // 选中态图标填充动画与平板侧栏共用同一实现（见 ui/common/NavigationTabIcon.kt）
          NavigationTabIcon(
            icon = item.icon ?: AppIcons.home,
            unselectedIcon = item.unselectedIcon ?: AppIcons.homeOutlined,
            contentDescription = label,
            selected = selected,
            unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedColor = MaterialTheme.colorScheme.onSecondaryContainer,
            animationLabel = "navIconFill$index",
          )
        },
        label = if (showLabels) { { Text(label) } } else null,
        // Material 3 标准指示器：选中项为 secondaryContainer 胶囊
        colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
          selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
          selectedTextColor = MaterialTheme.colorScheme.onSurface,
          indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
          unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
          unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
      )
    }
  }
}
