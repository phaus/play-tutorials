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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import play.db.jpa.Model;

@Entity
public class Contact extends Model {

    @ManyToOne
    public VCard card;
    @ManyToMany
    public List<Type> types;
    public String label;
    public String value;

    public Contact(){
        this.types = new ArrayList<Type>();
    }
    
    public void addType(String label){
        Type type = Type.findOrCreatyByLabel(label);
        if(!this.types.contains(type)){
            this.types.add(type);
        }
        this.save();
    }

    public List<Type> getTypes(){
        if(this.types == null){
            this.types = Type.find(" contact = ?", this.id).fetch();
        }
        return this.types;
    }

    private String formatValueHTML(String value){
        String parts[] = value.split(";");
        String out = "";
        for(String part : parts){
            if(!part.isEmpty()){
                out += part+"<br />";
            }
        }
        return out;
    }
    
    @Override
    public String toString(){
        return this.label+": "+this.value;
    }

    public String toTableRow(){
        StringBuilder sb = new StringBuilder("<tr>");
        sb.append("<td style=\"vertical-align:top\">");
        sb.append(label);
        sb.append(":</td><td>");
        if(label.equals("TEL")){
            sb.append("<a href=\"tel:");
            sb.append(value);
            sb.append("\">");
        }
        if(label.equals("EMAIL")){
            sb.append("<a href=\"mailto:");
            sb.append(value);
            sb.append("\">");
        }
        if(label.equals("URL")){
            sb.append("<a target=\"BLANK\" href=\"");
            sb.append(value);
            sb.append("\">");
        }
        if(label.equals("X-JABBER")){
            sb.append("<a href=\"xmpp:");
            sb.append(value);
            sb.append("\">");
        }

        sb.append(formatValueHTML(value));
        if(label.equals("TEL") ||
           label.equals("URL") ||
           label.equals("EMAIL") ||
           label.equals("X-JABBER") ){
           sb.append("</a>");
        }
        sb.append("</td><td>");
        for(Type type : this.getTypes()){
            sb.append(type);
            sb.append(" ");
        }
        sb.append("</td></tr>");
        return sb.toString();
    }
}