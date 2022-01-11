package jp.ac.it_college.s20012.test.ui.dashboard

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.core.os.HandlerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import jp.ac.it_college.s20012.test.databinding.FragmentSignBookBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.util.concurrent.Executors

class SignBook : Fragment() {

    private val args by navArgs<SignBookArgs>()
    private var _binding: FragmentSignBookBinding? = null
    private val binding get() = _binding!!
    private var url = URL("https://livlog.xyz/hoshimiru/constellation?lat=0&lng=0&date=2021-12-16&hour=20&min=00&id=5")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignBookBinding.inflate(inflater, container, false)


        signUrl()
        fortuneTelling()

        return binding.root
    }

    private fun signUrl() {
        when(args.signId) {
            0 -> url = URL("https://livlog.xyz/hoshimiru/constellation?lat=0&lng=0&date=2021-6-16&hour=20&min=00&id=7") //牡羊座
            1 -> url = URL("https://livlog.xyz/hoshimiru/constellation?lat=0&lng=0&date=2021-6-16&hour=20&min=00&id=78") //牡牛座
            2 -> url = URL("https://livlog.xyz/hoshimiru/constellation?lat=0&lng=0&date=2021-6-16&hour=20&min=00&id=38") //双子座
            3 -> url = URL("https://livlog.xyz/hoshimiru/constellation?lat=0&lng=0&date=2021-6-16&hour=20&min=00&id=22") //蟹座
            4 -> url = URL("https://livlog.xyz/hoshimiru/constellation?lat=0&lng=0&date=2021-12-16&hour=24&min=00&id=46") //獅子座
            5 -> url = URL("https://livlog.xyz/hoshimiru/constellation?lat=0&lng=0&date=2021-12-16&hour=20&min=00&id=86") //乙女座
            6 -> url = URL("https://livlog.xyz/hoshimiru/constellation?lat=0&lng=0&date=2021-12-16&hour=20&min=00&id=48") //天秤座
            7 -> url = URL("https://livlog.xyz/hoshimiru/constellation?lat=0&lng=0&date=2021-12-16&hour=20&min=00&id=72") //蠍座
            8 -> url = URL("https://livlog.xyz/hoshimiru/constellation?lat=0&lng=0&date=2021-12-16&hour=20&min=00&id=77") //射手座
            9 -> url = URL("https://livlog.xyz/hoshimiru/constellation?lat=0&lng=0&date=2021-12-16&hour=20&min=00&id=12") //山羊座
            10 -> url = URL("https://livlog.xyz/hoshimiru/constellation?lat=0&lng=0&date=2021-12-16&hour=20&min=00&id=5") //水瓶座
            11 -> url = URL("https://livlog.xyz/hoshimiru/constellation?lat=0&lng=0&date=2021-6-16&hour=20&min=00&id=67") //魚座
        }
    }

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun fortuneTelling(){
        val handler = HandlerCompat.createAsync(Looper.getMainLooper())
        val executeService = Executors.newSingleThreadExecutor()

        executeService.submit @WorkerThread {
            var result = ""
            val url = url
            val con = url.openConnection() as? HttpURLConnection
            con?.let {
                try {
                    it.connectTimeout = 10000
                    it.readTimeout = 10000
                    it.requestMethod = "GET"
                    it.connect()
                    val stream = it.inputStream
                    result = is2String(stream)

                    stream.close()
                } catch (e: SocketTimeoutException) {
                    Log.d("TAG", "通信タイムアウト", e)
                }
                it.disconnect()
            }



            handler.post @UiThread {
                val rootJSON = JSONObject(result)
                val result = rootJSON.getJSONArray("result")
                val index = result.getJSONObject(0)

                val jpName = index.getString("jpName")
                val content = index.getString("content")
                val starIcon = index.getString("starIcon")
                val starImage = index.getString("starImage")
                val origin = index.getString("origin")
                val roughly = index.getString("roughly")

                Log.d("TAG", roughly)



                Glide.with(this)
                    .load(starIcon)
                    .into(binding.imageView)

                binding.textView5.text = content
                binding.imageView.drawable
                binding.textView.text = jpName
                binding.textView2.text = origin

            }

        }
    }
    private fun is2String(stream: InputStream): String {
        val sb = StringBuilder()
        val reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
        var line = reader.readLine()
        while (line != null) {
            sb.append(line)
            line = reader.readLine()
        }
        reader.close()
        return sb.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}