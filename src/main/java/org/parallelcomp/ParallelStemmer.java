package org.parallelcomp;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelStemmer {
    public static int NUM_CONSUMERS = 5;
    private static final int QUEUE_CAPACITY = 100;
    private static final String DEFAULT_INPUT_FILE = "input.txt";
    private static final String DEFAULT_OUTPUT_FILE = "output.txt";

    public static void main(String[] args) {
        String inputFile;
        String outputFile;
        if (args.length > 2) {
            System.out.println("Usage: java ParallelStemmer [input_file] [output_file]");
            System.out.println("  input_file:  Input text file (default: input.txt)");
            System.out.println("  output_file: Output text file (default: output.txt)");
            System.exit(1);
        }
        if (args.length == 2) {
            inputFile = args[0];
            outputFile  = args[1];
        }
        else if(args.length == 1){
            inputFile = args[0];
            outputFile = DEFAULT_OUTPUT_FILE;
            System.out.println("Using input file: " + inputFile);
            System.out.println("Using default output file: " + outputFile);
        } else {
            inputFile = DEFAULT_INPUT_FILE;
            outputFile = DEFAULT_OUTPUT_FILE;
            System.out.println("Using default input file: " + inputFile);
            System.out.println("Using default output file: " + outputFile);
        }

        try{
            processFileWithStemming(inputFile, outputFile);
            System.out.println("File processing completed successfully!");
        } catch (IOException | InterruptedException e){
            System.err.println("Error processing file: " + e.getMessage());
        }
    }

    public static void processFileWithStemming(String inputFile, String outputFile)
            throws IOException, InterruptedException{

        BlockingQueue<String> inputQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        BlockingQueue<ProcessedLine> outputQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        Producer producer = new Producer(inputFile, inputQueue);
        WritingConsumer writerConsumer = new WritingConsumer(outputFile, outputQueue);

        ExecutorService consumerExecutor = Executors.newFixedThreadPool(NUM_CONSUMERS);

        Thread producerThread = new Thread(producer);
        producerThread.start();

        for (int i = 0; i < NUM_CONSUMERS; i++){
            consumerExecutor.execute(new ProcessingConsumer(inputQueue, outputQueue, "Consumer-" + i));
        }

        Thread writerThread = new Thread(writerConsumer);
        writerThread.start();

        producerThread.join();

        consumerExecutor.shutdown();
        boolean b = consumerExecutor.awaitTermination(1, TimeUnit.MINUTES);

        outputQueue.put(ProcessedLine.POISON_PILL);

        writerThread.join();
    }
}