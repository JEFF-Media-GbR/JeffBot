package com.jeff_media.jeffbot.config;

import com.jeff_media.jeffbot.Utils;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

public class Config implements Map<String, Object> {

    @Getter final Map<String, Object> map;

    public Config(String filename) {
        final File file = new File(filename);
        if (!file.exists()) {
            Utils.saveResource(filename, false);
        }

        Map<String, Object> defaultConfig = null;
        try (final InputStream inputStream = Utils.getResource(filename); final Reader reader = new InputStreamReader(Objects.requireNonNull(inputStream))) {
            defaultConfig = new Yaml().load(reader);
        } catch (final IOException exception) {
            throw new IllegalArgumentException("Could not load resource " + filename);
        } catch (final Exception exception) {
            throw new IllegalArgumentException("Invalid default config for " + filename, exception);
        }

        Map<String, Object> changedConfig = null;
        try (final InputStream inputStream = new FileInputStream(file)) {
            changedConfig = new Yaml().load(inputStream);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Could not file " + filename);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Invalid config for " + filename, exception);
        }

        map = (merge(defaultConfig, changedConfig));

    }

    private static Map<String, Object> merge(Map<String, Object> original, Map<String, Object> newValues) {
        Map<String, Object> merged = new HashMap<>(original);
        merged.putAll(newValues);
        return merged;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Nullable
    @Override
    public Object put(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ?> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @NotNull
    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @NotNull
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }
}
