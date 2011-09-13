/**
 * VCard
 * 14.09.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import java.util.HashMap;
import javax.persistence.Entity;
import play.db.jpa.Model;
@Entity
public class VCard extends Model {
    HashMap<String,String> values = new HashMap<String, String>();
}
