package listeners;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {

	public String prefix = "s!";
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		
		String message = event.getMessage().getContentRaw();
		
		if (message.equals(prefix+"info")) {
			event.getChannel().sendMessage(
					"```s!about\n"
					+ "-> About the bot.\n\n"
					+ "s!info\n"
					+ "-> Gives list of commands and how to use them.\n\n"
					+ "s!courses\n"
					+ "-> Gives list of course names.\n\n"
					+ "s!set <block> <course name> <block> <course name> ... \n"
					+ "-> Sets course(s) to block(s) specified.\n"
					+ "IMPORTANT: no spaces in the course name, or my pc will explode thx\n\n"
					+ "s!viewuser <userID>\n"
					+ "-> Displays user's current schedule.\n"
					+ "   A block looks like this: room + course\n"
					+ "   Example: 323IBSCI10 -> rm 323, ib science 10\n\n"
					+ "s!viewblock <block>```").queue();
		}
		
	}

}
