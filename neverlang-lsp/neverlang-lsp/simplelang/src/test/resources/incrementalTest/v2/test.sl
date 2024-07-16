function ack(m) {
    return 0;
}


function ack2(m,n) {
    if (m == 0) {
        return n + 1;
    }
    if (n == 0) {
        n = 1;
    } else {
        n = ack(m);
    }
    return ack2(m - 1, n);
}
