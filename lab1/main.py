from ativ import *
import sys
import threading
import time

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Use: python3 find.py <size>")
        sys.exit(1)

    try:
        size = int(sys.argv[1])
    except ValueError:
        print("The size of the matrix must be an integer.")
        sys.exit(1)

    start_time = time.time()

    matrix = generate_matrix(size)
    for i in range(size):
        maior = threading.Thread(target=maximo(matrix[i]))
        maior.start()
        menor = threading.Thread(target=minimo(matrix[i]))
        menor.start()

    end_time = time.time()
    print(end_time - start_time)

    print(f'Max value: {max(buffer_max)}')
    print(f'Min value: {min(buffer_min)}')
