import math
import random

def findCell(n, m, x):
    #print(n, m)
    i = 1
    count = 0
    while(i < (1 << x)):
        count += math.ceil(n/i)*math.ceil(m/i) - 4*math.floor(n/(2*i))*math.floor(m/(2*i))
        i *= 2
    return count

def findMaxPow2Higher(n, pow_):
    return math.ceil(n/(1<<pow_))*(1<<pow_)

def findMaxPow2Lower(n, pow_):
    return math.floor(n/(1<<pow_))*(1<<pow_)

def findMaxPow2Range(left, right):
    assert left < right
    pow_ = 0
    left_ = left
    right_ = right
    while (left_ != right_):
        left_ = findMaxPow2Higher(left, pow_)
        right_ = findMaxPow2Lower(right, pow_)
        pow_+=1
    return left_, pow_

def findNumSquaresUpper(x1, x2, y1, y2, x):
    assert x1 < x2
    assert y1 < y2
    xMid, powX = findMaxPow2Range(x1, x2)
    yMid, powY = findMaxPow2Range(y1, y2)
    return findCell(xMid - x1, yMid - y1, x) + findCell(x2 - xMid, yMid - y1, x) + findCell(xMid - x1, y2 - yMid, x) + findCell(x2 - xMid, y2 - yMid, x)

def gen2Nums(left, right):
    a = 0
    b = 0
    while (a == b):
        a = random.randint(left, right)
        b = random.randint(left, right)
    if (a > b): 
        a, b = b, a
    return a, b

for i in range(1, 52):
    count = 0
    nums = 0
    for j in range(100000):
        x1, x2 = gen2Nums(0, 1<<i)
        y1, y2 = gen2Nums(0, 1<<i)
        count += findNumSquaresUpper(x1, x2, y1, y2, i)
        nums += 1
    print(str((1<<i)) + "," + str(count/nums))