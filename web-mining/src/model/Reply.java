package model;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents the Reply object.
 * This class has details regarding the comments on a given post/blog
 *
 * @author Alimuddin Khan
 *
 * @version Nov 12, 2016
 */
public class Reply {
    private int noOfComments;

    // set of bloggers who have commented on the blog
    private Set<Blogger> commentors;

    /**
     * This is a parameterized constructor
     * @param noOfComments no of comments on the given blog
     */
    public Reply(int noOfComments) {
        this.noOfComments = noOfComments;
        this.commentors = new HashSet<>();
    }

    /**
     * This is a paramterless constructor
     */
    public Reply() {
    }

    /**
     * Getter for no of comments
     * @return
     */
    public int getNoOfComments() {
        return noOfComments;
    }


    /**
     * Setter for no of comments
     * @param noOfComments no of comments on the given blog
     */
    public void setNoOfComments(int noOfComments) {
        this.noOfComments = noOfComments;
    }


    /**
     * Getter for commentros who have commented on the given blog
     * @return
     */
    public Set<Blogger> getCommentors() {
        return commentors;
    }


    /**
     * Setter for commentors
     * @param commentors    commentros who have commented on the given blog
     */
    public void setCommentors(Set<Blogger> commentors) {
        this.commentors = commentors;
    }


    /**
     * Adding one commentor at a time on the given blog using Blogger object
     * @param commentor
     */
    public void addCommentor(Blogger commentor){
        this.commentors.add(commentor);
    }

    /**
     * This method adds the commentor with the give ID of that commentor
     * @param blogger_id blogger if of the commentor
     */
    public void addCommenterWithID(Integer blogger_id){
        Blogger blogger = new Blogger(blogger_id);
        this.commentors.add(blogger);
    }

}
