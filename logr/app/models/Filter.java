/**
 * Filter
 * 03.10.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import play.db.jpa.Model;

@Entity
public class Filter extends Model {
    public String filterTerm;

    @ManyToMany
    public List<Event> events;
}
