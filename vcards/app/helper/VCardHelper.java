/**
 * VCardHelper
 * 14.09.2011
 * @author Philipp Haußleiter
 *
 */
package helper;

import java.util.ArrayList;
import java.util.List;
import models.Organisation;
import models.VCard;

public class VCardHelper {

    /**
     * URL;type=WORK;type=pref:http\://www.example.com
     * parts = ["URL;type=WORK;type=pref", "http\", "//www.example.com"]
     * @param parts
     * @return
     */
    private static String[] fixForUrl(String line) {
        line = line.replace("://", "####");
        String parts[] = line.split(":");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].replace("####", "://").replace("\\:", ":");
        }
        return parts;
    }
    private static String fixForItems(String line){
        String parts[] =  line.split(".");
        if(parts.length > 1){
            return parts[1];
        }
        return "";
    }

    public enum KEYS {
        VERSION, N, FN, NICKNAME, CATEGORIES, ORG, XABSHOWAS, TITLE, NOTE, SORTSTRING, CLASS, BDAY, PRODID, UID, XABADR, ADR
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

    private static String getFullname(String content){
        String parts[];
        for (String line : content.split("\n")) {
            if (line.startsWith("FN")) {
                parts = line.split(":");
                return parts[1];
            }
        }
        return "";
    }

    private static String getBirthday(String content){
        String parts[];
        for (String line : content.split("\n")) {
            if (line.startsWith("BDAY")) {
                parts = line.split(":");
                return parts[1];
            }
        }
        return "";
    }

    public static String checkGetEncoding(String content) {
        for (String line : content.split("\n")) {
            if (line.contains("CHARSET=")) {
                String parts[] = line.split("CHARSET=");
                parts = parts[1].split(":");
                return parts[0].trim();
            }
        }
        return "UTF-8";
    }

    public static VCard createVCWithContent(String content) {
        String fullName = getFullname(content);
        String birthday = getBirthday(content);
        VCard card = VCard.findByFullNameAndBirthday(fullName, birthday);
        if (card == null) {
            card = new VCard();
            card.birthday = birthday;
            card.fullName = fullName;
        }
        String[] lines = content.split("\n");
        String[] parts = null;
        String value;
        for (String line : lines) {
            if (line.startsWith("PHOTO;")) {
                parts = content.split("PHOTO;");
                card = PhotoHelper.addPhoto("PHOTO;"+parts[1], card);
            } else if (!line.startsWith("X-ABUID:")
                    && !line.startsWith("UID")
                    && !line.startsWith("BDAY")
                    && !line.startsWith("END:VCARD")) {
                if(line.startsWith("item")){
                    line = fixForItems(line);
                }
                parts = line.split(":");
                if (line.startsWith("URL")) {
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
                            value = parts[1].substring(0, parts[1].length() - 1);
                            card.organisation = Organisation.findOrCreatyByName(value);
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
                            value = "value=access:" + parts[1];
                            card.addContact("CLASS", value);
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
