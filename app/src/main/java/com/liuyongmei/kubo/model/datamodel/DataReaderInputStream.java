package com.liuyongmei.kubo.model.datamodel;

import android.util.Log;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/3/26 0026.
 */

public class DataReaderInputStream extends DataInputStream implements DataInput{

    public DataReaderInputStream(InputStream in) {
        super(in);
    }

    public int readIntReverse()throws IOException{
        int a=in.read();
        int b=in.read();
        int c=in.read();
        int d=in.read();
        if((a|b|c|d)<0)throw new EOFException();
        return (d<<24)+(c<<16)+(b<<8)+a;
    }
    public float readFloatReverse()throws IOException{
        return Float.intBitsToFloat(this.readIntReverse());
    }

    public void readChars(char[] cs)throws IOException{
        for(int i=0,len=cs.length;i<len;i++){
            cs[i]=readChar();
        }
    }
    public void readCharsReverse(char[] cs)throws IOException{
        for(int i=0,len=cs.length;i<len;i++){
            cs[i]=(char)Short.reverseBytes(readShort());
        }
    }

    public void readFloatsReverse(float[] fs)throws IOException{
        for(int i=0,len=fs.length;i<len;i++){
            fs[i]=readFloatReverse();
        }
    }
}
