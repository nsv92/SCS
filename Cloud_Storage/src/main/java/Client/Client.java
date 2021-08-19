package Client;

import Messages.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.sql.SQLOutput;
import java.util.Scanner;


public final class Client {

    private static SocketChannel channel;
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            channel = ch;
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(
                                    new ObjectEncoder(),
                                    new ObjectDecoder(1024 * 1024 * 1024, ClassResolvers.cacheDisabled(null)),
//                                    new ChunkedWriteHandler(),
                                    new ClientHandler());
                        }
                    });
            // Start the connection attempt.
            b.connect(HOST, PORT).sync().channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }

    //    Метод для ожидания ввода команды от пользователя и ее обработки (еще сырой)
    //    вынести в отдельный класс
    public static void inputListener() throws IOException {
        System.out.println("Input your command:");
        System.out.println("(input /commands for available commands)");
        Scanner in = new Scanner(System.in);
        String command = in.nextLine();

        if (command.equals("/commands")) {
            System.out.println("'/upload' - uploading file from client to the server user directory, " +
                    "e.g. '/upload D:/Test.txt'");
            System.out.println("'/dircontent' - show root directory's content.");
            System.out.println("'/dircontent folder/another_folder' - show content in some folder.");
            System.out.println("'/download' - download file from server, e.g. '/upload Test.txt'.");
        } else

        if (command.startsWith("/upload ")) {
            String[] split = command.split("\\s", 2);
            String filePath = split[1];
            try {
                FileMessage fileMessage = new FileMessage(filePath);
                System.out.println("Uploading " + fileMessage.getFileName());
                channel.writeAndFlush(fileMessage);
            } catch (NoSuchFileException e) {
                System.out.println(e.getMessage());
            }
        } else

        if (command.startsWith("/dircontent")) {
            DirContentRequest dirContentRequest = new DirContentRequest();
            String[] split = command.split("\\s", 2);
            if (split.length != 1) {
                String dirPath = split[1];
                dirContentRequest.setDirPath(dirPath);
            }
            channel.writeAndFlush(dirContentRequest);
        } else

        if (command.startsWith("/download ")) {
            String[] split = command.split("\\s", 2);
            DownloadRequest request = new DownloadRequest(split[1]);
            channel.writeAndFlush(request);
            System.out.println("Downloading...");
        } else {
            System.out.println("Incorrect command!");
            inputListener();
        }
    }
}