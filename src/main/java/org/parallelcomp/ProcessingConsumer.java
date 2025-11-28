package org.parallelcomp;

import java.util.concurrent.BlockingQueue;

/**
 * Consumer class that processes lines from the input queue by performing
 * text stemming and places the results in the output queue.
 *
 * <p>This class implements Runnable and is designed to run in multiple
 * parallel threads. Each instance takes lines from the input queue,
 * applies stemming using the TextStemmer, and places the processed
 * results in the output queue. Termination is signaled by encountering
 * an empty string (poison pill) in the input queue.
 *
 * @see ParallelStemmer
 * @see TextStemmer
 * @see ProcessedLine
 */
public class ProcessingConsumer implements Runnable{
    private final BlockingQueue<String> inputQueue;
    private final BlockingQueue<ProcessedLine> outputQueue;
    private final String consumerName;
    private final TextStemmer stemmer;

    /**
     * Constructs a new LineProcessingConsumer with the specified queues and name.
     *
     * @param inputQueue   The queue from which to take lines for processing
     * @param outputQueue  The queue where processed lines will be placed
     * @param consumerName A unique identifier for this consumer for logging purposes
     */
    public ProcessingConsumer(BlockingQueue<String> inputQueue,
                                  BlockingQueue<ProcessedLine> outputQueue,
                                  String consumerName) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.consumerName = consumerName;
        this.stemmer = new TextStemmer();
    }

    /**
     * Main execution method that processes lines from the input queue.
     *
     * <p>This method:
     * <ul>
     *   <li>Continuously takes lines from the input queue</li>
     *   <li>Checks for poison pill (empty string) to terminate</li>
     *   <li>Applies stemming to non-empty lines</li>
     *   <li>Creates ProcessedLine objects and places them in output queue</li>
     * </ul>
     */
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
    /**
     * Gets the name identifier of this consumer.
     *
     * @return The consumer's unique name
     */
    public String getConsumerName() {
        return consumerName;
    }

}
