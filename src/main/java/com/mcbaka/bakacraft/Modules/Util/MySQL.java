package com.mcbaka.bakacraft.Modules.Util;

import com.mcbaka.bakacraft.Modules.AbstractModule;
import com.mcbaka.bakacraft.Modules.Util.MySQLHelper.SQLs;
import com.mcbaka.bakacraft.Modules.Util.MySQLHelper.ThenableChainType;
import com.mcbaka.bakacraft.Modules.Util.MySQLHelper.ThenableQuery;
import com.mcbaka.bakacraft.Util.IConfigurationHolder;
import com.mcbaka.bakacraft.Util.InjectConfigRoot;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySQL extends AbstractModule implements IConfigurationHolder {

    private SqlService sqlService;
    private String dsnString;

    @InjectConfigRoot
    public ConfigurationNode config;

    public void initialSqlService() {
        if (sqlService == null) {
            Sponge.getServiceManager().provide(SqlService.class).ifPresent(p -> sqlService = p);
        }
    }

    public DataSource getDataSource(String dsnUrl) throws SQLException {
        return sqlService.getDataSource(dsnUrl);
    }

    @Override
    public void Load() {
        initialSqlService();
        ConfigurationNode config = this.config.getNode("mysql", "connection");
        //get sql username and password from configuration file
        String sqlUser = config.getNode("username").getString("root");
        String sqlPassword = config.getNode("password").getString();
        String sqlDriver = config.getNode("driver").getString("mysql");
        String sqlHost = config.getNode("host").getString("localhost");
        String sqlPort = config.getNode("port").getString("3306");
        String sqlDatabase = config.getNode("database").getString("minecraft");
        dsnString = sqlService.getConnectionUrlFromAlias(sqlDriver).orElse("mysql");
        if (dsnString.length() > 0) {
            logger.info("Using Sponge global DSN Connection string");
        } else {
            dsnString = String.format(
                    "jdbc:%s://%s:%s@%s:%s",
                    sqlDriver,
                    sqlUser, sqlPassword,
                    sqlHost, sqlPort
            );
            logger.info("Join DSN Connection string from config file");
        }
        logger.info("Testing Database connection");
        //a test sql query will be executed to check service healthy
        Query(SQLs.CreateDatabase(sqlDatabase))
        .Consumer(stmt -> {
            try {
                stmt.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        logger.info("Done.");
    }

    public ThenableQuery Query(String sql) {
        return new ThenableQuery(() -> {
            try {
                Connection conn = this.getDataSource(dsnString).getConnection();
                return conn.prepareStatement(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }, ThenableChainType.CHAIN_QUERY);
    }

    public ThenableQuery Query(String sql, ThenableChainType chainType) {
        return new ThenableQuery(() -> {
            try {
                Connection conn = this.getDataSource(dsnString).getConnection();
                return conn.prepareStatement(sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }, chainType);
    }

    @Override
    public void Unload() {

    }

    @Override
    public void initializeConfig(ConfigurationNode node) {
        ConfigurationNode connection = node.getNode("mysql", "connection");
        connection.getNode("username").setValue("root");
        connection.getNode("password").setValue("");
        connection.getNode("driver").setValue("mysql");
        connection.getNode("host").setValue("localhost");
        connection.getNode("port").setValue(3306);
        connection.getNode("database").setValue("minecraft");
    }
}
