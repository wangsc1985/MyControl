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
import kotlinx.android.synthetic.main.fragment_clock_control.tv_status
import kotlinx.android.synthetic.main.fragment_tv_control.*
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.net.SocketTimeoutException

/**
 * A simple [Fragment] subclass.
 * Use the [ControlFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Deprecated("",ReplaceWith("",""))
class ControlFragment : Fragment() {
    private lateinit var dataContext: DataContext
    private lateinit var ip: String
    private val port = 8123
    private val uiHandler = Handler()

    override fun onResume() {
        super.onResume()
        connectTV()
    }

    fun connectTV(){
        tv_status.text = "正在连接TV..."
        Thread {
            try {
                //region 播放按钮
                var socket = Socket(ip, port) //申请链接
                var dos = DataOutputStream(socket.getOutputStream())
                dos.writeInt(0)
                dos.flush()

                var dis = DataInputStream(socket.getInputStream())
                dis.readBoolean()

                dos.close()
                dis.close()
                socket.close()
                uiHandler.post {
                    tv_status.text = "TV已连接"
                }
                //endregion
            } catch (ex: SocketTimeoutException) {
                uiHandler.post {
                    tv_status.text = "无法连接TV"
                }
            } catch (e: Exception) {
                uiHandler.post {
                    tv_status.text = "无法连接TV : ${e.message}"
                }
            }
        }.start()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataContext = DataContext(context)
        ip = dataContext.getSetting(Setting.KEYS.tv_ip, "192.168.0.100").string

        tv_status.setOnClickListener {
            connectTV()
        }

        iv_left.setOnClickListener {
            Thread {
                try {
                    val s = Socket(ip, port) //申请链接
                    val os = s.getOutputStream()
                    val dos = DataOutputStream(os)
                    dos.writeInt(21)
                    dos.flush()
                    dos.close()
                    s.close()
                } catch (e: Exception) {
                }
            }.start()
        }

        iv_right.setOnClickListener {
            Thread {
                try {
                    val s = Socket(ip, port) //申请链接
                    val os = s.getOutputStream()
                    val dos = DataOutputStream(os)
                    dos.writeInt(22)
                    dos.flush()
                    dos.close()
                    s.close()
                } catch (e: Exception) {
                }
            }.start()
        }
        iv_up.setOnClickListener {
            Thread {
                try {
                    val s = Socket(ip, port) //申请链接
                    val os = s.getOutputStream()
                    val dos = DataOutputStream(os)
                    dos.writeInt(23)
                    dos.flush()
                    dos.close()
                    s.close()
                } catch (e: Exception) {
                }
            }.start()
        }
        iv_down.setOnClickListener {
            Thread {
                try {
                    val s = Socket(ip, port) //申请链接
                    val os = s.getOutputStream()
                    val dos = DataOutputStream(os)
                    dos.writeInt(24)
                    dos.flush()
                    dos.close()
                    s.close()
                } catch (e: Exception) {
                }
            }.start()
        }
        iv_ok.setOnClickListener {
            Thread {
                try {
                    val s = Socket(ip, port) //申请链接
                    val os = s.getOutputStream()
                    val dos = DataOutputStream(os)
                    dos.writeInt(11)
                    dos.flush()
                    dos.close()
                    s.close()
                } catch (e: Exception) {
                }
            }.start()
        }

        iv_back.setOnClickListener {
            Thread {
                try {
                    val s = Socket(ip, port) //申请链接
                    val os = s.getOutputStream()
                    val dos = DataOutputStream(os)
                    dos.writeInt(10)
                    dos.flush()
                    dos.close()
                    s.close()
                } catch (e: Exception) {
                }
            }.start()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_clock_control, container, false)
    }
}