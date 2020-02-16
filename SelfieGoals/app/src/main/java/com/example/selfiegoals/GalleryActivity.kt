package com.example.selfiegoals

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.File


class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val verticalLayout = findViewById<LinearLayout>(R.id.linearLayout)

        val db = GoalDB(this)
        val goals = db.selectAllGoal()

        for (goal in goals) {
            Log.e("GalleryActivity", "gid ${goal.gid} gname ${goal.gname} gdate ${goal.gdate}")

            makeHorizontalLayout(goal.gname!!, verticalLayout)
        }
        db.close()

    }

    private fun makeImageView(imageBitmap: Bitmap, constraintLayout: LinearLayout) {
        val newImageView = ImageView(this)

        newImageView.setImageBitmap(imageBitmap)

        constraintLayout.addView(newImageView)
    }

    private fun loadImages(currentGoal: String): MutableList<Bitmap> {

        val path = Environment.getExternalStorageDirectory()
        val files: Array<File> = path.listFiles()
        val imageBitmaps = mutableListOf<Bitmap>()
        for (file in files) {
            if (file.getName().startsWith(currentGoal))
                imageBitmaps.add(BitmapFactory.decodeFile(file.getAbsolutePath()))
        }
        return imageBitmaps
    }

    private fun makeHorizontalLayout(currentGoal: String, verticalLayout: LinearLayout) {
        val newScrollView = HorizontalScrollView(this)
        val newHorizontalLayout = LinearLayout(this)
        val newTextView = TextView(this)

        newTextView.text = currentGoal

        newHorizontalLayout.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        newHorizontalLayout.orientation = LinearLayout.HORIZONTAL

        verticalLayout.addView(newTextView)
        verticalLayout.addView(newScrollView)
        newScrollView.addView(newHorizontalLayout)

        var imageBitmaps = loadImages(currentGoal)
        for (imageBitmap in imageBitmaps) {
            makeImageView(imageBitmap,newHorizontalLayout)
        }
    }
}

