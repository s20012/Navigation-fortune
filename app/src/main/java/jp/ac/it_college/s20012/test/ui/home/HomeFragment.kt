package jp.ac.it_college.s20012.test.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import jp.ac.it_college.s20012.test.R
import jp.ac.it_college.s20012.test.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val signList = listOf(
            mapOf("pics" to R.drawable.ohitujiza, "name" to getString(R.string.signName1), "id" to 0),
            mapOf("pics" to R.drawable.oushiza, "name" to getString(R.string.signName2), "id" to 1),
            mapOf("pics" to R.drawable.futagoza, "name" to getString(R.string.signName3), "id" to 2),
            mapOf("pics" to R.drawable.kaniza, "name" to getString(R.string.signName4), "id" to 3),
            mapOf("pics" to R.drawable.shishiza, "name" to getString(R.string.signName5), "id" to 4),
            mapOf("pics" to R.drawable.otomeza, "name" to getString(R.string.signName6), "id" to 5),
            mapOf("pics" to R.drawable.tenbinza, "name" to getString(R.string.signName7), "id" to 6),
            mapOf("pics" to R.drawable.sasoriza, "name" to getString(R.string.signName8), "id" to 7),
            mapOf("pics" to R.drawable.iteza, "name" to getString(R.string.signName9), "id" to 8),
            mapOf("pics" to R.drawable.yagiza, "name" to getString(R.string.signName10), "id" to 9),
            mapOf("pics" to R.drawable.mizugameza, "name" to getString(R.string.signName11), "id" to 10),
            mapOf("pics" to R.drawable.uoza, "name" to getString(R.string.signName12), "id" to 11)
        )


        binding.gvconstellation.adapter = SimpleAdapter(
            activity, signList, R.layout.item_sign_gv,
            arrayOf("pics", "name"), intArrayOf(R.id.item_star_iv, R.id.item_star_tv)
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
        val action = HomeFragmentDirections.actionToFortune(signName, signId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}