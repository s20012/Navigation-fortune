package jp.ac.it_college.s20012.test.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import jp.ac.it_college.s20012.test.R
import jp.ac.it_college.s20012.test.databinding.FragmentDashboardBinding
import jp.ac.it_college.s20012.test.ui.home.HomeFragmentDirections
import java.net.URL

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var url = URL("https://livlog.xyz/hoshimiru/constellation?lat=0&lng=0&date=2021-12-16&hour=20&min=00&id=5")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val signList = listOf(
            mapOf("pics" to R.drawable.hituzi_img, "name" to getString(R.string.signName1), "roughly" to "秋の星座。21時に正中する時期は12月上旬", "id" to 0),
            mapOf("pics" to R.drawable.ousi_img, "name" to getString(R.string.signName2), "roughly" to "冬の代表的な星座。21時に正中する時期は1月中旬", "id" to 1),
            mapOf("pics" to R.drawable.hutagoza_img, "name" to getString(R.string.signName3), "roughly" to "冬の星座。21時に正中する時期は2月下旬", "id" to 2),
            mapOf("pics" to R.drawable.kani_img, "name" to getString(R.string.signName4), "roughly" to "春の星座。21時に正中する時期は3月中旬", "id" to 3),
            mapOf("pics" to R.drawable.sisi_img, "name" to getString(R.string.signName5), "roughly" to "春の星座。20時に正中する時期は4月下旬", "id" to 4),
            mapOf("pics" to R.drawable.otme_img, "name" to getString(R.string.signName6), "roughly" to "夏の星座。20時に正中する時期は6月上旬", "id" to 5),
            mapOf("pics" to R.drawable.tenbin_img, "name" to getString(R.string.signName7), "roughly" to "夏の星座。20時に正中する時期は7月上旬", "id" to 6),
            mapOf("pics" to R.drawable.sasori_img, "name" to getString(R.string.signName8), "roughly" to "夏の星座。20時に正中する時期は中旬", "id" to 7),
            mapOf("pics" to R.drawable.ite_img, "name" to getString(R.string.signName9), "roughly" to "夏を代表する星雲の1つ。8月下旬に見られる", "id" to 8),
            mapOf("pics" to R.drawable.yagi_img, "name" to getString(R.string.signName10), "roughly" to "秋頃に見られる星座。9月下旬頃", "id" to 9),
            mapOf("pics" to R.drawable.mizugame_img, "name" to getString(R.string.signName11), "roughly" to "秋の星座。20時に正中する時期は10月中旬", "id" to 10),
            mapOf("pics" to R.drawable.uo_img, "name" to getString(R.string.signName12), "roughly" to "少し見つけにくい星座。時期は秋の11月中旬", "id" to 11)
        )

        binding.gvconstellation.adapter = SimpleAdapter(
            activity, signList, R.layout.card_layout,
            arrayOf("pics", "name", "roughly"), intArrayOf(R.id.image, R.id.name, R.id.description)
        )


        binding.gvconstellation.setOnItemClickListener { parent, _, position, _ ->

            val item = parent.getItemAtPosition(position) as Map<*, *>
            order(item as MutableMap<String, Any>)
        }
        registerForContextMenu(binding.gvconstellation)

        return binding.root
    }
    private fun order(sign: MutableMap<String, Any>) {
        val signName = sign["name"] as String
        val signId = sign["id"] as Int
        val action = DashboardFragmentDirections.actionToBook(signName, signId)
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}