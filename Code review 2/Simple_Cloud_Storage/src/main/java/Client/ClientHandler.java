package Client;

import Message.AuthAnswer;
import Message.AuthRequest;
import Message.RegRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.util.Scanner;

public class ClientHandler extends ChannelInboundHandlerAdapter {
//  присвоение имени означает что логин/регистрация прошли успешно
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws IOException {
//        выбор зарегистрироваться или залогиниться при создании соединения с сервером
        System.out.println("Type /login for login or /reg for registration.");
//        позже необходимо вынести все /команды в отдельный класс
        Scanner scanner = new Scanner(System.in);

//        status - счетчик для выхода из цикла выбора "логин или регистрация" do-while
        boolean status = false;
        do {
            String str = scanner.nextLine();
            if (str.equals("/login")) {
                status = true;
                AuthRequest authMessage = new AuthRequest();
                authMessage.setLogin();
                authMessage.setPassword();
                ctx.writeAndFlush(authMessage);
            } else if (str.equals("/reg")) {
                status = true;
                RegRequest regMessage = new RegRequest();
                regMessage.setLogin();
                regMessage.setPassword();
                ctx.writeAndFlush(regMessage);
            } else {
                System.out.println("Incorrect input. Please try again.");
            }
        } while (!status);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {

//        обрабатываю ответ от сервера об успешности логина/регистрации
        if (msg instanceof AuthAnswer) {
            AuthAnswer authAnswer = (AuthAnswer) msg;
            if (authAnswer.isLoginAccepted()) {
                setName(authAnswer.getName());
                System.out.println("Connected successfully!");
//                клиент начинает ожидать команду от пользователя
                Client.inputListener();
//                обработка отказа логина/регистрации от сервера
            } else if (authAnswer.getReason() == 1) {
                System.out.println("Wrong login/password. Please try again.");
                AuthRequest authMessage = new AuthRequest();
                authMessage.setLogin();
                authMessage.setPassword();
                ctx.writeAndFlush(authMessage);
            } else if (authAnswer.getReason() == 2) {
                System.out.println("Login is occupied. Please try again.");
                RegRequest regMessage = new RegRequest();
                regMessage.setLogin();
                regMessage.setPassword();
                ctx.writeAndFlush(regMessage);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}