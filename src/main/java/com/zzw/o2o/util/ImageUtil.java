package com.zzw.o2o.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.zzw.o2o.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;

import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;

public class ImageUtil {
	private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final Random random = new Random();


	public static String generateThumbnails(ImageHolder imageHolder, String destPath) {
		// 拼接后的新文件的相对路径
		String relativeAddr = null;
		try {
			// 1.为了防止图片的重名，不采用用户上传的文件名，系统内部采用随机命名的方式
			String randomFileName = generateRandomFileName();
			// 2.获取用户上传的文件的扩展名,用于拼接新的文件名
			String fileExtensionName = getFileExtensionName(imageHolder.getFileName());
			// 3.校验目标目录是否存在，不存在创建目录
			makeDirPath(destPath);
			// 4.拼接新的文件名
			relativeAddr = destPath + randomFileName + fileExtensionName; //打上水印图片的输出的相对路径
			logger.info("图片相对路径 {}", relativeAddr);
			// 绝对路径的形式创建文件
			String basePath = FileUtil.getImgBasePath(); //"F:\\javaCode\\OfferProduct\\xioayuanshangpu\\Image\\win\\";

			File destFile = new File(basePath + relativeAddr);  //打上水印图片的输出的绝对路径

			logger.info("图片完整路径 {}", destFile.getAbsolutePath());
			// 5.给源文件加水印后输出到目标文件
			Thumbnails.of(imageHolder.getIns()).size(500, 500).watermark(Positions.BOTTOM_RIGHT,
					ImageIO.read(FileUtil.getWaterMarkFile()), 0.25f).outputQuality(0.1f).toFile(destFile);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("创建水印图片失败：" + e.toString());
		}
		return relativeAddr;
	}


	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = FileUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	private static String getFileExtensionName(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf("."));
		logger.debug("extension : {}", extension);
		return extension;
	}

	public static String generateRandomFileName() {
		String sysdate = sdf.format(new Date());
		// 5位随机数 10000到99999之间 ,下面的取值[ 包括左边，不包括右边 ]，满足10000到99999
		int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;
		String randomFileName = sysdate + rannum;
		logger.debug("fileName: {}", randomFileName);
		return randomFileName;
	}

	/**
	 * storePath是文件的路径还是目录路径
	 * 如果storePath是文件路径则删除该文件
	 * 如果storePath是目录路径则删除该目录下的所有文件
	 * @param storePath
	 */
	public static void deleteFileOrPath(String storePath){
		File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);

		if(fileOrPath.exists()) {
			if (fileOrPath.isDirectory()) {
				File files[] = fileOrPath.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
		}
		fileOrPath.delete();
	}

	public static List<String> generateNormalImgs(List<ImageHolder> prodImgDetailList, String relativePath){
		int count = 0;
		List<String> relativeAddrList = new ArrayList<>();
		if(prodImgDetailList != null && prodImgDetailList.size() > 0){
			makeDirPath(relativePath);
			for(ImageHolder imageHolder : prodImgDetailList){
				//
				String randomFileName = generateRandomFileName();

				String fileExtensionName = getFileExtensionName(imageHolder.getFileName());
				//
				String relativeAddr = relativePath + randomFileName + count + fileExtensionName;
				logger.info("图片相对路径 {}", relativeAddr);
				count++;
				//
				String basePath = FileUtil.getImgBasePath();
				File destFile = new File(basePath + relativeAddr);
				logger.info("图片完整路径 {}", destFile.getAbsolutePath());

				try {
					//
					Thumbnails.of(imageHolder.getIns()).size(337, 640).outputQuality(0.5).toFile(destFile);
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException("创建图片失败: " + e.toString());
				}
				relativeAddrList.add(relativeAddr);
			}
		}
		return relativeAddrList;
	}


	public static void main(String[] args) throws IOException{
		String destPath = "F:\\javaCode\\OfferProduct\\xioayuanshangpu\\Image\\win\\heizhende.png";
		File destFile = new File(destPath);

		Thumbnails.of(new File("F:\\javaCode\\imageTest\\/zhende.png")).
				size(200,200).watermark(Positions.BOTTOM_RIGHT,
				ImageIO.read(FileUtil.getWaterMarkFile()), 0.25f).outputQuality(0.1f)
				.toFile(destFile);

		/*Thumbnails.of(new File("F:\\javaCode\\imageTest\\/zhende.png")).
				size(200,200).watermark(Positions.BOTTOM_RIGHT,
				ImageIO.read(FileUtil.getWaterMarkFile()), 0.25f).outputQuality(0.1f)
		.toFile("F:\\javaCode\\OfferProduct\\xioayuanshangpu\\Image\\win\\heizhende.png");*/
	}


}
