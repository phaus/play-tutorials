/**
 * UserHelper
 * 30.06.2011
 * @author Philipp Haußleiter
 *
 */
package helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import models.VCard;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.libs.Codec;
import play.libs.IO;

public class UserHelper {

    public static final int AVATAR_MODE_VCARD = 1;
    public static final int AVATAR_MODE_LDIF = 2;
    private static final String READER_USER = Play.configuration.getProperty("reader.user");
    private static final String READER_PASS = Play.configuration.getProperty("reader.pass");

    /**
     * Loads an user avatar from the gravatar.com WebServices.
     * @param user the user of the avatar.
     * @return the gravatars avatar.
     */
    public static File getGravatar(final VCard card) {
        if (card.getMail() == null) {
            return Play.getFile("public/images/no_user.png");
        }
        File gravatar = null;
        FileOutputStream os = null;
        InputStream is = null;
        try {
            StringBuilder urlString = new StringBuilder("http://gravatar.com/avatar/");
            urlString.append(Codec.hexMD5(card.getMail()));
            urlString.append("?s=69&d=wavatar");
            Logger.debug("loading gravatar " + urlString.toString());
            URL url = new URL(urlString.toString());
            int len1 = 0;
            gravatar = new File(Play.tmpDir + "/avatars/" + card.name + ".png");
            if (!gravatar.exists()) {
                gravatar.createNewFile();
            }
            is = url.openStream();
            os = new FileOutputStream(gravatar);
            byte[] buff = new byte[4096];
            while (-1 != (len1 = is.read(buff))) {
                os.write(buff, 0, len1);
            }
            os.flush();
        } catch (FileNotFoundException ex) {
            Logger.warn("FileNotFoundException", ex.getLocalizedMessage());
        } catch (IOException ex) {
            Logger.warn("IOException", ex.getLocalizedMessage());
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.warn("IOException", ex.getLocalizedMessage());
            }
            try {
                is.close();
            } catch (IOException ex) {
                Logger.warn("IOException", ex.getLocalizedMessage());
            }

        }
        Logger.info("Loading Gravatar for User " + card.name);
        return gravatar;
    }

    // TODO needs to be optimized
    //card.getMail()
    public static File getAvatar(final VCard card) {
        File avatar = card.photo.getFile();
        if (avatar != null) {
            return avatar;
        }
        avatar = Cache.get("avatar_" + card.name, File.class);
        if (avatar == null) {
            avatar = new File(Play.tmpDir + "/avatars/" + card.name + ".png");
        }
        if (!avatar.exists()) {
            avatar = UserHelper.getGravatar(card);
            if (avatar == null) {
                avatar = Play.getFile("public/images/no_user.png");
            }
            Cache.set("avatar_" + card.name, avatar, "720mn");
        }
        return avatar;
    }

    public static String getAvatarAsBase64(final VCard card, final int mode) {
        String photoHash = null;
        if (mode == AVATAR_MODE_VCARD) {
            photoHash = Cache.get("avatar_hash_vcard_" + card.name, String.class);
        }
        if (mode == AVATAR_MODE_LDIF) {
            photoHash = Cache.get("avatar_hash_ldif_" + card.name, String.class);
        }
        if (photoHash != null) {
            return photoHash;
        }

        File avatar = Cache.get("avatar_" + card.name, File.class);
        if (avatar == null) {
            avatar = card.photo.getFile();
        }
        if (avatar == null) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        try {
            photoHash = Codec.encodeBASE64(IO.readContent(avatar));
            if (mode == AVATAR_MODE_VCARD) {
                for (int i = 0; i < photoHash.length(); i++) {
                    if (i % 76 == 0) {
                        out.append("\r\n  ");
                    }
                    out.append(photoHash.charAt(i));
                }
                Cache.set("avatar_hash_vcard_" + card.name, out.toString());
            }
            if (mode == AVATAR_MODE_LDIF) {
                int i = 0;
                for (; i < 64; i++) {
                    out.append(photoHash.charAt(i));
                }
                for (; i < photoHash.length(); i++) {
                    if ((i - 64) % 75 == 0) {
                        out.append("\n ");
                    }
                    out.append(photoHash.charAt(i));
                }
                Cache.set("avatar_hash_ldif_" + card.name, out.toString());
            }
        } catch (Exception e) {
            Logger.warn("Exception: " + e.getLocalizedMessage());
        }
        return out.toString();
    }
}
