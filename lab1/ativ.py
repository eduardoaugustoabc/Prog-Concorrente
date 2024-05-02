import threading
import time

import random


buffer_max = []
buffer_min = []

def generate_matrix(size):
    return [[random.randint(250, 29500) for _ in range(size)] for _ in range(size)]

def minimo(linha):
    elemento = min(linha)
    buffer_min.append(elemento)

def maximo(linha):
    elemento = max(linha)
    buffer_max.append(elemento)
