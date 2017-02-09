package model;

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a Blogger object.
 * It has the details regarding the blogger like its blogger id
 * and all the famous posts which has been posted by him/her
 *
 * @author Alimuddin Khan
 *
 * @version Nov 12, 2016
 */
public class Blogger {
    private Integer bloggerId;

    // list of post objects to store famous posts by him/her
    private List<Post> posts;


    /**
     * Parameterized constructor to initialize the blogger object
     * @param bloggerId ID of the blogger
     */
    public Blogger(Integer bloggerId) {
        this.bloggerId = bloggerId;
        this.posts = new LinkedList<>();
    }


    /**
     * This is the getter for the id of the blogger
     * @return  ID of the blogger
     */
    public Integer getBloggerId() {
        return bloggerId;
    }


    /**
     * This is the setter for the Blogger Id
     * @param bloggerId  ID of the blogger
     */
    public void setBloggerId(Integer bloggerId) {
        this.bloggerId = bloggerId;
    }


    /**
     * This method returns the list of famous posts by Blogger
     * @return  List of post objects which are posted by the blogger
     */
    public List<Post> getPosts() {
        return posts;
    }


    /**
     * This method sets the list of posts by the given blogger
     * @param posts
     */
    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    /**
     * This method adds one post into the list of famous posts by the
     * blogger
     * @param post Post object to be added in the list of famous posts
     */
    public void addPost(Post post){
        this.posts.add(post);
    }
}
