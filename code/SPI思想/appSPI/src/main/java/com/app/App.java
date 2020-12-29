package com.app;

import com.spi.DataBaseDriver;

import java.util.ServiceLoader;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        ServiceLoader<DataBaseDriver> serviceLoader = ServiceLoader.load(DataBaseDriver.class);
        for(DataBaseDriver dataBaseDriver : serviceLoader){
            System.out.println(dataBaseDriver.buildConnect("test"));
        }
        System.out.println( "Hello World!" );
    }
}
