package neverlang.lsp.clients;

import java.io.*;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipFiles {
//    public static void main(String[] args) {
//        String zipFilePath = "/Users/pankaj/tmp.zip";
//
//        String destDir = "/Users/pankaj/output";
//
//        unzip(zipFilePath, destDir);
//    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public static void moveFiles(String dirName, String destDirName) throws IOException {
        File dir = new File(dirName);
        File destDir = new File(destDirName);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                // Preserving folders structure
                Path source = Paths.get(file.getAbsolutePath());
                Path target = Paths.get(destDir.getAbsolutePath() + "/" + file.getName());
                Files.move(source, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        }
        if (!dir.delete()) {
            throw new IOException("Failed to delete " + dir);
        }
    }

    public static void unzip(String fileZip, String destDirName)  {
        try {
        File destDir = new File(destDirName);

        byte[] buffer = new byte[1024];
        ZipInputStream zis = null;

        zis = new ZipInputStream(new FileInputStream(fileZip));

        ZipEntry zipEntry = zis.getNextEntry();

        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unzip(InputStream fileZipIS, String destDirName)  {
        try {
            File destDir = new File(destDirName);

            byte[] buffer = new byte[1024];
            ZipInputStream zis = null;

            zis = new ZipInputStream(fileZipIS);

            ZipEntry zipEntry = zis.getNextEntry();

            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new IOException("Failed to create directory " + parent);
                    }

                    // write file content
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
