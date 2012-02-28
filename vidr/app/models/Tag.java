/**
 * Tag
 * 28.02.2012
 * @author Philipp Haussleiter
 *
 */
package models;

import javax.persistence.Entity;
import play.db.jpa.Model;

@Entity
public class Tag extends Model {
    public String name;

    public Tag(String name){
        this.name = name;
    }

    public static Tag findOrCreateByName(String name){
        Tag tag = Tag.find(" name = ?", name).first();
        if(tag == null){
            tag = new Tag(name);
            return tag.save();
        }
        return tag;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
