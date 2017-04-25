//Joseph Patricelli and Andrew Maguire's implementation of FaceSpace
import java.sql.*;  
import java.text.ParseException;
import oracle.jdbc.*;
import java.util.*;
import java.text.*;

import java.util.Calendar;
import java.text.SimpleDateFormat;

public class FaceSpace
{
	//variables to be used when dealing with the database
    private Statement statement;
    private PreparedStatement prepStatement; 
    private ResultSet resultSet;
    private String query;
    private static Connection connection; 
	
	public FaceSpace()
	{
		try
		{
			String username, password;
			username = "jdp70"; //This is your username in oracle
			password = "3841091"; //This is your password in oracle
		
			System.out.println("Registering DB..");
			// Register the oracle driver.  
	    	DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());	    

	    	System.out.println("Set url..");
	    	//This is the location of the database.  This is the database in oracle
	    	//provided to the class 
		    String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";
	    
		    System.out.println("Connect to DB..");
	    	//create a connection to DB on class3.cs.pitt.edu
	    	connection = DriverManager.getConnection(url, username, password); 
		}
		catch(Exception Ex)  
		{
		    System.out.println("Error connecting to database.  Machine Error: " + Ex.toString());
		}
	}

	public boolean closeConnection() throws SQLException
	{
		connection.close();
		return true;
	}

	public int getLastUserID()
	{
		//gets the next ID to be used in both createUser and both of the friendships for first submission
		int ret = -1;
		try
		{
			statement = connection.createStatement();
			query = "SELECT * FROM (SELECT * FROM Users ORDER BY pk DESC)";
			
			//get values from SQL statement
			resultSet = statement.executeQuery(query);
			resultSet.next();
			ret = resultSet.getInt(1);
			//get value to return from resultSet
		}
		catch(SQLException Ex) {
	    	System.out.println("Error running the query.  Machine Error: " +
		    Ex.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
		return ret;
	}

	public int getFriendshipPk(int firstID, int secondID) throws SQLException
	{
		try
		{
			statement = connection.createStatement();
			query = "SELECT * FROM Friendships";
			resultSet = statement.executeQuery(query);	

			int one;
			int two;
		
			//loop through values in resultSet to see if there is a match
			while(resultSet.next())
			{
				/*getInt(2) and getInt(3) for values to compare to the userIDs to check for friendship
				 * if the IDS equal getString(4) and compare to see what the friendship is for returns
				 */
				one = resultSet.getInt(2);
				two = resultSet.getInt(3);	

				//check to see if the IDs are both equal
				if((one == firstID && two == secondID) || (two == firstID && one == secondID))
				{
					//return the pk value from here if found
					return resultSet.getInt(1);
				}
			}
		}
		catch(SQLException Ex) {
	    	System.out.println("Error running the query.  Machine Error: " +
		    Ex.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
		return -1;
	}

	public int checkFriendshipExists(int firstID, int secondID) throws SQLException
	{
		try
		{
			//check through friendships that exist to make sure this isn't a duplicate first
			statement = connection.createStatement();
			query = "SELECT * FROM Friendships";
			resultSet = statement.executeQuery(query);	

			int one;
			int two;
		
			//loop through values in resultSet to see if there is a match
			while(resultSet.next())
			{
				/*getInt(2) and getInt(3) for values to compare to the userIDs to check for friendship
				 * if the IDS equal getString(4) and compare to see what the friendship is for returns
				 */
				one = resultSet.getInt(2);
				two = resultSet.getInt(3);	

				//check to see if the IDs are both equal
				if((one == firstID && two == secondID) || (two == firstID && one == secondID))
				{
					//now check the friendship status to see what to return
					String friendStatus;
					friendStatus = resultSet.getString(4);
					if(friendStatus.compareTo("Pending") == 0)
					{
						return 0;
					}
					else
					{
						return 1;
					}
				}
			}
		}
		catch(SQLException Ex) {
	    	System.out.println("Error running the query.  Machine Error: " +
		    Ex.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
		return -1;
	}

	public int getLastFriendshipID()
	{
		//gets the next ID to use for a friendship
		int ret = -1;
		try
		{
			statement = connection.createStatement();
			query = "SELECT * FROM (SELECT * FROM Friendships ORDER BY pk DESC) WHERE ROWNUM = 1";

			//get values from SQL statement
			resultSet = statement.executeQuery(query);
			resultSet.next();
			ret = resultSet.getInt(1);
			//get value to return from resultSet
		}
		catch(SQLException Ex) {
	    	System.out.println("Error running the query.  Machine Error: " +
		    Ex.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
		return ret;
	}


	//----------------------------------------------------------------------------
	// 
	//	Module: getLastPK(String tableName)
	//	
	//----------------------------------------------------------------------------
	public int getLastPK(String tableName)
	{
		//gets the next ID to be used in both createUser and both of the friendships for first submission
		int ret = 0;
		try
		{
			statement = connection.createStatement();
			query = "SELECT * FROM (SELECT * FROM " + tableName + " ORDER BY pk DESC)";
			
			//get values from SQL statement
			resultSet = statement.executeQuery(query);
			resultSet.next();
			ret = resultSet.getInt(1);
		}
		catch(SQLException Ex) 
		{
	    
		}
		finally
		{
			try 
			{
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} 
			catch (SQLException e) 
			{
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
		return ret;
	}
	//----------------------------------------------------------------------------


	//----------------------------------------------------------------------------
	// 
	//	1. createUser
	//	
	//----------------------------------------------------------------------------
	public void createUser() throws SQLException
	{
		Scanner reader = new Scanner(System.in);
		//get input from user about name, email, and date of birth here
		System.out.println("Creating a new user\nEnter the name of the new user: ");
		String name = reader.nextLine();
		System.out.println("Enter the email of the new user");
		String email = reader.nextLine();
		System.out.println("Getting the date of birth\nEnter the day in dd format");
		int day = reader.nextInt();
		System.out.println("Enter the month in mm format");
		int month = reader.nextInt();
		System.out.println("Enter the year in yyyy format");
		int year = reader.nextInt();

		boolean check = createUser(name, email, day, month, year);

		if(check)
		{
			System.out.println("The new user was created successfully!");
		}
		else
		{
			System.out.println("There was an error creating the new user");
		}
	}

	public boolean createUser(String name, String email, int day, int month, int year) throws SQLException
	{
		//run actual SQL to do the insert
		int userID;
		try
		{
			//get the new user ID based on the tables greatest user ID
			userID = getLastUserID();

			//prepare date to enter for the user
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
			java.sql.Date date_reg = new java.sql.Date (df.parse(year + "-" + month + "-" + day).getTime());

			/*get the current date for last login
			* do this by getting the date using java.util.date
			* then converting it to a java.sql.date in the prepStatement
			* only way to get current date and transfer to SQL date easily
			*/

			java.util.Date uDate = new java.util.Date();

			//sDate is the formatted date to use in the insert statement

			/* now have all the different values ready
			 * inserting new values into database
			 */

			query = "insert into Users values (?,?,?,?,?)";
			prepStatement = connection.prepareStatement(query);

			//add the different values into the query
			prepStatement.setInt(1, userID+1);
			prepStatement.setString(2, name);
			prepStatement.setString(3, email);
			prepStatement.setDate(4, date_reg);
			prepStatement.setTimestamp(5, new Timestamp(uDate.getTime()));

			//run the execute statement to actually add the new values to the database
			prepStatement.executeUpdate();
		}
		catch(SQLException Ex) 
		{
	    	System.out.println("Error running the insert.  Machine Error: " +
		    Ex.toString());
		} 
		catch (ParseException e) 
		{
			System.out.println("Error parsing the date. Machine Error: " +
			e.toString());
		}
		finally
		{
			try 
			{
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} 
			catch (SQLException e) 
			{
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
		
		return true;
	}
	//----------------------------------------------------------------------------


	//----------------------------------------------------------------------------
	// 
	//	2. initiateFriendship
	//	
	//----------------------------------------------------------------------------
	public void initiateFriendship()
	{
		Scanner reader = new Scanner(System.in);

		System.out.println("Attempting to initiate a friendship");
		//get input from user about the user IDs
		System.out.println("Enter the ID of the first user");
		int firstID = reader.nextInt();
		System.out.println("Enter the ID of the second user");
		int secondID = reader.nextInt();

		boolean check = initiateFriendship(firstID, secondID);

		if(check)
		{
			System.out.println("The friendship initiation completed successfully");
		}
		else
		{
			System.out.println("The friendship did not initiate correctly");
		}
	}

	public boolean initiateFriendship(int firstID, int secondID)
	{
		/* This method will do a few different things
		 * First it will check if there is a friendship already
		 * if there is it will return saying it exists already
		 * otherwise it will create the friendship initiation
		 * by setting the status value to "Pending"
		 * and assigning a pk value to the friendship
		 * based on a value from getLastFriendshipID() 
		 */

		int pkUse = getLastFriendshipID() + 1;
		try
		{
			int use = checkFriendshipExists(firstID, secondID);

			if(use > -1)
			{
				System.out.println("The friendship has already been initiated");
				return false;
			}

			//add friendship
			query = "insert into Friendships values (?,?,?,?,?)";
			prepStatement = connection.prepareStatement(query);

			//get current date to add to the final value
			java.util.Date uDate = new java.util.Date();
			java.sql.Date sqlDate = new java.sql.Date(uDate.getTime());

			//add the values into the query
			prepStatement.setInt(1, pkUse);
			prepStatement.setInt(2, firstID);
			prepStatement.setInt(3, secondID);
			prepStatement.setString(4, "Pending");
			prepStatement.setDate(5, sqlDate);

			prepStatement.executeUpdate();
		}
		catch(SQLException Ex) 
		{
	    	System.out.println("Error running the insert.  Machine Error: " +
		    Ex.toString());
		}
		finally
		{
			try 
			{
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} 
			catch (SQLException e) 
			{
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}

		return true;
	}
	//----------------------------------------------------------------------------


	//----------------------------------------------------------------------------
	// 
	//	3. establishFriendship
	//	
	//----------------------------------------------------------------------------
	public void establishFriendship() throws SQLException
	{
		Scanner reader = new Scanner(System.in);

		System.out.println("Attempting to establish a friendship");
		//get input from user about the user IDs
		System.out.println("Enter the ID of the first user");
		int firstID = reader.nextInt();
		System.out.println("Enter the ID of the second user");
		int secondID = reader.nextInt();

		boolean check = initiateFriendship(firstID, secondID);
		if(check)
		{
			System.out.println("The friendship established successfully");
		}
		else
		{
			System.out.println("The friendship did not establish correctly");
		}
	}

	public boolean establishFriendship(int firstID, int secondID)
	{
		/*
		 * This method is very similar to initiateFriendship
		 * the only difference is it makes the friendship "Established" 
		 * as opposed to "Pending"
		 * it also must remove the previous friendship if it was "Initiated"
		*/

		int pkUse = getLastFriendshipID() + 1;
		try
		{
			int use = checkFriendshipExists(firstID, secondID);

			if(use == 1)
			{
				System.out.println("The friendship has already been engaged");
				return false;
			}

			//Remove previous friendship if it was initiated
			//do this by getting pk from getFriendshipPk
			//then remove based on that
			if(use == 0)
			{
				int pkRemove = getFriendshipPk(firstID, secondID);
				query = "Delete from Friendships where PK = ?";
				prepStatement = connection.prepareStatement(query);
				prepStatement.setInt(1, pkRemove);

				prepStatement.executeUpdate();
			}

			//add friendship
			query = "insert into Friendships values (?,?,?,?,?)";
			prepStatement = connection.prepareStatement(query);

			//get current date to add to the final value
			java.util.Date uDate = new java.util.Date();
			java.sql.Date sqlDate = new java.sql.Date(uDate.getTime());

			//add the values into the query
			prepStatement.setInt(1, pkUse);
			prepStatement.setInt(2, firstID);
			prepStatement.setInt(3, secondID);
			prepStatement.setString(4, "Established");
			prepStatement.setDate(5, sqlDate);

			prepStatement.executeUpdate();
		}
		catch(SQLException Ex) 
		{
	    	System.out.println("Error running the insert.  Machine Error: " + Ex.toString());
		}
		finally
		{
			try 
			{
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} 
			catch (SQLException e) 
			{
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}

		return true;
	}
	//----------------------------------------------------------------------------


	//----------------------------------------------------------------------------
	// 
	//	4. displayFriends
	//	
	//----------------------------------------------------------------------------
	public void displayFriends()
	{
		try
		{
			Scanner reader = new Scanner(System.in);
			System.out.println("Displaying a user's friendships...\nEnter the id of a user: ");
			int userID = Integer.parseInt(reader.nextLine());

			statement = connection.createStatement();
			query = "SELECT * FROM Friendships WHERE fk_user1 = " + userID + " OR fk_user2 = " + userID;
			resultSet = statement.executeQuery(query);

			System.out.println("---------------------------");
	        System.out.println("User " + userID + "'s Friendships");
	        System.out.println("---------------------------");
			while (resultSet.next())
			{
				
				System.out.println("User " + resultSet.getInt(2) + " is friends with " + "User " + resultSet.getInt(3) + ".");
			}
			System.out.println("---------------------------");
			System.out.println();


			resultSet.close();
		}
		catch(SQLException Ex) 
		{
	    	System.out.println("Error running the query.  Machine Error: " + Ex.toString());
		}
		finally
		{
			try 
			{
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} 
			catch (SQLException e) 
			{
				System.out.println("Cannot close Statement. Machine error: "+ e.toString());
			}
		}
	}
	//----------------------------------------------------------------------------


	//----------------------------------------------------------------------------
	// 
	//	5. createGroup
	//	
	//----------------------------------------------------------------------------
	public void createGroup()
	{
		try
		{
			Scanner reader = new Scanner(System.in);
			System.out.println("Creating a group...\nEnter a name for your new group: ");
			String groupName = reader.nextLine();
			System.out.println("Enter a description for your new group: ");
			String groupDescription = reader.nextLine();
			System.out.println("Enter a membership limit for your new group (1 - 50): ");
			int membershipLimit = Integer.parseInt(reader.nextLine());
			int newPK = getLastPK("Groups") + 1;

			statement = connection.createStatement();
			query = "INSERT INTO Groups VALUES(" + newPK + ", '" + groupName + "', '" + groupDescription + "', '" + membershipLimit + "')";
	        resultSet = statement.executeQuery(query);
	        connection.commit();
	        resultSet.close();

	        System.out.println("Group added!\n");
	    }
        catch(SQLException Ex) 
        {
            System.out.println("Machine Error: " + Ex.toString());
        }
        finally
        {
            try 
            {
                if (statement != null) statement.close();
                if (prepStatement != null) prepStatement.close();
            } 
            catch (SQLException e) 
            {
                System.out.println("Machine error: " + e.toString());
            }
        }
	}
	//----------------------------------------------------------------------------


	//----------------------------------------------------------------------------
	// 
	//	6. addToGroup
	//	
	//----------------------------------------------------------------------------
	public void addToGroup()
	{
		try
		{
			Scanner reader = new Scanner(System.in);
			System.out.println("Adding a user to a group...\nEnter a user's id: ");
			int userID = Integer.parseInt(reader.nextLine());
			System.out.println("Enter a group id to add user " + userID + " to: ");
			int groupID = Integer.parseInt(reader.nextLine());
			int newPK = getLastPK("GroupMemberships") + 1;

			statement = connection.createStatement();
			query = "INSERT INTO GroupMemberships VALUES(" + newPK + ", " + groupID + ", " + userID + ")";
	        resultSet = statement.executeQuery(query);
	        connection.commit();
	        resultSet.close();

	        System.out.println("User added!\n");
	    }
        catch(SQLException Ex) 
        {
            System.out.println("Machine Error: " + Ex.toString());
        }
        finally
        {
            try 
            {
                if (statement != null) statement.close();
                if (prepStatement != null) prepStatement.close();
            } 
            catch (SQLException e) 
            {
                System.out.println("Machine error: " + e.toString());
            }
        }
	}
	//----------------------------------------------------------------------------


	//----------------------------------------------------------------------------
	// 
	//	7. sendMessageToUser
	//	
	//----------------------------------------------------------------------------
	public void sendMessageToUser()
	{
		try
		{
			Scanner reader = new Scanner(System.in);
			System.out.println("Sending a message...\nEnter the subject of your new message (up to 32 characters): ");
			String subject = reader.nextLine();
			System.out.println("Enter the body of your new message (up to 100 characters): ");
			String body = reader.nextLine();
			System.out.println("Enter the sender's id: ");
			int sender = Integer.parseInt(reader.nextLine());
			System.out.println("Enter the recipients's id: ");
			int recipient = Integer.parseInt(reader.nextLine());

			int newPK = getLastPK("UserMessages") + 1; //calculate next pk value
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Format date_sent
            String date_sent = formatter.format(Calendar.getInstance().getTime()); // Format date_sent

			statement = connection.createStatement();
			query = "INSERT INTO UserMessages VALUES(" + newPK + ", '" + subject + "', '" + body + "', " + sender + ", " + recipient + ", DATE '" + date_sent + "')";
	        resultSet = statement.executeQuery(query);
	        connection.commit();
	        resultSet.close();

	        System.out.println("Message sent!");
	    }
        catch(SQLException Ex) 
        {
            System.out.println("Machine Error: " + Ex.toString());
        }
        finally
        {
            try 
            {
                if (statement != null) statement.close();
                if (prepStatement != null) prepStatement.close();
            } 
            catch (SQLException e) 
            {
                System.out.println("Cannot close Statement. Machine error: " + e.toString());
            }
        }
	}
	//----------------------------------------------------------------------------


	//----------------------------------------------------------------------------
	// 
	//	8. displayMessages
	//	
	//----------------------------------------------------------------------------
	public void displayMessages()
	{
		try
		{
			Scanner reader = new Scanner(System.in);
			System.out.println("Looking up user's received messages...\nEnter a user's id to view their received messages: ");
			int userID = Integer.parseInt(reader.nextLine());

			statement = connection.createStatement();
			query = "SELECT * FROM UserMessages WHERE recipient = " + userID;
	        resultSet = statement.executeQuery(query);

	        while (resultSet.next())
	        {
	        	System.out.println("---------------------------");
	        	System.out.println("Message ID: " + resultSet.getInt(1));
	        	System.out.println("---------------------------");
	        	System.out.println("From: User " + resultSet.getInt(4));
	        	System.out.println("To: User " + resultSet.getInt(5));
	        	System.out.println("Subject: " + resultSet.getString(2));
	        	System.out.println("Body: " + resultSet.getString(3));
	        	System.out.println("Date Sent: " + resultSet.getString(6));
	        	System.out.println("---------------------------");
	        	System.out.println();
	        }

	        resultSet.close();
	    }
        catch(SQLException Ex) 
        {
            System.out.println("Machine Error: " + Ex.toString());
        }
        finally
        {
            try 
            {
                if (statement != null) statement.close();
                if (prepStatement != null) prepStatement.close();
            } 
            catch (SQLException e) 
            {
                System.out.println("Cannot close Statement. Machine error: " + e.toString());
            }
        }
	}

	//----------------------------------------------------------------------------
	// 
	//	9. Search for a user
	//	
	//----------------------------------------------------------------------------

	public void searchFor()
	{
		//just get the string to search for
		Scanner reader = new Scanner(System.in);

		System.out.println("Enter the String you would like to search for");
		String search = reader.nextLine();

		searchForUsers(search);
	}

	public boolean searchForUsers(String find)
	{
		try
		{
			//split the string into an array based on " "
			String[] search = find.split(" ");

			//create the query which needs a loop because of the variable nature
			query = "(select * from users where name Like ? or email Like ? or dob like ?) ";
			

			for(int i = 1; i < search.length; i++)
			{
				//add more to the string if required
				query = query + "Union (select * from users where name Like ? or email Like ? or dob like ?)";
			}

			//now make it a prepared statement and insert the values
			prepStatement = connection.prepareStatement(query);
			for(int i = 0; i < search.length; i++)
			{
				prepStatement.setString((i * 3 + 1), "%" + search[i] + "%");
				prepStatement.setString((i * 3 + 2), "%" + search[i] + "%");
				prepStatement.setString((i * 3 + 3), "%" + search[i] + "%");
			}
			
			resultSet = prepStatement.executeQuery();

			System.out.println("Here is a list of the search results");
			int count = 1;
			while(resultSet.next())
			{
				System.out.println(count + ". " + resultSet.getString(2));
				count++;
			}
		}
		catch(SQLException Ex) 
        {
            System.out.println("Machine Error: " + Ex.toString());
        }
        finally
        {
            try 
            {
                if (statement != null) statement.close();
                if (prepStatement != null) prepStatement.close();
            } 
            catch (SQLException e) 
            {
                System.out.println("Cannot close Statement. Machine error: " + e.toString());
            }
        }
        return true;
	}


	//----------------------------------------------------------------------------
	// 
	//	10. threeDegrees
	//	
	//----------------------------------------------------------------------------

	public boolean thirdDegreeUse()
	{
		//get the userIds of the two users to search for thirdDegree between
		Scanner reader = new Scanner(System.in);
		System.out.println("Checking to see if users are within three degrees of friendships");
		
		System.out.println("Enter the user id of the first user to use");
		int user1 = reader.nextInt();

		System.out.println("Enter the user id of the second user to use");
		int user2 = reader.nextInt();
		
		if(!threeDegrees(user1, user2))
		{
			System.out.println("The users were not connected with three degrees of friendship");
			return false;
		}

		return true;
	}

	public boolean threeDegrees(int user1, int user2)
	{
		//set round to be one in driver program

		/* For this method first check to make sure the users are not friends
		 * Next begin checking their friends
		 * Do this recursively until there is either a match or
		 */

		//semi special case already friends
		try
		{
			query = "select * from friendships where (fk_user1 = ? and fk_user2 = ?) or (fk_user2 = ? and fk_user1 = ?)";
			prepStatement = connection.prepareStatement(query);
			prepStatement.setInt(1, user1);
			prepStatement.setInt(2, user2);
			prepStatement.setInt(3, user2);
			prepStatement.setInt(4, user1);
			resultSet = prepStatement.executeQuery();

			//now check if it is empty go through and find friends to check with
			if(resultSet.next())
			{
				System.out.println("The two users are friends!");
				return true;
			}

			//now go through user 1 and user 2's friends
			query = "select * from friendships where fk_user1 = ?";
			prepStatement = connection.prepareStatement(query);
			prepStatement.setInt(1, user1);
			resultSet = prepStatement.executeQuery();

			while(resultSet.next())
			{
				if(thirdDegreeRound(resultSet.getInt(3), user2, 2))
				{
					//get user 1's name and output it
					query = "select * from users where pk = ?";
					PreparedStatement getName = connection.prepareStatement(query);
					getName.setInt(1, user1);
					ResultSet lastName = getName.executeQuery();
					lastName.next();
					System.out.println(" is friends with " + lastName.getString(2));
					return true;
				}
			}
		}
		catch(SQLException Ex) {
	    	System.out.println("Error running the insert.  Machine Error: " +
		    Ex.toString());
		} 
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
				return false;
			}
		}
		return false;
	}

	public boolean thirdDegreeRound(int userStart, int userLook, int round)
	{
		try
		{
			//just like before check for friendship
			//after basically just repeat the previous method until friend found for round 2 and 3
			//first check for friends then continue checking friends of friends if round > 3 return 
			if(round > 3)
			{
				return false;
			}

			query = "select * from friendships where (fk_user1 = ? and fk_user2 = ?) or (fk_user2 = ? and fk_user1 = ?)";
			PreparedStatement prep;
			prep = connection.prepareStatement(query);
			prep.setInt(1, userStart);
			prep.setInt(2, userLook);
			prep.setInt(3, userLook);
			prep.setInt(4, userStart);
			ResultSet thanksScope = prep.executeQuery();

			//now check if it is empty go through and find friends to check with
			if(thanksScope.next())
			{
				//query to get the names of users to print
				String nameQuery = "select * from users where pk = ?";
				PreparedStatement use = connection.prepareStatement(nameQuery);
				use.setInt(1, userLook);
				ResultSet temp = use.executeQuery();
				temp.next();
				String first = temp.getString(2);

				//get other user's name
				use = connection.prepareStatement(nameQuery);
				use.setInt(1, userStart);
				temp = use.executeQuery();
				temp.next();
				String second = temp.getString(2);

				System.out.print(first + " is friends with " + second);
				return true;
			}

			//now go through userStarts friends searching for userLook
			query = "select * from friendships where fk_user1 = ?";

			prep = connection.prepareStatement(query);
			prep.setInt(1, userStart);
			ResultSet temp = prep.executeQuery();

			while(temp.next())
			{
				if(thirdDegreeRound(temp.getInt(3), userLook, ++round))
				{
					System.out.println(" is friends with " + temp.getString(3));
					return true;
				}
			}


		}
		catch(SQLException Ex) {
	    	System.out.println("Error running the insert.  Machine Error: " +
		    Ex.toString());
		}
		
		return false;
	}


	//----------------------------------------------------------------------------
	// 
	//	11. topMessages
	//	
	//----------------------------------------------------------------------------
	public boolean topMessages(int k, int x)
	{
		/*get the top k users who have sent the most messages
		 *in the past x months
		 *first get the messages from x months
		 *group by k users sort low to high
		 *resultset.next while less thank 
		 */

		/* first step is to get todays date 
		 * then subtract x%12 months from the curr month
		 * subtract x/12 years from current year
		 */

		try
		{
			java.util.Date date = new java.util.Date();
			Calendar cal = Calendar.getInstance();
   			cal.setTime(date);
    		int year = cal.get(Calendar.YEAR);
    		int month = cal.get(Calendar.MONTH);
    		int day = cal.get(Calendar.DAY_OF_MONTH);

			//do some weird math because years
			if(month - (x % 12) < 0)
			{
				year--;
				int subFromMonth = x%12 - month;
				month = 12 - subFromMonth;
			}

			year = year - (x/12);

			//now we can make the date we wish to search for messages after
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
			java.sql.Date date_reg = new java.sql.Date (df.parse(year + "-" + month + "-" + day).getTime());

			//now we can do a query for messages with a date later than date_reg
			query = "SELECT users.pk, count(UserMessages.pk) from Users join Usermessages on (Users.pk = Usermessages.sender) join Usermessages on (Users.pk = UserMessages.recipient) group by Users.pk order by count(UserMessages.pk) desc";

			

			//get the pks from user of top ones and print out the names
			prepStatement = connection.prepareStatement(query);
			resultSet = prepStatement.executeQuery();

			int count = 0;
			System.out.println("The names of the " + k + " users who sent the most messages in the past " + x + " months are ");
			PreparedStatement temp;
			while(resultSet.next())
			{
				if(count == k)
				{
					break;
				}
				
				String nameQuery = "Select name from Users where pk = ?";

				//do preparedstatement for query
				temp = connection.prepareStatement(nameQuery);
				temp.setInt(1, resultSet.getInt(1));

				ResultSet here = temp.executeQuery();

				while(here.next())
				{
					System.out.println((count + 1) + " " + here.getString(1));
				}
				count++;
			}
			
		}
		catch(SQLException Ex) {
	    	System.out.println("Error running the insert.  Machine Error: " +
		    Ex.toString());
		} catch (ParseException e) {
			System.out.println("Error parsing the date. Machine Error: " +
			e.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
				return false;
			}
		}
		return true;
	}

	public void mostMessages() throws SQLException
	{
		Scanner reader = new Scanner(System.in);

		//get input from user about months and users
		System.out.println("How many users would you like to see?");
		int users = reader.nextInt();
		System.out.println("How many months back would you like to go?");
		int months = reader.nextInt();

		boolean check = topMessages(users, months);
		if(!check)
			System.out.println("An error occured during runtime");
	}


	//----------------------------------------------------------------------------
	// 
	//	12. dropUser
	//	
	//----------------------------------------------------------------------------

	public boolean dropUser(int userID)
	{
		//delete all instances of this pk in all tables except messages,
		// where it should only be deleted if both users are deleted
		try
		{
			//finally delete the user
			query = "delete from Users where pk = ?";
			prepStatement = connection.prepareStatement(query);
			prepStatement.setInt(1, userID);
			prepStatement.executeUpdate();
		}
		catch(SQLException Ex) {
	    	System.out.println("Error running the insert.  Machine Error: " +
		    Ex.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
				return false;
			}
		}
		return true;
	}

	public void deleteUser() throws SQLException
	{
		Scanner reader = new Scanner(System.in);

		//get the user id from the user
		System.out.println("Enter the userID of the user to delete");
		int user = reader.nextInt();
	}

	//----------------------------------------------------------------------------
	// 
	//	12. dropUser
	//	
	//----------------------------------------------------------------------------

	public void demo() throws SQLException
	{
		//just run all of the methods once
		createUser("John Q", "jlq53@pitt.edu", 11, 03, 1995);
		System.out.println("Added user John Q");

		initiateFriendship(101, 2);
		System.out.println("Initiated friendship between John Q and Marilyn Medina");

		establishFriendship(101, 3);
		System.out.println("Established friendship between John Q and Jane Day");

		displayFriends();

		createGroup();

		addToGroup();

		sendMessageToUser();

		displayMessages();

		searchFor();

		thirdDegreeUse();

		mostMessages();

		deleteUser();
		System.out.println("This is the end of the demo");
	}
	//----------------------------------------------------------------------------
}