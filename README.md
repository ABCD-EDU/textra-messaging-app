# 9322-preGrp3

## Project Preview:
![vlcsnap-00045](https://user-images.githubusercontent.com/90618180/235820362-275e422b-4ac7-4df3-b143-1fcfb324bf74.png)
![vlcsnap-00044](https://user-images.githubusercontent.com/90618180/235820366-75ac0dd6-d444-4713-b40b-0aba91912221.png)



## Starting Information
**First, make sure to set the project's SDK and Language level to both java version "11.0.9" and 11 - Local variable syntax for lambda parameters**

## Project Setup
1. Create a JavaFX Project inside Intellij.
2. After successfully creating the project, go into VCS and create a Git repository. (Same directory as project)
3. Go ahead and click Git and select Manage Remotes.
4. Get the HTTPS clone link of our repository and add it as one of our remotes.
5. Go to Git again and click Fetch then Pull.

### How to add JavaFX and JDBC Drivers lib
#### JDBC

To download JDBC, use the link and choose Platform Independent for the Operating System. Select the
ZIP File to download.
[Download link here](https://dev.mysql.com/downloads/connector/j/)

#### JavaFX

To download JavaFX, select the LTS for Java 11.0.9.
[Download link here](https://gluonhq.com/products/javafx/)

#### Installation
Step 1. Extract the zip file into a safe location in your PC or "C:\Program Files\***Folder Name***".<br/>
Change **Folder Name** to MySQL Connector or Java FX<br/>
Step 2. Go to IDEA and select File > Project Structure > Modules<br/>
Step 3. Click the + button on the right side window and click add JARs or Directories<br/>
Step 4. Here select all the files in your javafx/lib folder including the zip file<br/>
Step 5. Now, go to Run > Edit Configurations and add this into your VM options --module-path "path to javafx/lib" --add-modules=javafx.controls,javafx.fxml<br/>

### For JDBC:
For the MySQL Connector, choose mysql-connector-java-<'version'>-bin.jar to import as an external library.
Double check in modules if the library is imported.

### For Java FX:
For Java FX, we must import all of the jar files found in the lib folder. Navigate to javafx-sdk-<'version'>
then move to lib and import everything.

## MySQL Setup

Step 1. Run mysqld.exe from WAMPserver64 to start the server<br/>
Step 2. Run mysql.exe with root access. This will be used to add another user with privileges<br/>
Step 3. Run these commands:
``` mysql
$>CREATE USER 'cs222-pregrp3'@'localhost' IDENTIFIED BY 'prelimgroup3';
$>GRANT ALL PRIVILEGES ON * . * to 'cs222-pregrp3'@'localhost';
```
When using the database from IDEA, you can use these credentials to login.

## Import SQL File
Step 1. Go to http://localhost/phpmyadmin and login with the root account.<br/>
Step 2. Create a new database on the left-side of the interface and name it as "messenger". (leave everything as default)<br/>
Step 3. Select your newly created database in the list on the left side of the interface.<br/>
Step 4. Go to the Import tab and select browse file. Locate the SQL file included in the project and select that file.<br/>
Step 5. Leave everything as default and click the "Go" button to import the dump.

# User Accounts

These are the credentials to be used for logging in.

| user | password |
| ------ | ------ |
| test$number<span>@</span>mail.com | password |
| admin<span>@</span>mail.com | root |

### How to use JDBC
Resources:<br/>
https://www.vogella.com/tutorials/MySQLJava/article.html

# Checklist
Login, Session Management, User management and Security

Each user must login to the system before he/she can perform actions/tasks. Management of the users and groups must be done on the server application or a separate client solely for administration purposes which should be accessed by admin users only. The application must be capable of managing sessions so that a user can only communicate with the server using one transaction session and within the limits of that session only. For example, a user cannot send a message or file unless the user has properly logged in.


- [ ] Broadcast (to all users) - Users must be allowed to send a broadcast message to all other users that are logged in.


- [ ] Private Message - Any user must be allowed to send private messages to anyone. Offline users will be notified of such message/s once they login.


- [ ] Bookmarking of contacts (“favorite” contacts/groups) - Any user must be able to mark/bookmark frequently contacted users or groups for easy access.


- [ ] Conference (creation and invitation – similar to groups in messenger) - Any user must be capable of creating a conference and invite different users or groups to join the conference. Other invited users may also invite other users but only the creator of the conference is allowed to remove/kick users out of the conference.


- [ ] Searching Contacts/Groups

Auxiliary feature for broadcasting messages, sending private messages, bookmarking of
users or groups and invitation to conferences.

Other features<br />
- [ ] Sending of files<br />
- [ ] Status updates (online, idle, away from keyboard, busy, etc.)<br />
- [ ] Help module<br />

You may look into existing chat applications for features that you may want to include in your project.
