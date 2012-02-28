package controllers;

import helper.EventTimeHelper;
import helper.LogHelper;
import java.io.File;
import models.Log;
import play.*;
import play.libs.Codec;
import play.mvc.*;
import play.libs.IO;

public class Application extends Controller {

    public static void index() {
        EventTimeHelper.getEventTimeFromString("[29/Sep/2011:01:21:45 +0200]");
        EventTimeHelper.getEventTimeFromString("Sep 29 01:52:41");
        EventTimeHelper.getEventTimeFromString("Sep 28 06:29:38");
        EventTimeHelper.getEventTimeFromString("[31/May/2010:17:05:18 +0200]");
        EventTimeHelper.getEventTimeFromString("[12/Apr/2009:23:21:45 +0200]");
        EventTimeHelper.getEventTimeFromString("Jan 30 01:52:41");
        EventTimeHelper.getEventTimeFromString("Feb 01 06:29:38");
        EventTimeHelper.getEventTimeFromString("[31/May/2010:17:05:18 +0200]");
        render();
    }

    public static void read(File log) {
        if (log != null) {
            Logger.info("tmp file: " + log.getAbsolutePath());
            String content = IO.readContentAsString(log);
            Log logO = Log.findOrCreateByChecksum(Codec.hexSHA1(content));
            if (!logO.reloaded) {
                logO.name = log.getName();
                LogHelper.parseLog(content, logO);
                //Logger.info("vcard content: \n\n" + content + "\n\n");
            }

        }
        index();
    }
}
