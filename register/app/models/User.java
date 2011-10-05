/**
 * User
 * 03.10.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import javax.persistence.Entity;
import play.data.validation.Email;
import play.db.jpa.Model;
import play.libs.Codec;

@Entity
public class User extends Model {
    @Email
    public String email;

    public String password;

    public String firstname;
    public String givenname;

    public void setPassword(String password){
        this.password = Codec.hexSHA1(password);
    }

}
