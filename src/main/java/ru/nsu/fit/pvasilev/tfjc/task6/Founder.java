package ru.nsu.fit.pvasilev.tfjc.task6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public final class Founder {
    private final List<Runnable> workers;
    private CyclicBarrier cyclicBarrier;
    public Founder(final Company company) {
        this.workers = new ArrayList<>(company.getDepartmentsCount());
        for (int i = 0; i < company.getDepartmentsCount(); i++) {
            int finalI = i;
            workers.add(()-> {
                company.getFreeDepartment(finalI).performCalculations();
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
            cyclicBarrier = new CyclicBarrier(company.getDepartmentsCount(), company::showCollaborativeResult);
        }
    }
    public void start() {
        for (final var worker : workers) {
            new Thread(worker).start();
        }

    }
}
