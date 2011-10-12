/**
 * Event
 * 29.07.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import play.db.jpa.Model;

@Entity
public class Event extends Model {
    @OneToMany(mappedBy="event")
    public List<Attendee> attendees;
    @OneToMany(mappedBy="event")
    public List<Voter> voters;
    @OneToMany(mappedBy="event")
    public List<Session> sessions;
    
    public String name;
    public String location;
    @Temporal(TemporalType.DATE)
    public Date startDate;
    @Temporal(TemporalType.DATE)
    public Date endDate;
}
