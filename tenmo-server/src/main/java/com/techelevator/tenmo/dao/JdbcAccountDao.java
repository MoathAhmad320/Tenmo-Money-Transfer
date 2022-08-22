package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao() {
    }

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account retrieveAccountById(long id) {
        Account account = new Account();
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?;";
        SqlRowSet rowset = jdbcTemplate.queryForRowSet(sql,id);
        if(rowset.next()){
           account = mapRowToAccount(rowset);
        } else {
            System.out.println("error mapping account by id");
        }
return account;
    }

    @Override
    public Account retrieveAccountByUserId(long id) {
        Account account = new Account();
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?;";
        SqlRowSet rowset = jdbcTemplate.queryForRowSet(sql,id);
        if(rowset.next()){
            account = mapRowToAccount(rowset);
        } else {
            System.out.println("error mapping account by id");
        }
        return account;
    }

    @Override
    public List<Account> listAccounts() {
            List<Account> accounts = new ArrayList<>();
            String sql = "SELECT account_id, user_id, balance FROM account;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while(results.next()) {
                Account account = mapRowToAccount(results);
               accounts.add(account);
            }
            return accounts;
        }


    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getLong("account_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setUserId(rs.getLong("user_id"));
        return account;
    }
}
