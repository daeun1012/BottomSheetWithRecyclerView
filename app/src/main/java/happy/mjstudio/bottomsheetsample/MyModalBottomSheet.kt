package happy.mjstudio.bottomsheetsample

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_modal.*
import kotlin.random.Random


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemCount = Random.nextInt(1, 15)
        rv_content.adapter = object : RecyclerView.Adapter<MyViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
                return MyViewHolder(view)
            }

            override fun getItemCount(): Int {
                return itemCount
            }

            override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

            }

        }

        val maxHeight = getPopupHeight(.85f)
        container.maxHeight = maxHeight + 1

        dialog.setOnShowListener { dialog ->

            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet)

            initLayoutHeight()

            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {}

                override fun onStateChanged(p0: View, p1: Int) {
                    Log.d("TEST", "state : $p1")
                    when(p1) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            dismiss()
                        }
                        else -> {

                        }
                    }
                }

            })
        }
    }

    private fun getPopupHeight(percent: Float): Int {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return (displayMetrics.heightPixels * percent).toInt()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

//    /**
//     * Modal Bottom Sheet 이 Dismiss 될 때 호출되는 콜백
//     */
//    override fun onDismiss(dialog: DialogInterface?) {
//        super.onDismiss(dialog)
//    }

    private fun initLayoutHeight() {
        val d = dialog as BottomSheetDialog
        val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet)

        behavior.skipCollapsed = true
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        val params: CoordinatorLayout.LayoutParams = bottomSheet?.layoutParams as CoordinatorLayout.LayoutParams
        val maxHeight = getPopupHeight(.85f)

//        Log.d("TEST", "maxHeight : $maxHeight")
//        Log.d("TEST", "bottomSheet.measuredHeight : ${bottomSheet.measuredHeight}")
//        Log.d("TEST", "bottomSheet.layoutParams.height : ${params.height}")
//        Log.d("TEST", "bottomSheet.height : ${bottomSheet.height}")
//        Log.d("TEST", "rv_content.measuredHeight : ${rv_content.measuredHeight}")
//        Log.d("TEST", "rv_content.height : ${rv_content.height}")

        if(maxHeight < bottomSheet.height) {
            params.height = maxHeight
            bottomSheet.layoutParams = params
            behavior.peekHeight = maxHeight
        }
    }

}