/**
 * VoteEntry
 * 29.07.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import play.db.jpa.Model;

@Entity
public class VoteEntry extends Model {
    @ManyToOne
    public Vote vote;
    public String description;

}
