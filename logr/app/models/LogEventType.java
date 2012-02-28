/**
 * EventType
 * 29.09.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import play.cache.Cache;
import play.db.jpa.Model;

@Entity
public class LogEventType extends Model {

    @OneToMany(mappedBy = "type")
    public List<Event> events;

    public String label;


    public LogEventType(){
        this.events = new ArrayList<Event>();
    }

    public static LogEventType findOrCreateByLabel(String label){
        String key = "EventType-"+label;
        LogEventType eventtype = Cache.get(key, LogEventType.class);
        if(eventtype != null){
            return eventtype;
        }
        eventtype = LogEventType.find(" label = ?", label).first();
        if(eventtype == null){
            eventtype = new LogEventType();
            eventtype.label = label;
            eventtype.save();
            Cache.set(key, eventtype, "1d");
        }
        return eventtype;
    }
}
