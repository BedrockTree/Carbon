package cn.org.bedrocktree.carbon;

import cn.org.bedrocktree.carbon.exceptions.OsNotSupportsException;
import cn.org.bedrocktree.carbon.init.InitFiles;
import cn.org.bedrocktree.carbon.ui.MainWindow;
import cn.org.bedrocktree.carbon.utils.SystemUtils;
import org.apache.commons.cli.*;

import java.io.IOException;

public final class Carbon {

    public static final String APPLICATION_NAME = "Carbon";

    public static final String VERSION = "ALPHA_0.0.1";

    public static final String CONFIGURATION_DIRECTORY_PATH = SystemUtils.getSystemConfigDirectory() + APPLICATION_NAME;

    public static final String ROOT_LOCATION = "/data/Projects/Java/Another/Carbon/Test/";

    public static final Options CARBON_OPTIONS = new Options();

    private static boolean enableGui;

    private static boolean debugMode;

    public static void main(String[] args) {
        if (SystemUtils.getSystemName().contains("Mac")){
            try {
                throw new OsNotSupportsException();
            } catch (OsNotSupportsException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        System.out.println("Carbon  Copyright (C) 2021  BedrockTree");
        genOptions(args);
    }

    public static Option withArgName(Option opt,String argName){
        opt.setArgName(argName);
        return  opt;
    }

    private static void genOptions(String[] args){
        CARBON_OPTIONS.addOption(new Option("h","help",false,"Print all options"));
        CARBON_OPTIONS.addOption(withArgName(new Option("d","debug",true,"Whether to enable debug mode"),"true/false"));
        CARBON_OPTIONS.addOption(withArgName(new Option("g","gui",true,"Whether to show gui"),"true/false"));
        try {
            CommandLine commandLine = new BasicParser().parse(CARBON_OPTIONS,args);
            if (commandLine.hasOption('h')){
                Carbon.Controller.printHelpInformation();
            }
            if (commandLine.hasOption('d')){
                debugMode = (boolean) commandLine.getOptionObject('d');
            }else {
                debugMode = false;
            }
            if (commandLine.hasOption('g')){
                enableGui = (boolean) commandLine.getOptionObject('g');
            }else {
                enableGui = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static final class Controller{

        public static void init(){
            InitFiles.init();
        }

        public static void printHelpInformation(){
            new HelpFormatter().printHelp("minecraft",CARBON_OPTIONS);
            System.exit(0);
        }

        public static void showGUI(){
            MainWindow.genUIManager();
            try {
                new MainWindow().init();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void enterCliEnv(){

        }

    }

}