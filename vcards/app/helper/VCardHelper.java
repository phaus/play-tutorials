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
import models.Contact;
import models.VCard;
import play.Logger;

public class VCardHelper {

    public enum KEYS {
        VERSION, N, FN, NICKNAME, CATEGORIES, ORG, XABSHOWAS
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
        }
        return "";
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
            } else if(!line.startsWith("X-ABUID:")
                    && !line.startsWith("END:VCARD")
                    && !line.startsWith("item")){

                parts = line.split(":");
                if (parts.length > 1 && parts[0].split(";").length > 1) {
                    card.save();
                    card = addContact(card, parts[0], parts[1]);
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
                            card.addCategories(parts[1]);
                            break;
                        case ORG:
                            card.organisation = parts[1];
                            break;
                        case XABSHOWAS:
                            card.showAs = parts[1];
                            break;
                        default:
                    }
                }
            }
        }
        card.save();
        return card;
    }

    public static VCard addContact(VCard card, String key, String value) {
        String keyParts[] = key.split(";");
        String keyValue[] = null;
        Contact contact = new Contact();
        contact.value = value;
        contact.save();
        for (String part : keyParts) {
            keyValue = part.split("=");
            if (keyValue.length > 1) {
                contact.addType(keyValue[1]);
            } else {
                contact.label = part;
            }
        }
        contact.save();
        card.addContact(contact);
        return card;
    }

    public static int countVCards(String content) {
        return content.split("BEGIN:VCARD").length - 1;
    }
}
