package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcTransferDAO implements TransferDAO{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDAO() {
    }

    public JdbcTransferDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> listTransfers() {

        List<Transfer> transfers = new ArrayList<>();
            String sql = "SELECT * FROM transfer;";
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while(results.next()) {
                Transfer transfer = mapRowToTransfer(results);
               transfers.add(transfer);
            }
            return transfers;
        }

        public Transfer retrieveTransferById(long id){
        Transfer transfer = new Transfer();
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";
        SqlRowSet rowset = jdbcTemplate.queryForRowSet(sql,id);
        if (rowset.next()){
            transfer = mapRowToTransfer(rowset);
        } else {
            System.out.println("error mapping account by id");
        }
        return transfer;

        }

    @Override
    public boolean create(long toAccount, long fromAccount, int type, int status, BigDecimal amount) {

        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?,?,?) RETURNING transfer_id;";
        try {
            jdbcTemplate.queryForObject(sql, Long.class, type, status, fromAccount, toAccount, amount);
        } catch (DataAccessException e) {
            return false;
        }
        return true;
    }

    public void updateTransfer (Transfer transfer, long id) {
        String sql = "UPDATE transfer SET transfer_id = ?, transfer_type_id = ?, transfer_status_id = ?, account_from = ?, account_to = ?, amount = ? WHERE transfer_id = ?;";
        int rowsupdated = jdbcTemplate.update(sql,transfer.getId(),transfer.getType(),transfer.getStatus(),transfer.getFromAccount(),transfer.getToAccount(),transfer.getAmount(),id);
        if(rowsupdated != 1){
            System.out.println("error updating account by id");
        }
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setId(rs.getLong("transfer_id"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        transfer.setFromAccount(rs.getLong("account_from"));
        transfer.setToAccount(rs.getLong("account_to"));
        transfer.setType(rs.getInt("transfer_type_id"));
        transfer.setStatus(rs.getInt("transfer_status_id"));
        return transfer;
    }
}
