package com.seoulWomenTech.ss505


import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.transition.MaterialSharedAxis
import com.seoulWomenTech.ss505.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var activityMainBinding: ActivityMainBinding

    val permissionList = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION,
        Manifest.permission.INTERNET
    )

    val userPosition = 2
    // 사용자가 누른 항목 번호
    var rowPosition = 0

    var newFragment:Fragment? = null
    var oldFragment:Fragment? = null



    companion object {
        // Activity가 관리할 프래그먼트들의 이름
        val MAIN_FRAGMENT = "MainFragment" //  메인 화면
        val PARTICIPATE_FRAGMENT = "ParticipateFragment" //  참여 화면
        val CPI_FRAGMENT = "CPIFragment" //  인증샷 제출 화면

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // challenge insert 문
//        val challenge1 = ChallengeClass(0,1,1,"챌린지1","챌린지내용입니다","2023-07-25","2023-08-04","12:00",5,1,100,"이미지주소")
//        ChallengeDAO.insertData(this,challenge1)
//        val challenge2 = ChallengeClass(0,1,1,"챌린지2","챌린지내용입니다2","2023-07-26","2023-08-05","14:00",5,1,100,"이미지주소2")
//        ChallengeDAO.insertData(this,challenge2)
//        val challenge3 = ChallengeClass(0,1,1,"챌린지3","챌린지내용입니다3","2023-07-27","2023-08-06","15:00",5,1,100,"이미지주소3")
//        ChallengeDAO.insertData(this,challenge3)

        val splashScreen = installSplashScreen()
         splashScreenCustomizing(splashScreen)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)



        requestPermissions(permissionList,0)
        replaceFragment(MAIN_FRAGMENT, false, null)



    }


    // SplashScreen 커스터마이징
    fun splashScreenCustomizing(splashScreen: SplashScreen){
        // SplashScreen이 사라질 때 동작하는 리스너를 설정한다.
        splashScreen.setOnExitAnimationListener{
            // 가로 비율 애니메이션
            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 2f, 1f, 0f)
            // 세로 비율 애니메이션
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 2f, 1f, 0f)
            // 투명도
            val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 1f, 0.5f, 0f)

            // 애니메이션 관리 객체를 생성한다.
            // 첫 번째 : 애니메이션을 적용할 뷰
            // 나머지는 적용한 애니메이션 종류
            val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(it.iconView, scaleX, scaleY, alpha)
            // 애니메이션 적용을 위한 에이징
            objectAnimator.interpolator = AnticipateInterpolator()
            // 애니메이션 동작 시간
            objectAnimator.duration = 1000
            // 애니메이션이 끝났을 때 동작할 리스너
            objectAnimator.addListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)

                    // SplashScreen을 제거한다.
                    it.remove()
                }
            })

            // 애니메이션 가동
            objectAnimator.start()
        }
    }

    // 지정한 Fragment를 보여주는 메서드
    fun replaceFragment(name:String, addToBackStack:Boolean, bundle: Bundle?){
        // Fragment 교체 상태로 설정한다.
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        // newFragment 에 Fragment가 들어있으면 oldFragment에 넣어준다.
        if(newFragment != null){
            oldFragment = newFragment
        }

        // 새로운 Fragment를 담을 변수
        newFragment = when(name){
            MAIN_FRAGMENT -> MainFragment()
            PARTICIPATE_FRAGMENT -> ParticipateFragment()
            CPI_FRAGMENT -> CPIFragment()
            else -> Fragment()
        }

        newFragment?.arguments = bundle

        if(newFragment != null) {

            // oldFragment -> newFragment로 이동
            // oldFramgent : exit
            // newFragment : enter

            // oldFragment <- newFragment 로 되돌아가기
            // oldFragment : reenter
            // newFragment : return

            // 애니메이션 설정
            if(oldFragment != null){
                oldFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                oldFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
                oldFragment?.enterTransition = null
                oldFragment?.returnTransition = null
            }

            newFragment?.exitTransition = null
            newFragment?.reenterTransition = null
            newFragment?.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
            newFragment?.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

            // Fragment를 교채한다.
            fragmentTransaction.replace(R.id.mainContainer, newFragment!!)

            if (addToBackStack == true) {
                // Fragment를 Backstack에 넣어 이전으로 돌아가는 기능이 동작할 수 있도록 한다.
                fragmentTransaction.addToBackStack(name)
            }

            // 교체 명령이 동작하도록 한다.
            fragmentTransaction.commit()
        }
    }

    // Fragment를 BackStack에서 제거한다.
    fun removeFragment(name:String){
        supportFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}
// 정보를 담을 객체
data class AddrClass(var idx: Int, var g_nm: String, var d_nm: String)
data class ChallengeClass(var idx: Int, var admin_id: Int,var addr_id: Int,var name: String, var content: String,var postDate: String,var progDate: String, var progTime: String, var maxUser: Int, var active: Int, var reword: Int,var img: String,var location : String)
data class ParticipantsClass(var clg_id:Int, var user_id:Int)
data class UserInfo(
    val idx: Int,
    val name: String,
    val email: String,
    val password: String,
    val role: Int,
    val gender: Int?,
    val point: Int?,
    val phone: String?,
    val sns: String?,
    val address: String?,
    val date: String?,
    val image: String?,
    val admin_office : String?,
    val admin_rank : String?
)

data class CPIClass(
    val idx: Int,
    val clg_id: Int,
    val user_id: Int,
    val url: String?,
)

data class SafetyData(
    val idx: Int,
    val adminId: Int,
    val title: String?,
    val content: String?,
    val date: String?
)

data class SafetyImage(
    val idx: Int,
    val sdId: Int?,
    val url: String?
)