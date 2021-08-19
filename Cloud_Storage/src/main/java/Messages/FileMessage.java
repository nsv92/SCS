package Messages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileMessage implements Message {
    private final String fileName;
    private final byte[] fileData;

//    Поля для ChunkedInput
//    private static final byte[] EMPTY = new byte[0];
//    private byte[] previousPart = EMPTY;
//    private final Iterator<Object> iterator;


    public FileMessage(String str) throws IOException {
        fileName = Paths.get(str).getFileName().toString();
        fileData = Files.readAllBytes(Paths.get(str));
//        Поля для ChunkedInput
//        List<Object> objects = new ArrayList<>();
//        objects.add(fileName);
//        objects.add(fileData);
//        this.iterator = objects.iterator();
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }
//    оверрайд методов ChunkedInput
//    @Override
//    public boolean isEndOfInput() throws Exception {
//        return !iterator.hasNext() && previousPart.length == 0;
//    }
//
//    @Override
//    public void close() throws Exception {
//
//    }
//
//    @Override
//    public ByteBuf readChunk(ChannelHandlerContext ctx) throws Exception {
//        return readChunk(ctx.alloc());
//    }
//
//    @Override
//    public ByteBuf readChunk(ByteBufAllocator allocator) throws Exception {
//        if (isEndOfInput())
//            return null;
//        else {
//            int chunkSize = 1024 * 1024;
//            ByteBuf buf = allocator.buffer(chunkSize);
//            boolean release = true;
//            try {
//                int bytesRead = 0;
//                if (previousPart.length > 0) {
//                    if (previousPart.length > chunkSize) {
//                        throw new IllegalStateException();
//                    }
//                    bytesRead += previousPart.length;
//                    buf.writeBytes(previousPart);
//                }
//                boolean done = false;
//                while (!done) {
//                    if (!iterator.hasNext()) {
//                        done = true;
//                        previousPart = EMPTY;
//                    } else {
//                        Object obj = iterator.next();
//                        byte[] bytes = obj instanceof String ? ((String) obj).getBytes() : (byte[]) obj;
//                        bytesRead += bytes.length;
//                        if (bytesRead > chunkSize) {
//                            done = true;
//                            previousPart = bytes;
//                        } else {
//                            buf.writeBytes(bytes);
//                        }
//                    }
//                }
//                release = false;
//            } finally {
//                if (release)
//                    buf.release();
//            }
//            return buf;
//        }
//    }
//
//    @Override
//    public long length() {
//        return -1;
//    }
//
//    @Override
//    public long progress() {
//        return 0;
//    }
}
