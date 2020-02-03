package happy.mjstudio.bottomsheetsample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_persistent.*

class MainActivity : AppCompatActivity() {

    private lateinit var persistentBottomSheetBehavior : BottomSheetBehavior<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initPersistentBottomSheetBehavior()
        initButtons()
    }

    private fun initPersistentBottomSheetBehavior() {
        persistentBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_persistent)

        /**
         * 콜백을 달아줘서 PersistentBottomSheet의 상태 변경을 감지가 가능하다.
         *
         * Modal Style은 이런 방식보다 [BottomSheetFragmentDialog] 에서 [onDismiss] 같은 메서드를 오버라이딩 해야한다.
         */
        persistentBottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(p0: View, state: Int) {
                when(state) {
                    BottomSheetBehavior.STATE_EXPANDED-> {

                    }

                }
            }
        })
    }

    private fun initButtons() {
        /**
         * Modal 스타일의 BottomSheet을 띄워주기 위한 버튼 콜백
         */
        button_modal_bottom_sheet.setOnClickListener {
            /**
             * 그저 우리가 정의한 [BottomSheetDialogFragment] 를 상속한 클래스의 객체를 생성해서 show 메서드를 호출해주면 된다.
             *
             * 인자로 [FragmentManager] 와 tag 를 받는데, 여긴 액티비티이므로 supportFragmentManager 와 생성한 객체의 tag를 전달해준다.
             */
            val bottomSheet = MyModalBottomSheet()
            bottomSheet.show(supportFragmentManager,bottomSheet.tag)
        }

        /**
         * Persistent 스타일의 BottomSheet을 띄워주기 위한 버튼 콜백
         */
        button_persistent_bottom_sheet.setOnClickListener {
            /**
             * Persistent Bottom Sheet 을 사용해주기 위해서는 Behavior 를 사용하기 때문에 CoordinatorLayout이 부모 레이아웃으로 있어야 한다.
             *
             * bottom_sheet_persistent 레이아웃을 보면 다음과 같은 속성들이 있다.
             *
             *         app:layout_behavior="@string/bottom_sheet_behavior" - BottomSheet 의 Behavior 로써 CoordinatorLayout 이 이 속성이 있는 뷰를
             *                                                                  Persistent BottomSheet 으로 인식하게 된다.
             *
             *         app:behavior_hideable="true" - 아래로 드래그해서 완전히 숨길 수 있게 한다.
             *
             *         app:behavior_peekHeight="0dp" - 중간만큼 확장되었을 때의 높이이다. 이 값을 변경하면서 관찰해보자.

                        android:elevation="24dp" - 그림자를 주기위해 설정해준 elevation 속성이다.
             */


            //버튼을 클릭했을 때 State 를 [BottomSheetBehavior.STATE_EXPANDED] 로 변경해서 확장시켜준다.
            persistentBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}
