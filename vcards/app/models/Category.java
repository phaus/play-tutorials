/**
 * Category
 * 16.09.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import javax.persistence.Entity;
import play.db.jpa.Model;

@Entity
public class Category extends Model {
    public String label;

    
    public Category(String label){
        this.label = label;
    }

    public static Category findOrCreatyByLabel(String label){
        Category category = Category.find(" label = ?", label).first();
        if(category == null){
            category = new Category(label);
            category.save();
        }
        return category;
    }

    @Override
    public String toString(){
        return label;
    }

}
