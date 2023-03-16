package com.kim.discordbot.util;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import okhttp3.OkHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class Util {
    private static final Logger log = BotLogger.getLogger(Util.class);
    public static final Path APPDATA = Path.of("data");

    public static OkHttpClient getHttpClient() {
        return new OkHttpClient();
    }

    public static Gson getGson() {
        return new Gson();
    }

    public static File validFile(String filename) {
        return APPDATA.toAbsolutePath().resolve(filename).toFile();
    }

    public static File validFile(String filename, String... subdirs) {
        Path path = APPDATA.toAbsolutePath();
        for (String subdir : subdirs) {
            path = path.resolve(subdir);
        }
        return path.resolve(filename).toFile();
    }

    public static InputStream loadResources(@NotNull Object clazz, String filename) {
        InputStream input = clazz.getClass().getResourceAsStream(filename);
        if (input == null) {
            return null;
        }
        return input;
    }

    public static String loadResourcesAsString(@NotNull Object clazz, String filename) {
        InputStream input = loadResources(clazz, filename);
        if (input == null) {
            return null;
        }
        return toString(input);
    }

    public static InputStream loadFile(@NotNull String filePathString) {
        File file = Path.of(filePathString).toAbsolutePath().toFile();

        if (!file.exists()) {
            return null;
        }
        try {
            Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
            return new ByteArrayInputStream(CharStreams.toString(reader)
                .getBytes());
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static String loadFileAsString(@NotNull String filename) {
        InputStream input = loadFile(filename);
        if (input == null) {
            return null;
        }
        return toString(input);
    }

    private static String toString(InputStream in) {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        ) {
            StringBuilder sb = new StringBuilder();
            reader.lines().forEach(
                line -> sb.append(line).append(System.lineSeparator())
            );
            return sb.toString().trim();
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static Set<Class<?>> lookForAnnotatedOn(String packageName, Class<? extends Annotation> annotation) {
        ClassGraph classGraph = new ClassGraph()
            .acceptPackages(packageName)
            .enableAnnotationInfo();

        try (ScanResult scanResult = classGraph.scan(3)) {
            return scanResult.getAllClasses()
                .stream()
                .filter(classInfo -> classInfo.hasAnnotation(annotation.getName())).map(ClassInfo::loadClass)
                .collect(Collectors.toSet());
        }
    }
}