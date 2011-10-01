/**
 * LogHelper
 * 01.10.2011
 * @author Philipp Haußleiter
 *
 */
package helper;

import models.Event;
import models.EventApp;
import models.Host;
import models.Log;
import models.LogEventType;
import models.LogType;
import play.Logger;

public class LogHelper {
    public static void parseLog(String content, Log log){
        if(content.startsWith("/usr/sbin/mysqld")){
            parseMysql(content, log);
        }
        if(content.contains("postfix") ||
            content.contains("dovecot") ||
            content.contains("postgrey")){
            parseMail(content, log);
        }
        if(content.contains("GET /") || content.contains("POST /")){
            parseHttp(content, log);
        }
    }


    private static void parseMysql(String content, Log log){
        Logger.info("parsing MySql");
        log.type = LogType.findOrCreateByLabel("Database");
        log.save();
    }

    private static void parseMySqlEvent(String content, Log log){
    }

    private static void parseMail(String content, Log log){
        Logger.info("parsing Mail");
        log.type = LogType.findOrCreateByLabel("Mail");
        log.save();
        parseMailEvents(content, log);
    }

    private static Log parseMailEvents(String content, Log log){
        String p1[];
        Event event;
        int i=0;
        for(String line : content.split("\n")){
            p1 = line.split(" ");
            if(p1.length > 7){
                event = Event.findOrCreateByMessage(line);
                // Sep 30 06:39:02
                event.addTime(EventTimeHelper.getEventTimeFromString(p1[0]+" "+p1[1]+" "+p1[2]));
                event.addType(LogEventType.findOrCreateByLabel(p1[5].replace(":", "")));
                event.addApp(EventApp.findOrCreateByLabel(getMailEventApp(p1[4])));
                event.message = line;
                log.addEvent(event);
            }
            if(i==0){
                log.host = Host.findOrCreateByHostname(p1[3]);
                log.save();
            }
            i++;
        }
        return log;
    }

    private static String getMailEventApp(String app){
        app = app.replace(":", "");
        int i = app.indexOf("[");
        if(i > 0){
            return app.substring(0, i);
        }
        return app;
    }

    private static void parseHttp(String content, Log log) {
        Logger.info("parsing Http");
        log.type = LogType.findOrCreateByLabel("Http");
        log.save();
    }
}
