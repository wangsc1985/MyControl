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
import kotlinx.android.synthetic.main.fragment_tv_control.*
import kotlinx.android.synthetic.main.fragment_tv_control.iv_repeat
import kotlinx.android.synthetic.main.fragment_tv_control.iv_volume
import kotlinx.android.synthetic.main.fragment_tv_control.tv_status
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.net.SocketTimeoutException

/**
 * A simple [Fragment] subclass.
 */
class TvControlFragment : Fragment() {
    private lateinit var dataContext: DataContext
    private lateinit var tvIp:String
    private lateinit var clockIp:String
    private val uiHandler = Handler()


    override fun onResume() {
        super.onResume()

        Thread{
            try {
                //region 快进快退
                val socket = Socket(clockIp, 8000) //申请链接
                val dos = DataOutputStream(socket.getOutputStream())

                dos.writeInt(0)
                dos.flush()
                dos.close()
                socket.close()
                //endregion

                uiHandler.post {
                    clock_status.text="CLOCK已连接"
                }

            }catch (ex: SocketTimeoutException){
                uiHandler.post {
                    clock_status.text="无法连接CLOCK"
                }
            }
            catch (e: Exception) {
                e(e.message.toString())
            }
        }.start()

        Thread{
            try {
                //region 播放按钮
                var socket = Socket(tvIp, 8123) //申请链接
                var dos = DataOutputStream(socket.getOutputStream())
                dos.writeInt(0)
                dos.flush()

                var dis = DataInputStream(socket.getInputStream())
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
                //endregion


                //region 快进快退
                socket = Socket(tvIp, 8123) //申请链接
                dos = DataOutputStream(socket.getOutputStream())

                dos.writeInt(4)
                dos.flush()

                dis = DataInputStream(socket.getInputStream())
                val isShow=dis.readBoolean()
                e("---------------------------------------- 在顶栈？$isShow")
                uiHandler.post {
                    if(isShow){
                        iv_forward.visibility=View.VISIBLE
                        iv_rewind.visibility = View.VISIBLE
                    }else{
                        iv_forward.visibility=View.INVISIBLE
                        iv_rewind.visibility = View.INVISIBLE
                    }
                }

                dos.close()
                dis.close()
                socket.close()
                //endregion

                uiHandler.post {
                    tv_status.text="TV已连接"
                }

            }catch (ex: SocketTimeoutException){
                uiHandler.post {
                    tv_status.text="无法连接TV"
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
        tvIp = dataContext.getSetting(Setting.KEYS.tv_ip,"192.168.0.106").string
        clockIp = dataContext.getSetting(Setting.KEYS.clock_ip,"192.168.0.100").string

        //region 妙音
        iv_volume.setOnClickListener {
            Thread{
                try {
                    val s = Socket(clockIp, 8000) //申请链接
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
                    val s = Socket(clockIp, 8000) //申请链接
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
        iv_play.setOnClickListener {
            Thread{
                try {
                    val socket = Socket(tvIp, 8123) //申请链接
                    val dos = DataOutputStream(socket.getOutputStream())
                    dos.writeInt(1)
                    dos.flush()

                    val dis = DataInputStream(socket.getInputStream())
                    val status = dis.readBoolean()
                    e("---------------------------------------- 播放状态？$status")
                    uiHandler.post {
                        if(status){
                            iv_play.setImageResource(R.drawable.pause)
                        }else{
                            iv_play.setImageResource(R.drawable.play)
                        }
                    }

                    dos.close()
                    dis.close()
                    socket.close()
                } catch (ex: SocketTimeoutException){
                    uiHandler.post {
                        tv_status.text="无法连接"
                    }
                }catch (e: Exception) {
                    e(e.message.toString())
                }
            }.start()
        }

        iv_volume_up.setOnClickListener {
            Thread{
                try {
                    val socket = Socket(tvIp, 8123) //申请链接
                    val dos = DataOutputStream(socket.getOutputStream())
                    dos.writeInt(2)
                    dos.flush()
                    dos.close()
                    socket.close()
                } catch (ex: SocketTimeoutException){
                    uiHandler.post {
                        tv_status.text="无法连接"
                    }
                }catch (e: Exception) {
                    e(e.message.toString())
                }
            }.start()
        }
        iv_volume_down.setOnClickListener {
            Thread{
                try {
                    val socket = Socket(tvIp, 8123) //申请链接
                    val dos = DataOutputStream(socket.getOutputStream())
                    dos.writeInt(3)
                    dos.flush()
                    dos.close()
                    socket.close()
                } catch (ex: SocketTimeoutException){
                    uiHandler.post {
                        tv_status.text="无法连接"
                    }
                }catch (e: Exception) {
                    e(e.message.toString())
                }
            }.start()
        }

        iv_forward.setOnClickListener {
            Thread{
                try {
                    val socket = Socket(tvIp, 8000) //申请链接
                    val dos = DataOutputStream(socket.getOutputStream())
                    dos.writeInt(4)
                    dos.flush()
                    dos.close()
                    socket.close()
                } catch (ex: SocketTimeoutException){
                    uiHandler.post {
                        tv_status.text="无法连接"
                    }
                }catch (e: Exception) {
                    e(e.message.toString())
                }
            }.start()
        }
        iv_rewind.setOnClickListener {
            Thread{
                try {
                    val socket = Socket(tvIp, 8000) //申请链接
                    val dos = DataOutputStream(socket.getOutputStream())
                    dos.writeInt(5)
                    dos.flush()
                    dos.close()
                    socket.close()
                } catch (ex: SocketTimeoutException){
                    uiHandler.post {
                        tv_status.text="无法连接"
                    }
                }catch (e: Exception) {
                    e(e.message.toString())
                }
            }.start()
        }
        iv_forward.setOnLongClickListener {
            Thread{
                try {
                    val socket = Socket(tvIp, 8000) //申请链接
                    val dos = DataOutputStream(socket.getOutputStream())
                    dos.writeInt(6)
                    dos.flush()
                    dos.close()
                    socket.close()
                } catch (ex: SocketTimeoutException){
                    uiHandler.post {
                        tv_status.text="无法连接"
                    }
                }catch (e: Exception) {
                    e(e.message.toString())
                }
            }.start()
            true
        }
        iv_rewind.setOnLongClickListener {
            Thread{
                try {
                    val socket = Socket(tvIp, 8000) //申请链接
                    val dos = DataOutputStream(socket.getOutputStream())
                    dos.writeInt(7)
                    dos.flush()
                    dos.close()
                    socket.close()
                }catch (ex: SocketTimeoutException){
                    uiHandler.post {
                        tv_status.text="无法连接"
                    }
                } catch (e: Exception) {
                    e(e.message.toString())
                }
            }.start()
            true
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tv_control, container, false)
    }
}