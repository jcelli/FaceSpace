//Joseph Patricelli and Andrew Maguire's driver program

import java.sql.*;  
import java.text.ParseException;
import oracle.jdbc.*;
import java.util.*;
import java.text.*;

public class driver
{
	private static FaceSpace fs;

    public static void main(String[] args) throws SQLException
	{
		try
		{
			fs = new FaceSpace();
		    //after this the database is connected so loop through the different options the user has
			Scanner reader = new Scanner(System.in);
			int choice = 0;

			while(true)
			{
				System.out.println("What would you like to do?");
				System.out.println("Enter 1 to add a new user");
				System.out.println("Enter 2 to initiate a friendship");
				System.out.println("Enter 3 to establish a friendship");
				System.out.println("Enter 4 to display a user's friends");
				System.out.println("Enter 5 to create a group");
				System.out.println("Enter 6 to add a user to a group");
				System.out.println("Enter 7 to send a message to a user");
				System.out.println("Enter 8 to display a user's received messages");
				System.out.println("Enter 9 to search for a user");
				System.out.println("Enter 10 to check for a connection of 3 or less degrees");
				System.out.println("Enter 11 to display users with most sent or recieved messages");
				System.out.println("Enter 12 to remove a user from FaceSpace");
				System.out.println("Enter 13 to run the demo");
				System.out.println("Enter 14 to terminate the program");

				choice = reader.nextInt();

				if(choice == 1)
				{
					fs.createUser();
				}
				else if(choice == 2)
				{
					fs.initiateFriendship();
				}
				else if(choice == 3)
				{
					fs.establishFriendship();
				}
				else if(choice == 4)
				{
					fs.displayFriends();
				}
				else if(choice == 5)
				{
					fs.createGroup();
				}
				else if(choice == 6)
				{
					fs.addToGroup();
				}
				else if(choice == 7)
				{
					fs.sendMessageToUser();
				}
				else if(choice == 8)
				{
					fs.displayMessages();
				}
				else if(choice == 9)
				{
					fs.searchFor();
				}
				else if(choice == 10)
				{
					fs.thirdDegreeUse();
				}
				else if(choice == 11)
				{
					fs.mostMessages();
				}
				else if(choice == 12)
				{
					fs.deleteUser();
				}
				else if(choice == 13)
				{
					fs.demo();
				}
				else
				{
					break;
				}
			}
		}
		catch(Exception Ex)  
		{
		 System.out.println("Error connecting to database.  Machine Error: " + Ex.toString());
		    return;
		}
		finally
		{
			fs.closeConnection();
		}
		
		return;
	}
}