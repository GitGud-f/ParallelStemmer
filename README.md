# Parallel Text Stemming with Producer-Consumer Pattern

## Introduction
This repository contains a Java implementation of a multi-threaded text processing system that applies the producer-consumer pattern for parallel text stemming. The project demonstrates parallel computing principles by processing text files through a pipeline of concurrent workers, as part of a homework assignment for a parallel computing course at the Higher Institute for Applied Sciences and Technology (HIAST).

The system features a single producer that reads lines from an input file, multiple processing consumers that perform text stemming in parallel, and a single consumer that writes the results to an output file. Two blocking queues connect these components, enabling efficient data flow and workload distribution across threads.

The implementation uses the Snowball stemmer for English text processing and showcases proper thread synchronization, resource management, and graceful shutdown mechanisms.

## Content
- **Producer-Consumer Pattern**: Implementation of the classic concurrent design pattern with multiple processing stages
- **Blocking Queues**: Thread-safe data transfer between producer and consumers using `ArrayBlockingQueue`
- **Parallel Text Processing**: Multiple concurrent threads performing stemming operations
- **Thread Synchronization**: Proper coordination using poison pill pattern for graceful shutdown
- **Resource Management**: Efficient file I/O handling with try-with-resources
- **Performance Optimization**: Configurable queue sizes and thread counts for optimal throughput
- **Software Engineering Best Practices**: Separation of concerns, immutability, and comprehensive error handling

## Features
- **Thread-safe operations** with proper synchronization using blocking queues
- **Configurable parallelism** with adjustable number of processing consumers
- **Graceful shutdown** using poison pill pattern to ensure all threads terminate properly
- **Comprehensive error handling** with detailed logging and exception management
- **Efficient resource utilization** with proper file handling and memory management
- **Modular design** with clear separation between file I/O, text processing, and coordination logic
- **Maven-based build** with external dependency management for the stemming library

## Setup
### Requirements
- Java JDK 11 or higher
- Maven 3.6 or higher
- Internet connection for downloading dependencies (first build only)

### Installation
1. Clone the repository:
```bash
git clone https://github.com/GitGud-f/ParallelStemmer.git
```

2. Navigate to the project directory:
```bash
cd ParallelStemmer
```


## Usage
Run the application main class 
or run via cli and pass input and output file as positional arguments


### Configuration
The application can be configured by modifying constants in `ParallelStemmingApplication.java`:
- `QUEUE_CAPACITY`: Size of the blocking queues (default: 100)
- `NUM_CONSUMERS`: Number of parallel processing threads (default: 4)

## Architecture Overview
```
Producer → Queue1 → Multiple ProcessingConsumers → Queue2 → WritingConsumer
     ↓              ↓                    ↓                    ↓           ↓
  Reads        Raw lines          Stemmed lines        Processed     Writes to
 input file                    (parallel processing)     results     output file
```

### Components
- **Producer**: Reads lines from input file and feeds them to Queue 1
- **ProcessingConsumer**: Multiple instances that stem text in parallel
- **WritingConsumer**: Single consumer that writes results to output file
- **TextStemmer**: Wrapper around Snowball stemmer for English text
- **ProcessedLine**: Immutable data transfer object

## Performance Characteristics
The system demonstrates significant performance improvements through parallel processing:

- **Scalability**: Processing time decreases as more consumer threads are added (up to optimal point)
- **Memory Efficiency**: Bounded queues prevent memory exhaustion with large files
- **I/O Overlap**: Reading, processing, and writing occur concurrently
- **Load Balancing**: Multiple consumers share the computational workload

### Expected Performance Gains
- **2x-4x speedup** compared to single-threaded processing on multi-core systems
- **Linear scaling** with number of cores (for CPU-bound stemming operations)
- **Minimal blocking** due to efficient queue management

## Implementation Details

### Synchronization Strategy
- **Blocking Queues**: Provide built-in thread safety and blocking operations
- **Poison Pill Pattern**: Ensures clean shutdown of all threads
- **Executor Service**: Manages pool of processing consumer threads
- **Thread Coordination**: Proper joining and termination sequencing

### Error Handling
- **Comprehensive Exception Handling**: IOExceptions and InterruptedException properly handled
- **Thread Interruption**: Proper response to thread interruption signals
- **Resource Cleanup**: Automatic resource management with try-with-resources
- **Graceful Degradation**: Continues processing despite individual line errors

## Example
**Input Text:**
```
believable processing
doing finding unbound
```

**Output Text:**
```
believ process
do find unbound
```

## Dependencies
- **lucene-snowball** (v3.0.3): Provides English stemming functionality
- **Maven**: For dependency management and build automation

## Report
A detailed report is included in `report.pdf`, covering implementation, analysis, and some discussion points.
## References

- [Producer-Consumer Pattern](https://java-design-patterns.com/patterns/producer-consumer/)
- [Poison Pill Pattern](https://java-design-patterns.com/patterns/poison-pill/#programmatic-example-of-poison-pill-pattern-in-java)
