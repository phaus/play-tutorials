/**
 * Mails
 * 03.10.2011
 * @author Philipp Haußleiter
 *
 */
package notifiers;

import java.util.Random;
import models.User;
import play.mvc.Mailer;

public class Mails extends Mailer {


    public static void register(){

    }
    
    public static void welcome(User user){
      setSubject("Welcome %s %s", user.firstname, user.givenname);
      addRecipient(user.email);
      setFrom("Robot consolving.de <robot@consolving.de>");
      //EmailAttachment attachment = new EmailAttachment();
      //attachment.setDescription("A pdf document");
      //attachment.setPath(Play.getFile("rules.pdf").getPath());
      //addAttachment(attachment);
      send(user);
    }

    public static void lostPassword(User user){
      String newpassword = "PW"+new Random().nextInt(123456789);
      user.password = newpassword;
      user.save();
      setFrom("Robot consolving.de <robot@consolving.de>");
      setSubject("Your password has been reset");
      addRecipient(user.email);
      send(user, newpassword);
    }
}
