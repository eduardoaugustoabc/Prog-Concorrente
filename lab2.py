import random
import threading
import string
import queue

def generate_random_string(length):
    return "".join(random.choices(string.ascii_letters + string.digits, k=length))

def producer(string_length, result_queue):
    while True:
        strings = [generate_random_string(string_length)]
        result_queue.put(strings)

def filter_strings(input_queue, output_queue):
    while True:
        strings = input_queue.get()
        if strings is None:
            output_queue.put(None)
            break
        filtered_strings = [string for string in strings if string.isalpha()]
        output_queue.put(filtered_strings)

def consumer(output_queue, num_strings):
    counter = 0
    while True:
        strings = output_queue.get()
        if strings is None:
            break
        if counter == num_strings: 
            break
        for string in strings:
            counter += 1
            print(string)

def main():
    num_strings = 10
    string_length = 5

    input_queue = queue.Queue()
    output_queue = queue.Queue()

    producer_thread = threading.Thread(target = producer, args = (string_length, input_queue))
    filter_thread = threading.Thread(target = filter_strings, args = (input_queue, output_queue))
    consumer_thread = threading.Thread(target = consumer, args = (output_queue, num_strings))

    producer_thread.start()
    filter_thread.start()
    consumer_thread.start()

    producer_thread.join()
    filter_thread.join()
    consumer_thread.join()

if __name__ == "__main__":
    main()


