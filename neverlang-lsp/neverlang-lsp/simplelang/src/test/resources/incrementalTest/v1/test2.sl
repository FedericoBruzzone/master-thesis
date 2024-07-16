function test2(x,y) {
    if (x == 0) {
        return y + 1;
    }
    if (y == 0) {
        y = 1;
    } else {
        y = ack(x, y - 1);
    }
    return ack2(x - 1, y);
}
