package com.company;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * 客户端线程类，专门接收服务器响应的信息
 */
public class NioClientHandler implements Runnable {

    private Selector selector;

    public NioClientHandler(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {

        try{
        for (;;){

            int readyChannels = selector.select();


            if (readyChannels == 0) continue;

            /**
             * 获取可用channel的集合
             */
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator iterator = selectionKeySet.iterator();

            while (iterator.hasNext()){
                /**
                 * SelectorKey实例
                 */
                SelectionKey selectionKey = (SelectionKey) iterator.next();

                /**
                 * 移除Set中的当前selectorKey
                 */
                iterator.remove();


                /**
                 * 7.根据就绪状态，调用相应的业务逻辑
                 */



                /**
                 * 如果是可读事件
                 */
                //do something
                if (selectionKey.isReadable()){
                    readHandler(selectionKey,selector);
                }

            }
        }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 可读事件处理器
     */
    private void readHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        /**
         * 用从selectionKey中获取到已经就绪的channel
         */
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();


        /**
         * 创建buffer
         */
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);


        /**
         * 循环读取服务器响应信息
         */
        String response = "";
        while (socketChannel.read(byteBuffer) > 0){

            /**
             * 切换buffer为读模式
             */
            byteBuffer.flip();

            /**
             * 读取buffer中的内容
             */
            response += Charset.forName("UTF-8").decode(byteBuffer);

        }

        /**
         * 将channel再次注册到selector上，监听他的可读事件
         */
        socketChannel.register(selector, SelectionKey.OP_READ);

        /**
         * 将服务器端响应信息打印到本地
         */
        if (response.length() > 0){
            //广播给其他客户端
            System.out.println(response);
        }

    }
}
