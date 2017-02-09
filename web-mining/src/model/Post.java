package model;

/**
 * This class represents single post with various details associated with it
 *
 * @author Alimuddin Khan
 *
 * @version Nov 12, 2016
 */
public class Post {

    // physical url of the post
    private String postUrl;


    // post id of the post
    private Integer posttId;

    // other post details
    private String postTitle;
    private String excerpt;
    private String content;
    private Integer likesCount;


    // person who has posted the blog
    private Blogger blogger;

    //object to store the comments
    private Reply replies;

    /**
     * This is the parameterized constructor used to initialize a post object
     * @param posttId       post ID of the object
     * @param blogger_id    Blogger id of the object
     */
    public Post(Integer posttId, Integer blogger_id) {
        this.posttId = posttId;
        this.blogger = new Blogger(blogger_id);
        this.replies = new Reply();
    }


    /*
    This is the paramterless constructor of the Post object
     */
    public Post() {

    }


    /**
     * Getter for post url
     * @return  url of the post
     */
    public String getPostUrl() {
        return postUrl;
    }


    /**
     * Setter for post url
     * @param postUrl   url of the post
     */
    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }


    /**
     * Getter for post ID
     * @return ID of the post
     */
    public Integer getPosttId() {
        return posttId;
    }


    /**
     * Setter for the post ID
     * @param posttId    id of the post
     */
    public void setPosttId(Integer posttId) {
        this.posttId = posttId;
    }


    /**
     * Getter for the post title
     * @return  Returns the title for the post
     */
    public String getPostTitle() {
        return postTitle;
    }


    /**
     * Setter for the post title
     * @param postTitle title for the post
     */
    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }


    /**
     * Getter for the excerpt of the post
     * @return  the excerpt of the post
     */
    public String getExcerpt() {
        return excerpt;
    }


    /**
     * Setter for the excerpt(Brief content in the post) of the post
     * @param excerpt   the excerpt of the post
     */
    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }


    /**
     * Getter for the content in the post
     * @return  Content in the post
     */
    public String getContent() {
        return content;
    }


    /**
     * This method sets thhe content of the post
     * @param content     Content in the post
     */
    public void setContent(String content) {
        this.content = content;
    }


    /**
     * This is the getter for the Blogger who has posted the object
     * @return
     */
    public Blogger getBlogger() {
        return blogger;
    }


    /**
     * This is the setter for the Blogger object
     * @param blogger
     */
    public void setBlogger(Blogger blogger) {
        this.blogger = blogger;
    }


    /**
     * This method returns the comment details on the post
     * @return
     */
    public Reply getReplies() {
        return replies;
    }


    /**
     * This method sets the cpmments details on the post
     * @param replies
     */
    public void setReplies(Reply replies) {
        this.replies = replies;
    }


    /**
     * This method returns the no of likes count on the given object
     * @return
     */
    public Integer getLikesCount() {
        return likesCount;
    }


    /**
     * This method sets the like counts on a given post object
     * @param likesCount
     */
    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

}
