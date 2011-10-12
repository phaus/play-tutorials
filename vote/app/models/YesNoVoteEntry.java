/**
 * YesNoVote
 * 29.07.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import javax.persistence.Entity;


@Entity
public class YesNoVoteEntry extends VoteEntry {
    public final static String NO = "no";
    public final static String YES = "yes";
    public String value;

    @Override
    public String toString(){
        return value;
    }
}
