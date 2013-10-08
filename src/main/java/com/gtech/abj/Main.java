package com.gtech.abj;

import com.gtech.abj.impl.InstantiateDealerImpl;


public final class Main {

private Main() { }
public static void main(final String[] args) {
    new InstantiateDealerImpl().instantiate("table", "board");
}
}
