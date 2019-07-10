package com.example.test1;

public class Music {
    //time when a note appears
    public int appearTime[];
    //state of notes, 0:have not appeared 1:appearing 2:have appeared
    public int state[];
    //pixel per ms
    public static long velocity;

    public long position[];

    public Music(){
        velocity = 2;
        position = new long[]{0,0,0,0,0};
    }
}
