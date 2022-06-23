import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GameInstallation {

    public static void main(String[] args) {
        StringBuilder log = new StringBuilder();

        //1. В папке Games создайте несколько директорий: src, res, savegames, temp.
        createDir("Games/src", log);
        createDir("Games/res", log);
        createDir("Games/savegames", log);
        createDir("Games/temp", log);

        //2. В каталоге src создайте две директории: main, test.
        createDir("Games/src/main", log);
        createDir("Games/src/test", log);

        //3. В подкаталоге main создайте два файла: Main.java, Utils.java.
        createFile("Games/src/main", "Main.java", log);
        createFile("Games/src/main", "Utils.java", log);

        //4. В каталог res создайте три директории: drawables, vectors, icons.
        createDir("Games/res/drawables", log);
        createDir("Games/res/vectors", log);
        createDir("Games/res/icons", log);
        
        //5. В директории temp создайте файл temp.txt.
        File tempTxt = createFile("Games/temp", "temp.txt", log);

        //записать в лог файл
        try (FileWriter writer = new FileWriter(tempTxt)) {
            writer.write(String.valueOf(log));
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void createDir(String name, StringBuilder log) {
        File dir = new File(name);
        dir.mkdir();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        if(dir.exists())
            log.append(sdf.format(date) + ": " + String.format("Директория %s создана (%s)\n", dir.getName(), dir.getPath()));
        else
            log.append(sdf.format(date)  + ": " + String.format("Директория %s не найдена (%s)\n", dir.getName(), dir.getPath()));
    }

    private static File createFile(String dir, String name, StringBuilder log) {
        File file = new File(dir, name);
        try {
            file.createNewFile();
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        if (file.exists())
            log.append(sdf.format(date) + ": " + String.format("Файл %s создан (%s)\n", file.getName(), file.getPath()));
        else
            log.append(sdf.format(date) + ": " + String.format("Файл %s не найден (%s)\n", file.getName(), file.getPath()));
        return file;
    }
}
