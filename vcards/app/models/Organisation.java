/**
 * Organisation
 * 27.09.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import play.db.jpa.Model;

@Entity
public class Organisation extends Model {
    public String name;

    @OneToMany(mappedBy = "organisation")
    public List<VCard> cards;
        
    public Organisation(String name){
        this.name = name;
    }
    
    public static Organisation findOrCreatyByName(String name){
        Organisation organisation = Organisation.find(" name = ?", name).first();
        if(organisation == null){
            organisation = new Organisation(name);
            organisation.save();
        }
        return organisation;
    }

    @Override
    public String toString(){
        return this.name;
    }
    
}
