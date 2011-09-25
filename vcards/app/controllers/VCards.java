/**
 * VCards
 * 14.09.2011
 * @author Philipp Haußleiter
 *
 */
package controllers;

import helper.VCardHelper;
import java.io.File;
import java.util.List;
import models.VCard;
import play.Logger;
import play.libs.IO;
import play.mvc.Controller;

public class VCards extends Controller {

    public static void save(File vcard) {
        Logger.info("tmp file: "+vcard.getAbsolutePath());
        String content = IO.readContentAsString(vcard);
        Logger.info("vcard content: \n\n"+content+"\n\n");
    }

    public static void read(File vcard){
        Logger.info("tmp file: "+vcard.getAbsolutePath());
        String content = IO.readContentAsString(vcard);
        String encoding = VCardHelper.checkGetEncoding(content);
        if(!encoding.equals("UTF-8")){
            Logger.info("encoding is: "+encoding);
            content = IO.readContentAsString(vcard, encoding);
        }
        int count = VCardHelper.countVCards(content);
        List<VCard> cards = VCardHelper.createVCsWithContent(content);
        render(cards, content, count);
    }

    public static void index() {
        List<VCard> cards = VCard.findAll();
        render(cards);
    }

    public static void form() {
        render();
    }
}
