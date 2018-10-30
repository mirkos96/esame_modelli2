#iPersonal
iPersonal is a personal dashboard for users to store their bookmarks, pins, todos, notes , diaries, expenses , add tags to it, search across entities based on keywords,tags,titles (Works best on chrome :-) )

(Inspired by iGoogle)

#Technologies Used

1. Java (Spring framework, Jersey for REST, Guice for Dependency Injection and Maven as a build tool)
2. MongoDB as a primary data store
3. ActiveMQ for message passing
4. Elasticsearch for searching
5. Spring Social for Auth(Google Plus)
6. Backbone.js for front end

#How to setup

1. Install Java, Maven, MongoDB, Elasticsearch, activeMQ
2. Set up a MongoDB username and password using http://stackoverflow.com/questions/4881208/how-to-put-username-password-in-mongodb (Alternatively use MongoLabs which provides 500MB free )
3. Add a cluster name and node name to elasticsearch in elasticsearch.yml
4. Add destination Queue for activemq
5. Copy config.properties.dist to config.properties
6. Create a google developer account to get appId and appSecret (Used for signup and login)
7. Update config.properties (let the debugMode be false unless there is a need to run test cases. Test cases should be run with clean data for mongodb and elasticsearch)
8.  Compile the code using (mvn clean install) and run using ( mvn jetty:run)
9.  Visit http://localhost:8080/iPersonal/ to see the home page

#Screenshots

![Alt text](https://github.com/sudan/iPersonal/blob/master/screenshots/one.png "Signup Screen")
![Alt text](https://github.com/sudan/iPersonal/blob/master/screenshots/two.png "Dashboard Screen")
![Alt text](https://github.com/sudan/iPersonal/blob/master/screenshots/three.png)
![Alt text](https://github.com/sudan/iPersonal/blob/master/screenshots/four.png)
![Alt text](https://github.com/sudan/iPersonal/blob/master/screenshots/five.png)
![Alt text](https://github.com/sudan/iPersonal/blob/master/screenshots/six.png)

