package com.mcbaka.bakacraft.Modules.System.PlayerLoginHelper;

import com.mcbaka.bakacraft.Modules.Util.MySQL;
import com.mcbaka.bakacraft.Modules.Util.MySQLHelper.SQLs;
import com.mcbaka.bakacraft.Modules.Util.MySQLHelper.ThenableChainType;
import com.mcbaka.bakacraft.ModulesLoader;
import com.mcbaka.bakacraft.Util.Whirlpool;

import java.util.Optional;
import java.util.UUID;

/**
 * This class is design for Player password authentication.
 */
public class AuthenticationUtil {

    public static boolean TryLogin(String userName, String password) {
        Optional<String> realPasswordOpt = FetchPasswordByUserName(userName);
        if (realPasswordOpt.isPresent()) {
            String realPassword = realPasswordOpt.get();
            int saltPosition = (password.length() >= realPassword.length() ? realPassword.length() - 1 : password.length());
            String salt = realPassword.substring(saltPosition, saltPosition + 12);
            String hash = Whirlpool(salt + password);
            String checkedHash = hash.substring(0, saltPosition) + salt + hash.substring(saltPosition);
            if (checkedHash.equals(realPassword)) {
                return UpdateUserPasswordByUserName(userName, checkedHash);
            }
        }

        return false;
    }

    public static boolean UpdateUserPasswordByUserName(String userName, String password) {
        return ModulesLoader.GetModule(MySQL.class)
        .target()
        .Query(SQLs.UpdateUserPassword(userName, password), ThenableChainType.CHAIN_UPDATE)
        .AffectRows() == 1;
    }

    public static Optional<String> FetchPasswordByUserName(String userName) {
        return Optional.of(
            ModulesLoader.GetModule(MySQL.class)
            .target()
            .Query(SQLs.SelectUserPassword(userName))
            .QueryResult(rs -> rs.getString("password"))
            .get(0)
        );
    }

    public static boolean IsPlayerRegistered(String playerName) {
        return ModulesLoader.GetModule(MySQL.class)
        .target()
        .Query(SQLs.CountPlayerByName(playerName))
        .QueryResult(rs -> rs.getInt("id")).size() == 1;
    }

    public static String Hash(String toHash) {
        String salt = Whirlpool(UUID.randomUUID().toString()).substring(0, 12);
        String hash = Whirlpool(salt + toHash);
        int saltPos = (toHash.length() >= hash.length() ? hash.length() - 1 : toHash.length());
        return hash.substring(0, saltPos) + salt + hash.substring(saltPos);
    }

    public static String Whirlpool(String toHash) {
        Whirlpool w = new Whirlpool();
        byte[] digest = new byte[Whirlpool.DIGESTBYTES];
        w.NESSIEinit();
        w.NESSIEadd(toHash);
        w.NESSIEfinalize(digest);
        return Whirlpool.display(digest);
    }
}
