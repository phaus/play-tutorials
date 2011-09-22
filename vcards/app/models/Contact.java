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
    public VCard card;
    @OneToMany(mappedBy="contact")
    public List<Type> types;
    public String label;
    public String value;

    public Contact(){
        this.types = new ArrayList<Type>();
    }
    
    public void addType(String label){
        Type type = Type.find(" label = ? and contact = ?", label, this).first();
        if(type == null){
            type = new Type(label);
            type.contact = this;
        }
        type.save();
        this.types.add(type);
    }

    public List<Type> getTypes(){
        if(this.types == null){
            this.types = Type.find(" contact = ?", this.id).fetch();
        }
        return this.types;
    }
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(label);
        sb.append(": ");
        sb.append(value);
        sb.append(" ");
        for(Type type : this.getTypes()){
            sb.append(type);
            sb.append(" ");
        }
        return sb.toString();
    }
}
