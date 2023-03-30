package io.github.lynbean.lynbot.cogs.gpt.util;

import io.github.lynbean.lynbot.util.Util;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class GPTUtil extends Util{
    public static MessageEmbed exceptionEmbed(Exception e, SlashCommandInteractionEvent event) {
        return new EmbedBuilder()
            .setAuthor("Error", null, event.getUser().getAvatarUrl())
            .setTitle(e.toString())
            .setDescription(e.getMessage())
            .build();
    }

    public static List<String> trimMessage(String message) {
        return trimMessage(message, 4096);
    }

    public static List<String> trimMessage(String message, int limit) {
        List<String> strings = new ArrayList<>();
        message = message.trim();

        String[] split = message.split("\n");
        StringBuilder builder = new StringBuilder();

        for (String s : split) {
            if (!(builder.length() + s.length() + 1 <= limit)) {
                strings.add(builder.toString());
                builder = new StringBuilder();
            }

            builder.append(s +"\n");
        }

        if (builder.length() > 0)
            strings.add(builder.toString());

        return strings;
    }
}