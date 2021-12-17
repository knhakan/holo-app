# Simple full stack demo application with CI/CD pipeline

A simple repository to implement a demo application for Holoride.   
The project contains both Backend written in Java/Spring Boot and frontend written in ReactJs.  
For database, one in-memory database, H2, have been used.
It is deployed onto a CI/CD pipeline installed on Github actions, Heroku.

## 🚀 Technologies  
Java 11  
Spring Boot 2.6.1  
Node 16.13.1  
ReactJS 17.0.2  
H2 database  
Docker 20.10.11  
React Router  
JWT  
Junit  
Heroku  
Github Actions

## ✨ Features
✔️  User can sign up  
✔️  User can log in the system  
✔️  User adds, deletes, edits his/her personal data  
✔️  User can see only his/her own personal data. Authorization system has been enabled.

## 👨🏻‍💻 Run Locally
- Clone the project  
  `https://github.com/knhakan/holo-app.git`  
- Go to the project directory for frontend  
  `cd demo-app`  
- Install dependencies  
  `npm install`  
- Start the server  
  `npm start`  

This will run the frontend part.  
For backend, you can simply use maven, and it will deal with all the necessary dependencies. 


## ✅ Requirements
Before starting, you need to have Git and Node installed.


### Application Installation in Docker
The application can run as a Docker container.

To run the application in docker, one must install Docker first. After the installation of Docker, install jar file of the application,
as `demo-0.0.1-SNAPSHOT.jar`, (Maven can be used to install it) and place it under target folder (target folder is placed in root
directory of the project). Precisement both in naming and in path selection is important since the Dockerfile
contains `COPY target/demo-0.0.1-SNAPSHOT.jar demoapp-service.jar` command. If the user wants to customise naming or path,
he/she should simply change the paths and the namings as desired in this command, as well.

## API documentation

API list can be found here: https://documenter.getpostman.com/view/6429348/UVJkBYir 

