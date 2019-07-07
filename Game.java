 /**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */
import java.util.Random;
public class Game 
{
    private Parser parser;
    private Room currentRoom; 
    int gotweapon,gotweapon2,killed,killed2,i,a,b,a2;
    Room entrance,die2,library2;
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        gotweapon=0;
        i=0;
        killed=0;
        gotweapon2=0;
        Random r = new Random();
        int number = r.nextInt(100);         
        a=number%2;           
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room dead,dontrun,court,library,chamber,meetingRoom,bridgea,bridgeb,guards,ammuniroom,edge,coward,bridgeb1;
        // create the rooms
        //balcony = new Room("You are at the balcony! What a beautiful view of these mountains!!","balcony");
        entrance = new Room("\nWelcome! You are at the entrance of the castle, let's go save the princess!","entrance");
        court = new Room("You are in the kings court ","court");
        library = new Room("you are in library.","library");
        
        library2 = new Room("you had already killed the guards proceed towards the chamber.","library2");
        chamber = new Room("This is it! This is the chamber where the princess is confined.","chamber");
        meetingRoom = new Room("You are in the meeting room.","meetingRoom");
        bridgea = new Room("You are on the Bridge. The Bridge looks a bit broken. The only way to know if you'll survive is to try!","bridgea");
        bridgeb = new Room("\n You took your Leap of Faith, go meet the princess :) .","bridgeb");        
        guards = new Room("You are in guards room.","gaurds");
        ammuniroom = new Room("Woah!! You got a sword, this might help you later! ","ammuniroom");
        edge = new Room("Watch out! A step further and you will lose your life! ","edge");
        dontrun =new Room("Don't run you have to impress the Princess! You didn't come here to run away, go ahead!","dontrun");
        coward = new Room("Well, seems like you missed your chance! The king will send his guards after you!","coward");
        dead = new Room("You are dead.","dead");
        die2 = new Room("nothing","die2");       
        //roomsdirections(north,east,south,west)
        entrance.setExits(court, null, dontrun, null); //N,E,S,W
        court.setExits(bridgea,meetingRoom,entrance,library);
        library.setExits(chamber,court,guards,null);
        chamber.setExits(null, null,bridgeb,null);
        meetingRoom.setExits(edge,null,ammuniroom,court);
        bridgea.setExits(bridgeb,null,court,null);
        bridgeb.setExits(chamber,null,null,null);
        guards.setExits(library2,null,null,null);
        library2.setExits(chamber,court,guards,null);
        ammuniroom.setExits(meetingRoom,null,null,null);
        edge.setExits(dead,null,meetingRoom,null);
        dontrun.setExits(entrance,null,coward,null);
        die2.setExits(entrance,entrance,entrance,entrance);
        
        //return time
        
        currentRoom = entrance;  /* start game entrance*/
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to Impress the Princess");
        System.out.println("You will fight the guards and save the princess.");
        System.out.println("Be brave and keep your faith! Type 'help' if you need help.");
        System.out.println();
        System.out.println( currentRoom.getDescription());
        System.out.print("Exits: ");
        
        if(currentRoom.northExit != null) {System.out.print("north ");}
        if(currentRoom.eastExit != null) {System.out.print("east ");}
        if(currentRoom.southExit != null) {System.out.print("south ");}
        if(currentRoom.westExit != null) {System.out.print("west ");}
        System.out.println();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {System.out.println("I don't know what you mean...");return false;}
        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {            printHelp();        }
        else if (commandWord.equals("go")) {            goRoom(command);        }
        else if (commandWord.equals("quit")) {            wantToQuit = quit(command);        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("This might be difficult. But you need to reach the chamber!");
        System.out.println("Make sure you find your sword!");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println("   go      quit     help");
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();
        
        if(i==3){System.out.print("You have lost all your three lifes!Game Over");System.exit(0);}
        // Try to leave current room.
        Room nextRoom = null;
        //bridgebrandomloopfrombridgea
        if(currentRoom.name.equals("bridgea") && direction.equals("north")) {
            Random r = new Random();
            int numbers = r.nextInt(100);
            if(numbers%2==0) {System.out.print("You have lost your life!"); currentRoom = die2;i++;
            if(i==3){System.out.print("You have lost all your three lifes!Game Over");System.exit(0);}}
            else {nextRoom = currentRoom.northExit;
                currentRoom = currentRoom.northExit;}
        }
       
        
        if(currentRoom.name.equals("dontrun") && direction.equals("south")) 
                {System.out.print("Well, seems like you missed your chance! The king will send his guards after you!");System.exit(0); }
        if(currentRoom.name.equals("meetingRoom") && direction.equals("south")){
            gotweapon = 1;            
        }
        
        if( currentRoom.name.equals("court") && direction.equals("west") )
        {
            if(killed==1)
                  {System.out.print("You had killed the gaurds, waste no more time, go and save the princess! ");}
            if(killed==0){
            if(gotweapon==0 && a==0 ) 
                            {System.out.print("Warned you about the guards! You have been killed.");currentRoom = die2;i++;
                            if(i==3){System.out.println("You have lost all your three lifes!Game Over");System.exit(0);}} 
            else if(gotweapon==0 && a==1 ) 
                            {System.out.print("you were lucky there were no gaurds in the library.");a2=0;}                 
            else if(gotweapon==1 && a==0 )  
                  {System.out.print("Magnificent! What a brave fight! You used your sword and killed all the gaurds here.");
                      killed = 1;a2=1; }            
            else if(gotweapon==1 && a==1 )  
                  {System.out.print("you were lucky there were no gaurds in the library.");a2=0 ;}}           
                            
        }    
        
        
        if(currentRoom.name.equals("library")  && direction.equals("south")){
            if(gotweapon==1 && gotweapon2==0)
                    {System.out.println("Well done! You have killed the guards.");killed2 = 1;gotweapon2=1 ; }
            else if(gotweapon2==1 && killed2==1)
                    {System.out.println("You had already killed the gaurds proceed to the chamber.");}
            else if(gotweapon==0 && killed2==0)
                    {System.out.println("Oops..You have been killed by the guards.");currentRoom = die2;i++;
                    if(i==3){System.out.println("You have lost all your three lifes!Game Over");System.exit(0);}}
        } 
        if(currentRoom.name.equals("library2")  && direction.equals("south")){
            System.out.println("You had already killed the gaurds proceed to the chamber.");
        }
        
        
        if((currentRoom.name.equals("guards") &&direction.equals("north"))&& a2==1 ){
            currentRoom = library2;           
        }
        //kill gaurds going in ammuniroom
        
        
        
        
        if(currentRoom.name.equals("edge") && direction.equals("north"))
        {System.out.println("You trembled and fell off! You have to watch out! "); currentRoom = die2;i++;
        if(i==3){System.out.println("You have lost all your three lifes!Game Over");System.exit(0);}}
      
        if(currentRoom.name.equals("library") && direction.equals("north")) { currentRoom.getDescription(); 
            System.out.println("Great job! You fought the guards and found your way to the chamber where the princess was confined.");
            System.out.println("You have successfully rescued the princess! We'll see you at the castle to award you for this bravery. "); System.exit(0);}
        
        if(currentRoom.name.equals("bridgeb") && direction.equals("north")) { currentRoom.getDescription(); 
            System.out.println("Great job! You took a leap pf faith and found your way to the chamber where the princess was confined.");
            System.out.println("You have successfully rescued the princess! We'll see you at the castle to award you for this bravery. "); System.exit(0);}
        if(direction.equals("north")) {
            nextRoom = currentRoom.northExit;
        }
        if(direction.equals("east")) {
            nextRoom = currentRoom.eastExit;
        }
        if(direction.equals("south")) {
            nextRoom = currentRoom.southExit;
        }
        if(direction.equals("west")) {
            nextRoom = currentRoom.westExit;
        }

        if (nextRoom == null) {
            System.out.println("There is no door here! But there must be another way.");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getDescription());
            System.out.print("Exits: ");
            if(currentRoom.northExit != null) {
                System.out.print("north ");
            }
            if(currentRoom.eastExit != null) {
                System.out.print("east ");
            }
            if(currentRoom.southExit != null) {
                System.out.print("south ");
            }
            if(currentRoom.westExit != null) {
                System.out.print("west ");
            }
            System.out.println();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {   
        String secondWord = command.getSecondWord();
        if(secondWord=="quit" && command.getCommandWord()=="quit") {
            return true;
        }
        else if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
