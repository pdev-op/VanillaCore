package com.pdev.vanillacore.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

public class ChatUtils {
    public static boolean shouldRemoveAd(Player player, String message) {
        if (player.hasPermission("vanillacore.bypass.antiad")) {
            return false;
        }

        Pattern pattern = Pattern.compile("\\b[0-9]{1,3}(\\.|dot|\\(dot\\)|-|;|:|,|(\\W|\\d|_)*\\s)+[0-9]{1,3}(\\.|dot|\\(dot\\)|-|;|:|,|(\\W|\\d|_)*\\s)+[0-9]{1,3}(\\.|dot|\\(dot\\)|-|;|:|,|(\\W|\\d|_)*\\s)+[0-9]{1,3}\\b");
        Pattern pattern2 = Pattern.compile("\\b[0-9]{1,3}(\\.|dot|\\(dot\\)|-|;|:|,)+[0-9]{1,3}(\\.|dot|\\(dot\\)|-|;|:|,)+[0-9]{1,3}(\\.|dot|\\(dot\\)|-|;|:|,)+[0-9]{1,3}\\b");
        Pattern pattern3 = Pattern.compile("[a-zA-Z0-9\\-.]+\\s?(\\.|dot|\\(dot\\)|-|;|:|,)\\s?(c(| +)o(| +)m|o(| +)r(| +)g|n(| +)e(| +)t|c(| +)z|c(| +)o|u(| +)k|s(| +)k|b(| +)i(| +)z|m(| +)o(| +)b(| +)i|x(| +)x(| +)x|e(| +)u|m(| +)e|i(| +)o)\\b");
        Pattern pattern4 = Pattern.compile("[a-zA-Z0-9\\-.]+\\s?(\\.|dot|\\(dot\\)|;|:|,)\\s?(com|org|net|cz|co|uk|sk|biz|mobi|xxx|eu|io)\\b");

        Matcher matcher = pattern.matcher(message);
        Matcher matcher2 = pattern2.matcher(message);
        Matcher matcher3 = pattern3.matcher(message);
        Matcher matcher4 = pattern4.matcher(message);

        if (matcher.find() || matcher2.find() || matcher3.find() || matcher4.find()) {
            return true;
        }

        return false;
    }

    public static boolean shouldReplaceAscii(Player player, String message) {
        if (player.hasPermission("vanillacore.bypass.ascii")) {
            return false;
        }

        char[] chars = message.toCharArray();

        for (char c : chars) {
            if (c >= 128) {
                return true;
            }
        }

        return false;
    }
}
