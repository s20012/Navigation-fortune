package jp.ac.it_college.s20012.test.ui.home

import android.animation.ObjectAnimator
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
import jp.ac.it_college.s20012.test.R
import jp.ac.it_college.s20012.test.databinding.FragmentFortuneBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors

class FortuneFragment : Fragment() {
    private val args by navArgs<FortuneFragmentArgs>()

    private var _binding: FragmentFortuneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFortuneBinding.inflate(inflater, container, false)

        signImage()
        fortuneTelling()

        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val today = LocalDate.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        return dateFormatter.format(today)
    }

    private fun signImage() {
        when(args.signId) {
            0 -> binding.signImage.setImageResource(R.drawable.ohitujiza)
            1 -> binding.signImage.setImageResource(R.drawable.oushiza)
            2 -> binding.signImage.setImageResource(R.drawable.futagoza)
            3 -> binding.signImage.setImageResource(R.drawable.kaniza)
            4 -> binding.signImage.setImageResource(R.drawable.shishiza)
            5 -> binding.signImage.setImageResource(R.drawable.otomeza)
            6 -> binding.signImage.setImageResource(R.drawable.tenbinza)
            7 -> binding.signImage.setImageResource(R.drawable.sasoriza)
            8 -> binding.signImage.setImageResource(R.drawable.iteza)
            9 -> binding.signImage.setImageResource(R.drawable.yagiza)
            10 -> binding.signImage.setImageResource(R.drawable.mizugameza)
            11 -> binding.signImage.setImageResource(R.drawable.uoza)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun fortuneTelling(){
        val handler = HandlerCompat.createAsync(Looper.getMainLooper())
        val executeService = Executors.newSingleThreadExecutor()

        executeService.submit @WorkerThread {
            var result = ""
            val url = URL("http://api.jugemkey.jp/api/horoscope/free/" + getCurrentDate())
            val con = url.openConnection() as? HttpURLConnection
            con?.let {
                try {
                    it.connectTimeout = 1000
                    it.readTimeout = 1000
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
                val horoscope = rootJSON.getJSONObject("horoscope")
                val getDay = horoscope.getJSONArray(getCurrentDate())
                val index = args.signId
                val sign = getDay.getJSONObject(index)
                //星座の名前
                val signName = sign.getString("sign")
                //金運
                val money = sign.getString("money")
                //仕事運
                val job = sign.getString("job")
                //恋愛運
                val love = sign.getString("love")
                //総合評価
                val total = sign.getString("total")
                //アドバイス
                val advice = sign.getString("content")
                Log.d("TAG", advice)

                binding.signName.text = signName

                binding.moneyBar.stepSize = 0.01F
                val anim1 = ObjectAnimator.ofFloat(binding.moneyBar, "rating", binding.moneyBar.rating, money.toFloat())
                anim1.duration = 6000
                anim1.start()

                binding.jobBar.rating = job.toFloat()

                binding.loveBar.rating = love.toFloat()

                binding.totalBar.rating = total.toFloat()

                binding.adviceBox.text = advice

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