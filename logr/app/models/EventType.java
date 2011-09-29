/**
 * EventType
 * 29.09.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import play.cache.Cache;
import play.db.jpa.Model;

@Entity
public class EventType extends Model {

    @OneToMany(mappedBy = "type")
    public List<Event> events;

    public String label;


    public static EventType findOrCreateByLabel(String label){
        String key = "EventType-"+label;
        EventType eventtype = Cache.get(key, EventType.class);
        if(eventtype != null){
            return eventtype;
        }
        eventtype = EventType.find(" label = ?", label).first();
        if(eventtype == null){
            eventtype = new EventType();
            eventtype.label = label;
            eventtype.save();
            Cache.set(key, eventtype, "1d");
        }
        return eventtype;
    }
}
