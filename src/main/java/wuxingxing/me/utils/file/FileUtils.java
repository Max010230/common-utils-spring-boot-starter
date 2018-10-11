package wuxingxing.me.utils.file;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author wll
 */
public class FileUtils {

    static Pattern p = Pattern.compile("\\s*|\t|\r|\n");

    /**
     * 从网络链接下载对账单
     * @param urlStr
     * @param fileName 2017-10-10.zip
     * @param savePath D:/aliBill/
     * @throws IOException
     */
    public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);
        //文件保存位置
        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdir();
        }
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        System.out.println("info:" + url + " download success");
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * @param zipPath    zip路径
     * @param charset    编码 GBK
     * @param outPutPath 输出路径 D:/aliBill/2017-10-10/
     * @Description (解压)
     */
    public static void unzip(String zipPath, String charset, String outPutPath) {
        long startTime = System.currentTimeMillis();
        try {
            //输入源zip路径
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath), Charset.forName(charset));
            BufferedInputStream bin = new BufferedInputStream(zin);
            //输出路径（文件夹目录）
            String parent = outPutPath;
            File fout = null;
            ZipEntry entry;
            try {
                while ((entry = zin.getNextEntry()) != null && !entry.isDirectory()) {
                    fout = new File(parent, entry.getName());
                    if (!fout.exists()) {
                        (new File(fout.getParent())).mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(fout);
                    BufferedOutputStream bout = new BufferedOutputStream(out);
                    int b;
                    while ((b = bin.read()) != -1) {
                        bout.write(b);
                    }
                    bout.close();
                    out.close();
                    System.out.println(fout + "解压成功");
                }
                bin.close();
                zin.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("耗费时间： " + (endTime - startTime) + " ms");
    }

    /**
     * 创建文件
     * @param fileName    文件名称
     * @param filecontent 文件内容
     * @return 是否创建成功，成功则返回true
     */
    public static boolean createFile(String fileName, String filecontent) {
        Boolean status = false;
        try {
            File file = new File(System.getProperty("user.home") + "/" + fileName);// if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(filecontent);
            bw.close();
            status = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * 去除制表符
     * @param strings
     * @return
     */
    public static String[] replaceLt(String[] strings) {
        String[] newStr = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {

            Matcher m = p.matcher(strings[i]);
            newStr[i] = m.replaceAll("");
        }
        return newStr;
    }

    /**
     * 去掉双引号
     * @param strings
     * @return
     */
    public static String[] replaceStr(String[] strings) {
        String[] newStr = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].contains("\"")) {
                strings[i] = strings[i].replace("\"", "");
            }
            newStr[i] = strings[i].trim();
        }
        return newStr;
    }

    public static String getUserHome() {
        return System.getProperty("user.home");
    }
}
