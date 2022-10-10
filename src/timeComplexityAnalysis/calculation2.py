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


for i in range(1, 10):
    count = 0
    nums = 0
    for x1 in range(1<<i):
        for x2 in range(x1+1, 1<<i):       
            for y1 in range(1<<i):
                for y2 in range(y1+1, 1<<i):
                    count += findNumSquaresUpper(x1, x2, y1, y2, i)
                    nums += 1
    print("For n = {}, we have an (upper) average count of: {}".format(1<<i, count/nums))