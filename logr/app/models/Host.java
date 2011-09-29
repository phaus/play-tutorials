/**
 * Host
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
public class Host extends Model {

    @OneToMany(mappedBy = "host")
    public List<Event> events;

    public String hostname;
    public String dnsHostname;


    public static Host findOrCreateByHostname(String hostname){
        String key = "Host-"+hostname;
        Host host = Cache.get(key, Host.class);
        if(host != null){
            return host;
        }
        host = Host.find(" hostname = ?", hostname).first();
        if(host == null){
            host = new Host();
            host.hostname = hostname;
            host.save();
            Cache.set(key, host, "1d");
        }
        return host;
    }
}
