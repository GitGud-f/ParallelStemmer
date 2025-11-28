package org.parallelcomp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * consumer class that writes processed lines from the output queue
 * to the specified output file.
 *
 * <p>This class implements Runnable and is designed to run in a separate
 * thread. It takes ProcessedLine objects from the output queue and writes
 * the stemmed text to the output file. Termination is signaled by encountering
 * the special POISON_PILL instance in the queue.
 *
 * @see ParallelStemmer
 * @see ProcessedLine
 */
public class WritingConsumer implements Runnable{
    private final String outputFilePath;
    private final BlockingQueue<ProcessedLine> inputQueue;

    /**
     * Constructs a new WritingConsumer with the specified output file and input queue.
     *
     * @param outputFilePath Path to the output file where stemmed text will be written
     * @param inputQueue     The queue from which processed lines will be taken
     */
    public WritingConsumer(String outputFilePath, BlockingQueue<ProcessedLine> inputQueue) {
        this.outputFilePath = outputFilePath;
        this.inputQueue = inputQueue;
    }

    /**
     * Main execution method that writes processed lines to the output file.
     *
     * <p>This method:
     * <ul>
     *   <li>Opens the output file for writing</li>
     *   <li>Continuously takes ProcessedLine objects from the input queue</li>
     *   <li>Writes the stemmed text to the output file</li>
     *   <li>Terminates when the POISON_PILL is encountered</li>
     * </ul>
     */
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
