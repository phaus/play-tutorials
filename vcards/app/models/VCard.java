/**
 * VCard
 * 14.09.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import play.db.jpa.Blob;
import play.db.jpa.Model;

@Entity
public class VCard extends Model {

    public Blob photo;
    public String name;
    public String nickName;
    public String fullName;
    @ManyToOne
    public Organisation organisation;
    public String showAs;
    public String title;
    public String note;
    public String birthday;
    
    @OneToMany(mappedBy = "card")
    public List<Contact> contacts;
    @ManyToMany
    public List<Category> categories;

    public VCard() {
        this.categories = new ArrayList<Category>();
        this.contacts = new ArrayList<Contact>();
    }

    public static VCard findByNameAndFullName(String name, String fullName) {
        return VCard.find(" name = ? and fullName = ?", name, fullName).first();
    }

    /**
     * Needs to find the best Match (1. Fullname 2. Fullname & Birthday).
     * @param fullName
     * @param birthday
     * @return
     */
    public static VCard findByFullNameAndBirthday(String fullName, String birthday) {
        if(VCard.find(" fullName = ?", fullName).fetch().size() == 1){
            return VCard.find(" fullName = ?", fullName).first();
        }
        return VCard.find(" fullName = ? and birthday = ?", fullName, birthday).first();
    }

    public static VCard findByName(String name) {
        return VCard.find(" name = ?", name).first();
    }

    public void setPhoto(File photo){

    }
    
    public List<Category> getCategories() {
        if (this.categories == null) {
            this.categories = Category.find("card = ?", this.id).fetch();
        }
        return this.categories;
    }

    public List<Contact> getContacts() {
        if (this.contacts == null) {
            this.contacts = Contact.find("card = ?", this.id).fetch();
        }
        return this.contacts;
    }

    public void addCategories(String categoriesString) {
        String categoriesLabels[] = categoriesString.split(",");
        Category category = null;
        for (String categoryLabel : categoriesLabels) {
            category = Category.findOrCreatyByLabel(categoryLabel);
            if(!this.categories.contains(category)){
                this.categories.add(category);
            }
        }
        this.save();
    }

    public void addContact(String key, String value) {
        String keyParts[] = key.split(";");
        String keyValue[] = null;
        Contact contact = null;
        contact = Contact.find(" value = ? and card = ?", value, this).first();
        if (contact == null) {
            contact = new Contact();
        }
        contact.card = this;
        contact.value = value;
        contact.save();
        for (String part : keyParts) {
            keyValue = part.split("=");
            if (keyValue.length > 1) {
                contact.addType(keyValue[1].toLowerCase());
            } else {
                contact.label = part;
                if (part.equals("N")) {
                    this.name = value;
                }
                if (part.equals("FN")) {
                    this.fullName = value;
                }
                if (part.equals("ORG")) {
                    this.organisation = Organisation.findOrCreatyByName(value);
                }
            }
        }
        contact.save();
        this.contacts.add(contact);
    }

    @Override
    public String toString() {
        if(this.fullName.length() > 30){
            return this.fullName.substring(0, 30)+"...";
        }
        return this.fullName;
    }

    public String toHtml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        sb.append("<tr><th>Categories</th><th>Contact</th><th>Organisation</th></tr>");
        sb.append("<tr>");
        sb.append("<td style=\"vertical-align:top\">");
        for (Category category : this.getCategories()) {
            sb.append(category);
            sb.append("<br />");
        }
        sb.append("</td>");
        sb.append("<td style=\"vertical-align:top\"><table>");
        for (Contact contact : this.getContacts()) {
            sb.append(contact.toTableRow());
        }
        sb.append("</table></td>");
        sb.append("<td style=\"vertical-align:top\">");
        if (this.organisation != null) {
            sb.append(this.organisation);
        }
        sb.append("</td>");
        sb.append("</tr>");
        sb.append("</table>");
        return sb.toString();
    }


}
