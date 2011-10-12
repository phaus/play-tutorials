/**
 * Session
 * 29.07.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import play.db.jpa.Model;

@Entity
public class Session extends Model {
    @ManyToOne
    public Event event;
    @OneToMany(mappedBy="session")
    public List <Vote>votes;
    
    public String title;
    public String description;
    public String speaker;

}
