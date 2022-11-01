public class SumThread extends Thread {
    private MainThread mainThread;
    private int i;
    private long[] numbers;

    SumThread(MainThread mainThread_, int i_) {
        mainThread = mainThread_;
        i = i_;
        numbers = new long[2];
    }

    synchronized public void run() {
        do {
            numbers = mainThread.GetNumbers(i);
            int s = mainThread.getSize();
            mainThread.ReportFinish(i, numbers[0] + numbers[1]);
            if (i < (s / 2 + s % 2) / 2)
            {
                if (mainThread.getFinishedCount() > 0)
                {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            else
            {
                break;
            }
        }
        while (mainThread.getSize() > 1);
    }

    synchronized public void WakeUp()
    {
        notify();
    }
}
