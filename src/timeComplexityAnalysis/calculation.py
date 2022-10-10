import math

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

for i in range(2, 1000):
    x1 = 1
    y1 = 1
    x2 = (1<<i) -1
    y2 = (1<<i) -1
    count = findNumSquaresUpper(x1, x2, y1, y2, i) / ((1<<i) -1)
    print(count)
