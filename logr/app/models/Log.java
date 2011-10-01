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

    public Log(){
        this.events = new ArrayList<Event>();
    }
    
    public void addEvent(Event event){
        event.log = this;
        event.save();
    }

}
