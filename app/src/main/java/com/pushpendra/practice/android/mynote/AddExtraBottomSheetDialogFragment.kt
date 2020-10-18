package com.pushpendra.practice.android.mynote

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val REQUEST_IMAGE_CAPTURE = 101

open class AddExtraBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var listener : BottomSheetImageClickableListener
    private lateinit var clickedImage : Bitmap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View =
            inflater.inflate(R.layout.fragment_add_extra_bottom_sheet_dialog, container, false)

        val photoClickButton: Button = v.findViewById(R.id.photo_click_button)

        photoClickButton.setOnClickListener {

            takePicture()

            listener.onButtonClicked(clickedImage)
            dismiss()
        }

        val addPicture = v.findViewById<Button>(R.id.add_picture_button)

        addPicture.setOnClickListener {
            // add the picture from the disk!
        }
        return v
    }

    private fun takePicture() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val extras: Bundle? = data?.extras
        clickedImage = extras?.get("data") as Bitmap
    }

    interface BottomSheetImageClickableListener {
        fun onButtonClicked(image : Bitmap)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as BottomSheetImageClickableListener
    }
}