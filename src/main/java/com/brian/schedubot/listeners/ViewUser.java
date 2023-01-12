package listeners;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;

import database.SQLiteDataSource;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ViewUser extends ListenerAdapter {

	public String prefix = "s!";
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		
		String[] message = event.getMessage().getContentRaw().split(" ");
		
		
		//read data
		String userID;
		User user;
		Member mem;
		String name;
		
		if (message[0].equals(prefix+"viewuser")) {
			if (message.length > 1) {
				userID = message[1];
				mem = event.getGuild().getMemberById(userID);
				user = mem.getUser();
				name = user.getName();
				System.out.println(name);
			}
			else {
				mem = event.getMember();
				userID = mem.getId();
				user = mem.getUser();
				name = user.getName();
				System.out.println(name);
			}
			try {
				if (!columnExists(userID)) {
					event.getChannel().sendMessage("User does not exist! use s!set to add a schedule first.").queue();
				}
				else {
					String[] schedule = getSched(userID).split(" ");
					String msg =  "```User "+name+"'s current schedule is:\n\n"
								+ "Block 0: "+schedule[0]+"\n\n"
								+ "Block 1: "+schedule[1]+"\n\n"
								+ "Block 2: "+schedule[2]+"\n\n"
								+ "Block 3: "+schedule[3]+"\n\n"
								+ "Block 4: "+schedule[4]+"\n\n"
								+ "Block 5: "+schedule[5]+"\n\n"
								+ "Block 6: "+schedule[6]+"```";
					event.getChannel().sendMessage(msg).queue();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			//set data
			
			//print data
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
	private String getSched(String guildID) throws SQLException {
		Connection con = SQLiteDataSource.getConnection();
		try {
			String insertSQL = "SELECT * FROM user_schedules WHERE id = "+guildID;
			PreparedStatement ps = con.prepareStatement(insertSQL);
			ResultSet rs = ps.executeQuery();
			rs.next();
			String blk0 = rs.getString("blk0");
			String blk1 = rs.getString("blk1");
			String blk2 = rs.getString("blk2");
			String blk3 = rs.getString("blk3");
			String blk4 = rs.getString("blk4");
			String blk5 = rs.getString("blk5");
			String blk6 = rs.getString("blk6");
			String schedule = blk0+" "+blk1+" "+blk2+" "+blk3+" "+blk4+" "+blk5+" "+blk6;
			rs.close();
			ps.close();
			con.close();
			return schedule;
		} catch (SQLException e) {
			con.close();
			return null;
		}
		
	}
}
