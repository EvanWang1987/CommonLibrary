package com.github.evan.common_library.utils;

import android.os.Environment;

import com.github.evan.common_library.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 2017/10/2.
 */
public class FileUtil {
    /**
     * 默认SharedPreferences文件名
     */
    public static final String DEFAULT_SHARED_PREFERENCE_FILE_NAME = "default_shared_preferences";

    public static final String SUFFIX_NAME_XML = ".xml";
    public static final String SUFFIX_NAME_JAVA = ".java";
    public static final String SUFFIX_NAME_TXT = ".txt";
    public static final String SUFFIX_NAME_DB = ".db";
    public static final String SUFFIX_NAME_PNG = ".png";
    public static final String SUFFIX_NAME_JPG = ".JPG";
    public static final String SUFFIX_NAME_MP3 = ".mp3";


    public enum FileStatus {
        UNKNOWN, NOT_EXISTS, FILE_EXISTS, CREATE_SUCCESS, CREATE_FAIL, CREATE_PARENT_DIR_FAIL,
        NOT_DELETE, DELETE_SUCCESS, SOURCE_FILE_DELETE_FAIL, DELETE_FAIL, WRITE_SUCCESS,
        WRITE_FAIL, IS_FILE, IS_DIRECTORY, COPY_SUCCESS, COPY_FAIL, CUT_FILE_SUCCESS, CUT_FILE_FAIL
    }

    public static boolean isSdcardMounted() {
        String externalStorageState = Environment.getExternalStorageState();
        return externalStorageState.equals(Environment.MEDIA_MOUNTED);
    }

    public static String getSdcardSize() {
        if (isSdcardMounted()) {
            File file = Environment.getExternalStorageDirectory();
            long totalSpace = file.getTotalSpace();
            Object[] returnValue = new Object[2];
            UnitConvertUil.byte2MaxUnit(totalSpace, 3, returnValue);
            return returnValue[0] + returnValue[1].toString();
        }

        return "";
    }

    public static String getSdcardFreeSize() {
        if (isSdcardMounted()) {
            File file = Environment.getExternalStorageDirectory();
            long totalSpace = file.getFreeSpace();
            Object[] returnValue = new Object[2];
            UnitConvertUil.byte2MaxUnit(totalSpace, 2, returnValue);
            return returnValue[0] + returnValue[1].toString();
        }

        return "";
    }

    /**
     * 判断是否是指定文件后缀名
     *
     * @param fileName
     * @param suffixName
     * @return
     */
    public static boolean isFileOfTargetSuffixName(String fileName, String suffixName) {
        return fileName.endsWith(suffixName);
    }


    /**
     * 获取存储目录
     *
     * @return
     */
    public static String getApplicationDataDir() {
        String returnValue = BaseApplication.getApplication().getFilesDir().getAbsolutePath();
        boolean isExternalCardEnable = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED);
        if (isExternalCardEnable) {
            returnValue = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + BaseApplication.getApplication().getPackageName();
        }
        return returnValue;
    }

    /**
     * 获取SharedPreference目录
     *
     * @return
     */
    public static File getSharedPreferenceDir() {
        return new File(BaseApplication.getApplication().getFilesDir().getParentFile(), "shared_prefs");
    }

    /**
     * 列出App SharedPreference目录下所有.xml结尾的文件名
     *
     * @return
     */
    public static List<String> listSharedPreferenceFiles() {
        List<String> returnValue = new ArrayList<>();
        File sharedPreferenceDir = getSharedPreferenceDir();
        String[] fileNames = sharedPreferenceDir.list();
        if (null != fileNames) {
            int N = fileNames.length;
            for (int i = 0; i < N; i++) {
                String fileName = fileNames[i];
                boolean isXmlFile = isFileOfTargetSuffixName(fileNames[i], SUFFIX_NAME_XML);
                if (isXmlFile) {
                    returnValue.add(fileName);
                }
            }
        }

        return returnValue;
    }

    /**
     * 获取SharedPreference文件
     *
     * @param isDefaultSpFile
     * @param fileName
     * @return
     */
    public static File getSharedPreferenceFile(boolean isDefaultSpFile, String fileName) {
        String targetFileName = isDefaultSpFile ? DEFAULT_SHARED_PREFERENCE_FILE_NAME : fileName;
        String dir = getSharedPreferenceDir().getAbsolutePath();
        return new File(dir, targetFileName + SUFFIX_NAME_XML);
    }

    /**
     * 删除SharedPreference文件
     *
     * @param isDefaultSpName
     * @param spName
     * @return
     */
    public static FileStatus removeSharedPreferenceFile(boolean isDefaultSpName, String spName) {
        File sharedPreferenceFile = getSharedPreferenceFile(isDefaultSpName, spName);
        return deleteFile(sharedPreferenceFile.getAbsolutePath());
    }

    /**
     * 判断SharedPreference文件是否存在
     *
     * @param isDefaultSp
     * @param spName
     * @return
     */
    public static boolean isSharedPreferenceFileExistsOnDisk(boolean isDefaultSp, String spName) {
        boolean returnValue = false;
        String targetSpName = isDefaultSp ? FileUtil.DEFAULT_SHARED_PREFERENCE_FILE_NAME : spName;

        List<String> sharedPreferenceFiles = listSharedPreferenceFiles();
        for (int i = 0; i < sharedPreferenceFiles.size(); i++) {
            String fileName = sharedPreferenceFiles.get(i);
            if (StringUtil.equals(targetSpName + SUFFIX_NAME_XML, fileName, false)) {
                returnValue = true;
                break;
            }
        }

        return returnValue;
    }

    /**
     * 读取文本文件
     *
     * @param absPath
     * @param charset 编码格式
     * @return
     */
    public static String readFromFile(String absPath, String charset) {
        try {
            FileStatus status = isFileExists(absPath);
            if (status == FileStatus.FILE_EXISTS) {
                File file = new File(absPath);
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                int len = -1;
                while ((len = fis.read(buffer)) != -1) {
                    bos.write(buffer, 0, buffer.length);
                }
                String value = new String(bos.toByteArray(), charset);
                return value;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写入文字数据
     *
     * @param fileAbsPath      文件路径
     * @param value            数据
     * @param isDeleteIfExists 文件存在是否重新创建
     * @param isAppend2File    是否保存之前数据，新数据写到文件末尾
     * @return
     */
    public static FileStatus write2File(String fileAbsPath, String value, boolean isDeleteIfExists, boolean isAppend2File) {
        FileStatus fileStatus = createFile(fileAbsPath, isDeleteIfExists);
        if (fileStatus == FileStatus.NOT_DELETE) {
            return FileStatus.NOT_DELETE;
        }

        try {
            if (fileStatus == FileStatus.CREATE_SUCCESS) {
                boolean writable = isWritable(fileAbsPath);
                if (writable) {
                    RandomAccessFile file = new RandomAccessFile(fileAbsPath, "rw");
                    file.seek(isAppend2File ? file.length() : 0);
                    file.write(value.getBytes());
                    file.close();
                    return FileStatus.WRITE_SUCCESS;
                }
            }
            return fileStatus;
        } catch (IOException e) {
            Logger.printStackTrace(e);
            return FileStatus.WRITE_FAIL;
        }
    }

    /**
     * 是否可读
     *
     * @param absPath
     * @return
     */
    public static boolean isReadable(String absPath) {
        File file = new File(absPath);
        return file.exists() && file.isFile() && file.canRead();
    }

    /**
     * 是否可写
     *
     * @param absPath
     * @return
     */
    public static boolean isWritable(String absPath) {
        File file = new File(absPath);
        return file.exists() && file.isFile() && file.canWrite();
    }

    /**
     * 文件是否存在
     *
     * @param absPath
     * @return
     */
    public static FileStatus isFileExists(String absPath) {
        File file = new File(absPath);
        FileStatus status = file.exists() && file.isFile() ? FileStatus.FILE_EXISTS : FileStatus.NOT_EXISTS;
        return status;
    }

    /**
     * 目录是否存在
     *
     * @param absPath
     * @return
     */
    public static FileStatus isDirExists(String absPath) {
        File file = new File(absPath);
        FileStatus status = file.exists() && file.isDirectory() ? FileStatus.FILE_EXISTS : FileStatus.NOT_EXISTS;
        return status;
    }

    /**
     * 创建文件
     *
     * @param absPath
     * @return
     */
    public static FileStatus createFile(String absPath, boolean isDeleteIfExists) {
        try {
            File file = new File(absPath);
            if (file.exists()) {
                if (isDeleteIfExists) {
                    FileStatus status = clearFileOrDir(absPath);
                    if (status == FileStatus.DELETE_SUCCESS) {
                        return file.createNewFile() ? FileStatus.CREATE_SUCCESS : FileStatus.CREATE_FAIL;
                    } else {
                        return FileStatus.SOURCE_FILE_DELETE_FAIL;
                    }
                } else {
                    return FileStatus.NOT_DELETE;
                }
            }

            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                boolean mkdirs = parentFile.mkdirs();
                if (!mkdirs) {
                    return FileStatus.CREATE_PARENT_DIR_FAIL;
                }
            }

            return file.createNewFile() ? FileStatus.CREATE_SUCCESS : FileStatus.CREATE_FAIL;
        } catch (IOException e) {
            Logger.w(e.toString());
            return FileStatus.CREATE_FAIL;
        }
    }

    /**
     * 创建目录
     *
     * @return
     */
    public static FileStatus createDir(String absPath, boolean isDeleteIfExists) {
        File file = new File(absPath);

        if (file.exists()) {
            if (isDeleteIfExists) {
                FileStatus status = clearFileOrDir(absPath);
                if (status == FileStatus.DELETE_SUCCESS) {
                    boolean mkdirs = file.mkdirs();
                    return mkdirs ? FileStatus.CREATE_SUCCESS : FileStatus.CREATE_FAIL;
                } else {
                    return FileStatus.SOURCE_FILE_DELETE_FAIL;
                }
            } else {
                return FileStatus.NOT_DELETE;
            }
        }

        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            boolean mkdirs = parentFile.mkdirs();
            if (!mkdirs) {
                boolean parentMkDirs = parentFile.mkdirs();
                if (!parentMkDirs) {
                    return FileStatus.CREATE_PARENT_DIR_FAIL;
                }
            }
        }
        return file.mkdir() ? FileStatus.CREATE_SUCCESS : FileStatus.CREATE_FAIL;
    }

    /**
     * 删除文件 | 清空目录
     *
     * @param absPath
     * @return
     */
    public static FileStatus clearFileOrDir(String absPath) {
        File file = new File(absPath);
        if (file.exists()) {
            if (file.isFile()) {
                //文件
                return deleteFile(absPath);
            } else {
                //目录
                List<File> unDeletedFiles = clearDir(absPath);
                return unDeletedFiles.isEmpty() ? FileStatus.DELETE_SUCCESS : FileStatus.DELETE_FAIL;
            }
        }

        return FileStatus.NOT_EXISTS;
    }

    /**
     * 删除文件
     *
     * @param absPath
     * @return
     */
    public static FileStatus deleteFile(String absPath) {
        File file = new File(absPath);
        if (file.exists()) {
            if (file.isFile()) {
                return file.delete() ? FileStatus.DELETE_SUCCESS : FileStatus.DELETE_FAIL;
            } else {
                return FileStatus.IS_DIRECTORY;
            }
        }
        return FileStatus.NOT_EXISTS;
    }

    /**
     * 清空目录
     *
     * @param absDirPath
     * @return 未删除成功文件
     */
    public static List<File> clearDir(String absDirPath) {
        List<File> unDeletedFile = new ArrayList<>();

        File file = new File(absDirPath);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File innerFile = files[i];
                if (innerFile.isFile()) {
                    boolean delete = innerFile.delete();
                    if (!delete) {
                        unDeletedFile.add(innerFile);
                    }
                } else {
                    List<File> undeleted = clearDir(innerFile.getAbsolutePath());
                    if (null != undeleted && !undeleted.isEmpty()) {
                        unDeletedFile.addAll(undeleted);
                    }
                }
            }
        }
        return unDeletedFile;
    }

    /**
     * 复制文件
     *
     * @param sourceAbsPath          源文件
     * @param targetAbsPath          目标文件
     * @param isDeleteTargetIfExists 目标文件存在是否自动删除
     * @return
     */
    public static FileStatus copyFile(String sourceAbsPath, String targetAbsPath, boolean isDeleteTargetIfExists) {
        try {
            File sourceFile = new File(sourceAbsPath);
            File targetFile = new File(targetAbsPath);

            if ((sourceFile.exists() && sourceFile.isFile())) {
                if (targetFile.exists()) {
                    if (isDeleteTargetIfExists) {
                        FileStatus status = clearFileOrDir(targetAbsPath);
                        if (status == FileStatus.DELETE_SUCCESS) {
                            if (!targetFile.createNewFile()) {
                                return FileStatus.CREATE_FAIL;
                            }
                            return copyFileInner(sourceAbsPath, targetAbsPath);
                        } else {
                            return FileStatus.DELETE_FAIL;
                        }
                    } else {
                        return FileStatus.NOT_DELETE;
                    }
                }

                targetFile.createNewFile();
                return copyFileInner(sourceAbsPath, targetAbsPath);
            }

            return FileStatus.IS_DIRECTORY;
        } catch (IOException e) {
            e.printStackTrace();
            return FileStatus.COPY_FAIL;
        }
    }

    public static FileStatus copyFile(InputStream in, String targetAbsPath, boolean isDeleteTargetIfExists) {
        try {
            File targetFile = new File(targetAbsPath);
            if (targetFile.exists()) {
                if (isDeleteTargetIfExists) {
                    FileStatus status = clearFileOrDir(targetAbsPath);
                    if (status == FileStatus.DELETE_SUCCESS) {
                        if (!targetFile.createNewFile()) {
                            return FileStatus.CREATE_FAIL;
                        }
                        return copyFileInner(in, targetAbsPath);
                    } else {
                        return FileStatus.DELETE_FAIL;
                    }
                } else {
                    return FileStatus.NOT_DELETE;
                }
            }

            boolean createNewFile = targetFile.createNewFile();
            if (!createNewFile) {
                return FileStatus.CREATE_FAIL;
            }
            return copyFileInner(in, targetAbsPath);
        } catch (IOException e) {
            Logger.printStackTrace(e);
            return FileStatus.COPY_FAIL;
        }
    }

    /**
     * 剪切文件
     *
     * @param sourceAbsPath          源文件
     * @param targetAbsPath          目标文件
     * @param isDeleteTargetIfExists 目标文件存在是否自动删除
     * @return
     */
    public static FileStatus cutFile(String sourceAbsPath, String targetAbsPath, boolean isDeleteTargetIfExists) {
        FileStatus status = copyFile(sourceAbsPath, targetAbsPath, isDeleteTargetIfExists);
        switch (status) {
            case COPY_SUCCESS:
                //复制成功，将源文件删除。
                boolean delete = new File(sourceAbsPath).delete();
                if (!delete) {
                    //源文件删除失败，删除目标文件，回滚剪切操作，返回剪切失败。
                    boolean targetFileDeleted = new File(targetAbsPath).delete();
                    if (!targetFileDeleted) {
                        throw new RuntimeException("DeleteTargetFileFailWhenRollbackCutFileException " + targetAbsPath);
                    }
                    return FileStatus.CUT_FILE_FAIL;
                }
                return FileStatus.CUT_FILE_SUCCESS;

            default:
                return status;
        }
    }

    private static FileStatus copyFileInner(String sourceAbsPath, String targetAbsPath) {
        try {
            File sourceFile = new File(sourceAbsPath);
            FileInputStream fis = new FileInputStream(sourceFile);
            return copyFileInner(fis, targetAbsPath);
        } catch (IOException e) {
            Logger.printStackTrace(e);
            return FileStatus.COPY_FAIL;
        }
    }

    private static FileStatus copyFileInner(InputStream in, String targetAbsPath) {
        try {
            File targetFile = new File(targetAbsPath);
            FileOutputStream fos = new FileOutputStream(targetFile);
            byte[] buffer = new byte[2048];
            while (in.read(buffer) != -1) {
                fos.write(buffer, 0, buffer.length);
            }

            in.close();
            fos.close();
            return FileStatus.COPY_SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return FileStatus.COPY_FAIL;
        }
    }
}
