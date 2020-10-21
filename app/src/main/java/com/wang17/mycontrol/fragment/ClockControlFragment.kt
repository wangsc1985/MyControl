package com.wang17.mycontrol.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wang17.mycontrol.R
import com.wang17.mycontrol.e
import com.wang17.mycontrol.model.DataContext
import com.wang17.mycontrol.model.Setting
import kotlinx.android.synthetic.main.fragment_clock_control.*
import java.io.DataOutputStream
import java.net.Socket
import java.net.SocketTimeoutException

/**
 * A simple [Fragment] subclass.
 * Use the [ClockControlFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClockControlFragment : Fragment() {
    private lateinit var dataContext: DataContext
    private lateinit var ip:String
    private val uiHandler = Handler()

    override fun onResume() {
        super.onResume()

        Thread{
            try {
                //region 快进快退
                val socket = Socket(ip, 8000) //申请链接
                val dos = DataOutputStream(socket.getOutputStream())

                dos.writeInt(0)
                dos.flush()
                dos.close()
                socket.close()
                //endregion

                uiHandler.post {
                    tv_status.text="已连接"
                }

            }catch (ex: SocketTimeoutException){
                uiHandler.post {
                    tv_status.text="无法连接"
                }
            }
            catch (e: Exception) {
                e(e.message.toString())
            }
        }.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataContext = DataContext(context)
        ip = dataContext.getSetting(Setting.KEYS.clock_ip,"192.168.0.100").string
        //region 妙音
        iv_volume.setOnClickListener {
            Thread{
                try {
                    val s = Socket(ip, 8000) //申请链接
                    val os = s.getOutputStream()
                    val dos = DataOutputStream(os)
                    dos.writeInt(1)
                    dos.flush()
                    dos.close()
                    s.close()
                } catch (e: Exception) {
                }
            }.start()
        }

        //endregion
        //region 切换
        iv_repeat.setOnClickListener {
            Thread{
                try {
                    val s = Socket(ip, 8000) //申请链接
                    val os = s.getOutputStream()
                    val dos = DataOutputStream(os)
                    dos.writeInt(2)
                    dos.flush()
                    dos.close()
                    s.close()
                } catch (e: Exception) {
                }
            }.start()
        }
        //endregion

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_clock_control, container, false)
    }
}