import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
	public static String generateRandomString(int length) {
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMONPQRSTUVWXYZ0123456789";
		StringBuilder sb = new StringBuilder(length);
		Random random = new Random();
		for (int i = 0; i < length; i++){
			sb.append(characters.charAt(random.nextInt(characters.length())));
		}
		return sb.toString();
	}

	public static void producer (int stringLength, BlockingQueue<String[]> resultQueue){
		while(true){
			String[] strings = {generateRandomString(stringLength)};
			try{
				resultQueue.put(strings);
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	public static void filterStrings (BlockingQueue <String[]> inputQueue, BlockingQueue <String[]> outputQueue){
		while(true){
			try{
				String[] strings = inputQueue.take();
				if (strings == null){
					outputQueue.put(new String[0]);
					break;
				}
				String[] filteredStrings = filter(strings);
				if (filteredStrings.length > 0){
					outputQueue.put(filteredStrings);
				}
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	public static String[] filter(String[] strings){
		List<String> filteredList = new ArrayList<>();
		for (String string : strings) {
			if (string.matches("[a-zA-Z]+")) {
				filteredList.add(string);
			}
		}
		return filteredList.toArray(new String[0]);
	}

	public static void consumer (BlockingQueue <String[]> outputQueue, int numStrings){
		int counter = 0;
		while(true){
			try{
				String[] strings = outputQueue.take();
				if (strings.length == 0){
					break;
				}for (String string : strings) {
					if (counter == numStrings) break;
					counter++;
					System.out.println(string);
				}
			}catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	public static void main(String[] args) {
		int numStrings = 10;
		int stringLength = 5;

		BlockingQueue <String[]> inputQueue = new ArrayBlockingQueue<>(1);
		BlockingQueue <String[]> outputQueue = new ArrayBlockingQueue<>(1);

		Thread producerThread = new Thread(() -> producer(stringLength, inputQueue));
		Thread filterThread = new Thread(() -> filterStrings(inputQueue, outputQueue));
		Thread consumerThread = new Thread(() -> consumer(outputQueue, numStrings));

		producerThread.start();
		filterThread.start();
		consumerThread.start();


		try{
			producerThread.join();
			filterThread.join();
			consumerThread.join();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
	}

}
