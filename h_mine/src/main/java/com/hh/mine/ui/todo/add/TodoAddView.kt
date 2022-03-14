package com.hh.mine.ui.todo.add

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.hh.common.theme.HhfTheme
import com.hh.common.util.formatDate
import com.hh.common.view.ColumnTopBar
import com.hh.mine.R
import com.hh.mine.bean.TodoBean

/**
 * @Description: todo
 * @Author: yshh
 * @CreateDate: 2022/3/4  20:41
 */
const val todoAddBean = "todoBean"

@Composable
fun TodoAddView(modifier: Modifier = Modifier, todoBean: TodoBean? = null) {
    val viewModel: TodoAddViewModel = viewModel()
    val context = LocalContext.current
    //目前Dialog版本没适配accompanist  后续优化
//    val dialogState = rememberMaterialDialogState()
//    MaterialDialog(
//        dialogState = dialogState,
//        buttons = {
//            positiveButton(stringResource(id = R.string.confirm))
//            negativeButton(stringResource(id = R.string.cancel))
//        }
//    ) {
//        datepicker { date ->
//            viewModel.dispatch(TodoAddAction.UpdateTime("${date.year}-${date.month}-${date.dayOfMonth}"))
//        }
//    }
    LaunchedEffect(Unit) {
        todoBean?.apply {
            viewModel.dispatch(TodoAddAction.Change)
            viewModel.dispatch(TodoAddAction.InitState(title, content, priority, dateStr, id))
        }
    }
    ColumnTopBar(
        modifier
            .fillMaxSize()
            .background(HhfTheme.colors.background),
        todoBean?.run { "Todo Change" } ?: run { "Todo Add" }) {
        Row(Modifier.padding(16.dp)) {
            Text(
                "${stringResource(R.string.title)}:",
                Modifier,
                HhfTheme.colors.textColor,
                14.sp
            )
            BasicTextField(
                viewModel.viewStates.title,
                { viewModel.dispatch(TodoAddAction.UpdateTitle(it)) },
                Modifier
                    .weight(1f)
                    .padding(start = 13.dp),
                textStyle = TextStyle(HhfTheme.colors.textColor, 13.sp),
                singleLine = true,
                maxLines = 20, decorationBox = {
                    if (viewModel.viewStates.title.isEmpty()) {
                        Text(
                            stringResource(id = R.string.please_input_title),
                            Modifier,
                            color = HhfTheme.colors.textInactiveColor
                        )
                    }
                    it()
                }
            )
        }
        Divider()
        Row(Modifier.padding(16.dp)) {
            Text(
                "${stringResource(R.string.detail)}:",
                Modifier,
                HhfTheme.colors.textColor,
                14.sp
            )
            BasicTextField(
                viewModel.viewStates.detail,
                { viewModel.dispatch(TodoAddAction.UpdateDetail(it)) },
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .height(224.dp)
                    .background(HhfTheme.colors.listItem)
                    .padding(5.dp),
                textStyle = TextStyle(HhfTheme.colors.textColor, 13.sp)
            )
        }
        Divider()
        Row(Modifier.padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "${stringResource(R.string.important)}:",
                Modifier,
                HhfTheme.colors.textColor,
                14.sp
            )
            Checkbox(checked = viewModel.viewStates.level == 0, onCheckedChange = {
                if (it) {
                    viewModel.dispatch(TodoAddAction.UpdateLevel(0))
                } else {
                    viewModel.dispatch(TodoAddAction.UpdateLevel(1))
                }
            }, colors = CheckboxDefaults.colors(HhfTheme.colors.themeColor))
            Spacer(Modifier.weight(1f))
        }
        Divider()
        Row(
            Modifier
                .clickable {
                    showDatePicker(context as AppCompatActivity) {
                        viewModel.dispatch(TodoAddAction.UpdateTime(formatDate("yyyy-MM-dd", it)))
                    }
                }
                .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                "${stringResource(R.string.date)}:",
                Modifier,
                HhfTheme.colors.textColor,
                14.sp
            )
            Text(
                viewModel.viewStates.time,
                Modifier.padding(start = 8.dp),
                HhfTheme.colors.textColor,
                13.sp
            )
            Icon(
                Icons.Filled.KeyboardArrowRight, "",
                Modifier
                    .weight(1f)
                    .padding(end = 10.dp)
                    .wrapContentWidth(Alignment.End),
                tint = HhfTheme.colors.textColor
            )
        }
        Divider()
        Button(
            onClick = {
                viewModel.dispatch(TodoAddAction.Add)
            },
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .wrapContentHeight(Alignment.Bottom)
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                .height(48.dp), colors = ButtonDefaults.buttonColors(
                HhfTheme.colors.themeColor,
                Color.White
            )
        ) {
            Text(stringResource(id = R.string.save))
        }
    }
}

fun showDatePicker(activity: AppCompatActivity, block: (date: Long) -> Unit) {
    val picker = MaterialDatePicker.Builder.datePicker().build()
    activity.run {
        picker.show(supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener {
            block(it)
        }
    }
}