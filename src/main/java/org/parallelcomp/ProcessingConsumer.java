package org.parallelcomp;

import java.util.concurrent.BlockingQueue;

public class ProcessingConsumer implements Runnable{
    private final BlockingQueue<String> inputQueue;
    private final BlockingQueue<ProcessedLine> outputQueue;
    private final String consumerName;
    private final TextStemmer stemmer;

    public ProcessingConsumer(BlockingQueue<String> inputQueue,
                                  BlockingQueue<ProcessedLine> outputQueue,
                                  String consumerName) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.consumerName = consumerName;
        this.stemmer = new TextStemmer();
    }

    @Override
    public void run(){
        try {
            while (true) {
                String line = inputQueue.take();

                // Check for poison pill
                if (line.isEmpty()) {
                    break;
                }

                String stemmedLine = stemmer.stemText(line);
                ProcessedLine processedLine = new ProcessedLine(line, stemmedLine);

                outputQueue.put(processedLine);
            }

            System.out.println(consumerName + " finished processing");

        } catch (InterruptedException e) {
            System.err.println(consumerName + " was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

}
