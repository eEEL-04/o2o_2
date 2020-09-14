package com.jhc.o2o.util;

import com.jhc.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {

    //private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static String basePath = "E:/image/watermark";
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random random = new Random();
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    public static void printBasePath() {
        System.out.println("BasePath:" + basePath);
    }

//    /**
//     * 将CommonsMultipartFile转换成File
//     *
//     * @param cFile
//     * @return
//     */
//    public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
//        File newFile = new File(cFile.getOriginalFilename());
//        try {
//            cFile.transferTo(newFile);
//        } catch (IOException e) {
//            logger.error(e.toString());
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            logger.error(e.toString());
//            e.printStackTrace();
//        }
//        return newFile;
//    }

    /**
     * 处理缩略图 并返回新生成图片的相对值路径
     *
     * @param thumbnail
     * @param targetAddr
     * @return
     */
    public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is:" + relativeAddr);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is:" + PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail.getImage())
                    .size(200, 200).watermark(Positions.TOP_LEFT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.5f).outputQuality(0.8f)
                    .toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
            /*throw new RuntimeException("图片未找到");*/
        }
        return relativeAddr;
    }

    /**
     * 处理缩略图 并返回新生成图片的相对值路径
     *
     * @param thumbnail
     * @param targetAddr
     * @return
     */
    public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is:" + relativeAddr);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        logger.debug("current complete addr is:" + PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail.getImage())
                    .size(337, 640).watermark(Positions.TOP_LEFT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.5f).outputQuality(0.9f)
                    .toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
            /*throw new RuntimeException("图片未找到");*/
        }
        return relativeAddr;
    }

    /**
     * storePath是文件的路径还是目录的路径
     * 如果storePath是文件的路径则删除该文件
     * 如果storePath是目录的路径则删除该目录下的所有文件
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath){
        File fileOrPath = new File(PathUtil.getImgBasePath()+storePath);
        if (fileOrPath.exists()){
            if (fileOrPath.isDirectory()){
                File files[] = fileOrPath.listFiles();
                for (File file : files) {
                    file.delete();
                }
            }
            fileOrPath.delete();
        }
    }


    /**
     * 创建目标路径所涉及到的目录
     *
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    /**
     * 获取输入文件流的扩展名
     *
     * @param fileName
     * @return
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成随机文件名，当前年月日小时分钟秒钟+五位随机数
     *
     * @return
     */
    public static String getRandomFileName() {
        //获取随机五位数
        int ranNum = random.nextInt(89999) + 10000;
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr + ranNum;
    }


    public static void main(String[] args) throws IOException {
        Thumbnails.of(new File("E:/image/xiaohuangren.jpg"))
                .size(500, 500).watermark(Positions.TOP_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.5f).outputQuality(0.8f)
                .toFile("E:/image/xiaohuangrenNew2.jpg");

        /*printBasePath();*/
    }

}
