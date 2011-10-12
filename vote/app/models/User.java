/**
 * User
 * 29.07.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import play.db.jpa.Model;

@Entity
public class User extends Model {
    @OneToMany(mappedBy="user")
    public List<Attendee> attendances;
    
    public String uid;
    public String mail;
    public String displayName;
}
