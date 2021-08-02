package org.gb.SimpleCloudStorage.client;

import org.gb.SimpleCloudStorage.Messages.FileMsg;
import org.gb.SimpleCloudStorage.Messages.Msg;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


import java.io.IOException;


public class NetworkHandler {
    private static final int PORT = 8129;
    private static final String HOST = "localhost";
    private io.netty.channel.socket.SocketChannel channel;

    public NetworkHandler() {
        new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<io.netty.channel.socket.SocketChannel>() {
                            @Override
                            protected void initChannel(io.netty.channel.socket.SocketChannel ch) throws Exception {
                                channel = ch;
                                ch.pipeline().addLast(new StringDecoder(), new StringEncoder());
                            }
                        });
                ChannelFuture future = b.connect(HOST, PORT).sync();
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        }).start();
    }

    public void sendMessage(String str) throws IOException {
        FileMsg fileMsg = new FileMsg(str);
        System.out.println(fileMsg.getFileName());
        channel.writeAndFlush(fileMsg.getFileName());
    }
}
