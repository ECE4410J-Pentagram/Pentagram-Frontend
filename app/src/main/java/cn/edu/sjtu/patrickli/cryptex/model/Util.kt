package cn.edu.sjtu.patrickli.cryptex.model

import android.content.ClipData
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

object Util {

    /**
     * Share a piece of text or an image externally
     * @param type Media type
     * @param text Text content. Must be set if type is text
     * @param file Image file. Must be set if type is image
     */
    fun shareExternally(context: Context, type: MediaType, text: String? = null, file: File? = null) {
        val intent = Intent(Intent.ACTION_SEND)
        val shareWith = "ShareWith"
        when (type) {
            MediaType.TEXT -> {
                if (text != null) {
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, text)
                }
            }
            MediaType.IMAGE -> {
                if (file != null) {
                    val uri = FileProvider.getUriForFile(
                        context, context.packageName + ".provider", file
                    )
                    intent.type = "image/jpeg"
                    intent.clipData = ClipData.newRawUri("", uri)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                }
            }
        }
        ContextCompat.startActivity(
            context,
            Intent.createChooser(intent, shareWith),
            null
        )
    }

}