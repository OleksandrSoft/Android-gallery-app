/*
 * Copyright (c) 2021.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jesusd0897.gallerydroid.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.jesusd0897.gallerydroid.databinding.GalleryDroidFragmentPicturePagerBinding
import com.jesusd0897.gallerydroid.model.GalleryDroid.Companion.TRANSFORMER_BACK_TO_FOREGROUND
import com.jesusd0897.gallerydroid.model.GalleryDroid.Companion.TRANSFORMER_CUBE_OUT
import com.jesusd0897.gallerydroid.model.GalleryDroid.Companion.TRANSFORMER_DEPTH
import com.jesusd0897.gallerydroid.model.GalleryDroid.Companion.TRANSFORMER_FLIP_HORIZONTAL
import com.jesusd0897.gallerydroid.model.GalleryDroid.Companion.TRANSFORMER_FOREGROUND_TO_BACK
import com.jesusd0897.gallerydroid.model.GalleryDroid.Companion.TRANSFORMER_ROTATE_DOWN
import com.jesusd0897.gallerydroid.model.GalleryDroid.Companion.TRANSFORMER_ROTATE_UP
import com.jesusd0897.gallerydroid.model.GalleryDroid.Companion.TRANSFORMER_TABLET
import com.jesusd0897.gallerydroid.model.GalleryDroid.Companion.TRANSFORMER_ZOOM_IN
import com.jesusd0897.gallerydroid.model.GalleryDroid.Companion.TRANSFORMER_ZOOM_OUT
import com.jesusd0897.gallerydroid.model.Picture
import com.jesusd0897.gallerydroid.transformation.*
import com.jesusd0897.gallerydroid.util.EXTRA_USE_LABEL_TAG
import com.jesusd0897.kutil.EXTRA_ITEMS_TAG
import com.jesusd0897.kutil.EXTRA_ITEM_TAG
import com.jesusd0897.kutil.EXTRA_POSITION_TAG

class PicturePagerFragment : Fragment() {

    private var _binding: GalleryDroidFragmentPicturePagerBinding? = null
    private val binding get() = _binding

    companion object {
        fun newInstance(
            pictures: ArrayList<Picture>,
            position: Int = 0,
            transformerId: Int? = null,
        ): PicturePagerFragment {
            val fragment = PicturePagerFragment()
            val args = Bundle()
            args.putParcelableArrayList(EXTRA_ITEMS_TAG, pictures)
            args.putInt(EXTRA_POSITION_TAG, position)
            transformerId?.let { args.putInt(EXTRA_ITEM_TAG, it) }
            fragment.arguments = args
            return fragment
        }
    }

    private var pictures = ArrayList<Picture>()
    private var position = 0
    private var transformerId: Int? = null
    private var useLabels: Boolean? = null

    var onPageChangeListener: ViewPager.OnPageChangeListener? = null

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        pictures = args!!.getParcelableArrayList(EXTRA_ITEMS_TAG)!!
        position = args.getInt(EXTRA_POSITION_TAG)
        transformerId = args.getInt(EXTRA_ITEM_TAG)
        useLabels = args.getBoolean(EXTRA_USE_LABEL_TAG)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = GalleryDroidFragmentPicturePagerBinding.inflate(inflater, container, false)
        val sectionsPagerAdapter = SectionsPagerAdapter(parentFragmentManager)
        binding?.picturePageContainer?.setPageTransformer(true, provideTransformer())
        binding?.picturePageContainer?.adapter = sectionsPagerAdapter
        binding?.picturePageContainer?.currentItem = position
        binding?.picturePageContainer?.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                onPageChangeListener?.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                onPageChangeListener?.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                onPageChangeListener?.onPageScrollStateChanged(state)
            }
        })
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun provideTransformer() =
        when (transformerId) {
            TRANSFORMER_DEPTH -> DepthPageTransformer()
            TRANSFORMER_ZOOM_IN -> ZoomInPageTransformer()
            TRANSFORMER_ZOOM_OUT -> ZoomOutPageTransformer()
            //CUBE_IN -> CubeInPageTransformer()
            TRANSFORMER_CUBE_OUT -> CubeOutPageTransformer()
            TRANSFORMER_FLIP_HORIZONTAL -> FlipHorizontalPageTransformer()
            //FLIP_VERTICAL -> FlipVerticalPageTransformer()
            TRANSFORMER_FOREGROUND_TO_BACK -> ForegroundToBackgroundPageTransformer()
            TRANSFORMER_BACK_TO_FOREGROUND -> BackgroundToForegroundPageTransformer()
            //PARALLAX -> ParallaxPageTransformer()
            TRANSFORMER_ROTATE_DOWN -> RotateDownPageTransformer()
            TRANSFORMER_ROTATE_UP -> RotateUpPageTransformer()
            TRANSFORMER_TABLET -> TabletPageTransformer()
            else -> null
        }

    private inner class SectionsPagerAdapter(fm: FragmentManager) :

        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int) =
            PictureDetailFragment.newInstance(position, pictures[position])

        override fun getCount(): Int = pictures.size

        override fun getPageTitle(position: Int) = pictures[position].fileName
    }

}