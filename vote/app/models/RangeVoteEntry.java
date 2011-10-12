/**
 * RangeVoteEntry
 * 29.07.2011
 * @author Philipp Haußleiter
 *
 */
package models;

import javax.persistence.Entity;

@Entity
public class RangeVoteEntry extends VoteEntry {
    public static int lowVote = 1;
    public static int highVote = 5;
    public int value;

    @Override
    public String toString(){
        return ""+value;
    }
}
