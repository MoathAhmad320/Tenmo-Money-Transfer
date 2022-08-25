package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping (path = "/transfers")
public class TransferController {

    @Autowired
    private TransferDAO transferDAO;

    public TransferController() {
    }

    public TransferController(TransferDAO transferDAO) {
        this.transferDAO = transferDAO;
    }

    @GetMapping
    public List<Transfer> listTransfers() {
        return transferDAO.listTransfers();
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestBody Transfer transfer) {
        transferDAO.create(transfer.getToAccount(), transfer.getFromAccount(), transfer.getType(), transfer.getStatus(), transfer.getAmount());
    }

    @GetMapping("/{id}")
    public Transfer retrieveTransferById(@PathVariable long id) {
        return transferDAO.retrieveTransferById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void update(@Valid @PathVariable long id, @RequestBody Transfer transfer) {
        transferDAO.updateTransfer(transfer, id);

    }
}