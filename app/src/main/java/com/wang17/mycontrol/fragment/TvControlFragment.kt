package com.wang17.mycontrol.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.wang17.mycontrol.R
import com.wang17.mycontrol.callback.CloudCallback
import com.wang17.mycontrol.e
import com.wang17.mycontrol.model.DataContext
import com.wang17.mycontrol.model.Setting
import com.wangsc.mytv.util._CloudUtils
import kotlinx.android.synthetic.main.fragment_tv_control.*
import kotlinx.android.synthetic.main.fragment_tv_control.iv_repeat
import kotlinx.android.synthetic.main.fragment_tv_control.iv_volume
import kotlinx.android.synthetic.main.fragment_tv_control.tv_status
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.*
import java.util.concurrent.CountDownLatch

/**
 * A simple [Fragment] subclass.
 */
class TvControlFragment : Fragment() {
    private lateinit var dataContext: DataContext
    private lateinit var tvIp: String
    private lateinit var clockIp: String
    private val port0 = 8000
    private val port1 = 8123
    private val timeout = 3000
    private val uiHandler = Handler()
    private var isTvRunning=true

    override fun onResume() {
        e("onResume")
        super.onResume()

        connectClock()
        connectTv()
    }

    fun connectTv() {
        tv_status.text = "正在连接TV..."
        Thread {
            try {
                //region 快进快退
                var socket = Socket() //申请链接
                socket.connect(InetSocketAddress(tvIp, port1), timeout)
                e("连接tv完毕")
                var dos = DataOutputStream(socket.getOutputStream())
                dos.writeInt(4)
                dos.flush()

                var dis = DataInputStream(socket.getInputStream())
                isTvRunning = dis.readBoolean()
                uiHandler.post {
                    if (isTvRunning) {
                        iv_forward.visibility = View.VISIBLE
                        iv_rewind.visibility = View.VISIBLE
                    } else {
                        iv_forward.visibility = View.INVISIBLE
                        iv_rewind.visibility = View.INVISIBLE
                    }
                }

                dos.close()
                dis.close()
                socket.close()
                //endregion

                uiHandler.post {
                    tv_status.text = "TV已连接"
                }

            } catch (e: SocketTimeoutException) {
                uiHandler.post {
                    tv_status.text = "连接TV超时"
                }
            } catch (e: ConnectException) {
                uiHandler.post {
                    tv_status.text = "无法连接TV"
                }
            } catch (e: Exception) {
                e(e.message)
            }

            try {
                //region 播放按钮
                e("tv ip : $tvIp , prot : $port1")
                var socket = Socket() //申请链接
                socket.connect(InetSocketAddress(tvIp, port1), timeout)
                var dos = DataOutputStream(socket.getOutputStream())
                dos.writeInt(0)
                dos.flush()

                var dis = DataInputStream(socket.getInputStream())
                val status = dis.readBoolean()
                e("play status : $status")
                e("---------------------------------------- 播放状态？$status")

                uiHandler.post {
                    if (status) {
                        iv_play.setImageResource(R.drawable.pause)
                    } else {
                        iv_play.setImageResource(R.drawable.play)
                    }
                }
                dos.close()
                dis.close()
                socket.close()
                //endregion

                uiHandler.post {
                    tv_status.text = "TV已连接"
                }

            } catch (e: SocketTimeoutException) {
                uiHandler.post {
                    tv_status.text = "连接TV超时"
                }
            } catch (e: ConnectException) {
                uiHandler.post {
                    tv_status.text = "无法连接TV"
                }
            } catch (e: Exception) {
                e(e.message)
            }
        }.start()
    }

    fun connectClock() {
        clock_status.text = "正在连接CLOCK..."
        Thread {
            try {
                var socket = Socket() //申请链接
                socket.connect(InetSocketAddress(clockIp, port0), timeout)
                var dos = DataOutputStream(socket.getOutputStream())
                dos.writeInt(0)
                dos.flush()
                dos.close()
                socket.close()
                uiHandler.post {
                    clock_status.text = "CLOCK已连接"
                }
            } catch (e: SocketTimeoutException) {
                uiHandler.post {
                    clock_status.text = "连接CLOCK超时"
                }
            } catch (e: ConnectException) {
                uiHandler.post {
                    clock_status.text = "无法连接CLOCK"
                }
            } catch (e: Exception) {
                e(e.message)
            }
        }.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        e("onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        dataContext = DataContext(context)

        val latch = CountDownLatch(1)  //-----------------
        _CloudUtils.getSetting(context!!,"0088","tv_ip",object:CloudCallback{
            override fun excute(code: Int, result: Any?) {
                when (code) {
                    0 -> {
                        tvIp = result.toString()
                    }
                }
                latch.countDown() //-----------------
            }
        })
        latch.await()
        clockIp = dataContext.getSetting(Setting.KEYS.clock_ip, "192.168.0.100").string

        tv_status.setOnClickListener {
            connectTv()
        }
//        tv_status.setOnLongClickListener {
//            var editText = EditText(context)
//            editText.setText(tvIp)
//            AlertDialog.Builder(context).setView(editText).setPositiveButton("确定", object : DialogInterface.OnClickListener {
//                override fun onClick(dialog: DialogInterface?, which: Int) {
//                    dataContext.editSetting(Setting.KEYS.tv_ip, editText.text.toString())
//                    tvIp = editText.text.toString()
//                    connectTv()
//                }
//            }).show()
//            true
//        }

        clock_status.setOnClickListener {
            connectClock()
        }
        clock_status.setOnLongClickListener {

            var editText = EditText(context)
            editText.setText(clockIp)
            AlertDialog.Builder(context).setView(editText).setPositiveButton("确定", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dataContext.editSetting(Setting.KEYS.clock_ip, editText.text.toString())
                    clockIp = editText.text.toString()
                    connectClock()
                }
            }).show()
            true
        }

        iv_volume.setOnClickListener {
            Thread {
                try {
                    val s = Socket(clockIp, port0) //申请链接
                    val os = s.getOutputStream()
                    val dos = DataOutputStream(os)
                    dos.writeInt(1)
                    dos.flush()
                    dos.close()
                    s.close()
                } catch (e: Exception) {
                    clock_status.text = e.message
                }
            }.start()
        }

        iv_repeat.setOnClickListener {
            Thread {
                try {
                    val s = Socket(clockIp, port0) //申请链接
                    val os = s.getOutputStream()
                    val dos = DataOutputStream(os)
                    dos.writeInt(2)
                    dos.flush()
                    dos.close()
                    s.close()
                } catch (e: Exception) {
                    clock_status.text = e.message
                }
            }.start()
        }

        iv_play.setOnClickListener {
            Thread {
                try {
                    val socket = Socket(tvIp, port1) //申请链接
                    val dos = DataOutputStream(socket.getOutputStream())
                    dos.writeInt(1)
                    dos.flush()

                    val dis = DataInputStream(socket.getInputStream())
                    val status = dis.readBoolean()
                    e("---------------------------------------- 播放状态？$status")
                    uiHandler.post {
                        if (status) {
                            iv_play.setImageResource(R.drawable.pause)
                        } else {
                            iv_play.setImageResource(R.drawable.play)
                        }
                    }

                    dos.close()
                    dis.close()
                    socket.close()
                } catch (e: SocketTimeoutException) {
                    uiHandler.post {
                        tv_status.text = e.message
                    }
                } catch (e: Exception) {
                    uiHandler.post {
                        tv_status.text = e.message
                    }
                }
            }.start()
        }

        iv_volume_up.setOnClickListener {
            Thread {
                try {
                    val socket = Socket(tvIp, port1) //申请链接
                    val dos = DataOutputStream(socket.getOutputStream())
                    dos.writeInt(2)
                    dos.flush()
                    dos.close()
                    socket.close()
                } catch (e: SocketTimeoutException) {
                    uiHandler.post {
                        tv_status.text = e.message
                    }
                } catch (e: Exception) {
                    uiHandler.post {
                        tv_status.text = e.message
                    }
                }
            }.start()
        }

        iv_volume_down.setOnClickListener {
            Thread {
                try {
                    val socket = Socket(tvIp, port1) //申请链接
                    val dos = DataOutputStream(socket.getOutputStream())
                    dos.writeInt(3)
                    dos.flush()
                    dos.close()
                    socket.close()
                } catch (e: SocketTimeoutException) {
                    uiHandler.post {
                        tv_status.text = e.message
                    }
                } catch (e: Exception) {
                    uiHandler.post {
                        tv_status.text = e.message
                    }
                }
            }.start()
        }

        iv_forward.setOnClickListener {
            Thread {
                try {
                    val socket = Socket(tvIp, port0) //申请链接
                    val dos = DataOutputStream(socket.getOutputStream())
                    dos.writeInt(4)
                    dos.flush()
                    dos.close()
                    socket.close()
                } catch (e: SocketTimeoutException) {
                    uiHandler.post {
                        tv_status.text = e.message
                    }
                } catch (e: Exception) {
                    uiHandler.post {
                        tv_status.text = e.message
                    }
                }
            }.start()
        }

        iv_rewind.setOnClickListener {
            Thread {
                try {
                    val socket = Socket(tvIp, port0) //申请链接
                    val dos = DataOutputStream(socket.getOutputStream())
                    dos.writeInt(5)
                    dos.flush()
                    dos.close()
                    socket.close()
                } catch (e: SocketTimeoutException) {
                    uiHandler.post {
                        tv_status.text = e.message
                    }
                } catch (e: Exception) {
                    uiHandler.post {
                        tv_status.text = e.message
                    }
                }
            }.start()
        }

        iv_forward.setOnLongClickListener {
            Thread {
                try {
                    val socket = Socket(tvIp, port0) //申请链接
                    val dos = DataOutputStream(socket.getOutputStream())
                    dos.writeInt(6)
                    dos.flush()
                    dos.close()
                    socket.close()
                } catch (e: SocketTimeoutException) {
                    uiHandler.post {
                        tv_status.text = e.message
                    }
                } catch (e: Exception) {
                    uiHandler.post {
                        tv_status.text = e.message
                    }
                }
            }.start()
            true
        }

        iv_rewind.setOnLongClickListener {
            Thread {
                try {
                    val socket = Socket(tvIp, port0) //申请链接
                    val dos = DataOutputStream(socket.getOutputStream())
                    dos.writeInt(7)
                    dos.flush()
                    dos.close()
                    socket.close()
                } catch (e: SocketTimeoutException) {
                    uiHandler.post {
                        tv_status.text = e.message
                    }
                } catch (e: Exception) {
                    uiHandler.post {
                        tv_status.text = e.message
                    }
                }
            }.start()
            true
        }
        iv_alarm.setOnClickListener {
            Thread {
                try {
                    val s = Socket(clockIp, port0) //申请链接
                    val os = s.getOutputStream()
                    val dos = DataOutputStream(os)
                    dos.writeInt(3)
                    dos.flush()
                    dos.close()
                    s.close()
                } catch (e: Exception) {
                    clock_status.text = e.message
                }
            }.start()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        e("onCreateView")
        return inflater.inflate(R.layout.fragment_tv_control, container, false)
    }
}