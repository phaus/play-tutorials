/**
 * VCard
 * 14.09.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import play.db.jpa.Model;

@Entity
public class VCard extends Model {
    //public HashMap<String,String> values = new HashMap<String, String>();
    public File photo;
    public String uid;
    public String name;
    public String nickName;
    public String fullName;
    public String organisation;
    public String showAs;
    
    @OneToMany(mappedBy="owner")
    public List<Contact>contacts;
    
    @OneToMany(mappedBy="owner")
    public List<Category> categories;

    public VCard(){
        this.categories = new ArrayList<Category>();
        this.contacts = new ArrayList<Contact>();
    }
    
    public static VCard findByUid(String uid){
        return VCard.find(" uid = ?", uid).first();
    }

    public static VCard findByName(String name){
        return VCard.find(" name = ?", name).first();
    }

    public List<Category>getCategories(){
        if(this.categories == null){
            this.categories = Category.find("byOwner", this.id).fetch();
        }
        return this.categories;
    }

    public List<Contact> getContacts(){
        if(this.contacts == null){
            this.contacts = Contact.find("byOwner", this.id).fetch();
        }
        return this.contacts;
    }

    public void addContact(Contact contact){
        this.contacts.add(contact);
    }

    public void addCategories(String categoriesString){
        String categoriesLabels[] = categoriesString.split(",");
        Category category = null;
        for(String categoryLabel : categoriesLabels){
            category = Category.find(" label = ?", categoryLabel).first();
            if(category == null){
                category = new Category(categoryLabel);
            }
            this.categories.add(category);
            category.save();
        }
    }
    
    @Override
    public String toString(){
        /*
        StringBuilder sb = new StringBuilder(this.uid+" #"+values.size()+" [");
        for(String key : values.keySet()){
            sb.append(key+":"+values.get(key)+" ");
        }
        sb.append("]");
        return sb.toString();
        */
        return "";
    }

    public String toTableRow(){
        StringBuilder sb =  new StringBuilder("<tr>");
        sb.append("<td>");
        sb.append(this.fullName);
        sb.append("</td>");
        sb.append("<td>");
        sb.append(this.organisation);
        sb.append("</td>");
        sb.append("<td>");
        for(Contact contact : this.getContacts()){
            sb.append(contact);
            sb.append("<br />");
        }
        for(Category category : this.getCategories()){
            sb.append(category);
            sb.append("<br />");
        }
        sb.append("</td>");
        sb.append("</tr>");
        return sb.toString();
    }
}
