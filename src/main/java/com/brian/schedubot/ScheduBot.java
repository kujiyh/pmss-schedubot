import java.sql.SQLException;

import javax.security.auth.login.LoginException;

import database.SQLiteDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import listeners.About;
import listeners.Commands;
import listeners.Set;
import listeners.ViewUser;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class ScheduBot {
	
	private final ShardManager shardManager;
	private final Dotenv config;
	
	public ScheduBot() throws LoginException, SQLException {
		
		SQLiteDataSource.getConnection();
		 
		config = Dotenv.configure().load();
		String token = config.get("TOKEN");
		DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
		builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
		builder.setActivity(Activity.playing("s!info"));
		builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
		builder.setMemberCachePolicy(MemberCachePolicy.ALL);
		builder.setChunkingFilter(ChunkingFilter.ALL);
		builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
		
		shardManager = builder.build();
		shardManager.addEventListener(new Commands());
		shardManager.addEventListener(new About());
		shardManager.addEventListener(new Set());
		shardManager.addEventListener(new ViewUser());
	}

	public ShardManager getShardManager() {
		return shardManager;
	}
	
	public static void main(String[] args) throws SQLException {
		try {
			ScheduBot bot = new ScheduBot();
		} catch (LoginException e) {
			System.out.println("Error: bot token invalid");
		}
		
	}

}