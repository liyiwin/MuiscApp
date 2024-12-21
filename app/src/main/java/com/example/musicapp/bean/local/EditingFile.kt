package com.example.musicapp.bean.local

import java.io.File

data class EditingFile(
    var isSelected:Boolean,
    val file: File
)