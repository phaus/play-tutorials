/**
 * Contact
 * 16.09.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import play.db.jpa.Model;

@Entity
public class Contact extends Model {

    @ManyToOne
    public VCard owner;
    @OneToMany(mappedBy="owner")
    public List<Type> types;
    public String label;
    public String value;

    public Contact(){
        this.types = new ArrayList<Type>();
    }
    
    public void addType(String label){
        Type type = Type.find(" label = ?", label).first();
        if(type == null){
            type = new Type(label);
        }
        this.types.add(type);
    }

    public List<Type> getTypes(){
        if(this.types == null){
            this.types = Type.find(" owner = ?", this.id).fetch();
        }
        return this.types;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(label);
        sb.append(": ");
        sb.append(value);
        for(Type type : this.getTypes()){
            sb.append(type);
            sb.append(" ");
        }
        return sb.toString();
    }
}
