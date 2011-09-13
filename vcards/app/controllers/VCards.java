/**
 * VCards
 * 14.09.2011
 * @author Philipp Haußleiter
 *
 */
package controllers;

import java.io.File;
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
        render(content);
    }

    public static void index() {
        render();
    }

    public static void form() {
        render();
    }
}
