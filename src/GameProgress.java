import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class GameProgress implements Serializable {
    private static final long serialVersionUID = 1L;

    private int health;
    private int weapons;
    private int lvl;
    private double distance;

    public GameProgress(int health, int weapons, int lvl, double distance) {
        this.health = health;
        this.weapons = weapons;
        this.lvl = lvl;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "GameProgress{" +
                "health=" + health +
                ", weapons=" + weapons +
                ", lvl=" + lvl +
                ", distance=" + distance +
                '}';
    }

    private static void saveGame(String path, GameProgress game) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(game);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void zipFiles(String pathZip, String[] filesToZip) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathZip))) {
            for (String f: filesToZip) {
                FileInputStream fis = new FileInputStream(f);
                File file = new File(f);
                ZipEntry entry = new ZipEntry(file.getName());
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        GameProgress game1 = new GameProgress(3, 2, 1, 1);
        GameProgress game2 = new GameProgress(2, 1, 3, 3);
        GameProgress game3 = new GameProgress(1, 0, 4, 4);

        String path = "Games/savegames/";
        String path1 = path + "game1.data";
        String path2 = path + "game2.data";
        String path3 = path + "game3.data";

        saveGame(path1, game1);
        saveGame(path2, game2);
        saveGame(path3, game3);

        String[] filesToZip = {path2, path3};
        String  zipPath = "Games/savegames/gamesProgress.zip";
        zipFiles(zipPath, filesToZip);

        //Delete games that were not saved to zip

        ArrayList<File> files = new ArrayList<File>();
        File dir = new File(path);
        if (dir.isDirectory()) {
            for (File f: dir.listFiles()) {
                if (f.isFile()) {
                    files.add(f);
                }
            }
        }
        ArrayList<File> filesToDelete = files;

        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                File file = new File(path + name);
                if (files.contains(file)) {
                    filesToDelete.remove(file);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }

        } catch (Exception ex)  {
            System.out.println(ex.getMessage());
        }

        for (File f: filesToDelete) {
            if (f.isFile() && !f.equals(new File(zipPath)))
                f.delete();
        }

    }
}
