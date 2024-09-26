public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello World!");
	try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            // Handle the exception if needed
            e.printStackTrace();
        }
    }
}
