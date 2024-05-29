package springbootdeveloper.common;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

public class QRUtil {
    public static String MakeQR(String content, String filePath) {

        String fileName = "";

        try {
            File file = new File(content); //저장할 파일경로 객체 생성
            if (!file.exists()) { //파일 경로가 존재하지 않으면 폴더 생성
                file.mkdir();
            }

            //QR코드 그리기
            QRCodeWriter qr = new QRCodeWriter();

            BitMatrix bitMatrix = qr.encode(content, BarcodeFormat.QR_CODE, 200, 200);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            fileName = UUID.randomUUID().toString() + ".png"; //랜덤 이름값 부여

            ImageIO.write(bufferedImage, "png", new File(filePath + fileName));//.png이미지 파일로 생성

        } catch (Exception e) {
//			log.info(e.getMessage());
            e.printStackTrace();
        }
        return fileName;
    }

}
