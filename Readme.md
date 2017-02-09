# Web mining
##### contributor: 
Alimudin Khan (aak5031@rit.edu)
***
### Project Description
Today finding right content regarding trending and intresting topics on the internet is one of the most difficult and time consuming process. 
With growing web content, it is tedious to follow precise information and get the content which exactly matters us the most. In this project, using the Wordpress APIs, I will be filtering out content specific to the user depending on his interests and will give him related content suggestions. As web mining is a huge topic, I will be focusing only on blogs. For this purpose, I have chosen WordPress as our research blogging platform as it is the most widely used blogging platform throughout the world. There are millions wordpress bloggers writing everyday. This project not only searches/crawls on blogs hosted on wordpress.org but also on custom domains running wordpress as CMS in the backend. I will be using WordPress's RESTful APIs to collect the data.


---

### Running the Program (Command Line)
###### Step 1 : Unzip 
You can unzip using following command
unzip web_mining.zip
###### Step 2: Setting up the environment
directory lib has all the requires jars for spring framework
Hnece you need to update your CLASSPATH variable such that program 
can find all the used classes in the program
Here is how you can do that;
a ) Open the terminal
b ) Open bash profile file
  vi ~/.bash_profile
c ) Append this line into that file;
  export CLASSPATH=$CLASSPATH:/<path-to-project-directory>/lib/*

d ) save the file and restart your terminal.
	Restarting the terminal will load all the environment variables with
	new values.
	Open the terminal and type
	echo $CLASSPATH
	to see if the path variable has been updated successfully.



###### Step 3 : Creating and configuring database
We assume you have the Mysql 5.7.13  or higher to run this program
run following command to created database on the go 
mysql < webmining.sql

This command will create the database called web_mining 
with all required tables with sample data in them.


part b} Configuring database handler program:
Open file DatabaseHandler.java
-> DB_URL: 
on line 21 we have static final String DB_URL = "jdbc:mysql://localhost:3306/web_mining";
make sure you have mySQL running on port 3306 and have the database web_mining in it
if you have different configuration, then please change it accordingly

-> Credetials:
on lines 24 and 25 we have database credentials as follows;
    static final String USER = "root";
    static final String PASS = "Root@01";

    Change those credentials to your database user name and password.
    Make sure that user has root previlleges.
    
###### Step 4 : Compile the java source files
you can compile the source files as follows;
a) javac model/*.java
b) javac controller/*.java


###### Step 5 : Run the program
You can run the program as follows
java controller.BlogMining

###### Step 6: Interacting with the program
You can interact with the program as follows;

|command| usage|
|-----| ---|
| crawl \<url\>|  To crawl a valid wordpress URL |
| crawl \<site-id\> \<post-id\> | To crawl a particular post|
| help| To print this help instruction|
| updateDB | To crawl top 10 uncrawled sites|
| top | To print top 10 famous blogs |
|related \<post-id\> \<site-id\> | To get related blogs for a particular post |
| quit | To quit this program  | 

e.g.
A}you can type 
crawl https://truthandsatire.wordpress.com/2015/07/03/greece-the-one-biggest-lie-you-are-being-told-by-the-media/

This command will help you crawl through all the related blogs 
(i.e. blogs posted by people who have commented on this blog)


### Dataset Desciption
This dataset has been adaptively generated using WordPress APIs. I am calling WordPRess APIs to get the post details and blogger details. Using those details we are storing them into database named 'web_mining'.
Web mining database has 3 tables;

|Table name| Description |
|-----|----|
| posts | It stores post details |
| bloggers | It stores blogger details |
| comments | It stores comments done on a particular post |

### Alternate Usage
I have also create a web user interface using Java servlets. Web related files are in /web-mining/WebContent directory.















