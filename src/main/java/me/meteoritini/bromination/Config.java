package me.meteoritini.bromination;

import me.meteoritini.bromination.module.Bridge;
import me.meteoritini.bromination.util.Chroma;
import me.meteoritini.bromination.util.Log;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Config {
    private static final int VERSION = 108;

    private static void parseConfigEntry(String line, int number) {
        String[] tokens = line.split(" = ");
        if(tokens[0].equals("VERSION")) return;
        if(tokens[0].equals("COLOURS")) {
            String[] colours = tokens[1].split(" ");
            for(int i = 0; i < colours.length; ++i) if(Chroma.isValidColour(colours[i])) Bridge.colourConf[i] = colours[i];
            return;
        }
        Bridge.users.add(line);
    }

    private static File getConfigFile() throws IOException {
        File fOld = FabricLoader.getInstance().getConfigDir().resolve("bridge.conf").toFile();
        File f = FabricLoader.getInstance().getConfigDir().resolve("bromination.conf").toFile();
        if(fOld.exists()) {
            if(f.exists()) fOld.delete();
            else fOld.renameTo(f);
        }
        if(!f.exists()) {
            f.getParentFile().mkdirs();
            f.createNewFile();
        }
        return f;
    }

    public static void load() throws IOException {
        File f = getConfigFile();
        Scanner reader = new Scanner(f);
        try {
            int i = 0;
            Bridge.users.clear();
            while (reader.hasNextLine()) {
                parseConfigEntry(reader.nextLine(), i++);
            }
        } catch (Exception e) {
            Log.log("Error reading config: " + e, 2);
        }
        reader.close();
    }
    public static void save() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("VERSION = ").append(VERSION).append("\nCOLOURS =");
        for(String s : Bridge.colourConf) sb.append(" ").append(s);
        for(String user : Bridge.users) {
            sb.append('\n').append(user);
        }
        File f = getConfigFile();
        PrintWriter writer = new PrintWriter(f, StandardCharsets.UTF_8);
        writer.write(sb.toString());
        writer.close();
    }
}
