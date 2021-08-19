package Messages;

import java.io.File;
import java.util.ArrayList;

// отдает лист с файлами и папками (в форме стрингов)
public class DirContentAnswer implements Message {
    private final ArrayList<String> list = new ArrayList<>();

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(String dirPath) {
        File path = new File(dirPath);
        File[] files = path.listFiles();
        if (files == null) {
            list.add("Empty directory!");
        } else {
//        сначала пишем в лист папки
            for (File file : files) {
                if (file.isDirectory()) {
                    list.add("Dir: " + file.getName());
                }
            }
//        теперь пишем в лист файлы
            for (File file1 : files) {
                if (!file1.isDirectory()) {
                    list.add("File: " + file1.getName());
                }
            }
        }
    }
}
