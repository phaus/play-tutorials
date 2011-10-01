/**
 * Event
 * 29.09.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import play.db.jpa.Model;
import play.libs.Codec;

@Entity
public class Event extends Model {

    @ManyToOne
    public Log log;

    @ManyToOne
    public EventTime time;

    @ManyToOne
    public LogEventType type;

    @ManyToOne
    public EventApp app;

    @Lob
    public String message;

    public String checkSum;
    
    public Event(){
        
    }

    public static Event findOrCreateByMessage(String message){
        Event event;
        event = Event.find(" checkSum = ?", Codec.hexSHA1(message)).first();
        if(event == null){
            event = new Event();
            event.setMessage(message);
        }
        return event;
    }

    public void setMessage(String message){
        this.message = message;
        this.checkSum = Codec.hexSHA1(message);
    }

    public void addTime(EventTime time){
        this.time = time;
    }

    public void addType(LogEventType type){
        this.type = type;
    }

    public void addApp(EventApp app){
        this.app = app;
    }
}
