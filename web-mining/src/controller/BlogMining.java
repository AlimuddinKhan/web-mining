package controller;

import model.Blogger;
import model.Post;
import model.Reply;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;


/**
 * This class provides a CLi for the blog mining app
 * It allows a person to interact with the application by
 * passing pot url, post id, blod id etc. to crawl the
 * wordPress blogs. It also has various utility functions
 * to crawl the post.
 *
 * @author Alimuddin Khan
 *
 * @version Nov 12, 2016
 */
public class BlogMining {

    // WordPress API url
    public static final String WORDPRESS_API = "https://public-api.wordpress.com/rest/v1.1/";

    // variable to count the number of distinct urls found
    static int urlCount = 1;

    /**
     * This is the main method which gives an Interacitve CLI to
     * crawl the wordpress blogs
     * @param args
     */
    public static void main(String[] args)
            throws Exception{

        Scanner scanner = new Scanner(System.in);
        printInstruction();
        String command = "";

        // create a database handler
        DatabaseHandler dbh = new DatabaseHandler();

        // keep running until user types 'quit' command
        while(!command.matches("quit")){
            command = scanner.nextLine();
            String[] commandArray = command.split(" ");
            String comm = commandArray[0];

            // taking action based on the command tyoed by the user
            // this section represents our CLIs as well
            switch (comm){
                case "crawl":
                    if(commandArray.length == 2){
                        crawlUsingPostUrl(commandArray[1], dbh);
                        System.out.println("Finished crawling!!");
                    }else if(commandArray.length == 3){
                        try {
                            crawlUsingPostAndSiteId(Integer.parseInt(commandArray[1]),
                                    Integer.parseInt(commandArray[2]), dbh);
                            System.out.println("Finished crawling!!");
                        }catch (NumberFormatException e){
                            System.out.println("ERROR: site ID and post id must be a valid Integer");
                        }
                    }else{
                        System.out.println("Please use a valid command as follows");
                        printCommands();
                    }
                    break;
                case "crawlsite":
                    if(commandArray.length == 2){
                        System.out.println("NOTE: will crawl the entire site here");
                        crawlUsingSiteID(Integer.parseInt(commandArray[1]), dbh);
                        System.out.println("Finished crawling!!");
                    }else{
                        System.out.println("Please use a valid command as follows");
                        printCommands();
                    }
                    break;
                case "updatedb":
                    if(commandArray.length == 1){
                        updateDB(dbh);
                    }else{
                        System.out.println("Please use a valid command as follows");
                        printCommands();
                    }
                    break;

                case "top":
                    if(commandArray.length == 1){
                        System.out.println("## TOP ##");
                        getTopPosts(dbh);

                    }else{
                        System.out.println("Please use a valid command as follows");
                        printCommands();
                    }
                    break;

                case "related":
                    if(commandArray.length == 3){
                        printRelatedPosts(Integer.parseInt(commandArray[1]),
                                Integer.parseInt(commandArray[2]), dbh);

                    }else{
                        System.out.println("Please use a valid command as follows");
                        printCommands();
                    }
                    break;


                case "help":
                    printCommands();
                    break;
                case "quit":
                    System.out.println("Thanks for using our application");
                    break;
                default:
                    System.out.println("Please type a valid command or type 'help' to see avaialble commands");
            }
        }

        // close the database connection
        dbh.closeConnection();
        scanner.close();

    }


    /**
     * This method prints the instructions regarding this program
     */
    public static void printInstruction() {
        String msg = "####################################\n" +
                "\t\tBLOG MINING\n" +
                "####################################\n" +
                "Weclome to our Blog mining application\n" +
                "You have following commands to interact with our app";
        System.out.println(msg);
        printCommands();
    }


    /**
     * This method prints the commands related to this post
     */
    public static void printCommands(){
        String commands = "1. crawl <url> : where url is valid wordpress blog url\n" +
                "2. crawl <site-id> <post-id> :  where " +
                "\tsite-id is valid Wordpress Blogger site ID" +
                "\tAnd post-id is valid wordpress post-id\n" +
                "3. help: To print this help instruction\n" +
                "4. updateDB: This command crawls top 10 uncrawled sites\n" +
                "5. top: This command prints top trending posts as per comments and likes count\n" +
                "6. related <post-id> <site-id> :  where " +
                "\tsite-id is valid Wordpress Blogger site ID" +
                "\tAnd post-id is valid wordpress post-id\n" +
                "7. quit: To quit this program";
        System.out.println(commands);
    }


    /**
     * This method crawls a particular post object of a site / blogger
     * @param post Post object
     * @return
     */
    public static void  crawlUsingPostObject(Post post, DatabaseHandler dbh){
        for(Blogger blogger: post.getReplies().getCommentors()) {
            // can do here valdation like whether we have crawled this blog id
            List<Post> commentorPosts = getPostsBySiteId(blogger.getBloggerId());
            if(commentorPosts != null) {
                for (Post singlePost : commentorPosts) {
                    //System.out.println(urlCount + " : " + singlePost.getPostUrl());

                    // will add the post to database here
                    System.out.printf("%5d:%s\n", urlCount, singlePost.getPostUrl());

                    //System.out.println("will add the post to database here");
                    try {
                        dbh.insertPost(singlePost);
                    } catch (SQLException e) {
                        // squash the error
                        // e.printStackTrace();
                    }
                    urlCount++;
                }
            }
        }
    }


    /**
     * This method crawls all the posts of a site / blogger
     * @param siteID siteID of the blogger
     */
    public static void crawlUsingSiteID(Integer siteID, DatabaseHandler dbh){
        List<Post> myPosts = getPostsBySiteId(siteID);
        //System.out.println("Found : " + myPosts.size() + " posts");
        if(myPosts != null) {
            for (Post post : myPosts) {
                //System.out.println("Crawling " + post);
                // I can check here whether that is present in database or not :)
                try {
                    if(!dbh.isPostPresent(siteID, post.getPosttId())){
                        crawlUsingPostObject(post, dbh);
                    }
                } catch (SQLException e) {
                   // e.printStackTrace();
                }

            }
        } else{
            System.out.println("Found null");
        }
    }


    /**
     * This method crawls a particular post using its postID andd siteID
     * @param postID
     * @param siteID
     */
    public static void crawlUsingPostAndSiteId(Integer postID, Integer siteID, DatabaseHandler dbh){
        Post post = getPostById(postID, siteID);
        if(post != null) {
            crawlUsingPostObject(post, dbh);
        }
    }


    /**
     * This method crawls a particular post using its URL
     * @param postUrl
     */
    public static void crawlUsingPostUrl(String postUrl, DatabaseHandler dbh){
        Post post = getPostByUrl(postUrl);
        if(post != null) {
            crawlUsingPostObject(post, dbh);
        }
    }


    /**
     * This method Prints Details of a post
     * @param post Post object whose details are to be printed
     */
    public static void printPostDetails(Post post){
        System.out.println("postID is " + post.getPosttId());
        System.out.println("site ID is " + post.getBlogger().getBloggerId());
        System.out.println("Tile is " + post.getPostTitle());
        System.out.println("postUrl is " + post.getPostUrl());
        System.out.println("like count are : " + post.getLikesCount());
        System.out.println("Excerpt is : " + post.getExcerpt());

        System.out.println("Commentors are : ");
        Reply replies = post.getReplies();
        for(Blogger commentor: replies.getCommentors()){
            System.out.println(commentor.getBloggerId());
        }
    }


    /**
     * This method creates a post Object using post url
     * This object contains all the details about the Post
     * @param stringurl url string passed
     * @return Post Object
     */
    public static Post getPostByUrl(String stringurl){
        Post post = null;
        try {
            URL url = new URL(stringurl);

            // parsing the url to get the slug
            String[] parsedUrl = url.getPath().split("\\/");
            String slug = parsedUrl[parsedUrl.length - 1];
            String host = url.getHost();
            String requestUrl = WORDPRESS_API + "sites/" + host +
                    "/posts/slug:" + slug + "?pretty=true&number=100&meta=replies";
            post = getPostDetails(requestUrl);

        } catch (MalformedURLException e) {
            System.out.println("ERROR: Url passed is not a valid url");
        }
        return post;
    }


    /**
     * This method returns the Post Object provided the post ID and sire id
     * @param postID
     * @param siteID
     * @return Post Object
     */
    public static Post getPostById(Integer postID, Integer siteID){
        String requestUrl = WORDPRESS_API + "sites/" + siteID + "/posts/" + postID;
        requestUrl +=  "?pretty=true&number=100&meta=replies";
        return getPostDetails(requestUrl);
    }


    /**
     * This method returns the Post object provided a valid
     * Wordpress API request URL
     * @param request
     * @return Post Object
     */
    public static Post getPostDetails(String request){
        Post post = null;
        // requesting the json response
        try {

            // craeting a rest template to request a rest api
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(request, String.class);

            // converting the string response in to the json object for efficient parsing
            JSONObject jsonResponse = new JSONObject(response);


            // initialising the post Object with different details
            Integer postID = jsonResponse.getInt("ID");
            Integer siteID = jsonResponse.getInt("site_ID");
            String title = jsonResponse.getString("title");
            String postUrl = jsonResponse.getString("URL");
            Integer likesCount = jsonResponse.getInt("like_count");
            String content = jsonResponse.getString("content");
            String excerpt = jsonResponse.getString("excerpt");

            post = new Post(postID, siteID);
            post.setPostTitle(title);
            post.setPostUrl(postUrl);
            post.setLikesCount(likesCount);
            post.setContent(content);
            post.setExcerpt(excerpt);


            // Gathering comment details and blogger who have commented on this blog
            JSONObject replies = jsonResponse.getJSONObject("meta").getJSONObject("data").getJSONObject("replies");
            Integer noOfComments = replies.getInt("found");
            Reply replyObject = new Reply(noOfComments);

            JSONArray comments = replies.getJSONArray("comments");
            Set<Integer> uniqueCommentors = new HashSet<>();
            for (int i = 0; i < comments.length(); i++) {
                JSONObject comment = (JSONObject) comments.get(i);
                JSONObject author = comment.getJSONObject("author");
                try {
                    Integer site_ID = (Integer) author.get("site_ID");
                    // here is the issue
                    uniqueCommentors.add(site_ID);
                } catch (JSONException e) {
                    // ignoring authors who do not have site id
                }


            }
            // adding only unique commentors in the commentor list
            for(Integer uniqueSiteID : uniqueCommentors){
                replyObject.addCommenterWithID(uniqueSiteID);
            }

            // setting the reply object
            post.setReplies(replyObject);
        }catch (HttpClientErrorException e){
            // if site_id or post id is not valid then this error will be thrown
            System.out.println("ERROR: Not a valid Wordpress API call");
        }
        return  post;
    }

    /**
     * This method returns list of top
     * Post Objects for a particular siteID or blogger
     * @param siteID
     * @return
     */
    public static List<Post> getPostsBySiteId(Integer siteID){
        List<Post> postList = null;
        String requestUrl = "https://public-api.wordpress.com/rest/v1.1/sites/" +
                siteID + "/posts/?after=2014-01-01&order_by=comment_count?pretty=true";
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(requestUrl, String.class);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray posts = jsonResponse.getJSONArray("posts");
            postList = new LinkedList<>();
            Set<Integer> uniqueSiteIDs = new HashSet<>();
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.getJSONObject(i);
                Integer post_id = post.getInt("ID");
                uniqueSiteIDs.add(post_id);
            }

            // adding Post object only with unique post IDs
            for(Integer localSiteID : uniqueSiteIDs){
                postList.add(getPostById(localSiteID, siteID));
            }
        }catch (HttpClientErrorException e){
            System.out.println("ERROR: provide site ID is not a valid Wordpress site ID");
        }
        return postList;
    }


    public static void updateDB(DatabaseHandler dbh){
        System.out.println("Updating DB....");
        try {
            ArrayList<Integer> uncrawledSites = dbh.getUncrawledTopSiteIDs();
            for(Integer siteID : uncrawledSites){
                System.out.println("Crawling: " + siteID);
                crawlUsingSiteID(siteID, dbh);

                // update site id as crawled in database
                dbh.setCrawled(siteID);
            }
        } catch (SQLException e) {

        }

        System.out.println("Finished updating DB....");
    }


    /**
     * This method prints top liked posts from database
     * @param dbh
     */
    public static void getTopPosts(DatabaseHandler dbh){
        try {
            dbh.printTopLikedPosts();
        } catch (SQLException e) {

        }
    }


    /**
     * This method prints the related posts for the given  post
     * @param postID
     * @param siteID
     * @param dbh
     */
    public static void printRelatedPosts(Integer postID, Integer siteID, DatabaseHandler dbh){
        try {
            dbh.getRelatedPosts(postID, siteID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
