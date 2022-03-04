import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        File dir1 = new File("C://Users/1/Desktop/Java/Games/src");
        stringBuilder.append(makeDir(dir1));
        File dir2 = new File("C://Users/1/Desktop/Java/Games/res");
        stringBuilder.append(makeDir(dir2));
        File dir3 = new File("C://Users/1/Desktop/Java/Games/savegames");
        stringBuilder.append(makeDir(dir3));
        File dir4 = new File("C://Users/1/Desktop/Java/Games/temp");
        stringBuilder.append(makeDir(dir4));
        File dirInFirstDir1 = new File("C://Users/1/Desktop/Java/Games/src/main");
        stringBuilder.append(makeDir(dirInFirstDir1));
        File dirInFirstDir2 = new File("C://Users/1/Desktop/Java/Games/src/test");
        stringBuilder.append(makeDir(dirInFirstDir2));
        File file1 = new File(dirInFirstDir1, "Main.java");
        stringBuilder.append(makeFile(file1));
        File file2 = new File(dirInFirstDir1, "Utils.java");
        stringBuilder.append(makeFile(file2));
        File dirInSecondDir1 = new File("C://Users/1/Desktop/Java/Games/res/drawables");
        stringBuilder.append(makeDir(dirInSecondDir1));
        File dirInSecondDir2 = new File("C://Users/1/Desktop/Java/Games/res/vectors");
        stringBuilder.append(makeDir(dirInSecondDir2));
        File dirInSecondDir3 = new File("C://Users/1/Desktop/Java/Games/res/icons");
        stringBuilder.append(makeDir(dirInSecondDir3));
        File file3 = new File(dir4, "temp.txt");
        stringBuilder.append(makeFile(file3));
        GameProgress gameProgress1 = new GameProgress(10, 20, 3, 61.6);
        String name1 = "C://Users/1/Desktop/Java/Games/savegames/save1.dat";
        saveGame(gameProgress1, name1);
        GameProgress gameProgress2 = new GameProgress(7, 15, 2, 45.0);
        String name2 = "C://Users/1/Desktop/Java/Games/savegames/save2.dat";
        saveGame(gameProgress2, name2);
        GameProgress gameProgress3 = new GameProgress(4, 34, 6, 78.4);
        String name3 = "C://Users/1/Desktop/Java/Games/savegames/save3.dat";
        saveGame(gameProgress3, name3);
        ArrayList<String> waysForZip = new ArrayList<>();
        waysForZip.add(name1);
        waysForZip.add(name2);
        waysForZip.add(name3);
        String nameForZos = "C://Users/1/Desktop/Java/Games/savegames/zip.zip";
        String nameForEntry = "save";
        zipFiles(nameForZos, nameForEntry, waysForZip);
        File fileForDelete1 = new File(dir3, "save1.dat");
        stringBuilder.append(deleteFile(fileForDelete1));
        File fileForDelete2 = new File(dir3, "save2.dat");
        stringBuilder.append(deleteFile(fileForDelete2));
        File fileForDelete3 = new File(dir3, "save3.dat");
        stringBuilder.append(deleteFile(fileForDelete3));
        openZip(nameForZos, dir3);
        try (FileWriter fileWriter = new FileWriter(file3, false)) {
            fileWriter.write(stringBuilder.toString());
            fileWriter.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(openProgress(name2));
    }

    public static String makeDir(File dir) {
        if (dir.mkdir()) {
            return "\nКаталог " + dir.getName() + " был создан";
        }
        return "\nКаталог " + dir.getName() + " не был создан";
    }

    public static String makeFile(File file) {
        try {
            if (file.createNewFile()) {
                return "\nФайл " + file.getName() + " был создан";

            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return "\nФайл " + file.getName() + " не был создан";
    }

    public static String deleteFile(File file) {
        try {
            if (file.delete()) {
                return "\nФайл " + file.getName() + " был удален";

            }
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
        return "\nФайл " + file.getName() + " не был удален";
    }

    public static void saveGame(GameProgress gameProgress, String name) {
        try (FileOutputStream fos = new FileOutputStream(name);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String nameForZos, String nameForEntry, ArrayList<String> ways) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(nameForZos))) {
            for (int i = 0; i < ways.size(); i++) {
                FileInputStream fis = new FileInputStream(ways.get(i));
                ZipEntry entry = new ZipEntry(nameForEntry + (i + 1) + ".dat");
                zos.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zos.write(buffer);
                zos.closeEntry();
                fis.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void openZip(String nameForZis, File path) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(nameForZis))) {
            ZipEntry entry;
            String name;
            while ((entry = zis.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fos = new FileOutputStream(path + "/" + name);
                for (int c = zis.read(); c != -1; c = zis.read()) {
                    fos.write(c);
                }
                fos.flush();
                zis.closeEntry();
                fos.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static GameProgress openProgress(String path) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }
}
