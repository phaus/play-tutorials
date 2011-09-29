/**
 * Event
 * 29.09.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import play.db.jpa.Model;

@Entity
public class Event extends Model {

    @ManyToOne
    EventTime time;

    @ManyToOne
    Host host;

    @ManyToOne
    EventType type;

    @ManyToOne
    EventApp app;

    String message;
}
