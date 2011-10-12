/**
 * Attendee
 * 29.07.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import play.db.jpa.Model;

@Entity
public class Attendee extends Model {
    @ManyToOne
    public Event event;
    @ManyToOne
    public User user;

    public boolean voted;
}
