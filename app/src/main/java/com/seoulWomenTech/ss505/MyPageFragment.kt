package com.seoulWomenTech.ss505

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.seoulWomenTech.ss505.databinding.FragmentMypageBinding
import com.seoulWomenTech.ss505.databinding.NavHeaderSidebarBinding

class MyPageFragment : Fragment() {

    lateinit var fragmentMyPageFragmentBinding: FragmentMypageBinding
    lateinit var mainActivity: MainActivity
    lateinit var navHeaderSidebarBinding: NavHeaderSidebarBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentMyPageFragmentBinding = FragmentMypageBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        val userInfo = UserInfoDAO.selectData(mainActivity,mainActivity.userPosition)
        fragmentMyPageFragmentBinding.run {

            toolbarMyPage.run{
                title = "Safety Seoul"

                setNavigationOnClickListener {
                    drawerLayoutMyPage.open()

                }

            }

            navigationViewMyPage.run {
                val headerView = getHeaderView(0)
                navHeaderSidebarBinding = NavHeaderSidebarBinding.bind(headerView)
                navHeaderSidebarBinding.run {
                    val imgSrc = mainActivity.resources.getIdentifier(
                        userInfo.image,
                        "drawable",
                        mainActivity.packageName
                    )
                    if (imgSrc != null) {
                        navHeaderUserImg.setImageResource(imgSrc)
                    }

                    navHeaderUserName.text = userInfo.name
                    navHeaderUserEmail.text = userInfo.email
                    navHeaderUserPoint.text = "POINT : ${userInfo.point}"

                }
                setNavigationItemSelectedListener {
                    when(it.itemId){
                        R.id.nav_main -> {
                            drawerLayoutMyPage.close()
                            mainActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        }

                        R.id.nav_safedata -> {
                            mainActivity.replaceFragment(MainActivity.SAFETYDATA_FRAGMENT,true,null)
                            drawerLayoutMyPage.close()
                        }

                        R.id.mypage -> {
                            mainActivity.replaceFragment(MainActivity.MYPAGE_FRAGMENT,true,null)
                            drawerLayoutMyPage.close()
                        }
                        R.id.nav_logout -> {
                            mainActivity.supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                            mainActivity.replaceFragment(MainActivity.LOGIN_FRAGMENT,false,null)

                        }

                        else -> {

                        }

                    }
                    false
                }
            }




            val imgSrc = mainActivity.resources.getIdentifier(
                userInfo.image,
                "drawable",
                mainActivity.packageName
            )
            if (imgSrc != null) {
                myPageUserImg.setImageResource(imgSrc)
            }

            myPageUserName.text= userInfo.name
            myPageUserEmail.text= userInfo.email

            btnMyPageUsePoint.setOnClickListener {
                mainActivity.replaceFragment(MainActivity.REWARDS_FRAGMENT,true,null)
            }



        }

        return fragmentMyPageFragmentBinding.root
    }

}