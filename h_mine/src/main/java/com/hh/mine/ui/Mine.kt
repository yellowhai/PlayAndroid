package com.hh.mine.ui

import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import com.hh.common.bean.MineItemBean
import com.hh.common.bean.ModelPath
import com.hh.common.bean.UserInfo
import com.hh.common.theme.HhfTheme
import com.hh.common.util.*
import com.hh.common.util.CacheUtils.avatar
import com.hh.common.util.CacheUtils.isLogin
import com.hh.common.view.NetworkImage
import com.hh.common.view.QureytoImageShapes
import com.hh.mine.R
import com.hh.mine.mineList
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * @ProjectName: com.hh.hhf.ui
 * @Package: com.hh.hhf.ui
 * @Description: 类描述
 * @Author: Hai Huang
 * @CreateDate: 2021/8/27  9:40
 */
@Composable
fun Mine(modifier: Modifier = Modifier, list: List<MineItemBean> = mineList) {
    val context = LocalContext.current
    val mineViewModel: MineViewModel = viewModel()
    LaunchedEffect(CacheUtils.userInfo) {
        if (CacheUtils.userInfo != "") {
            mineViewModel.dispatch(
                MineViewEvent.SetUserInfo(
                    Gson().fromJson(
                        CacheUtils.userInfo,
                        UserInfo::class.java
                    )
                )
            )
            mineViewModel.dispatch(MineViewEvent.GetIntegral)
        }
    }
    Column(modifier) {
        MineTopAvatar(
            Modifier
                .fillMaxWidth()
                .height(320.dp)
                .clip(QureytoImageShapes(160f)),
            mineViewModel.viewStates.userInfo?.run {
                nickname
            } ?: "avatar"
        ) {
            XXPermissions
                .with(context)
                .permission(Permission.CAMERA)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .request(object : OnPermissionCallback {
                    override fun onGranted(
                        granted: List<String>,
                        all: Boolean
                    ) {
                        if (all) {
                            if(isLogin){
                                mineViewModel.dispatch(MineViewEvent.ChangePopupState(true))
                            }
                            else{
                                CpNavigation.to(ModelPath.Login)
                            }
                        }
                    }

                    override fun onDenied(
                        denied: List<String>,
                        never: Boolean
                    ) {
                        if (never) {
                            context.showToast(context.stringResource(R.string.permissions_cm_error))
                            XXPermissions.startPermissionActivity(
                                context,
                                denied
                            )
                        } else {
                            context.showToast(context.stringResource(R.string.permissions_camera_error))
                        }
                    }
                })
        }
        Surface(
            Modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth()
                .background(HhfTheme.colors.background),
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colors.surface, // color will be adjusted for elevation
        ) {
            LazyColumn(Modifier.background(HhfTheme.colors.listItem)) {
                itemsIndexed(list) { i, bean ->
                    MineItem(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                mineViewModel.dispatch(MineViewEvent.ToComposable(bean.id))
                            }
                            .height(45.dp), bean.name, HhfTheme.colors.themeColor,
                        bean.icon)
                    if (i < list.size - 1) {
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp)
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun MineItem(
    modifier: Modifier = Modifier, textName: String, iconColor: Color, icon: ImageVector
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            icon, "$textName icon",
            Modifier
                .size(28.dp)
                .padding(start = 10.dp), iconColor
        )
        Text(
            textName,
            Modifier.padding(start = 10.dp),
            fontSize = 14.sp,
            fontFamily = FontFamily.Serif,
            color = HhfTheme.colors.textColor
        )
        Icon(
            Icons.Filled.KeyboardArrowRight, textName,
            Modifier
                .weight(1f)
                .padding(end = 10.dp)
                .wrapContentWidth(Alignment.End),
            tint = HhfTheme.colors.textColor
        )
    }
}

@Composable
fun MineTopAvatar(
    modifier: Modifier = Modifier,
    title: String,
    imgClick: () -> Unit = {}
) {
    val viewModel: MineViewModel = viewModel()
    var fileBitmap by remember { mutableStateOf(avatar) }
    LaunchedEffect(fileBitmap) {
        withContext(IO) {
            viewModel.dispatch(MineViewEvent.Blur(fileBitmap))
        }
    }
    Box(modifier, contentAlignment = Alignment.Center) {
        viewModel.viewStates.backgroundBitmap?.apply {
            NetworkImage(
                this,
                contentScale = ContentScale.FillWidth,
                modifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    Modifier.graphicsLayer(
                        renderEffect = RenderEffect.createBlurEffect(
                            25f,
                            25f,
                            Shader.TileMode.MIRROR
                        )
                            .asComposeRenderEffect()
                    )
                } else Modifier,
                defaultImg = R.mipmap.ic_default_round
            )
        }
        Column {
            NetworkImage(
                if (avatar.startsWith("http")) viewModel.viewStates.avatarBitmap
                else File(viewModel.viewStates.avatarBitmap),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(120.dp)
                    .background(color = Color.White, shape = CircleShape)
                    .padding(3.dp)
                    .clip(CircleShape)
                    .shadow(elevation = 150.dp, clip = true)
                    .clickable {
                        imgClick.invoke()
                    },
                defaultImg = R.mipmap.ic_default_round
            )
            PicPopup(viewModel.viewStates.isShowPopup, Modifier) { path, isShow ->
                path?.apply {
                    fileBitmap = this
                }
                viewModel.dispatch(MineViewEvent.ChangePopupState(isShow))
            }
        }
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(bottom = 72.dp)
                .align(Alignment.BottomCenter)
        )
        Row(
            modifier = Modifier
                .padding(bottom = 48.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = viewModel.viewStates.userInfo?.run { "id: $id" } ?: "",
                color = Color.White,
                fontSize = 12.sp,
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = viewModel.viewStates.integral?.run { "${stringResource(id = R.string.rank)}: $rank" }
                    ?: "",
                color = Color.White,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
fun PicPopup(
    isShowPopup: Boolean,
    modifier: Modifier = Modifier,
    photoOrShow: (path: String?, isShow: Boolean) -> Unit = { _, _ -> }
) {
    "PicPopup $isShowPopup".logE()
    val context = LocalContext.current
    val coroutineStore = rememberCoroutineScope()
    var f: File? = null
    // 定义一个申请系统权限的意图
    val photoLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        f?.let { file ->
            if (file.exists()) {
                PhotoUtils.createFile(context, file, context.stringResource(R.string.photo_path))
                coroutineStore.launch {
                    withContext(IO) {
                        val photoPath =
                            "${context.getStorageEmulated0Dirs()}/${Environment.DIRECTORY_PICTURES}/${
                                context.stringResource(R.string.photo_path)
                            }/${file.name}"
                        avatar = photoPath
                        file.delete()
                        photoOrShow.invoke(photoPath, false)
                    }
                }
            }
        }
    }
    val pickLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.let {
            it.data?.also { uri ->
                coroutineStore.launch {
                    withContext(IO) {
                        PhotoUtils.getPath2uri(context, uri)
                            ?.let { it1 ->
                                avatar = it1
                                photoOrShow.invoke(it1, false)
                            }
                    }
                }
            }
        }
    }
    DropdownMenu(
        isShowPopup,
        { photoOrShow.invoke(null, false) },
        modifier, offset = DpOffset(20.dp, 0.dp)
    ) {
        DropdownMenuItem(onClick = {
            // 从相机中获得照片
            val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val imageFileName = String.format("JPEG_%s.jpg", formatDate("yyyyMMdd_HH-mm-ss"))
            f = File(
                context.getExternalFilesDir("${Environment.DIRECTORY_PICTURES}/photo"),
                imageFileName
            )
            val photoUri =
                FileProvider.getUriForFile(context, context.packageName + ".provider", f!!)
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            intentCamera.putExtra(MediaStore.Images.Media.ORIENTATION, 0)
            photoLauncher.launch(intentCamera)
        }) {
            Text(stringResource(R.string.photo))
        }
        DropdownMenuItem(onClick = {
            // 从相簿中获得照片
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            pickLauncher.launch(intent)
        }) {
            Text(stringResource(R.string.album))
        }
    }
}