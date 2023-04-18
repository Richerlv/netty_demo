package com.Richerlv.client;

import com.Richerlv.client.handler.LoginResponseHandler;
import com.Richerlv.client.handler.MessageResponseHandler;
import com.Richerlv.packet.MessageRequestPacket;
import com.Richerlv.serializer.PacketDecoder;
import com.Richerlv.serializer.PacketEncoder;
import com.Richerlv.util.LoginUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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
                socketChannel.pipeline().addLast(new PacketDecoder());
                socketChannel.pipeline().addLast(new PacketEncoder());
                socketChannel.pipeline().addLast(new LoginResponseHandler());
                socketChannel.pipeline().addLast(new MessageResponseHandler());
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

                    //发送消息
                    Channel channel = ((ChannelFuture) future).channel();
                    startConsoleThread(channel);

                } else if(retry > MAX_RETRY){
                    System.out.println("已经达到最大连接次数，停止重连～");
                } else {
                    int delay = 1 << (retry - 1);
                    System.out.println(new Date() + ":第" + retry + "次连接失败...");
                    bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry + 1), delay, TimeUnit.SECONDS);
                }
            }
        });
    }


    /**
     * 控制台接受消息发送至服务端
     * @param channel
     */
    public static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (LoginUtil.hasLogin(channel)) {
                    System.out.println("输入消息发送至服务端: ");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();

                    MessageRequestPacket packet = new MessageRequestPacket();
                    packet.setMessage(line);

                    channel.writeAndFlush(packet);
                }
            }
        }).start();
    }

}

