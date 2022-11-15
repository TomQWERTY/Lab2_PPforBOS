import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Phaser;
public class MainThread {
    private long sum;
    private int size;
    private long[] mas;
    private SumThread[] threads;
    private int finishedCount;
    private Phaser phaser;
    MainThread(long[] mas_)
    {
        mas = mas_;
        size = mas.length;
        sum = 0;
        finishedCount = 0;
        threads = new SumThread[size / 2];
        phaser = new Phaser(size / 2) {
            protected boolean onAdvance(int phase, int parties) {
                size = size / 2 + size % 2;
                WakeUp();
                return super.onAdvance(phase, parties); }
        };
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
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        sum = mas[0];
    }

    public void AwaitPhaser()
    {
        phaser.arriveAndAwaitAdvance();
    }

    public void GetOutOfPhaser()
    {
        phaser.arriveAndDeregister();
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
