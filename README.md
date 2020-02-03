# BottomSheet
Material Design 의 두 가지 BottomSheet 디자인을 안드로이드에서 구현한 예제입니다.



----
## Sample Images

<img src="/Images/modal.jpg" width="200">  <img src="/Images/persistent.jpg" width="200">


----
## Concept

### Modal Style BottomSheet
BottomSheet이 올라올 때 마치 Toast 처럼 어떤 화면에 속하지 않고 띄워지며 뒷 배경이 불투명하게(dim) 되면서 터치 시에 내려간다.

### Persistent Style BottomSheet
BottomSheet이 CoordinatorLayout의 자식뷰로 존재하며 레이아웃에 속하게 된다. BottomSheet이 올라와도 뒷 배경이 흐려지지 않고, 밀어서 내려야 한다.

----
## Code - Modal

우린 BottomSheetDialogFragment 를 상속하는 다이얼로그 프라그먼트를 하나 만들어주어야 한다.



```kotlin
class MyModalBottomSheet : BottomSheetDialogFragment() {

    /**
     * 현재 BottomSheet(Fragment)의 Theme를 얻어오는 메서드를 오버라이딩 해서 우리가 커스텀하게 정의한
     *
     * RoundBottomSheetDialog 라는 Theme.Design.Light.BottomSheetDialog 스타일을 상속한 스타일을 반환하게 해준다.
     */
    override fun getTheme(): Int = R.style.RoundBottomSheetDialog

    /**
     * 커스텀한 Dialog 객체를 반환해주기 위해 BottomSheetDialog 대화상자를 반환해준다.
     *
     * 여기서 중요한 것은 우리가 [getTheme] 메서드를 오버라이딩 해서 반환해주는 Theme 를 이용해서 [BottomSheetDialog]를 생성해주어서
     *
     * 우리가 원하는 Style 대로 BottomSheet 를 동작시킬 수 있다는 것이다.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(activity!!, theme)

    /**
     * 여타 [Fragment] 와 같이 onCreateView 로 뷰를 만들어준다.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_modal,container,false)
    }

    /**
     * Modal Bottom Sheet 이 Dismiss 될 때 호출되는 콜백
     */
    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
    }
}
```

onCreateView 를 제외한 다른 override 메서드들은 신경쓰지 않아도 무관하다.

getTheme와 onCreateDialog는 Modal style의 BottomSheet을 커스텀 Style을 이용해서 커스터마이징해주기 위해 정의해준 것이고,

onDismiss는 ModalBottomSheet이 사라질 때 콜백을 받기 위해 오버라이딩 했다.

다음은 values/styles.xml 에 정의해 둔 Modal Bottom Sheet 의 동작을 위한 커스텀 스타일이다.

다시 말하지만, 다음과 같이 커스텀 스타일을 지정해서 사용하는 것은 일반적인 Modal Bottom Sheet을 사용할 땐 필요 없지만, 좀 더 구체적인 동작을 정의해주거나 배경 모서리에 둥글게 corner radius를 주고 싶다든지 할 때 유용하다. skip해도 무관하다. 



```xml
<style name="RoundBottomSheet" parent="Widget.Design.BottomSheet.Modal">
    <item name="android:background">@drawable/round_bottom_sheet_background</item>
</style>
<style name="RoundBottomSheetDialog" parent="Theme.Design.Light.BottomSheetDialog">
    <item name="bottomSheetStyle">@style/RoundBottomSheet</item>
    <item name="android:windowCloseOnTouchOutside">true</item>
    <item name="android:windowContentOverlay">@null</item>
    <item name="android:colorBackground">#fff</item>
    <item name="colorAccent">@color/colorAccent</item>
    <item name="android:backgroundDimEnabled">true</item>
    <item name="android:backgroundDimAmount">0.8</item>
    <item name="android:windowFrame">@null</item>
    <item name="android:windowIsFloating">true</item>
</style>
```


Modal BottomSheet 은 다음과 같이 띄워줄 수 있다.
```kotlin
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
 ```
 
 
 ----
## Code - Persistent
 
Persistent 스타일의 BottomSheet 은 Modal 과 다르게 CoordinatorLayout 의 직속 자식뷰로 속해있어야 한다.
 
그리고 다음과 같은 속성들을 정의할 수 있다.
 
```xml
app:layout_behavior="@string/bottom_sheet_behavior"
app:behavior_hideable="true"
app:behavior_peekHeight="0dp"
```

layout_behavior 는 CoordinatorLayout 의 자식 뷰들이 가질 수 있는 속성인데, 어떤 Behavior 를 쓸 것인지 지정한다.

여기서 값으로 준 bottom_sheet_behavior 는 구글에서 만들어 둔 BottomSheet 을 위한 Behavior이다.

hideable은 아래로 당겨서 감출 수 있는 지를 결정한다.

peekHeight는 중간만큼 BottomSheet이 펼쳐졌을 때 높이를 의미한다. 이 값을 변경해가며 관찰해보자.


Persistent 스타일의 BottomSheet 을 사용하기 위해서는 BottomSheetBehavior<T> 라는 객체를 얻어야 하는데, 다음과 같이 
 
MainActivity.kt에 정의해놓고,

```kotlin
private lateinit var persistentBottomSheetBehavior : BottomSheetBehavior<*>
```

```kotlin
persistentBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_persistent)
```

이렇게 얻을 수 있다.

이를 이용해서 state 를 변경하면, BottomSheet의 상태를 변경할 수 있고, 콜백도 등록이 가능하다.

```kotlin
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
```

```kotlin
/**
 * Persistent 스타일의 BottomSheet을 띄워주기 위한 버튼 콜백
 */
button_persistent_bottom_sheet.setOnClickListener {
    /**
     * Persistent Bottom Sheet 을 사용해주기 위해서는 Behavior 를 사용하기 때문에 CoordinatorLayout이 부모 레이아웃으로 있어야 한다.
     *
     * bottom_sheet_persistent 레이아웃을 보면 다음과 같은 속성들이 있다.
     *
     *         app:layout_behavior="@string/bottom_sheet_behavior" - BottomSheet 의 Behavior 로써 CoordinatorLayout 이 이속성이 있는 뷰를
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
```
