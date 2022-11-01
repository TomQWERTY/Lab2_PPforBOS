public class MainThread {
    private long sum;
    private int size;
    private long[] mas;
    private SumThread[] threads;

    private int finishedCount;
    MainThread(long[] mas_)
    {
        mas = mas_;
        size = mas.length;
        sum = 0;
        finishedCount = 0;
        threads = new SumThread[size / 2];
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

    synchronized public void ReportFinish(int i, long partSum)
    {
        mas[i] = partSum;
        finishedCount++;

        if (finishedCount == size / 2)
        {
            size = size / 2 + size % 2;
            for (int j = 0; j < size / 2; j++)
            {
                threads[j].WakeUp();
            }
            notify();
            finishedCount = 0;
        }
    }

    synchronized public long[] GetNumbers(int i)
    {
        return new long[] {mas[i], mas[size - 1 - i]};
    }
}
