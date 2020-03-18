/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.period;

import ed.robust.dom.data.TimeSeries;
import ed.robust.dom.tsprocessing.FailedPPA;
import ed.robust.dom.tsprocessing.PPAResult;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 * @author tzielins
 */
public abstract class PPAMultiAnalyser {
    
    protected final int NThreads;
    
    protected final int DEF_CHUNK_SIZE;
    
    protected final int THREAD_PRIORITY = Thread.MIN_PRIORITY+1;
    
    protected final PPAAnalyser analyser;

    public PPAMultiAnalyser(PPAAnalyser analyser, int threads, int defChunk) {
        this.NThreads = threads;
        this.analyser = analyser;
        this.DEF_CHUNK_SIZE = defChunk;
    }    
    
    
    public List<PPAResult> analyse(List<TimeSeries> data, double periodMin, double periodMax) {
        if (periodMin <= 0) throw new IllegalArgumentException("Period min parameter must be >0");
        if (periodMax <= periodMin) throw new IllegalArgumentException("Max period parameter must be > min period");
        
        return analyse(data, periodMin, periodMax, DEF_CHUNK_SIZE, NThreads);
    }

    public List<PPAResult> analyse(List<TimeSeries> data, double periodMin, double periodMax, int chunkSize) {
        return analyse(data, periodMin, periodMax, chunkSize, NThreads);
    }
    
    public List<PPAResult> analyse(List<TimeSeries> data, double periodMin, double periodMax, int chunkSize, int threads) {
        
        ExecutorService pool = createRunningPool(threads);
        
        try {
            final int length = data.size();
            List<Future<List<PPAResult>>> futures = new ArrayList<>(length);
        
            for (int ix = 0; ix < length; ix+=chunkSize) {

                final int start = ix;
                final int end = Math.min(ix+chunkSize, length);
                futures.add(
                    pool.submit( () -> analyseDataSubset(data, periodMin, periodMax, start, end))
                );
            }
        
        
            try {

                List<PPAResult> res = new ArrayList<>(length);
                for (Future<List<PPAResult>> f : futures) {
                    if (Thread.currentThread().isInterrupted()) {
                        pool.shutdownNow();
                    }

                    res.addAll(f.get());
                }
                return res;
            } catch (InterruptedException e) {
                pool.shutdownNow();
                throw new RuntimeException("Interrupted analysis in parallel "+e.getMessage(),e);
            } catch (ExecutionException e) {
                pool.shutdownNow();
                throw new RuntimeException("Could not calculate periods "+e.getMessage(),e);
            }        
        } finally {
            pool.shutdown();        
        }
    }
    
    protected ExecutorService createRunningPool(int threads) {
        ExecutorService pool = Executors.newFixedThreadPool(threads,FixedPriorityThreadFactory.threadFactory(THREAD_PRIORITY));
        return pool;
    }    

    protected List<PPAResult> analyseDataSubset(final List<TimeSeries> data, final double periodMin, final double periodMax, 
            int startInclusive, int endExclusive) throws InterruptedException {
        
        List<PPAResult> res = new ArrayList<>(endExclusive-startInclusive);
        
        for (int i = startInclusive; i< endExclusive; i++) {
            res.add(analyseData(data.get(i), periodMin, periodMax));
        }
        
        return res;
    }

    protected PPAResult analyseData(TimeSeries data, double periodMin, double periodMax) throws InterruptedException {
        
        try {
            return analyser.analyse(data, periodMin, periodMax);
        } catch (IllegalArgumentException | IllegalStateException | PeriodProcessException e) {
            return new FailedPPA("Could not estimate "+e.getMessage(),analyser.getMethod()); 
        }
        
    }
}
