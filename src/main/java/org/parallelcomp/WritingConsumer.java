package org.parallelcomp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

public class WritingConsumer implements Runnable{
    private final String outputFilePath;
    private final BlockingQueue<ProcessedLine> inputQueue;

    public WritingConsumer(String outputFilePath, BlockingQueue<ProcessedLine> inputQueue) {
        this.outputFilePath = outputFilePath;
        this.inputQueue = inputQueue;
    }

    @Override
    public void run(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))){
            while(true){
                ProcessedLine processedLine = inputQueue.take();
                // Check for poison pill
                if (processedLine.isPoisonPill()) {
                    break;
                }

                writer.write(processedLine.getStemmedLine());
                writer.newLine();

            }
            System.out.println("Writer finished.");
        }
        catch (InterruptedException | IOException e){
            System.err.println("Error in writer consumer: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
