/**
 * Type
 * 16.09.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import javax.persistence.Entity;
import play.db.jpa.Model;

@Entity
public class Type extends Model {
    public String label;

    public Type(String label){
        this.label = label;
    }

    public static Type findOrCreatyByLabel(String label){
        Type type = Type.find(" label = ?", label).first();
        if(type == null){
            type = new Type(label);
            type.save();
        }
        return type;
    }

    @Override
    public String toString(){
        return label;
    }
}
