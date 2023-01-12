package listeners;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class About extends ListenerAdapter {

	public String prefix = "s!";
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		
		String message = event.getMessage().getContentRaw();
		
		if (message.equals(prefix+"about")) {
			event.getChannel().sendMessage(
					"```ScheduBot is a bot developed independently by kuji,\n"
					+ "as well as fully written in Java (better language :yawn:).```").queue();
		}
		
	}

}
