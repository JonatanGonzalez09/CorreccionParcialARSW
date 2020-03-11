package edu.eci.arsw.moneylaundering;

import static edu.eci.arsw.moneylaundering.MoneyLaundering.amountOfFilesProcessed;

import java.io.File;
import java.util.List;

public class AccountReporterThread extends Thread {
    private int inicio;
    private int tamaño;
    private List<File> transactionFiles;
    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private MoneyLaundering moneyLaundering;

    public AccountReporterThread(int inicio, int tamaño, List<File> transactionFiles,
            TransactionAnalyzer transactionAnalyzer, MoneyLaundering moneyLaundering) {
        this.inicio = inicio;
        this.tamaño = tamaño;
        this.transactionFiles = transactionFiles;
        this.transactionAnalyzer = transactionAnalyzer;
        transactionReader = new TransactionReader();
        this.moneyLaundering = moneyLaundering;
    }

    @Override
    public void run() {
        try {
            // Por cada hilo que ejecute analizo la data.
            processTransactionData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void processTransactionData() throws InterruptedException {
        for (int i = 0; i < tamaño; i++) {
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFiles.get(inicio));
            for (Transaction transaction : transactions) {
                transactionAnalyzer.addTransaction(transaction);
            }
            inicio += 1;
            amountOfFilesProcessed.incrementAndGet();
        }
    }

}