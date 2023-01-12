package listeners;

import java.util.*;

import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Courses extends ListenerAdapter {

	public String prefix = "s!";
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		
		String message = event.getMessage().getContentRaw();
		
		ArrayList<String> course9 = new ArrayList<String>();
		
		
		if (message.equals(prefix+"courses")) {
			
		}
		
	}

}
