/**
 * Type
 * 16.09.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import play.db.jpa.Model;

@Entity
public class Type extends Model {
    public String label;
    @ManyToOne
    public Contact contact;
    public Type(String label){
        this.label = label;
    }

    @Override
    public String toString(){
        return label;
    }
}
