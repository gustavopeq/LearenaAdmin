package gustavo.projects.learenaadmin.categories.newCategory

import android.widget.ImageView
import gustavo.projects.learenaadmin.R

interface INewCategoryForm {

    var starImg1: ImageView
    var starImg2: ImageView
    var starImg3: ImageView
    var starImg4: ImageView
    var starImg5: ImageView
    var listOfStarsImg: ArrayList<ImageView>
    var starLevel: Int

    fun createArrayOfStarsImg() : ArrayList<ImageView>{
        val arrayOfStarsImg = arrayListOf<ImageView>()
        arrayOfStarsImg.add(starImg1)
        arrayOfStarsImg.add(starImg2)
        arrayOfStarsImg.add(starImg3)
        arrayOfStarsImg.add(starImg4)
        arrayOfStarsImg.add(starImg5)

        return arrayOfStarsImg
    }

    fun setOnClickStarListeners() {
        for(starImg in listOfStarsImg) {
            starImg.setOnClickListener { changeAllTheStarsImg(it as ImageView) }
        }
    }

    // THIS FUNCTION WILL BE USED WHEN THE USER IS SELECTING THE STAR LEVEL
    private fun changeAllTheStarsImg(selectedStar: ImageView) {
        var maximumLevelSet = false
        starLevel = 0
        for (starImg in listOfStarsImg) {
            if (starImg != selectedStar) {
                if (!maximumLevelSet) {
                    selectStarImg(starImg)
                    starLevel += 1
                }else {
                    unselectStarImg(starImg)
                }
            }else if (starImg == selectedStar) {
                selectStarImg(starImg)
                starLevel += 1
                maximumLevelSet = true
            }
        }
    }

    // THIS FUNCTION WILL BE USED WHEN DISPLAYING A CATEGORY STAR LEVEL
    fun displayStarLevel(starLevel: Int) {
        for ((index, starImg) in listOfStarsImg.withIndex()) {
            if (index + 1 <= starLevel) {
                selectStarImg(starImg)
            }else {
                unselectStarImg(starImg)
            }
        }
    }

    private fun selectStarImg(starSelected: ImageView) {
        starSelected.setImageResource(R.drawable.ic_star_24dp)
    }

    private fun unselectStarImg(starUnselected: ImageView) {
        starUnselected.setImageResource(R.drawable.ic_star_border_24dp)
    }


}