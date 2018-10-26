package com.mcbaka.bakacraft.Modules.Util.MySQLHelper;

public class SQLs {
    public static String CreateDatabase(String database) {
        return String.format("CREATE DATABASE IF NOT EXISTS `%s`", database);
    }

    public final static String UseDefaultDatabase = "minecraft";

    public static String SelectUserPassword(String playerName) {
        return String.format("SELECT `id`, `password` FROM `%s` WHERE `playerName`='%s' LIMIT 1", Tables.web_accounts, playerName);
    }

    public static String UpdateUserPassword(String playerName, String password) {
        return String.format("UPDATE `%s` SET `password`= '%s' WHERE `playerName`='%s'", Tables.web_accounts, password, playerName);
    }
    public static String UpdateUserPassword(Integer id) {
        return String.format("UPDATE `%s` SET `password`= ? WHERE `id`=%d", Tables.web_accounts, id);
    }

    public final static String GetUserCount = "SELECT id FROM `web_accounts` ORDER BY `id` DESC LIMIT 1";

    public static String CountPlayerByName(String playerName) {
        return String.format("SELECT id FROM `web_accounts` WHERE playerName = '%s'", playerName);
    }
}
