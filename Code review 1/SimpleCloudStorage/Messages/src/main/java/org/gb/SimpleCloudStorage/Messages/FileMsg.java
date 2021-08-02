package org.gb.SimpleCloudStorage.Messages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileMsg extends Msg{
    private final String fileName;
    private final byte[] fileData;

    public FileMsg(String str) throws IOException {

        fileName = Paths.get(str).getFileName().toString();
        fileData = Files.readAllBytes(Paths.get(str));
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }
}