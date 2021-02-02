# 9322-preGrp3

## Starting Information
**First, make sure to set the project's SDK and Language level to both java version "11.0.9" and 11 - Local variable syntax for lambda parameters**

###How to add JavaFX and JDBC Drivers lib
####JDBC

To download JDBC, use the link and choose Platform Independent for the Operating System. Select the
ZIP File to download.
[Download link here](https://dev.mysql.com/downloads/connector/j/)

#### JavaFX

To download JavaFX, select the LTS for Java 11.0.9.
[Download link here](https://gluonhq.com/products/javafx/)

####Installation
Step 1. Extract the zip file into a safe location in your PC or "C:\Program Files\***Folder Name***".<br/>
Change **Folder Name** to MySQL Connector or Java FX<br/>
Step 2. Go to IDEA and select File > Project Structure > Libraries<br/>
Step 3. Here, we can now import the Libraries that we'll use.<br/>

###For JDBC:
For the MySQL Connector, choose mysql-connector-java-<'version'>-bin.jar to import as an external library.
Double check in modules if the library is imported.

###For Java FX:
For Java FX, we must import all of the jar files found in the lib folder. Navigate to javafx-sdk-<'version'> <br/>
then move to lib and import everything.

## MySQL Setup

Step 1. Run mysqld.exe from WAMPserver64 to start the server<br/>
Step 2. Run mysql.exe with root access. This will be used to add another user with privileges<br/>
Step 3. Run these commands:
```mysql
$>CREATE USER 'cs222-pregrp3'@'localhost' IDENTIFIED BY 'prelimgroup3';
$>GRANT ALL PRIVILEGES ON * . * to 'cs222-pregrp3'@'localhost';
```

When using the database from IDEA, you can use these credentials to login.

###How to use JDBC
Resources:<br/>
https://www.vogella.com/tutorials/MySQLJava/article.html