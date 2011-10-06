/**
 * PhotoHelper
 * 27.09.2011
 * @author Philipp Haußleiter
 *
 */
package helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import models.VCard;
import play.Logger;
import play.Play;
import play.db.jpa.Blob;
import play.libs.Codec;
import play.libs.MimeTypes;

public class PhotoHelper {

    private static File getPhotoFromBase64(String content) {
        File photo = null;
        String base64 = "";
        try {
            String[] parts = content.split("\n");
            for (String part : parts) {
                if (part.startsWith("  ")) {
                    base64 += part.trim();
                }
            }
            photo = new File(Play.tmpDir+"/"+System.currentTimeMillis()+".png");
            if (!photo.exists()) {
                photo.createNewFile();
            }
            byte[] buf = Codec.decodeBASE64(base64);
            FileOutputStream osf = new FileOutputStream(photo);
            osf.write(buf);
            osf.flush();
        } catch (IOException ex) {
            Logger.info(ex, ex.getLocalizedMessage());
        }
        return photo;
    }

    //TYPE=JPEG:http://www.example.com/xyz
    private static File getPhotoFromUrl(String content) {
        File photo = null;
        try {
            String urlString = "";
            FileOutputStream os = null;
            InputStream is = null;
            int len1 = 0;
            String[] parts = content.split(":");
            photo = new File(Play.tmpDir+"/"+System.currentTimeMillis());
            if (!photo.exists()) {
                photo.createNewFile();
            }
            String ext = parts[0].replace("TYPE=", "").toLowerCase();
            for (int i = 1; i < parts.length; i++) {
                urlString += parts[i] + ":";
            }
            parts = urlString.split("\n");
            urlString = parts[0];
            URL url = new URL(urlString);
            is = url.openStream();
            os = new FileOutputStream(photo);
            byte[] buff = new byte[4096];
            while (-1 != (len1 = is.read(buff))) {
                os.write(buff, 0, len1);
            }
            os.flush();
            Logger.info("url: " + url + " ext: " + ext);
            return photo;
        } catch (MalformedURLException ex) {
            Logger.info(ex, ex.getLocalizedMessage());
        } catch (IOException ex) {
            Logger.info(ex, ex.getLocalizedMessage());
        }
        return photo;
    }

    public static VCard addPhoto(String content, VCard card) {
        String[] parts = null;
        File photo = null;
        for (String line : content.split("\n")) {
            if (line.startsWith("PHOTO;BASE64:")) {
                parts = content.split("PHOTO;BASE64:");
                photo = getPhotoFromBase64(parts[1]);
            }
            if (line.startsWith("PHOTO;VALUE=URL;")) {
                parts = content.split("PHOTO;VALUE=URL;");
                photo = getPhotoFromUrl(parts[1]);
            }
            if(photo != null){
                try {
                    card.photo = new Blob();
                    card.photo.set(new FileInputStream(photo), MimeTypes.getContentType(photo.getName()));
                    card.save();
                } catch (FileNotFoundException ex) {
                    Logger.info(ex, ex.getLocalizedMessage());
                }
            }
        }
        return card;
    }
}
