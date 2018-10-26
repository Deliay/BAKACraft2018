package com.mcbaka.bakacraft.Modules.Util.MySQLHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThenableQuery {
    private PreparedStatement stmt;
    private ResultSet resultSet;
    private int affectRows;
    private Supplier<PreparedStatement> statementSupplier;

    private ThenableChainType chainType;

    public ThenableQuery(Supplier<PreparedStatement> supplier, ThenableChainType chainType) {
        this.statementSupplier = supplier;
        this.chainType = chainType;
    }

    private void generateRealStatement() {
        stmt = statementSupplier.get();
    }

    private void finalizeStatement() {
        try {
            stmt.close();
            stmt.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Consumer(Consumer<PreparedStatement> supplier) {
        try {
            generateRealStatement();
            supplier.accept(stmt);
        } finally {
            finalizeStatement();
        }
    }

    public <T> T Function(Function<PreparedStatement, T> function) {
        try {
            generateRealStatement();
            T result = function.apply(stmt);
            return result;
        } finally {
            finalizeStatement();
        }
    }

    private void toResult() {
        try {
            switch (chainType) {
                case CHAIN_QUERY:
                    resultSet = stmt.executeQuery();
                    break;
                case CHAIN_UPDATE:
                case CHAIN_DELETE:
                case CHAIN_INSERT:
                    affectRows = stmt.executeUpdate();
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int AffectRows() {
        try {
            generateRealStatement();
            toResult();
        } finally {
            finalizeStatement();
        }
        return affectRows;
    }

    public <T> List<T> QueryResult(ThrowableFunction<ResultSet, T, SQLException> selector) {
        generateRealStatement(); toResult();
        ArrayList<T> results = new ArrayList<>();
        try {
            if (selector != null && resultSet.next()){
                T result = selector.apply(resultSet);
                if (result != null) results.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            finalizeStatement();
        }
        return results;
    }
}
