# Photo Album Creator
This is a Spring Boot application that allows users to create photo albums and add pictures to them. 
The application uses H2 database to persist data and also saves uploaded images to the disk.

# Technologies used
Java 17
Spring Boot
Spring MVC
H2 database

# Requirements:
To run the project, you will need the following installed on your system:

*JDK 17

*Maven

*MySQL

# Usage
To create a new album, click on the "New Album" button and enter the album name. 
Once you create an album, you can add pictures to it by clicking on the album name and then on the "Add Picture" button. 
You can upload pictures from your local file system by selecting them and clicking the "Upload" button. 
All uploaded pictures will be saved in the uploads directory in the project folder and their metadata will be saved in the H2 database.
After deleting an album, all the photos from the database and folder are also deleted.



