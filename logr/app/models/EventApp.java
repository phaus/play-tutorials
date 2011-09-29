/**
 * EventApp
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
public class EventApp extends Model {
    @OneToMany(mappedBy = "app")
    public List<Event> events;

    public String label;

    public static EventApp findOrCreateByLabel(String label){
        String key = "EventApp-"+label;
        EventApp app = Cache.get(key, EventApp.class);
        if(app != null){
            return app;
        }
        app = EventApp.find(" label = ? ", label).first();
        if(app == null){
            app = new EventApp();
            app.label = label;
            app.save();
            Cache.set(key, app, "1d");
        }
        return app;
    }
}
