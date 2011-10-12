/**
 * Voter
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
public class Voter extends Model {

    @OneToMany(mappedBy="voter")
    public List<Vote>votes;
    @ManyToOne
    public Event event;

    public String hash;
}
