package com.tyranor.next.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 全应用界面图标：统一使用 Material Icons（material-icons-extended）。
 * 仅引擎家族插画（ic_engine_card_*）等非图标性质的美术资源保留为 drawable。
 */
object AppIcons {
    val home: ImageVector = Icons.Filled.Home
    val game: ImageVector = Icons.Filled.SportsEsports
    val module: ImageVector = Icons.Filled.Memory
    val tools: ImageVector = Icons.Filled.Build
    val settings: ImageVector = Icons.Filled.Settings

    val homeOutlined: ImageVector = Icons.Outlined.Home
    val gameOutlined: ImageVector = Icons.Outlined.SportsEsports
    val moduleOutlined: ImageVector = Icons.Outlined.Memory
    val toolsOutlined: ImageVector = Icons.Filled.Build
    val settingsOutlined: ImageVector = Icons.Outlined.Settings

    val back: ImageVector = Icons.AutoMirrored.Filled.ArrowBack
    val chevronRight: ImageVector = Icons.AutoMirrored.Filled.ArrowForward

    val add: ImageVector = Icons.Filled.Add
    val refresh: ImageVector = Icons.Filled.Refresh
    val scan: ImageVector = Icons.Filled.Sync
    val search: ImageVector = Icons.Filled.Search
    val filter: ImageVector = Icons.Filled.FilterList
    val save: ImageVector = Icons.Filled.Save
    val rename: ImageVector = Icons.Filled.Edit
    val delete: ImageVector = Icons.Filled.Delete
    val folder: ImageVector = Icons.Filled.Folder
    val folderOpen: ImageVector = Icons.Filled.FolderOpen
    val archive: ImageVector = Icons.Filled.Archive
    val link: ImageVector = Icons.Filled.Link
    val cloud: ImageVector = Icons.Filled.Cloud
    val download: ImageVector = Icons.Filled.Download
    val fileDownload: ImageVector = Icons.Filled.FileDownload
    val update: ImageVector = Icons.Filled.SystemUpdate
    val code: ImageVector = Icons.Filled.Code
    val groups: ImageVector = Icons.Filled.Groups
    val chat: ImageVector = Icons.Filled.Chat
    val send: ImageVector = Icons.Filled.Send
    val info: ImageVector = Icons.Filled.Info
    val description: ImageVector = Icons.Filled.Description
    val warning: ImageVector = Icons.Filled.Warning
    val error: ImageVector = Icons.Filled.Error
    val success: ImageVector = Icons.Filled.CheckCircle
    val lock: ImageVector = Icons.Filled.Lock
    val key: ImageVector = Icons.Filled.Key
    val visibility: ImageVector = Icons.Filled.Visibility
    val image: ImageVector = Icons.Filled.Image
    val imageSearch: ImageVector = Icons.Filled.ImageSearch
    val collections: ImageVector = Icons.Filled.Collections
    val bookmark: ImageVector = Icons.Filled.Bookmark
    val play: ImageVector = Icons.Filled.PlayArrow
    val forward: ImageVector = Icons.AutoMirrored.Filled.ArrowForward
    val storage: ImageVector = Icons.Filled.Storage
    val memory: ImageVector = Icons.Filled.Memory
    val extension: ImageVector = Icons.Filled.Extension
    val build: ImageVector = Icons.Filled.Build
    val tune: ImageVector = Icons.Filled.Tune
    val graphicsUp: ImageVector = Icons.Filled.ArrowUpward
    val graphicsDown: ImageVector = Icons.Filled.ArrowDownward
    val graphicsScale: ImageVector = Icons.Filled.Fullscreen
    val phone: ImageVector = Icons.Filled.PhoneAndroid
    val pc: ImageVector = Icons.Filled.Computer
    val desktopShortcut: ImageVector = Icons.Filled.AddToHomeScreen
    val playStatus: ImageVector = Icons.Filled.Timeline
    val apps: ImageVector = Icons.Filled.Apps
    val gamepad: ImageVector = Icons.Filled.SportsEsports
}
