package Client;

import Message.FileMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
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
import java.util.Scanner;


public final class Client {

    private static SocketChannel channel;
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));
    //    для выхода из цикла inputListener
    private static boolean listenerStatus = false;

    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
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
        do {
            System.out.println("Input your command:");
            System.out.println("(input /commands for available commands)");
            Scanner in = new Scanner(System.in);
            String command = in.nextLine();
            if (command.equals("/commands")) {
                System.out.println("'/upload' - for uploading file from client to the server user directory, " +
                        "e.g '/upload D:/Test.txt'");
            } else if (command.startsWith("/upload")) {
//                добавить обработку noSuchFileException
                String[] split = command.split("\\s", 2);
                String filePath = split[1];
                System.out.println(filePath);
                try {
                    FileMessage fileMessage = new FileMessage(filePath);
                    System.out.println("Uploading " + fileMessage.getFileName());
                    new Thread(() -> {
                        ChannelFuture future = channel.writeAndFlush(fileMessage);
                    }).start();
                } catch (NoSuchFileException e) {
                    System.out.println(e.getMessage());
                }
            } else System.out.println("Incorrect command!");
        } while (!isListenerStatus());
    }

    public static boolean isListenerStatus() {
        return listenerStatus;
    }

}