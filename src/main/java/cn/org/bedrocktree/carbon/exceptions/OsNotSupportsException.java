package cn.org.bedrocktree.carbon.exceptions;

import cn.org.bedrocktree.carbon.utils.SystemUtils;

public class OsNotSupportsException extends Exception{
    public OsNotSupportsException(){
        super(SystemUtils.getSystemName() + " has not supported yet");
    }
}
