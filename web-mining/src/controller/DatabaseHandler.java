package controller;
import model.Blogger;
import model.Post;
import model.Reply;
import java.sql.*;
import java.util.ArrayList;

/**
 * This class provide database handling related utility methods
 *
 * @author Alimuddin Khan
 *
 * @version Nov 12, 2016
 */
public class DatabaseHandler {
    // defining mysql database driver
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    // defining which database to connect to
    static final String DB_URL = "jdbc:mysql://localhost:3306/web_mining";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "Root@01";

    Connection connection;
    Statement statement;

    /**
     * This method initializes the connection
     * @throws Exception
     */
    public DatabaseHandler()
            throws Exception{
        System.out.println("connecting to database.....");
        connection = null;
        statement = null;
        Class.forName(JDBC_DRIVER);
        connection = DriverManager.getConnection(DB_URL, USER, PASS);
        statement = connection.createStatement();
        System.out.println("successfully connected to database....");
    }

    /**
     * This method closes any open data base connection
     * @throws SQLException
     */
    public void closeConnection()
            throws SQLException{
        System.out.println("closing database ........");
        this.statement.close();
        this.connection.close();
        System.out.println("successfully closed database........");
    }


    /**
     * This method check whether a partucylar blog ID
     * is present in the database or not.
     *
     * @param bloggerID
     * @return
     * @throws SQLException
     */
    public  boolean  isBloggerPresent (Integer bloggerID)
            throws SQLException {
        int  found = 0;

        // query to check the presence of the given ID
        String query = "select count(*) as present from bloggers where blogger_id = " +
                bloggerID ;

        // execute the query
        ResultSet rs = this.statement.executeQuery(query);

        // iterate over the result
        while(rs.next()) {
            //Retrieve by column name
            found = rs.getInt("present");
        }
        if(found == 0){
            return false;
        }else{
            return true;
        }
    }


    /**
     * This method check whether a particular post
     * is present in the database or not.
     *
     * @param bloggerID
     * @return
     * @throws SQLException
     */
    public  boolean  isPostPresent (Integer bloggerID, Integer postID)
            throws SQLException {
        int  found = 0;

        // query to check the presence of the given ID
        String query = "select count(*) as present from posts where post_id =" +
                 postID + " && blogger_id = " + bloggerID ;

        // execute the query
        ResultSet rs = this.statement.executeQuery(query);

        // iterate over the result
        while(rs.next()) {
            //Retrieve by column name
            found = rs.getInt("present");
        }
        if(found == 0){
            return false;
        }else{
            return true;
        }
    }


    /**
     * This method check whether a particular comment
     * is present in the database or not
     *
     * @param bloggerID
     * @return
     * @throws SQLException
     */
    public  boolean  isCommentorPresent (Integer bloggerID,
                                         Integer postID, Integer commentorID)
            throws SQLException {
        int  found = 0;

        // query to check the presence of the given ID
        String query = "select count(*) as present from comments where post_id =" +
                postID + " && blogger_id = " + bloggerID + " " +
                "&& commenter_id = " + commentorID ;

        // execute the query
        ResultSet rs = this.statement.executeQuery(query);

        // iterate over the result
        while(rs.next()) {
            //Retrieve by column name
            found = rs.getInt("present");
        }
        if(found == 0){
            return false;
        }else{
            return true;
        }
    }


    /**
     * This method returns the top ten site IDs which haven;t been crawled
     * @return
     * @throws SQLException
     */
    public ArrayList<Integer> getUncrawledTopSiteIDs()
            throws SQLException{
        ArrayList<Integer> siteIDs = new ArrayList<>(10);
        System.out.println("Querying top ten uncrawled sites");
        String query = "select * from bloggers where crawled = 0 limit 10";

        // storing the result set
        ResultSet resultSet = this.statement.executeQuery(query);

        while(resultSet.next()){
            siteIDs.add(resultSet.getInt("blogger_id"));
        }
        return siteIDs;
    }


    /**
     * This method prints most liked or commented posts from database
     * @throws SQLException
     */
    public void printTopLikedPosts()
            throws SQLException {
        System.out.println("####################################\n" +
                "######### Top trending posts #######\n" +
                "####################################\n");
        String query = "select post_id , blogger_id ,post_url, comments_count , likes_count  " +
                "from posts  order by comments_count desc limit 10";
        // storing the result set
        ResultSet resultSet = this.statement.executeQuery(query);

        System.out.printf("%10s|%10s|%10s|%10s|%s\n","post id", "blogger id", "comments", "like", "post url");
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -" +
                "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
        while(resultSet.next()){
            System.out.printf("%10d|%10d|%10d|%10d|%s\n", resultSet.getInt("post_id"), resultSet.getInt("blogger_id"),
                    resultSet.getInt("comments_count"), resultSet.getInt("likes_count"),
                    resultSet.getString("post_url"));
        }
    }
    
    
    /**
     * This method return most liked or commented posts from database
     * @throws SQLException
     */
    public ResultSet getTopLikedPosts()
           {
        String query = "select post_id ,post_title, blogger_id ,post_url, comments_count , likes_count  " +
                "from posts  order by comments_count desc limit 10";
        // storing the result set
        ResultSet resultSet = null;
		try {
			resultSet = this.statement.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return resultSet;

    }



    /**
     * This method prints the related posts
     * @param postID
     * @param siteID
     * @throws SQLException
     */
    public void getRelatedPosts(Integer postID, Integer siteID)
            throws SQLException{
        System.out.println("##### printing related posts #####");
        String query ="select post_id , post_title,blogger_id ,post_url, comments_count , likes_count from posts  where " +
                "blogger_id IN (select commenter_id from comments where post_id =" + postID + "&& blogger_id = " +
                siteID + " ) " + "order by comments_count desc limit 10";
        ResultSet resultSet = this.statement.executeQuery(query);

        System.out.printf("%10s|%10s|%10s|%10s|%s\n","post id", "blogger id", "comments", "like", "post url");
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -" +
                "- - - - - - - - - - - - - - - - - - - - - - - - - - - - - ");
        while(resultSet.next()){
            System.out.printf("%10d|%10d|%10d|%10d|%s\n", resultSet.getInt("post_id"), resultSet.getInt("blogger_id"),
                    resultSet.getInt("comments_count"), resultSet.getInt("likes_count"),
                    resultSet.getString("post_url"));
        }
        System.out.println("##### ENDED PRINTING #####");
    }
    
    
    /**
     * This method prints the related posts
     * @param postID
     * @param siteID
     * @throws SQLException
     */
    public ResultSet getRelatedPostsForWeb(Integer postID, Integer siteID){
        String query ="select post_id , post_title,blogger_id ,post_url, comments_count , likes_count from posts  where " +
                "blogger_id IN (select commenter_id from comments where post_id =" + postID + "&& blogger_id = " +
                siteID + " ) " + " &&  blogger_id != " + siteID + " order by comments_count desc limit 10";
     
        ResultSet resultSet = null;
		try {
			resultSet = this.statement.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return resultSet;
        
    }


    /**
     * This method inserts the post int o posts table
     * @param post Post object to be insrted into the table
     */
    public  void insertPost(Post post)
            throws SQLException{

        // inserting post entry only if the entry is not present in the database
        if(!this.isPostPresent(post.getBlogger().getBloggerId(), post.getPosttId())){
            // I will start inserting here :)
            String query = "insert into posts (post_id , blogger_id , post_url , post_title , excerpt " +
                    " , likes_count , comments_count, content ) " +
                    "values (" + post.getPosttId() + ", " + post.getBlogger().getBloggerId() +
                    ", \'" + post.getPostUrl() + "\', \'" + post.getPostTitle() + "\',\'"  + post.getExcerpt() +
                     "\', " + post.getLikesCount() + ", " + post.getReplies().getNoOfComments() + ", \'" +
                    post.getContent() + "\' )";

            statement.executeUpdate(query);
        }

        // check if blogger is present in the blogger .. If not then add the blogger :)
        if(!isBloggerPresent(post.getBlogger().getBloggerId())){
            this.insertBlogger(post.getBlogger().getBloggerId());
        }


        // I need to add those things into the comments table
        Reply replies = post.getReplies();
        for(Blogger commentor: replies.getCommentors()){
            // First check if the comment is present of not
            if(!this.isCommentorPresent(post.getBlogger().getBloggerId(),
                    post.getPosttId(), commentor.getBloggerId())){
                // adding comment in comments table
                this.insertComment(post.getBlogger().getBloggerId(),
                        post.getPosttId(),commentor.getBloggerId());
            }

            // checking if the commentor is in bloggers table or not
            if(!this.isBloggerPresent(commentor.getBloggerId())){
                // add if not present in blogger table
                this.insertBlogger(commentor.getBloggerId());
            }
        }

    }


    /**
     *  This method inserts an entry into the comments table
     * @param bloggerID
     * @param postID
     * @param commentorID
     * @throws SQLException
     */
    public void insertComment(Integer bloggerID, Integer postID, Integer commentorID)
            throws SQLException{
        String query = "insert into comments (post_id, blogger_id, commenter_id) values(" +
                postID + "," + bloggerID + "," + commentorID + ")";
        this.statement.executeUpdate(query);
    }


    /**
     * This method inserts an entry into the bloggers table
     * @param bloggerID
     * @throws SQLException
     */
    public  void insertBlogger(Integer bloggerID)
            throws SQLException{
        String query = "insert into bloggers ( blogger_id) values(" + bloggerID + ")";
        this.statement.executeUpdate(query);
    }


    /**
     * This method sets the crawling status of a site ID/Blogger ID to crawled
     * @param bloggerID
     * @throws SQLException
     */
    public void setCrawled(Integer bloggerID)
            throws SQLException{
        System.out.println("Setting blogger ID " + bloggerID + " as crawled");
        String query = "update bloggers set crawled = 1 where blogger_id = " + bloggerID;
        this.statement.executeUpdate(query);
    }







    public static void main(String[] args)
            throws Exception{
        DatabaseHandler dbh = new DatabaseHandler();

        //System.out.println(dbh.isCommentorPresent(1111,1,2222));
        //dbh.insertComment(1111,1,2222);
        //System.out.println(dbh.isCommentorPresent(1111,1,2222));
        dbh.getRelatedPosts(1936, 4978923);


        dbh.closeConnection();

    }


}
