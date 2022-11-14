import java.util.concurrent.CyclicBarrier;
public class MainThread {
    private long sum;
    private int size;
    private long[] mas;
    private SumThread[] threads;
    private int finishedCount;
    private CyclicBarrier bar;
    MainThread(long[] mas_)
    {
        mas = mas_;
        size = mas.length;
        sum = 0;
        finishedCount = 0;
        threads = new SumThread[size / 2];
        bar = new CyclicBarrier(size / 2, new Runnable() {
            @Override
            public void run() {
                size = size / 2 + size % 2;
                WakeUp();
            }
        });
    }
    public CyclicBarrier getBar()
    {
        return bar;
    }
    public long getSum()
    {
        return sum;
    }

    public int getSize()
    {
        return size;
    }

    public int getFinishedCount()
    {
        return finishedCount;
    }

    synchronized public void Start()
    {
        /*do {
            for (int i = 0; i < size / 2; i++) {
                int end = size - 1 - i;
                mas[i] = mas[i] + mas[end];
            }

            size = size / 2 + size % 2;
        } while (size > 1);
        sum = mas[0];*/

        for (int i = 0; i < size / 2; i++) {
            threads[i] = new SumThread(this, i);
            threads[i].start();
        }
        while (size > 1){
            try {
                wait();
                bar.reset();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        sum = mas[0];
    }

    public void AwaitBarrier()
    {
        try {
            bar.await();
        } catch (InterruptedException ex) {
            return;
        } catch (java.util.concurrent.BrokenBarrierException ex) {
            return;
        }
    }
    synchronized public void ReportFinish(int i, long partSum)
    {
        mas[i] = partSum;
    }

    synchronized public long[] GetNumbers(int i)
    {
        return new long[] {mas[i], mas[size - 1 - i]};
    }

    synchronized public void WakeUp()
    {
        notify();
    }
}
