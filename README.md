#### Projects created with jdk 1.8 and javafx for GUI
- chat - allows user to send encrypted messages to given addres
	1. library - handles all bussiness logic - socket binding, listening on the socket, sending encrypted messages (RSA) to the given address (on setting up connection RSA public key is send)
	2. app - javafx desktop app which uses the library
- file-browser - lets user browse their files and open them in the application if they are jpg, png or encoded with UTF-8.\
Additionally stores opened files in a weak references map.
- office-queue-management-system - jmx application, contains 2 main parts:
	1. Department - provides each applicant with a ticket on request, if department has any free posts applicant is assigned to a post and occupies it for a period of time, then applicant's ticket is removed and post is free. If there is no available post then applicant is moved to the end of the queue and awaits for their time.\
  Department informs all applicants of current status of posts, their tickets and the queue. Tickets of 2 priorities are available (low and high)
	2. Applicant - requests ticket from department and recives information about depratment and ticket status
- nasa-api-fetcher - gets data from NASA OPEN APIs: 
	1. APOD - picture of the day 
	2. AsteroidsNewWs - information about asteroids in given period of time. \
Javafx application implements internationalization - English and Polish versions of GUI.\
https://api.nasa.gov/
- photo-transformer-graalvm - uses graalvm for jdk 1.8. Java application gets graalvm context, which is provided with Java object implementing GUI and loads pointed by user script to transform loaded picture. \
Provided simple set of picture tsnsformers in js: median_filter, negative and thresholding
