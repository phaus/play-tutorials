/**
 * VCardHelper
 * 14.09.2011
 * @author Philipp Haußleiter
 *
 */
package helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import models.VCard;

public class VCardHelper {
    /**
     * URL;type=WORK;type=pref:http\://www.example.com
     * parts = ["URL;type=WORK;type=pref", "http\", "//www.example.com"]
     * @param parts
     * @return
     */
    private static String[] fixForUrl(String line) {
        line = line.replace("\\:", "####");
        String parts[] = line.split(":");
        for(int i=0; i < parts.length; i++){
            parts[i] = parts[i].replace("####", ":");
        }
        return parts;
    }

    public enum KEYS {

        VERSION, N, FN, NICKNAME, CATEGORIES, ORG, XABSHOWAS, TITLE, NOTE, SORTSTRING, CLASS, BDAY, PRODID, UID
    };

    public static List<VCard> createVCsWithContent(String content) {
        List<VCard> cards = new ArrayList<VCard>();
        String lines[] = content.split("BEGIN:VCARD");
        for (String line : lines) {
            if (!line.isEmpty()) {
                cards.add(createVCWithContent(line));
            }
        }
        return cards;
    }

    private static File createAvatarFromContent(String content) {
        String base64 = "";
        String[] parts = content.split("\n");
        for (String part : parts) {
            if (part.startsWith("  ")) {
                base64 += part.trim();
            }
        }
        //Logger.info("avatar base64: " + base64);
        return null;
    }

    private static String getUid(String content) {
        for (String line : content.split("\n")) {
            if (line.startsWith("X-ABUID:")) {
                return line.replace("X-ABUID:", "");
            }
            if (line.startsWith("UID:")) {
                return line.replace("UID:", "");
            }
        }
        return "";
    }

    public static String checkGetEncoding(String content){
        for(String line : content.split("\n")){
            if(line.contains("CHARSET=")){
                String parts[] = line.split("CHARSET=");
                parts = parts[1].split(":");
                return parts[0].trim();
            }
        }
        return "UTF-8";
    }
    
    public static VCard createVCWithContent(String content) {
        String uid = getUid(content);
        VCard card = VCard.findByUid(uid);
        if (card == null) {
            card = new VCard();
            card.uid = uid;
        }
        String[] lines = content.split("\n");
        String[] parts = null;
        for (String line : lines) {
            if (line.startsWith("PHOTO;BASE64:")) {
                parts = content.split("PHOTO;BASE64:");
                card.photo = createAvatarFromContent(parts[1]);
            } else if (!line.startsWith("X-ABUID:")
                    && !line.startsWith("UID")
                    && !line.startsWith("END:VCARD")
                    && !line.startsWith("item")) {

                parts = line.split(":");
                if(line.startsWith("URL")){
                    parts = fixForUrl(line);
                }
                if (parts.length > 1 && parts[0].split(";").length > 1) {
                    card.save();
                    card.addContact(parts[0], parts[1]);
                } else if (parts.length > 1) {
                    //card.values.put(parts[0], parts[1]);
                    switch (KEYS.valueOf(parts[0].replace("-", "").toUpperCase())) {
                        case N:
                            card.name = parts[1];
                            break;
                        case FN:
                            card.fullName = parts[1];
                            break;
                        case NICKNAME:
                            card.nickName = parts[1];
                            break;
                        case CATEGORIES:
                            card.save();
                            card.addCategories(parts[1]);
                            break;
                        case ORG:
                            card.organisation = parts[1].substring(0, parts[1].length() - 1);
                            break;
                        case SORTSTRING:
                            card.nickName = parts[1];
                            break;
                        case NOTE:
                            card.note = parts[1];
                            break;
                        case TITLE:
                            card.title = parts[1];
                            break;
                        case XABSHOWAS:
                            card.showAs = parts[1];
                            break;
                        case CLASS:
                            card.addContact("CLASS", "value=access:"+parts[1]);
                            break;
                        case BDAY:
                            card.addContact("BDAY", "value=date:"+parts[1]);
                            break;
                        default:
                    }
                }
            }
        }
        card.save();
        return card;
    }

    public static int countVCards(String content) {
        return content.split("BEGIN:VCARD").length - 1;
    }
}
