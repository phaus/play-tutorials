/**
 * Log
 * 01.10.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import play.db.jpa.Model;

@Entity
public class Log extends Model {

    public String name;

    @OneToMany(mappedBy = "log")
    public List<Event> events;

    @ManyToOne
    public Host host;

    @ManyToOne
    public LogType type;

    public String checkSum;

    @Transient
    public boolean reloaded;

    public static Log findOrCreateByChecksum(String checkSum){
        Log log = Log.find(" checkSum = ? ", checkSum).first();
        if(log == null){
            log = new Log();
            log.checkSum = checkSum;
            log.reloaded = false;
        } else {
            log.reloaded = true;
        }
        return log;
    }

    public Log(){
        this.events = new ArrayList<Event>();
    }
    
    public void addEvent(Event event){
        event.log = this;
        event.save();
    }
    // TODO needs to be more dynamic.
    public String getEventType(){
        if(name.contains("warning") || name.contains("warn")){
            return "warning";
        }
        if(name.contains("info")){
            return "info";
        }
        if(name.contains("error") || name.contains("err")){
            return "error";
        }
        if(name.contains("access")){
            return "access";
        }
        return null;
    }

}
