package com.makentoshe.habrachan.application.android.common.fragment

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    abstract val arguments: FragmentArguments
}