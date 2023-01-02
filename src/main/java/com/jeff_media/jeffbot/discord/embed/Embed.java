package com.jeff_media.jeffbot.discord.embed;

import com.jeff_media.jeffbot.discord.Emojis;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Embed {

    private final String title;
    private final String description;
    private final List<Field> fields;
    private final String thumbnail;
    private final String footer;
    private final String footerIcon;

    public static MessageEmbed fromFile(final File file) throws IOException {
        try(FileReader reader = new FileReader(file)) {
            Map<String,Object> map = new Yaml().load(reader);
            return deserialize(map);
        } catch (FileNotFoundException e) {
            throw new IOException(e);
        }
    }

    private static MessageEmbed deserialize(final Map<String,Object> map) {
        final String title = Emojis.apply((String) map.get("title"));
        String description = null;
        if(map.containsKey("description")) {
            final Object descriptionObject = map.get("description");
            if(descriptionObject != null) {
                if (descriptionObject instanceof String) {
                    description = Emojis.apply((String) descriptionObject);
                } else if (descriptionObject instanceof List<?>) {
                    ((List<?>) descriptionObject).stream().forEach(line -> {
                        if (!(line instanceof String))
                            throw new IllegalArgumentException("Invalid string found in embed description");
                    });
                    description = Emojis.apply(((List<?>) descriptionObject).stream().map(o -> (String) o).collect(Collectors.joining(System.lineSeparator())));
                } else {
                    throw new IllegalArgumentException("Embed description must be a string or a list of strings");
                }
            }
        }
        final List<Field> fields = new ArrayList<>();
        if(map.containsKey("fields")) {
            final List<Map<String,Object>> fieldList = (List<Map<String,Object>>) map.get("fields");
            if(fieldList != null) {
                for (final Map<String, Object> fieldObject : fieldList) {
                    final Field field = Field.deserialize(fieldObject);
                    fields.add(field);
                }
            }
        }
        final String thumbnail = (String) map.get("thumbnail");
        final String footer = (String) map.get("footer");
        final String footerIcon = (String) map.get("footer-icon");
        return new Embed.Builder().buildEmbed(new Embed(title, description, fields, thumbnail, footer, footerIcon));
    }

    @RequiredArgsConstructor
    public static class Builder {

        private MessageEmbed buildEmbed(final Embed embed) {
            final EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle((embed.title));
            builder.setDescription((embed.description));
            for (final Field field : embed.fields) {
                if (field.isBlank()) {
                    builder.addBlankField(field.inline());
                } else {
                    builder.addField((field.name()), (field.text()), field.inline());
                }
            }

            final String thumbnail = (embed.thumbnail);
            if(thumbnail != null && !thumbnail.isEmpty()) {
                builder.setThumbnail(thumbnail);
            }
            builder.setFooter((embed.footer), (embed.footerIcon));

            return builder.build();
        }
    }


}
