package Server;

import Messages.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;


public class ServerHandler extends ChannelInboundHandlerAdapter {
    private String user;
    private String userDir;


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("New client connected");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException, SQLException {

//        отправляю клиенту ответ о результатах логина в виде объекта authAnswer
        if (msg instanceof AuthRequest) {
            AuthRequest authRequest = (AuthRequest) msg;
            String loginFromMsg = authRequest.getLogin();
            String passwordFromMsg = authRequest.getPassword();

            if (AuthService.checkLoginAndPass(loginFromMsg, passwordFromMsg)) {
                setUser(loginFromMsg);
                AuthAnswer trueAnswer = new AuthAnswer();
                trueAnswer.setLoginAccepted(true);
                trueAnswer.setName(loginFromMsg);
                ctx.writeAndFlush(trueAnswer);
                System.out.println("User " + getUser() + " online.");
                createUserDir();
            } else {
                AuthAnswer falseAnswer = new AuthAnswer();
                falseAnswer.setLoginAccepted(false);
                falseAnswer.setReason(1);
                ctx.writeAndFlush(falseAnswer);
            }
        }
        //        отправляю клиенту ответ о результатах регистрации в виде объекта authAnswer
        if (msg instanceof RegRequest) {
            RegRequest regRequest = (RegRequest) msg;
            String loginFromMsg = regRequest.getLogin();
            String passwordFromMsg = regRequest.getPassword();

            if (!AuthService.isLoginOccupied(loginFromMsg)) {
                AuthService.setLoginAndPass(loginFromMsg, passwordFromMsg);
                setUser(loginFromMsg);
                AuthAnswer trueAnswer = new AuthAnswer();
                trueAnswer.setLoginAccepted(true);
                trueAnswer.setName(loginFromMsg);
                ctx.writeAndFlush(trueAnswer);
                System.out.println("New user " + getUser() + " online.");
                createUserDir();
            } else {
                AuthAnswer falseAnswer = new AuthAnswer();
                falseAnswer.setLoginAccepted(false);
                falseAnswer.setReason(2);
                ctx.writeAndFlush(falseAnswer);
            }
        }
//        сохраняю присланый файл
        if (msg instanceof FileMessage) {
            FileMessage fileMessage = (FileMessage) msg;
            Path path = Files.createFile(Paths.get(userDir + "/" + fileMessage.getFileName()));
            Files.write(path, fileMessage.getFileData());
            System.out.println(getUser() + " uploaded file: " + fileMessage.getFileName());
//              отправка сообщения клиенту о загруженном файле
            InfoMessage infoMessage = new InfoMessage();
            infoMessage.setInformation(fileMessage.getFileName() + " uploaded successfully.");
            ctx.writeAndFlush(infoMessage);
        }
//          отдаю список файлов в дирректории на сервере
        if (msg instanceof DirContentRequest) {
            DirContentRequest request = (DirContentRequest) msg;
            DirContentAnswer answer = new DirContentAnswer();
            if (request.getDirPath() == null) {
//                если запрошен корневой каталог (/dircontent)
                answer.setList(userDir);
            } else {
//                если запрошена конкретная папка (/dircontent folder/folder)
                answer.setList(userDir + "/" + request.getDirPath());
            }
            ctx.writeAndFlush(answer);
        }
//        отдаю файл клиенту
        if (msg instanceof DownloadRequest) {
            DownloadRequest request = (DownloadRequest) msg;
            String filePath = userDir + "/" + request.getPath();
            FileMessage fileMessage = new FileMessage(filePath);
            ctx.writeAndFlush(fileMessage);
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    //    создаем папку пользователя на стороне сервера
    public void createUserDir() throws IOException {
        userDir = String.format("D:/SCS_Server/%s", getUser());
        if (!Files.exists(Paths.get(userDir))) {
            Files.createDirectories(Paths.get(userDir));
            System.out.println("Created new directory: " + userDir);
        } else {
            System.out.println("Working in directory: " + userDir);
        }
    }
}