package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoneyLaundering{
    static private TransactionAnalyzer transactionAnalyzer;
    static private TransactionReader transactionReader;
    static private int amountOfFilesTotal;
    static public AtomicInteger amountOfFilesProcessed;
    static private List<File> transactionFiles;
    static private int numHilos = 5; // Cantidad de hilos que se quieren ejecutar.
    static private AccountReporterThread[] listHilos;

    public MoneyLaundering(){
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
        amountOfFilesProcessed.set(0);
        transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        listHilos = new AccountReporterThread[numHilos];
    }

    public List<String> getOffendingAccounts(){
        return transactionAnalyzer.listOffendingAccounts();
    }

    private List<File> getTransactionFileList(){
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }
    
    public static void main(String[] args){
        MoneyLaundering moneyLaundering = new MoneyLaundering();
        int inicio = 0;
        int tamaño = amountOfFilesTotal / numHilos;
        int res = amountOfFilesTotal % numHilos;
        for (int j = 0; j < numHilos; j++) {
            if (j+1 == numHilos){
                listHilos[j] = new AccountReporterThread(inicio, tamaño + res, transactionFiles, transactionAnalyzer, moneyLaundering);
            }
            else{
                listHilos[j] = new AccountReporterThread(inicio, tamaño, transactionFiles, transactionAnalyzer, moneyLaundering);
            }
            listHilos[j].start();
            inicio+= tamaño;
        }

        while(amountOfFilesProcessed.get() < amountOfFilesTotal)
        {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.contains("exit"))
            {
                break;
            }
            else{
                for (int i = 0 ; i < numHilos ; i++){
                    listHilos[i].pausar();
                }
            }
            System.out.println("Pausado");
            String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
            List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
            String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
            message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
            System.out.println(message);
            scanner.nextLine();
            if(line.contains("exit"))
            {
                break;
            }
            else{
                for (int i = 0 ; i < numHilos ; i++){
                    listHilos[i].reanudar();
                }
            }
            System.out.println("Reunudado");
        }
        String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
        List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
        String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
        message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
        System.out.println(message);
    }
}
