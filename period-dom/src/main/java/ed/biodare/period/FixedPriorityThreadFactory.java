/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ed.biodare.period;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 *
 * @author tzielins
 */
public class FixedPriorityThreadFactory implements ThreadFactory {

    
    final ThreadFactory provider;
    final int priority;

    public static FixedPriorityThreadFactory threadFactory(int priority) {
        return new FixedPriorityThreadFactory(Executors.defaultThreadFactory(), priority);
    }
    
    public FixedPriorityThreadFactory(ThreadFactory provider, int priority) {
        this.provider = provider;
        this.priority = priority;
    }
    
    @Override
    public Thread newThread(Runnable r) {
        
        Thread t = provider.newThread(r);
        t.setPriority(priority);
        return t;
    }
    
    
    
}
