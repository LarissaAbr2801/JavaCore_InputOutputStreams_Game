import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        //task1
        StringBuilder stringBuilder = new StringBuilder();

        //создание директорий в папке Games
        String pathname1 = "../Games/src";
        appendDirInStringBuilder(stringBuilder, pathname1);
        String pathname2 = "../Games/res";
        appendDirInStringBuilder(stringBuilder, pathname2);
        String pathname3 = "../Games/saveGames";
        appendDirInStringBuilder(stringBuilder, pathname3);
        String pathname4 = "../Games/temp";
        appendDirInStringBuilder(stringBuilder, pathname4);

        //создание поддиректорий в src
        String pathnameForSrc1 = "../Games/src/main";
        appendDirInStringBuilder(stringBuilder, pathnameForSrc1);
        String pathnameForSrc2 = "../Games/src/test";
        appendDirInStringBuilder(stringBuilder, pathnameForSrc2);

        //создание директорий в res
        String pathnameForRes1 = "../Games/res/drawables";
        appendDirInStringBuilder(stringBuilder, pathnameForRes1);
        String pathnameForRes2 = "../Games/res/vectors";
        appendDirInStringBuilder(stringBuilder, pathnameForRes2);
        String pathnameForRes3 = "../Games/res/icons";
        appendDirInStringBuilder(stringBuilder, pathnameForRes3);

        //создание файлов в main
        String pathnameChildForMain1 = "Main.java";
        appendFileInStringBuilder(stringBuilder, pathnameForSrc1, pathnameChildForMain1);
        String pathnameChildForMain2 = "Utils.java";
        appendFileInStringBuilder(stringBuilder, pathnameForSrc1, pathnameChildForMain2);

        //создание файла в temp
        String pathnameChildForSaveGames = "temp.txt";
        appendFileInStringBuilder(stringBuilder, pathname4, pathnameChildForSaveGames);

        //task2
        //создание экземпляров GameProgress и сохраняем в saveGames
        GameProgress gameProgress1 = new GameProgress(10, 20, 3, 61.6);
        String name1 = "../Games/saveGames/save1.dat";
        saveGame(gameProgress1, name1);
        GameProgress gameProgress2 = new GameProgress(7, 15, 2, 45.0);
        String name2 = "../Games/saveGames/save2.dat";
        saveGame(gameProgress2, name2);
        GameProgress gameProgress3 = new GameProgress(4, 34, 6, 78.4);
        String name3 = "../Games/saveGames/save3.dat";
        saveGame(gameProgress3, name3);

        //добавление экземпляров в архив
        ArrayList<String> waysForZip = new ArrayList<>();
        waysForZip.add(name1);
        waysForZip.add(name2);
        waysForZip.add(name3);

        String nameForZos = "../Games/saveGames/zip.zip";
        String nameForEntry = "save";
        zipFiles(nameForZos, nameForEntry, waysForZip);

        //удаление файлов сохранения вне архива
        String pathnameForDeletedSave1 = "save1.dat";
        appendDeletedFileInStringBuilder(stringBuilder, pathname3, pathnameForDeletedSave1);
        String pathnameForDeletedSave2 = "save2.dat";
        appendDeletedFileInStringBuilder(stringBuilder, pathname3, pathnameForDeletedSave2);
        String pathnameForDeletedSave3 = "save3.dat";
        appendDeletedFileInStringBuilder(stringBuilder, pathname3, pathnameForDeletedSave3);

        //task3
        //разархивация
        openZip(nameForZos, new File(pathname3));

        //запись результатов в temp.txt
        try (FileWriter fileWriter = new FileWriter(new File(pathname4, pathnameChildForSaveGames), false)) {
            fileWriter.write(stringBuilder.toString());
            fileWriter.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        //вывод в консоль состояния сохраненной игры
        System.out.println(openProgress(name2));
    }

    public static void appendDirInStringBuilder(StringBuilder stringBuilder, String pathname) {
        File dir = new File(pathname);
        stringBuilder.append(makeDir(dir));
    }

    public static void appendFileInStringBuilder(StringBuilder stringBuilder, String nameParent, String nameChild) {
        File file = new File(nameParent, nameChild);
        stringBuilder.append(makeFile(file));
    }

    public static void appendDeletedFileInStringBuilder(StringBuilder stringBuilder, String nameParent, String nameChild) {
        File fileForDelete = new File(nameParent, nameChild);
        stringBuilder.append(deleteFile(fileForDelete));
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
