package utils;

import audioCore.AudioInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.awt.*;
import java.util.*;

public class Var {

    public static int volume = 25;

    public static final String Token = "MzQ1MDIxNDU4OTgyMTA5MTg0.DG4Wqw.7D_Yo9dbimGJdn-CuZCGWEowhpM";

    public static final String Prefix = "?";
    public static String input;
    public static Member member;

    public static final String[] PERMS = {"eG Founder", "eG Leader", "eG Leader League of Legends", "eG Leader GTA Online", "eG Leader PUBG", "eG Developer"};

    public static final EmbedBuilder error = new EmbedBuilder().setColor(Color.RED);
    public static final EmbedBuilder warning = new EmbedBuilder().setColor(Color.YELLOW);
    public static final EmbedBuilder noerror = new EmbedBuilder().setColor(Color.GREEN);
}
