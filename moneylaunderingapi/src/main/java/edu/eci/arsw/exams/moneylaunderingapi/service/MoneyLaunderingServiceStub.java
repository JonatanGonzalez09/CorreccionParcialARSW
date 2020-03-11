package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

@Service
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {
    List<SuspectAccount> suspectAccountList;

    public MoneyLaunderingServiceStub(){
        suspectAccountList = new CopyOnWriteArrayList<>();
        SuspectAccount sAccount1 = new SuspectAccount("1", 5);
        SuspectAccount sAccount2 = new SuspectAccount("2", 10);
        SuspectAccount sAccount3 = new SuspectAccount("3", 15);
        suspectAccountList.add(sAccount1);
        suspectAccountList.add(sAccount2);
        suspectAccountList.add(sAccount3);
    }
    
    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) throws MoneyLaunderingException {
        for (SuspectAccount sa : suspectAccountList) {
            if (sa.getAccountId().equals(suspectAccount.getAccountId())) {
                suspectAccount = sa;
            }
        }
        throw new MoneyLaunderingException("ERROR -- La cuenta no existe");
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) throws MoneyLaunderingException {
        for (SuspectAccount suspectAccount : suspectAccountList) {
            if (suspectAccount.getAccountId().equals(accountId)) {
                return suspectAccount;
            }
        }
        throw new MoneyLaunderingException("ERROR -- La cuenta no existe");

    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() {
        return suspectAccountList;
    }

    @Override
    public void addSuspectAccount(SuspectAccount suspacc) throws MoneyLaunderingException {

        for (int i = 0; i < suspectAccountList.size(); i++) {
            if (suspectAccountList.get(i).getAmountOfSmallTransactions() == suspacc.getAmountOfSmallTransactions()) {
                throw new MoneyLaunderingException("ERROR -- La cuenta ya existe");
            }
        }
        suspectAccountList.add(suspacc);
    }
}