package com.Richerlv.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Date;

/**
 * @author lvyanling
 * @date 2023-04-13
 */
public class NettyClient {

    final static int MAX_RETRY = 10;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(new FirstClientHandler());
            }
        });
        //建立连接
//        bootstrap.connect("km.sankuai.com", 80).addListener(new GenericFutureListener<Future<? super Void>>() {
//            @Override
//            public void operationComplete(Future<? super Void> future) throws Exception {
//                if(future.isSuccess()) {
//                    System.out.println("连接成功");
//                } else {
//                    System.out.println("连接失败");
//                }
//            }
//        });

        connect(bootstrap, "127.0.0.1", 80, 1);
    }

    /**
     * 实现失败重试，把建立连接的逻辑提取出来
     * {
     *     设置最大重试次数
     *     连接失败以二进制指数退避形式重连
     * }
     * @param bootstrap
     * @param host
     * @param port
     */
    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(future.isSuccess()) {
                    System.out.println("连接成功");
                } else if(retry > MAX_RETRY){
                    System.out.println("已经达到最大连接次数，停止重连～");
                } else {
                    int delay = 1 << (retry - 1);
                    System.out.println(new Date() + ":第" + retry + "次连接失败...");
                    connect(bootstrap, host, port, retry + 1);
                }
            }
        });
    }
}
