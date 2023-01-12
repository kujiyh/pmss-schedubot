package listeners;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;

import database.SQLiteDataSource;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Set extends ListenerAdapter {

	public String prefix = "s!";
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		
		String[] message = event.getMessage().getContentRaw().split(" ");
		
		//read data
		if (message[0].equals(prefix+"set")) {
			int size = message.length-1;
			if (size % 2 != 0) {
				event.getChannel().sendMessage("Invalid input").queue();
			}
			else {
				try {
					String[] blocks = new String[size/2];
					String[] course = new String[size/2];
					for (int i = 0; i < size/2; i++) {
						blocks[i] = message[2*i+1];
						course[i] = message[2*i+2];
					}
					User user = event.getAuthor();
					
					//set data
					String userID = user.getId();
					
					if (columnExists(userID)) {
						updateData(userID, blocks, course);
					}
					else {
						setData(userID, blocks, course);
					}
					event.getChannel().sendMessage("Successfully updated schedule").queue();
				} catch (NumberFormatException | IndexOutOfBoundsException | SQLException e) {
					e.printStackTrace();
					event.getChannel().sendMessage("smth went wrong lmao").queue();
				}
				
			}
			
		}
		
	}
	private boolean columnExists(String guildID) throws SQLException {
		Connection con = SQLiteDataSource.getConnection();
		try {
			String insertSQL = "SELECT EXISTS(SELECT * from user_schedules WHERE id="+guildID+")";
			PreparedStatement ps = con.prepareStatement(insertSQL);
			ResultSet rs = ps.executeQuery();
			rs.next();
		    int n = rs.getInt(1);
		    rs.close();
		    ps.close();
		    con.close();
		    return n != 0;
		} catch (SQLException e) {
			con.close();
			return false;
		}
	}
	private void updateData(String guildID, String[] blk, String[] course) throws SQLException {
		Connection con = SQLiteDataSource.getConnection();
		try {
			String tempblks = "";
			for (int i = 0; i < blk.length; i++) {
				tempblks+=" blk"+blk[i]+" = ?";
				if (i != blk.length-1) {
					tempblks+=",";
				}
			}
			String insertSQL = "UPDATE user_schedules set"+tempblks+" WHERE id = ?";
			PreparedStatement ps = con.prepareStatement(insertSQL);
			for (int i = 0; i < blk.length; i++) {
				ps.setString(i+1, course[i]);
			}
			ps.setString(blk.length+1, guildID);
			ps.execute();
		    ps.close();
		    con.close();
		} catch (SQLException e) {
				e.printStackTrace();
				con.close();
			}
		
	} 
	private void setData(String guildID, String[] blk, String[] course) throws SQLException {
		Connection con = SQLiteDataSource.getConnection();
		try {
			String tempblks = "";
			String tempvals = "";
			for (int i = 0; i < blk.length; i++) {
				tempblks+=", blk"+blk[i];
			}
			for (int i = 0; i <= blk.length; i++) {
				tempvals+="?";
				if (i != blk.length) {
					tempvals+=",";
				}
			}
			con = SQLiteDataSource.getConnection();
			String insertSQL = "INSERT INTO user_schedules(id"+tempblks+") VALUES("+tempvals+")";
			PreparedStatement ps = con.prepareStatement(insertSQL);
			ps.setString(1, guildID);
			for (int i = 0; i < blk.length; i++) {
				ps.setString(i+2, course[i]);
			}
			ps.execute();
		    ps.close();
		    con.close();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		    con.close();
		}
	}

}
