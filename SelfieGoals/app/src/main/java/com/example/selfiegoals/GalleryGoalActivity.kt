package com.example.selfiegoals

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.AlarmClock
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import java.io.File

class GalleryGoalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_goal)

        val imageLayout = findViewById<LinearLayout>(R.id.imageLayout)

        val imageFiles = loadImageFiles()
        var imageID = 0

        for (imageFile in imageFiles){
            imageBitmap = (BitmapFactory.decodeFile(imageFile.getAbsolutePath()))
            makeImageView(imageBitmap, imageLayout, imageID)
            imageID += 1
        }
    }

    private fun makeImageView(imageBitmap: Bitmap, constraintLayout: LinearLayout, imageID: Int) {
        // creates a new image view
        val newImageView = ImageView(this)

        // sets the image of the image view
        newImageView.setImageBitmap(imageBitmap)

        //gives the imageview an ID
        newImageView.id = imageID

        // adds it to the constraint layout so visible
        constraintLayout.addView(newImageView)

        // creating a context menu for the imageView (context menu is when you hold click and more options appear)
        registerForContextMenu(newImageView)
    }

    private fun loadImageFiles(): MutableList<File> {
        val currentGoal = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE)
        // get path to the storage directory
        val path = Environment.getExternalStorageDirectory()
        // create an array of all the files in the directory
        val files: Array<File> = path.listFiles()
        // create a mutableList (Mutable just means you can add to it) of Files
        val imageFiles = mutableListOf<File>()
        // search through all the files and get the ones that are associated with current goal
        for (file in files) {
            if (file.getName().startsWith(currentGoal))
                // add the file to the list
                imageFiles.add(file)
        }
        // return the list of Files
        return imageFiles
    }


    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val imageView = v as ImageView
        val imageID = imageView.id

        // give context menu buttons with group id matching gid
        menu!!.add(imageID, v!!.id, 0, "Delete Image")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        // loop through context menu items to see which was clicked
        when (item.title) {
            "Delete Image" -> {
                deleteImageFile(item.groupId)
                return true
            }
        }
        return true
    }
    private fun deleteImageFile(FileID: Int) {
        val imageFiles = loadImageFiles()
        val fileToDelete = imageFiles[FileID]

        fileToDelete.delete()
        val intentGoalGalleryActivity = Intent(this, GalleryGoalActivity::class.java)

        intent = intentGoalGalleryActivity.apply { putExtra(AlarmClock.EXTRA_MESSAGE, intent.getStringExtra(AlarmClock.EXTRA_MESSAGE))}
        startActivity(intent)

    }
}
