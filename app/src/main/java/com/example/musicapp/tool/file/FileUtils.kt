package com.example.musicapp.tool.file

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object FileUtils  {


    fun getFileName (uri: Uri,context: Context):String{

        val resolver: ContentResolver = context.contentResolver
        val mProjection = arrayOf( MediaStore.Files.FileColumns.DISPLAY_NAME)
        val cursor: Cursor? = resolver.query(uri, mProjection,null, null, null)

        var fileName = "null"
        if(cursor !=null){
          if (cursor.moveToFirst()) {
                val nameColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                fileName  = cursor.getString(nameColumn)
            }
            cursor.close();

        }

        return fileName

    }

    fun getmimeType (uri: Uri,context: Context):String{

        val contentResolver = context.contentResolver

        val mimeTypeMap = MimeTypeMap.getSingleton()

        var dataList = "null"

        val mimeType = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))

        if(mimeType != null) dataList = mimeType

        return dataList

    }


    fun getByte( uri: Uri,context: Context):ByteArray?{
        val contentResolver: ContentResolver = context.contentResolver ?: return null
        try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val byteBuff = ByteArrayOutputStream()
            val buffSize = inputStream.available()
            val buff = ByteArray(buffSize)
            var len: Int
            while (inputStream.read(buff).also { len = it } > 0) byteBuff.write(buff, 0, len)
            inputStream.close()
            return byteBuff.toByteArray()
        } catch (exception: IOException) {

        }
        return null
    }

    fun uriToFile(uri: Uri, contentResolver: ContentResolver, outputDir: File,name:String): File? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val file = File(outputDir, name)
            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}