package edu.eci.arsw.exams.moneylaunderingapi;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingException;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/fraud-bank-accounts")
public class MoneyLaunderingController {

    @Autowired
    MoneyLaunderingService moneyLaunderingService;


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGETRecursoAccounts() throws MoneyLaunderingException {
        return new ResponseEntity<>(moneyLaunderingService.getSuspectAccounts(), HttpStatus.ACCEPTED);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> manejadorPOSTRecursoAccounts(@RequestBody SuspectAccount sa)
    {
        try {
            moneyLaunderingService.addSuspectAccount(sa);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MoneyLaunderingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }


    @RequestMapping(value="/{accountId}",method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGETRecursoAccountsID(@PathVariable String accountId)
    {
        try {
            return new ResponseEntity<>(moneyLaunderingService.getAccountStatus(accountId), HttpStatus.ACCEPTED);
        } catch (MoneyLaunderingException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value="/{accountId}", method = RequestMethod.PUT)
    public ResponseEntity<?> manejadorPOSTRecursoAccountsID(@RequestBody SuspectAccount sa) {
        try {
            moneyLaunderingService.updateAccountStatus(sa);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MoneyLaunderingException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /* curl -i -X POST -HContent-Type:application/json -HAccept:application/json http://localhost:8080/fraud-bank-accounts/ -d "{"""accountId""":"""4""","""amountOfSmallTransactions""":20}" */
}