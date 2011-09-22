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
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import play.db.jpa.Model;

@Entity
public class VCard extends Model {
    //public HashMap<String,String> values = new HashMap<String, String>();

    @Transient
    public File photo;
    public String uid;
    public String name;
    public String nickName;
    public String fullName;
    public String organisation;
    public String showAs;
    public String title;
    public String note;
    @OneToMany(mappedBy = "card")
    public List<Contact> contacts;
    @OneToMany(mappedBy = "card")
    public List<Category> categories;

    public VCard() {
        this.categories = new ArrayList<Category>();
        this.contacts = new ArrayList<Contact>();
    }

    public static VCard findByNameAndFullName(String name, String fullName) {
        return VCard.find(" name = ? and fullName = ?", name, fullName).first();
    }

    public static VCard findByUid(String uid) {
        return VCard.find(" uid = ?", uid).first();
    }

    public static VCard findByName(String name) {
        return VCard.find(" name = ?", name).first();
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
            category = Category.find(" label = ? and card = ?", categoryLabel, this).first();
            if (category == null) {
                category = new Category(categoryLabel);
                category.card = this;
            }
            category.save();
            this.categories.add(category);
        }
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
                contact.addType(keyValue[1]);
            } else {
                contact.label = part;
                if (part.equals("N")) {
                    this.name = value;
                }
                if (part.equals("FN")) {
                    this.fullName = value;
                }
                if (part.equals("ORG")) {
                    this.organisation = value;
                }
            }
        }
        contact.save();
        this.contacts.add(contact);
    }

    @Override
    public String toString() {
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

    public String toTableRow() {
        StringBuilder sb = new StringBuilder("<tr>");
        sb.append("<td style=\"vertical-align:top\">");
        sb.append(this.fullName);
        sb.append("</td>");
        sb.append("<td style=\"vertical-align:top\">");
        for (Category category : this.getCategories()) {
            sb.append(category);
            sb.append("<br />");
        }
        sb.append("</td>");
        sb.append("<td style=\"vertical-align:top\">");
        for (Contact contact : this.getContacts()) {
            sb.append(contact);
            sb.append("<br />");
        }
        sb.append("</td>");
        sb.append("<td style=\"vertical-align:top\">");
        if (this.organisation != null) {
            sb.append(this.organisation);
        }
        sb.append("</td>");
        sb.append("</tr>");
        return sb.toString();
    }
}
