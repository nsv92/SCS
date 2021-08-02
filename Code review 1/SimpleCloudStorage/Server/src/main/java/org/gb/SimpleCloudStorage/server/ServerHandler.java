package org.gb.SimpleCloudStorage.server;

import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import org.gb.SimpleCloudStorage.Messages.FileMsg;
import org.gb.SimpleCloudStorage.Messages.Msg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.SimpleChannelInboundHandler;

import java.io.RandomAccessFile;


public class ServerHandler extends SimpleChannelInboundHandler  {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FileMsg) {
            String path = "D:" + "SERVER" + ((FileMsg) msg).getFileName();
            byte[] data = ((FileMsg) msg).getFileData();
            ByteBuf byteBuf = (ByteBuf) data;
            RandomAccessFile raf = new RandomAccessFile(path, "rw");
            FileRegion fr = new DefaultFileRegion(raf.getChannel(), 0, raf.length());

//            ByteBuf buf = (ByteBuf) msg;
//            while (buf.readableBytes() > 0) {
//                System.out.print((char) buf.readByte());
//            }
//            buf.release();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("New client connected.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//      Выводит ошибку и закрывает соединение
        cause.printStackTrace();
        ctx.close();
    }
}
