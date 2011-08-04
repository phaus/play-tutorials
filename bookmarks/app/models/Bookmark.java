/**
 * Bookmark
 * 31.07.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import play.data.validation.Required;
import play.db.jpa.Model;
@Entity
public class Bookmark extends Model {

    @Temporal(TemporalType.DATE)
    public Date createdAt;
    @Required
    public String title;
    @Required
    public String url;

    public Bookmark(){
        createdAt = new Date();
    }
}
