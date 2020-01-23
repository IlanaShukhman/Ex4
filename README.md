# The maze of waze

In this game, you can compete with friends to get the best score! 

## Getting Started

In order to run this project on your computer, clone it to your workspace directory, and follow the next steps! 

### Prerequisites

For this project to run smoothly, you will need:

1. JDK

2. JRE - be it eclipse, IntelliJ

3. Google Earth Pro - so you can see the KML files!

### Running the game

First, you will need to download this project. Make sure to built the pathes to all the JARs!

Then, create the game by calling the constructor of "Ex4_Client", which is in the "gameClient" package. On the screen,
you will have to put in your ID. The game will be save to a data base, and you will be able to see the number of games you played, your best score in each level, and your place amoung all those who played.

The second pop-up window will ask you to choose a level. 

Each level is different - number of robots, the time limit, the road map. 

Then, choose if you want to manually move the robots or not. If you choose yes, click on the robot you want to move,
and then on the destination node. If there are more than one robot, you can move them simultaneously. 
If you chose 'no', the robots will move automatically to the closest fruit, and eat as many as they can. 

In each level, you will need to achieve a certain score within a limited amount of moves. 

### Tests

To run the tests, you will need JUnit 5. The tests will help you to understand the code and avoid making mistakes!
