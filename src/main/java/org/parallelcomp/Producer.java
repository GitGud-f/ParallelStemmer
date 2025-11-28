package org.parallelcomp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Producer class that reads lines from an input file and places them
 * into a blocking queue for processing by consumers.
 *
 * <p>This class implements Runnable and is designed to run in a separate
 * thread. It reads the input file line by line, skipping empty lines,
 * and places non-empty lines into the output queue. When the end of file
 * is reached, it signals termination by placing poison pills in the queue.
 *
 * @see ParallelStemmer
 * @see ProcessingConsumer
 */
public class Producer implements Runnable {
    private final String inputFilePath;
    private final BlockingQueue<String> outputQueue;

    /**
     * Constructs a new Producer with the specified input file and output queue.
     *
     * @param inputFilePath Path to the input file to read lines from
     * @param outputQueue   The blocking queue where lines will be placed for processing
     */
    public Producer(String inputFilePath, BlockingQueue<String> outputQueue) {
        this.inputFilePath = inputFilePath;
        this.outputQueue = outputQueue;
    }

    /**
     * Main execution method that reads the input file and populates the queue.
     *
     * <p>This method:
     * <ul>
     *   <li>Opens the input file for reading</li>
     *   <li>Reads lines sequentially, skipping empty ones</li>
     *   <li>Places non-empty lines into the output queue</li>
     *   <li>Signals end of input with poison pills when file reading completes</li>
     *   <li>Handles I/O exceptions and thread interruptions gracefully</li>
     * </ul>
     *
     * @throws RuntimeException if file cannot be read or thread is interrupted
     */
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
