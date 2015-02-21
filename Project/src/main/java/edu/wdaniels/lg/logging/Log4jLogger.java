package edu.wdaniels.lg.logging;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * (c) Magnetic Variation Services, LLC. 2014. This class is used to obtain the
 * root logger that we'll be using initially throughout the entire program.
 * Later down the road, we might add in class-level specific loggers, but until
 * that happens, this will be used as the main interface to find retrieve a
 * common logger with a single configuration step.
 *
 * @author wdaniels
 */
public class Log4jLogger {

    //This is the actual private rootLogger that will be present throughout the entire program. 
    private static Logger rootLogger;

    /**
     * This is the default constructor, it is currently being called once in
     * Mark.java since it's static, only the single call is needed. This
     * constructor should ONLY be called once. Although I don't think subsequent
     * calls will hurt anything besides my personal happiness.
     */
    public Log4jLogger() {
        initializeRootLogger();
    }

    /**
     * This void class just abstracts a little bit of the logic out of the
     * constructor The only real goal here is to give the logger a location for
     * the configuration file, as well as to create the root logger object.
     */
    private void initializeRootLogger() {
        DOMConfigurator.configure(getClass().getResource("log4j2.xml"));
        rootLogger = Logger.getRootLogger();
    }

    /**
     * This method is for returning the rootLogger in a nice, clean, static,
     * object oriented fashion. All classes throughout the program should use
     * this method once throughout their life cycle, since a class without
     * logging, is hardly a class at all.
     *
     * @return The rootLogger that is going to be common across the whole
     * program.
     */
    public static Logger rootLogger() {
        if (rootLogger == null){
            Log4jLogger MainLogger = new Log4jLogger();
        }
        return rootLogger;
    }
}
