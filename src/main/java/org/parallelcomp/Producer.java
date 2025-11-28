package org.parallelcomp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    private final String inputFilePath;
    private final BlockingQueue<String> outputQueue;

    public Producer(String inputFilePath, BlockingQueue<String> outputQueue) {
        this.inputFilePath = inputFilePath;
        this.outputQueue = outputQueue;
    }

    @Override
    public void run(){
        try(BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))){
            String line;
            while((line = reader.readLine())!=null){
                line = line.trim();
                if(!line.isEmpty()){
                    outputQueue.put(line);
                }
            }
            // Signal end of input to all consumers
            for (int i = 0; i < ParallelStemmer.NUM_CONSUMERS; i++) {
                outputQueue.put(""); // Empty string as poison pill
            }

            System.out.println("Producer finished reading file: " + inputFilePath);
        }
        catch(IOException | InterruptedException e){
            System.err.println("Error in producer: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
