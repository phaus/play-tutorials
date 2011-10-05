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
                return fixEncoding(parts[1]);
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

    private static String fixEncoding(String value){
        String out = value;
        String find[] = {"Ž", "Š", "š", "ź"};
        String replace[] = {"é", "ä", "ö", "ü"};
        for(int i=0; i < find.length; i++){
            out = out.replace(find[i], replace[i]);
        }
        return out;
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
                    value = fixEncoding(parts[1]);
                    //card.values.put(parts[0], parts[1]);
                    switch (KEYS.valueOf(parts[0].replace("-", "").toUpperCase())) {
                        case N:
                            card.name = value;
                            break;
                        case FN:
                            card.fullName = value;
                            break;
                        case NICKNAME:
                            card.nickName = value;
                            break;
                        case CATEGORIES:
                            card.save();
                            card.addCategories(value);
                            break;
                        case ORG:
                            value = value.substring(0, value.length() - 1);
                            card.organisation = Organisation.findOrCreatyByName(value);
                            break;
                        case SORTSTRING:
                            card.nickName = value;
                            break;
                        case NOTE:
                            card.note = value;
                            break;
                        case TITLE:
                            card.title = value;
                            break;
                        case XABSHOWAS:
                            card.showAs = value;
                            break;
                        case CLASS:
                            value = "value=access:" + value;
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
