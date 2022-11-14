public class Main {
    public static void main(String[] args) throws InterruptedException {
        //array declaration
        int size = 200;
        long[] mas = new long[size];

        //single thread sum calculation
        long singleThreadSum = 0;
        for (int i = 0; i < size; i++) {
            singleThreadSum += i;
            mas[i] = i;
        }
        System.out.println("Сума в однопоточному режимі:");
        System.out.println(singleThreadSum);

        //multi thread sum calculation
        MainThread mainThread = new MainThread(mas);
        mainThread.Start();
        System.out.println("Сума в багатопоточному режимі:");
        System.out.println(mainThread.getSum());
    }
}