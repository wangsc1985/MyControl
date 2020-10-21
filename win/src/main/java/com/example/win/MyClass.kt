package com.example.win

import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.NetworkInterface
import java.net.Socket

class MyClass {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            val socket = Socket("27.207.71.187", 1080) //申请链接
            val dos = DataOutputStream(socket.getOutputStream())
            dos.writeInt(1)
            dos.flush()

//            val dis = DataInputStream(socket.getInputStream())
////                System.out.println(dis.readInt())
////            System.out.println(dis.readBoolean().toString())
////            dis.close()

            dos.close()
            socket.close()


//            System.out.println(dis.readBoolean().toString())
        }
    }
}