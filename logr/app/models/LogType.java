/**
 * LogType
 * 01.10.2011
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
public class LogType extends Model {
    public String label;
    @OneToMany(mappedBy = "type")
    public List<Log> logs;


    public LogType(){
        this.logs = new ArrayList<Log>();
    }
    
    public static LogType findOrCreateByLabel(String label){
        String key = "LogType-"+label;
        LogType type = Cache.get(key, LogType.class);
        if(type != null){
            return type;
        }
        type = LogType.find(" label = ?", label).first();
        if(type == null){
            type = new LogType();
            type.label = label;
            type.save();
            Cache.set(key, type, "1d");
        }
        return type;
    }
    
}
